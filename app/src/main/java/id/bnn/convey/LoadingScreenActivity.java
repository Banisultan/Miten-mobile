package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import id.bnn.convey.FinalActivity.HomeActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingScreenActivity extends AppCompatActivity {

    Context context;
    SharedPreferences datauser;
    String USERID;
    String TOKEN;

    VariableText var = new VariableText();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        context = this;

        datauser = getSharedPreferences("datauser", MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");

        callAPI_listall();
    }

    public void next(){
        callAPI_owner();
    }

    public void moveactivity(){
        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void callAPI_listall(){
        JsonArray dataarray = new JsonArray();

        JsonObject datajson_1 = new JsonObject();
        datajson_1.addProperty("title", "Authorization");
        datajson_1.addProperty("value", "Bearer "+TOKEN);

        dataarray.add(datajson_1);

        OkHttpClient.Builder httpClient = new VariableRequest().setHTTPCLIENT(dataarray);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL_v2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI_v2 interfaceApi = retrofit.create(InterfaceAPI_v2.class);

        JsonObject datajson = new JsonObject();
        datajson.addProperty("mobid", var.createMobID());
        datajson.addProperty("userid", USERID);

        Call<JsonObject> call = interfaceApi.list_all(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_all(obj_response.get("data").getAsJsonObject());
                    }else{
                        next();
                    }
                }else{
                    next();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                next();
            }
        });
    }

    public void callAPI_owner(){
        JsonArray dataarray = new JsonArray();

        JsonObject datajson_1 = new JsonObject();
        datajson_1.addProperty("title", "Authorization");
        datajson_1.addProperty("value", "Bearer "+TOKEN);

        dataarray.add(datajson_1);

        OkHttpClient.Builder httpClient = new VariableRequest().setHTTPCLIENT(dataarray);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL_v2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI_v2 interfaceApi = retrofit.create(InterfaceAPI_v2.class);

        JsonObject datajson = new JsonObject();
        datajson.addProperty("mobid", var.createMobID());
        datajson.addProperty("userid", USERID);

        Call<JsonObject> call = interfaceApi.list_owner(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_owner(obj_response.get("data").getAsJsonArray());
                    }else{
                        moveactivity();
                    }
                }else{
                    moveactivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                moveactivity();
            }
        });
    }

    public void addDatabase_all(JsonObject datajsonall){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        // === YEARS === //
        JsonArray list_year = datajsonall.get("list_years").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_YEAR);

        for(int i=0; i < list_year.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_YAR_VALUE, list_year.get(i).getAsString());
            db.insert(database.TABLE_YEAR, null, values);
        }

        // === WASH === //
        JsonArray list_wash = datajsonall.get("list_wash").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_WASH);

        for(int i=0; i < list_wash.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_wash.get(i).getAsJsonObject();
            values.put(database.COL_WAS_CODE, datajson.get("code").getAsString());
            values.put(database.COL_WAS_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_WASH, null, values);
        }

        // === TPC === //
        JsonArray list_tpc = datajsonall.get("list_tpc").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_TPC);

        for(int i=0; i < list_tpc.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_TPC_VALUE, list_tpc.get(i).getAsString());
            db.insert(database.TABLE_TPC, null, values);
        }

        // === TIPE === //
        JsonArray list_tipe = datajsonall.get("list_tipe").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_TIPE);

        for(int i=0; i < list_tipe.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_TIP_VALUE, list_tipe.get(i).getAsString());
            db.insert(database.TABLE_TIPE, null, values);
        }

        // === SIZE === //
        JsonArray list_size = datajsonall.get("list_size").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_SIZE);

        for(int i=0; i < list_size.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_SIZ_VALUE, list_size.get(i).getAsString());
            db.insert(database.TABLE_SIZE, null, values);
        }

        // === REPAIR === //
        JsonArray list_repair = datajsonall.get("list_repair").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_REPAIR);

        for(int i=0; i < list_repair.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_repair.get(i).getAsJsonObject();
            values.put(database.COL_RPR_CODE, datajson.get("code").getAsString());
            values.put(database.COL_RPR_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_REPAIR, null, values);
        }

        // === POSISI === //
        JsonArray list_posisi = datajsonall.get("list_posisi").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_POSISI);

        for(int i=0; i < list_posisi.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_posisi.get(i).getAsJsonObject();
            values.put(database.COL_PSI_CODE, datajson.get("code").getAsString());
            values.put(database.COL_PSI_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_POSISI, null, values);
        }

        // === GRADE === //
        JsonArray list_grade = datajsonall.get("list_grade").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_GRADE);

        for(int i=0; i < list_grade.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_GRD_VALUE, list_grade.get(i).getAsString());
            db.insert(database.TABLE_GRADE, null, values);
        }

        // === DIMENSION === //
        JsonArray list_dimension = datajsonall.get("list_dimension").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_DIMENSION);

        for(int i=0; i < list_dimension.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_dimension.get(i).getAsJsonObject();
            values.put(database.COL_DMN_CODE, datajson.get("code").getAsString());
            values.put(database.COL_DMN_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_DIMENSION, null, values);
        }

        // === DAMAGE === //
        JsonArray list_damage = datajsonall.get("list_damage").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_DAMAGE);

        for(int i=0; i < list_damage.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_damage.get(i).getAsJsonObject();
            values.put(database.COL_DMG_CODE, datajson.get("code").getAsString());
            values.put(database.COL_DMG_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_DAMAGE, null, values);
        }

        // === COMPONENT === //
        JsonArray list_component = datajsonall.get("list_component").getAsJsonArray();
        db.execSQL("DELETE FROM "+database.TABLE_COMPONENT);

        for(int i=0; i < list_component.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = list_component.get(i).getAsJsonObject();
            values.put(database.COL_CMP_CODE, datajson.get("code").getAsString());
            values.put(database.COL_CMP_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_COMPONENT, null, values);
        }

        db.close();
        next();
    }

    public void addDatabase_owner(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_OWNER);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_OW_CODE, datajson.get("code").getAsString());
            values.put(database.COL_OW_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_OWNER, null, values);
        }
        db.close();

        moveactivity();
    }
}