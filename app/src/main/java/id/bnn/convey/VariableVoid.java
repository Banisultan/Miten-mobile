package id.bnn.convey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VariableVoid {

    VariableText var = new VariableText();
    Context context;
    String USERID;
    String TOKEN;

    public VariableVoid(Context context){
        this.context = context;
    }

    public VariableVoid(Context context, String userid, String token){
        this.context = context;
        this.USERID = userid;
        this.TOKEN = token;
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
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listwash(){
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

        Call<JsonObject> call = interfaceApi.list_wash(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_wash(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listposisi(){
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

        Call<JsonObject> call = interfaceApi.list_posisi(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_posisi(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listsize(){
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

        Call<JsonObject> call = interfaceApi.list_size(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_size(obj_response.get("data").getAsJsonArray());

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listtipe(){
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

        Call<JsonObject> call = interfaceApi.list_tipe(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_tipe(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listgrade(){
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

        Call<JsonObject> call = interfaceApi.list_grade(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_grade(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listyear(){
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

        Call<JsonObject> call = interfaceApi.list_year(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_year(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listtpc(){
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

        Call<JsonObject> call = interfaceApi.list_tpc(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_tpc(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listcomponent(){
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

        Call<JsonObject> call = interfaceApi.list_component(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_component(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listdamage(){
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

        Call<JsonObject> call = interfaceApi.list_damage(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_damage(obj_response.get("data").getAsJsonArray());

                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listdimension(){
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

        Call<JsonObject> call = interfaceApi.list_dimension(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_dimension(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void callAPI_listrepair(){
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

        Call<JsonObject> call = interfaceApi.list_repair(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        addDatabase_repair(obj_response.get("data").getAsJsonArray());
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    public void addDatabase_posisi(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_POSISI);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_PSI_CODE, datajson.get("code").getAsString());
            values.put(database.COL_PSI_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_POSISI, null, values);
        }
        db.close();
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
    }

    public void addDatabase_wash(JsonArray dataarray){
//        db.execSQL("DELETE FROM "+database.TABLE_WASH);
//
//        for(int i=0; i < dataarray.size(); i++){
//            ContentValues values = new ContentValues();
//            JsonObject datajson = dataarray.get(i).getAsJsonObject();
//            values.put(database.COL_WAS_CODE, datajson.get("code").getAsString());
//            values.put(database.COL_WAS_NAME, datajson.get("nama").getAsString());
//
//            db.insert(database.TABLE_WASH, null, values);
//        }
//        db.close();
    }

    public void addDatabase_component(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_COMPONENT);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_CMP_CODE, datajson.get("code").getAsString());
            values.put(database.COL_CMP_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_COMPONENT, null, values);
        }
        db.close();
    }

    public void addDatabase_damage(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_DAMAGE);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_DMG_CODE, datajson.get("code").getAsString());
            values.put(database.COL_DMG_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_DAMAGE, null, values);
        }
        db.close();
    }

    public void addDatabase_dimension(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_DIMENSION);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_DMN_CODE, datajson.get("code").getAsString());
            values.put(database.COL_DMN_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_DIMENSION, null, values);
        }
        db.close();
    }

    public void addDatabase_repair(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_REPAIR);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_RPR_CODE, datajson.get("code").getAsString());
            values.put(database.COL_RPR_NAME, datajson.get("nama").getAsString());

            db.insert(database.TABLE_REPAIR, null, values);
        }
        db.close();
    }

    public void addDatabase_kategori(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_KATEGORI);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            values.put(database.COL_KAT_ID, datajson.get("id").getAsString());
            values.put(database.COL_KAT_NAME, datajson.get("nama").getAsString());
            values.put(database.COL_KAT_IMAGE, datajson.get("image").getAsString());

            db.insert(database.TABLE_KATEGORI, null, values);
        }
        db.close();
    }

    public void addDatabase_size(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_SIZE);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_SIZ_VALUE, dataarray.get(i).getAsString());
            db.insert(database.TABLE_SIZE, null, values);
        }
        db.close();
    }

    public void addDatabase_tipe(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_TIPE);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_TIP_VALUE, dataarray.get(i).getAsString());
            db.insert(database.TABLE_TIPE, null, values);
        }
        db.close();
    }

    public void addDatabase_grade(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_GRADE);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_GRD_VALUE, dataarray.get(i).getAsString());
            db.insert(database.TABLE_GRADE, null, values);
        }
        db.close();
    }

    public void addDatabase_year(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_YEAR);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_YAR_VALUE, dataarray.get(i).getAsString());
            db.insert(database.TABLE_YEAR, null, values);
        }
        db.close();
    }

    public void addDatabase_tpc(JsonArray dataarray){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_TPC);

        for(int i=0; i < dataarray.size(); i++){
            ContentValues values = new ContentValues();
            values.put(database.COL_TPC_VALUE, dataarray.get(i).getAsString());
            db.insert(database.TABLE_TPC, null, values);
        }
        db.close();
    }

    public long addDatabase_photo(
            String mobiid,
            String mobiid_photo,
            String mobiid_damage,
            String filepath,
            String posisi,
            String tipe,
            String idsurvey,
            String userid
    ){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(database.COL_UPLP_USERID, userid);
        values.put(database.COL_UPLP_MOBID, mobiid);
        values.put(database.COL_UPLP_MOBID_PHOTO, mobiid_photo);
        values.put(database.COL_UPLP_MOBID_DAMAGE, mobiid_damage);
        values.put(database.COL_UPLP_LOCATION, filepath);
        values.put(database.COL_UPLP_POSITION, posisi);
        values.put(database.COL_UPLP_TIPE, tipe);
        values.put(database.COL_UPLP_SURVEYID, idsurvey);
        values.put(database.COL_UPLP_STATUS, "W");
        values.put(database.COL_UPLP_COUNT, 0);
        values.put(database.COL_UPLP_TIME, var.datetimenow());
        long id = db.insert(database.TABLE_UPLOAD_PHOTO, null, values);
        db.close();

        return id;
    }

    public void removeDatabase_photo(
            String id
    ){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();

        db.execSQL("DELETE FROM "+database.TABLE_UPLOAD_PHOTO+" WHERE "+database.COL_UPLP_ID+" = "+id);
        db.close();
    }

    public JsonArray getDatabase_posisi(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_POSISI;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_PSI_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_PSI_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_owner(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_OWNER;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_OW_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_OW_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_wash(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_WASH;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_WAS_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_WAS_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_size(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_SIZE;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            dataarray.add(cursor.getString(cursor.getColumnIndex(database.COL_SIZ_VALUE)));
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_tipe(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_TIPE;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            dataarray.add(cursor.getString(cursor.getColumnIndex(database.COL_TIP_VALUE)));
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_grade(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_GRADE;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            dataarray.add(cursor.getString(cursor.getColumnIndex(database.COL_GRD_VALUE)));
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_year(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_YEAR;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            dataarray.add(cursor.getString(cursor.getColumnIndex(database.COL_YAR_VALUE)));
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_tpc(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_TPC;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            dataarray.add(cursor.getString(cursor.getColumnIndex(database.COL_TPC_VALUE)));
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_kategori(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_KATEGORI;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("id", cursor.getString(cursor.getColumnIndex(database.COL_KAT_ID)));
            datajson.addProperty("nama", cursor.getString(cursor.getColumnIndex(database.COL_KAT_NAME)));
            datajson.addProperty("image", cursor.getString(cursor.getColumnIndex(database.COL_KAT_IMAGE)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_component(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_COMPONENT;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_CMP_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_CMP_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_damage(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_DAMAGE;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_DMG_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_DMG_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_dimension(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_DIMENSION;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_DMN_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_DMN_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }

    public JsonArray getDatabase_repair(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_REPAIR;

        String[] selectionArgs = {};
        Cursor cursor = db.rawQuery(selection, selectionArgs);

        JsonArray dataarray = new JsonArray();
        while (cursor.moveToNext()){
            JsonObject datajson = new JsonObject();
            datajson.addProperty("code", cursor.getString(cursor.getColumnIndex(database.COL_RPR_CODE)));
            datajson.addProperty("name", cursor.getString(cursor.getColumnIndex(database.COL_RPR_NAME)));
            dataarray.add(datajson);
        }
        db.close();
        return dataarray;
    }
}
