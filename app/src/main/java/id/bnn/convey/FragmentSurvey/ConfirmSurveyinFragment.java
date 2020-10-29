package id.bnn.convey.FragmentSurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.bnn.convey.Database;
import id.bnn.convey.DoneActivity;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.Model.FileNameModel;
import id.bnn.convey.R;
//import id.bnn.convey.Service.CronUploadSurveyin;
import id.bnn.convey.Test.ProgressRequestBody;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import io.reactivex.FlowableEmitter;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmSurveyinFragment extends Fragment implements ProgressRequestBody.UploadCallbacks{

    TextView tombolsimpan;
    TextView tombolkembali;

    Context context;

    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();
    VariableText var = new VariableText();

    SharedPreferences datainputsurveyin;
    SharedPreferences dataakun;

    String ID;
    String GROUP;
    String DATA_SURVEY;
    String DATA_SURVEY_DETAIL;
    String DATA_HAPUS;
    String DATA_FOTO;
    String DATA_HAPUS_FOTO;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    AlertDialog.Builder message_loading;
    AlertDialog.Builder message_error;
    AlertDialog show_loading;
    AlertDialog show_error;
    LayoutInflater inflate_loading;
    LayoutInflater inflate_error;
    View view_loading;
    View view_error;

    LinearLayout tombolyes;
    TextView textmessage;
    TextView textjudul;

    boolean ACTION = false;

    String TIPE_ACTIVITY;

    public ConfirmSurveyinFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ) {
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        show_loading.dismiss();
        show_error.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_confirm_surveyin, container, false);
        context = container.getContext();

        for(int i=1; i<viewline.length; i++){
            viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
            viewline[i].setBackgroundResource(R.drawable.bg_line_on);
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_on);

        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        DATA_SURVEY = datainputsurveyin.getString("surveyin", "");
        DATA_SURVEY_DETAIL = datainputsurveyin.getString("surveyindetail", "");
        DATA_HAPUS = datainputsurveyin.getString("datahapus", "");
        DATA_FOTO = datainputsurveyin.getString("datafoto", "");
        DATA_HAPUS_FOTO = datainputsurveyin.getString("datahapusfoto", "");

        ID = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        TIPE_ACTIVITY = datainputsurveyin.getString("surveyintipe", "");

        tombolsimpan = itemview.findViewById(R.id.tombolsimpan);
        tombolkembali = itemview.findViewById(R.id.tombolkembali);

        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ACTION){
                    showdialog_loading();
                    // DELAY
                    new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                        public void onFinish() {
                            if(TIPE_ACTIVITY.equals("edit")){
                                getAPI_InsertSurvey();
                            }else {
                                addDatabase();
                            }
                        }

                        public void onTick(long millisUntilFinished) {

                        }
                    }.start();
                }
            }
        });

        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevfragment();
            }
        });

        showdialog();
        return itemview;
    }

    public void showdialog(){
        message_loading = new AlertDialog.Builder(context);
        message_error = new AlertDialog.Builder(context);

        inflate_loading = getLayoutInflater();
        inflate_error = getLayoutInflater();

        show_loading = message_loading.create();
        show_error = message_error.create();

        view_loading = inflate_loading.inflate(R.layout.layout_message_loading, null, false);
        view_error = inflate_error.inflate(R.layout.layout_message_error, null, false);

        message_loading.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_error.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);

        message_loading.setView(view_loading);
        message_error.setView(view_error);

        message_loading.setCancelable(false);
        message_error.setCancelable(false);

        show_loading = message_loading.create();
        show_error = message_error.create();

        tombolyes = view_error.findViewById(R.id.tombolyes);
        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_error.dismiss();
                ACTION = false;
            }
        });

        textmessage = view_error.findViewById(R.id.textmessage);

        textjudul = view_loading.findViewById(R.id.textjudul);
    }

    public void showdialog_loading(){
        ACTION = true;
        show_error.dismiss();
        show_loading.show();
    }

    public void showdialog_error(String message){
        ACTION = true;
        textmessage.setText(message);
        show_loading.dismiss();
        show_error.show();
    }

    public void prevfragment(){
        Fragment fragment = new InputKerusakan(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void getAPI_InsertSurvey(){
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

        List<FileNameModel> data_foto =  par.PARAM_INSERT_FOTO_SURVEY_IN_v2(context, DATA_SURVEY, DATA_SURVEY_DETAIL, DATA_FOTO);

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
            call = null;
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            moveactiviy();
                            ACTION = false;
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            showdialog_error(addmessage);
                        }

                    }catch (Exception e){
                        showdialog_error(var.MESSAGE_ALERT);
                    }
                }else{
                    showdialog_error(var.MESSAGE_ALERT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showdialog_error(var.MESSAGE_ALERT);
            }
        });
    }

    public void moveactiviy(){
        datainputsurveyin.edit().clear().apply();
        show_loading.dismiss();
        Intent intent = new Intent(context, DoneActivity.class);

        if(TIPE_ACTIVITY.equals("edit")){
            intent.putExtra("text", "Pengeditan Survey Berhasil");
        }else{
            intent.putExtra("text", "Pengisian Survey Berhasil");
        }

        startActivity(intent);
        ((Activity)context).finish();
    }

    @Override
    public void onProgressUpdate(float percentage, int jumlah) {
        textjudul.setText("Loading "+String.format("%.2f", percentage)+"%");
    }

    @Override
    public void onError() {

    }

    @Override
    public void onFinish() {

    }

    public void addDatabase(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        try{
            JSONObject datajson = new JSONObject();
            datajson.put("surveyin", DATA_SURVEY);
            datajson.put("surveyin_detail", DATA_SURVEY_DETAIL);
            datajson.put("surveyin_foto", DATA_FOTO);
            datajson.put("surveyin_hapus", DATA_HAPUS);
            datajson.put("surveyin_hapus_foto", DATA_HAPUS_FOTO);
            datajson.put("idakun", ID);
            datajson.put("group", GROUP);
            datajson.put("tipe", TIPE_ACTIVITY);

            values.put(database.COL_UPL_TIPE, "W");
            values.put(database.COL_UPL_TIPE_UPLOAD, "SURVEY");
            values.put(database.COL_UPL_DATA, datajson.toString());


        }catch (Exception e){
            Log.d("TAGGAR", "ERROR CREATE JSON "+e);
        }

        db.insert(database.TABLE_SURVEYIN_UPLOAD, null, values);
        db.close();

        runservice();
        moveactiviy();
    }

    public void runservice(){
//        Intent i= new Intent(context, CronUploadSurveyin.class);
//        context.startService(i);
    }
}
