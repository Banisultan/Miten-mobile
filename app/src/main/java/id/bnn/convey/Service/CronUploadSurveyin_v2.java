package id.bnn.convey.Service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
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

public class CronUploadSurveyin_v2 extends JobService {

    String TAG = "TAGGAR";
    boolean endJob = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "Job Started");
        checkDatabase(jobParameters);
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "Job canceled ");
        endJob = true;
        return false;
    }

    public void checkDatabase(JobParameters params){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(endJob){
                    Log.d(TAG, "END SERVICE");
                    return;
                }

                boolean loop = true;
                int i = 1;
                for (int j =0; j < 10; j++){
                    Log.d(TAG, "Run "+i++);
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
//                getDatabase();



                Log.d(TAG, "Job finished");
                jobFinished(params, false);
            }
        }).start();
    }


    ArrayList<String> listfoto = new ArrayList<>();

    Database database;
    SQLiteDatabase db;
    String no_container;

    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();
    VariableText var = new VariableText();

    String DATA_SURVEY;
    String DATA_SURVEY_DETAIL;
    String DATA_HAPUS;
    String DATA_FOTO;
    String DATA_HAPUS_FOTO;
    String TIPE_ACTIVITY;

    String ID;
    String GROUP;

    public void getDatabase(){
        database = new Database(this);
        db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_SURVEYIN_UPLOAD
                +" WHERE "+database.COL_UPL_TIPE_UPLOAD+" = ?";

        String[] selectionArgs = {"SURVEY"};

        Cursor cursor = db.rawQuery(selection, selectionArgs);
        while(cursor.moveToNext()){
            try {
                String id = cursor.getString(cursor.getColumnIndex("upl_id"));
                String tipe = cursor.getString(cursor.getColumnIndex(database.COL_DB_TIPE));
                if(tipe.equals("X") || tipe.equals("P")){
                    db.execSQL("DELETE FROM "+database.TABLE_SURVEYIN_UPLOAD+" WHERE "+database.COL_UPL_ID+" = "+id);
                }else {
                    String data = cursor.getString(cursor.getColumnIndex("upl_data"));
                    if (!data.equals("")) {
                        try {
                            JSONObject datajson = new JSONObject(data);
                            DATA_SURVEY = datajson.getString("surveyin");
                            DATA_SURVEY_DETAIL = datajson.getString("surveyin_detail");
                            DATA_FOTO = datajson.getString("surveyin_foto");
                            DATA_HAPUS = datajson.getString("surveyin_hapus");
                            DATA_HAPUS_FOTO = datajson.getString("surveyin_hapus_foto");
                            ID = datajson.getString("idakun");
                            GROUP = datajson.getString("group");
                            TIPE_ACTIVITY = datajson.getString("tipe");

                            JSONObject datajson_new = new JSONObject(DATA_SURVEY);
                            no_container = datajson_new.getString("no_container");

                            getAPI_InsertSurvey(id);
                        } catch (Exception e) {
                            Log.d("TAGGAR", "TESTING CRON GAGAL 1 " + e);
                        }
                    }
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                Log.d("TAGGAR", "TESTING CRON GAGAL");
                Thread.currentThread().interrupt();
            }
        }

        db.close();
    }

    public void getAPI_InsertSurvey(String id){
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

        String base64 = "";
        String base64_de = "";
        String base64_hapus = "";
        String base64_foto = "";
        String base64_hapus_foto = "";
        try{
            byte[] bytejson = DATA_SURVEY.getBytes("UTF-8");
            byte[] bytejson_de = DATA_SURVEY_DETAIL.getBytes("UTF-8");
            byte[] bytejson_hapus = DATA_HAPUS.getBytes("UTF-8");
            byte[] bytejson_foto = DATA_FOTO.getBytes("UTF-8");
            byte[] bytejson_hapus_foto = DATA_HAPUS_FOTO.getBytes("UTF-8");

            base64 = Base64.encodeToString(bytejson, Base64.DEFAULT);
            base64_de = Base64.encodeToString(bytejson_de, Base64.DEFAULT);
            base64_hapus = Base64.encodeToString(bytejson_hapus, Base64.DEFAULT);
            base64_foto = Base64.encodeToString(bytejson_foto, Base64.DEFAULT);
            base64_hapus_foto = Base64.encodeToString(bytejson_hapus_foto, Base64.DEFAULT);
        }catch (Exception e){}

        RequestBody data_id = RequestBody.create(MediaType.parse("text/plain"), ID);
        RequestBody data_group = RequestBody.create(MediaType.parse("text/plain"), GROUP);
        RequestBody data_survey = RequestBody.create(MediaType.parse("text/plain"), base64);
        RequestBody data_survey_detail = RequestBody.create(MediaType.parse("text/plain"), base64_de);
        RequestBody data_hapus = RequestBody.create(MediaType.parse("text/plain"), base64_hapus);
        RequestBody data_foto_v2 = RequestBody.create(MediaType.parse("text/plain"), base64_foto);
        RequestBody data_hapus_foto = RequestBody.create(MediaType.parse("text/plain"), base64_hapus_foto);

        List<FileNameModel> data_foto =  par.PARAM_INSERT_FOTO_SURVEY_IN(this, DATA_SURVEY, DATA_SURVEY_DETAIL, DATA_FOTO, listfoto);
        List<MultipartBody.Part> body = new ArrayList<>();
        for (int i=0; i < data_foto.size(); i++){
            ProgressRequestBody file = new ProgressRequestBody(data_foto.get(i).getFile(), "image/*",  data_foto.size(), i);
            body.add(MultipartBody.Part.createFormData(data_foto.get(i).getName(), data_foto.get(i).getName(), file));
        }

        MultipartBody.Part[] bodyvalue = new MultipartBody.Part[body.size()];
        body.toArray(bodyvalue);

        Call<JsonObject> call = null;

        if(TIPE_ACTIVITY.equals("edit")){
            call = interfaceApi.getapi_editsurveyin(
                    bodyvalue,
                    data_id,
                    data_group,
                    data_survey,
                    data_survey_detail,
                    data_hapus,
                    data_foto_v2,
                    data_hapus_foto);
        }else{
            call = interfaceApi.getapi_insertsurveyin(
                    bodyvalue,
                    data_id,
                    data_group,
                    data_survey,
                    data_survey_detail,
                    data_foto_v2);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    Log.d("TAGGAR", "RESPONSE API 1"+obj_response);

                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            updatestatus(id, "P");
                            removefoto(listfoto);
                        }else{
                            if(rescode.equals("144") || rescode.equals("102")){
                                updatestatus(id, "X");
                            }
                        }

                    }catch (Exception e){

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t){
                Log.d("TAGGAR", "RESPONSE API "+t.getMessage());
            }
        });
    }

    public void removefoto(ArrayList<String> listfoto){
        for(int i =0; i < listfoto.size(); i++){
            File dir = new File(listfoto.get(i));
            boolean tes = dir.delete();
        }
    }

    public void updatestatus(String id, String status){
        database = new Database(this);
        db = database.getReadableDatabase();
        String selection = database.COL_UPL_ID+ " = ?";
        String[] selectionArgs = {id};

        ContentValues values = new ContentValues();
        values.put(database.COL_UPL_TIPE, status);
        int update = db.update(database.TABLE_SURVEYIN_UPLOAD, values, selection, selectionArgs);
        Log.d("TAGGAR", "STATUS UPDATE "+update);

        db.close();
    }
}