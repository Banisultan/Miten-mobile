package id.bnn.convey.API;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import id.bnn.convey.Database;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class get_list_surveyin {

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();

    Context context;
    String IDAKUN;
    String GROUP;

    public get_list_surveyin(
            Context context,
            String idakun,
            String group
    ){
        this.context = context;
        this.IDAKUN = idakun;
        this.GROUP = group;
    }

    public boolean getAPI(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = req.setHTTPCLIENT(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI interfaceApi = retrofit.create(InterfaceAPI.class);

        Call<JsonObject> call = null;
        call = interfaceApi.getapi_listsurvey(par.PARAM_DEFAULT(IDAKUN, GROUP));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            JSONObject datajson = obj_convert.getJSONObject("resmessage");
                            addDatabaseSurveyIN(datajson);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                        }

                    }catch (Exception e){
                        Log.d("TAGGAR", "ERROR "+e);
                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });

        return true;
    }

    public void addDatabaseSurveyIN(JSONObject datajson){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.execSQL("DELETE FROM "+database.TABLE_SURVEYIN);


        // ===== DATA WAITING ===== //
        try{
            JSONArray dataarray_waiting = datajson.getJSONArray("datawaiting");
            for (int i=0; i < dataarray_waiting.length(); i++){
                JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                int sin_id = datajson_obj.getInt("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String keterangan  = datajson_obj.getString("keterangan");

                values.put(database.COL_SIN_ID, sin_id);
                values.put(database.COL_SIN_CONTAINER, sin_container);
                values.put(database.COL_SIN_CONDITION, sin_condition);
                values.put(database.COL_SIN_CREATED, sin_created);
                values.put(database.COL_SIN_STATUS, sin_status);
                values.put(database.COL_KETERANGAN, keterangan);
                values.put(database.COL_DB_TIPE, var.TIPE_DB_WAITING);

                String selection = database.COL_SIN_ID+ " = ?";
                String[] selectionArgs = {String.valueOf(sin_id)};

                int update = db.update(database.TABLE_SURVEYIN, values, selection, selectionArgs);

                if(update == 0){
                    db.insert(database.TABLE_SURVEYIN, null, values);
                }
            }

            // === COMPARE DATA === //
            String selection = "SELECT COUNT(*) as JUMLAH"
                    +" FROM "+database.TABLE_SURVEYIN
                    +" WHERE "+database.COL_DB_TIPE+" = ?";
            String[] selectionArgs = {var.TIPE_DB_WAITING};
            Cursor cursor = db.rawQuery(selection, selectionArgs);

            if(cursor.moveToNext()){
                int jumlah = cursor.getInt(cursor.getColumnIndex("JUMLAH"));
                if(jumlah != dataarray_waiting.length()){

                    int selisih = jumlah - dataarray_waiting.length();
                    int ketemu = 0;

                    String selection_search = "SELECT "+database.COL_SIN_ID+" as sin_id"
                            +" FROM "+database.TABLE_SURVEYIN
                            +" WHERE "+database.COL_DB_TIPE+" = ?";
                    String[] selectionArgs_search = {var.TIPE_DB_WAITING};
                    Cursor cursor_search = db.rawQuery(selection_search, selectionArgs_search);

                    while(cursor_search.moveToNext() && ketemu != selisih){
                        int SIN_ID = cursor_search.getInt(cursor_search.getColumnIndex("sin_id"));
                        for (int i=0; i < dataarray_waiting.length(); i++) {
                            JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                            if (SIN_ID != datajson_obj.getInt("sin_id")){
                                String selection_del = database.COL_SIN_ID+ " = ?";
                                String[] selectionArgs_del = { String.valueOf(SIN_ID)};
                                db.delete(database.TABLE_SURVEYIN, selection_del, selectionArgs_del);
                                ketemu += 1;
                                Log.d("TAGGAR ", "MASUK");
                                break;
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }

        // ===== DATA APPROVE ===== //
        try{
            JSONArray dataarray_waiting = datajson.getJSONArray("dataapprove");
            for (int i=0; i < dataarray_waiting.length(); i++){
                JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                int sin_id = datajson_obj.getInt("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String keterangan  = datajson_obj.getString("keterangan");

                values.put(database.COL_SIN_ID, sin_id);
                values.put(database.COL_SIN_CONTAINER, sin_container);
                values.put(database.COL_SIN_CONDITION, sin_condition);
                values.put(database.COL_SIN_CREATED, sin_created);
                values.put(database.COL_SIN_STATUS, sin_status);
                values.put(database.COL_KETERANGAN, keterangan);
                values.put(database.COL_DB_TIPE, var.TIPE_DB_APPROVE);

                String selection = database.COL_SIN_ID+ " = ?";
                String[] selectionArgs = {String.valueOf(sin_id)};

                int update = db.update(database.TABLE_SURVEYIN, values, selection, selectionArgs);

                if(update == 0){
                    db.insert(database.TABLE_SURVEYIN, null, values);
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }

        // ===== DATA REPAIR ===== //
        try{
            JSONArray dataarray_waiting = datajson.getJSONArray("datarepair");
            for (int i=0; i < dataarray_waiting.length(); i++){
                JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                int sin_id = datajson_obj.getInt("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String keterangan  = datajson_obj.getString("keterangan");

                values.put(database.COL_SIN_ID, sin_id);
                values.put(database.COL_SIN_CONTAINER, sin_container);
                values.put(database.COL_SIN_CONDITION, sin_condition);
                values.put(database.COL_SIN_CREATED, sin_created);
                values.put(database.COL_SIN_STATUS, sin_status);
                values.put(database.COL_KETERANGAN, keterangan);
                values.put(database.COL_DB_TIPE, var.TIPE_DB_REPAIR);

                String selection = database.COL_SIN_ID+ " = ?";
                String[] selectionArgs = {String.valueOf(sin_id)};

                int update = db.update(database.TABLE_SURVEYIN, values, selection, selectionArgs);

                if(update == 0){
                    db.insert(database.TABLE_SURVEYIN, null, values);
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }

        // ===== DATA APPROVE REPAIR ===== //
        try{
            JSONArray dataarray_waiting = datajson.getJSONArray("datadonerepair");
            for (int i=0; i < dataarray_waiting.length(); i++){
                JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                int sin_id = datajson_obj.getInt("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String keterangan  = datajson_obj.getString("keterangan");

                values.put(database.COL_SIN_ID, sin_id);
                values.put(database.COL_SIN_CONTAINER, sin_container);
                values.put(database.COL_SIN_CONDITION, sin_condition);
                values.put(database.COL_SIN_CREATED, sin_created);
                values.put(database.COL_SIN_STATUS, sin_status);
                values.put(database.COL_KETERANGAN, keterangan);
                values.put(database.COL_DB_TIPE, var.TIPE_DB_APPROVE_REPAIR);

                String selection = database.COL_SIN_ID+ " = ?";
                String[] selectionArgs = {String.valueOf(sin_id)};

                int update = db.update(database.TABLE_SURVEYIN, values, selection, selectionArgs);

                if(update == 0){
                    db.insert(database.TABLE_SURVEYIN, null, values);
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }

        // ===== DATA DONE ===== //
        try{
            JSONArray dataarray_waiting = datajson.getJSONArray("datadone");
            for (int i=0; i < dataarray_waiting.length(); i++){
                JSONObject datajson_obj = dataarray_waiting.getJSONObject(i);
                int sin_id = datajson_obj.getInt("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String keterangan  = datajson_obj.getString("keterangan");

                values.put(database.COL_SIN_ID, sin_id);
                values.put(database.COL_SIN_CONTAINER, sin_container);
                values.put(database.COL_SIN_CONDITION, sin_condition);
                values.put(database.COL_SIN_CREATED, sin_created);
                values.put(database.COL_SIN_STATUS, sin_status);
                values.put(database.COL_KETERANGAN, keterangan);
                values.put(database.COL_DB_TIPE, var.TIPE_DB_DONE);

                String selection = database.COL_SIN_ID+ " = ?";
                String[] selectionArgs = {String.valueOf(sin_id)};

                int update = db.update(database.TABLE_SURVEYIN, values, selection, selectionArgs);

                if(update == 0){
                    db.insert(database.TABLE_SURVEYIN, null, values);
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }


        db.close();
    }

}
