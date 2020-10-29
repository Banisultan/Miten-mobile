package id.bnn.convey.Service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Database;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.Model.FileNameModel;
import id.bnn.convey.Test.ProgressRequestBody;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CronUploadSurveyinRepair extends IntentService{

    public CronUploadSurveyinRepair() {
        super("Testing");
    }

    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();
    VariableText var = new VariableText();

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        getDatabase();
    }

    String ID;
    String GROUP;
    String ID_SURVEYIN;
    String DATA_ARRAY_FOTO;
    String DATA_DETAIL;
    JSONArray dataarray_foto;

    Database database;
    SQLiteDatabase db;
    SQLiteDatabase db_w;

    public void getDatabase(){
        database = new Database(this);
        db = database.getReadableDatabase();
        db_w = database.getWritableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_SURVEYIN_UPLOAD
                +" WHERE "+database.COL_UPL_TIPE_UPLOAD+" = ?";
        String[] selectionArgs = {"REPAIR"};

        Cursor cursor = db.rawQuery(selection, selectionArgs);
        while(cursor.moveToNext()){
            Log.d("TAGGAR", "MASUK NIH ");
            try {
                String id = cursor.getString(cursor.getColumnIndex("upl_id"));
                String tipe = cursor.getString(cursor.getColumnIndex(database.COL_DB_TIPE));
                if(tipe.equals("X") || tipe.equals("P")) {
                    db.execSQL("DELETE FROM " + database.TABLE_SURVEYIN_UPLOAD + " WHERE " + database.COL_UPL_ID + " = " + id);
                }else{
                    String data = cursor.getString(cursor.getColumnIndex("upl_data"));
                    if (!data.equals("")) {
                        try {
                            JSONObject datajson = new JSONObject(data);
                            ID = datajson.getString("idakun");
                            GROUP = datajson.getString("group");
                            ID_SURVEYIN = datajson.getString("idsurveyin");
                            DATA_ARRAY_FOTO = datajson.getString("data_array_foto");
                            dataarray_foto = new JSONArray(DATA_ARRAY_FOTO);
                            DATA_DETAIL = datajson.getString("surveyin_detail");
                            getAPI_InsertSurveyRepair(id);
                        } catch (Exception e) {
                            Log.d("TAGGAR", "INI ERROR LOH "+e);
                        }
                    }
                }
                Thread.sleep(5000);
            }catch (Exception e){
                Thread.currentThread().interrupt();
            }
        }
        closedb();
    }

    public void closedb(){
        db.close();
        db_w.close();
    }

    public void updatestatus(String id, String status){
        database = new Database(this);
        db = database.getReadableDatabase();
        db_w = database.getWritableDatabase();
        String selection = database.COL_UPL_ID+ " = ?";
        String[] selectionArgs = {id};

        ContentValues values = new ContentValues();
        values.put(database.COL_UPL_TIPE, status);
        int update = db.update(database.TABLE_SURVEYIN_UPLOAD, values, selection, selectionArgs);
//        Toast.makeText(getApplicationContext(), "status update "+update, Toast.LENGTH_LONG).show();
//        db.execSQL("DELETE FROM "+database.TABLE_SURVEYIN_UPLOAD+" WHERE "+database.COL_UPL_ID+" = "+id);
        Log.d("TAGGAR", "STATUS UPDATE "+update);

        closedb();
    }



    public void getAPI_InsertSurveyRepair(String id){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = req.setHTTPCLIENT(this);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI interfaceApi = retrofit.create(InterfaceAPI.class);


        RequestBody data_id = RequestBody.create(MediaType.parse("text/plain"), ID);
        RequestBody data_group = RequestBody.create(MediaType.parse("text/plain"), GROUP);
        RequestBody data_surveyin_id = RequestBody.create(MediaType.parse("text/plain"), ID_SURVEYIN);
        RequestBody data_survey_detail = RequestBody.create(MediaType.parse("text/plain"), DATA_DETAIL);

        Call<JsonObject> call = null;

        call = interfaceApi.getapi_surveyin_repair(
                par.PARAM_INSERT_FOTO_SURVEY_IN_REPAIR(this, dataarray_foto),
                data_id,
                data_surveyin_id,
                data_survey_detail,
                data_group);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
//                        Toast.makeText(getApplicationContext(), "ini message "+obj_convert, Toast.LENGTH_LONG).show();
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            updatestatus(id, "P");
                        }else{
                            if(rescode.equals("145")){
                                updatestatus(id, "X");
                            }else{
//                                closedb();
                            }
                        }

                    }catch (Exception e){
//                        closedb();
//                        Toast.makeText(getApplicationContext(), "GAGAL "+e, Toast.LENGTH_LONG).show();
                    }
                }else{
//                    closedb();
//                    Toast.makeText(getApplicationContext(), "GAGAL 2", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                closedb();
//                Toast.makeText(getApplicationContext(), "GAGAL 3"+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
