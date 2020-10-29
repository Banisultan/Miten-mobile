package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Adapter.AddRepairAdapter;
import id.bnn.convey.Model.FileNameModel;
import id.bnn.convey.Model.ListAddRepairModel;
import id.bnn.convey.Model.ListFotoView;
//import id.bnn.convey.Service.CronUploadSurveyin;
import id.bnn.convey.Service.CronUploadSurveyinRepair;
import okhttp3.MediaType;
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

public class AddRepairActivity extends AppCompatActivity {

    Context context;


    ImageView tombolkembali_toolbar;
    TextView texttoolbar;
    String TEXT_TOOLBAR = "Add Foto Perbaikan";

    RecyclerView view;
    List<ListAddRepairModel> list;

    AddRepairAdapter adapter;

    SharedPreferences dataviewsurveyin;
    SharedPreferences dataakun;
    String ID;
    String GROUP;
    String ID_SURVEYIN;
    String DATA_SURVEY_DETAIL;

    LinearLayout tombolsimpan;

    AlertDialog.Builder message_alert;
    AlertDialog message_show;
    LayoutInflater message_inflate;
    View message_view;

    LinearLayout tombolyes;
    LinearLayout tombolno;
    TextView textjudul;
    TextView textmessage;
    TextView textyes;

    AlertDialog.Builder message_loading;
    AlertDialog show_loading;
    LayoutInflater inflate_loading;
    View view_loading;

    AlertDialog.Builder message_error;
    AlertDialog show_error;
    LayoutInflater inflate_error;
    View view_error;
    LinearLayout tombolyes_error;
    TextView textmessage_error;

    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();
    VariableText var = new VariableText();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair);

        dataviewsurveyin = getSharedPreferences("dataviewsurveyin", MODE_PRIVATE);
        dataakun = getSharedPreferences("dataakun", MODE_PRIVATE);
        ID = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        ID_SURVEYIN = getIntent().getStringExtra("idsurveyin");

        context = this;

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText(TEXT_TOOLBAR);
        tombolkembali_toolbar = findViewById(R.id.tombolkembali);
        tombolkembali_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        view = findViewById(R.id.viewrepair);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));

        tombolsimpan = findViewById(R.id.tombolsimpan);
        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_show.show();
            }
        });

        addData();
        showdialog();
        showdialog_loading();
        showdialog_error();
    }

    public void endactivity(){
        finish();
    }

    public void showdialog(){
        message_alert = new AlertDialog.Builder(context);
        message_inflate = getLayoutInflater();
        message_view = message_inflate.inflate(R.layout.layout_message_option_v2, null, false);
        message_alert.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_alert.setView(message_view);
        message_alert.setCancelable(false);
        message_show = message_alert.create();

        tombolyes = message_view.findViewById(R.id.tombolyes);
        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showloading();
                // DELAY
                new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                    public void onFinish() {
                        addDatabase();
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }
        });

        tombolno = message_view.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideall();
            }
        });

        textjudul = message_view.findViewById(R.id.textjudul);
        textjudul.setText("Perhatian");

        textmessage = message_view.findViewById(R.id.textmessage);
        textmessage.setText("Yakin akan menyimpan data perbaikan ?");

        textyes = message_view.findViewById(R.id.texttombolyes);
        textyes.setText("Simpan");
    }

    public void showdialog_loading(){
        message_loading = new AlertDialog.Builder(context);
        inflate_loading = getLayoutInflater();
        show_loading = message_loading.create();
        view_loading = inflate_loading.inflate(R.layout.layout_message_loading, null, false);
        message_loading.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_loading.setView(view_loading);
        message_loading.setCancelable(false);
        show_loading = message_loading.create();
    }

    public void showdialog_error(){
        message_error = new AlertDialog.Builder(context);
        inflate_error = getLayoutInflater();
        show_error = message_error.create();
        view_error = inflate_error.inflate(R.layout.layout_message_error, null, false);
        message_error.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_error.setView(view_error);
        message_error.setCancelable(false);
        show_error = message_error.create();

        tombolyes_error = view_error.findViewById(R.id.tombolyes);
        tombolyes_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGGAR", "tombol yes error");
                hideall();
            }
        });

        textmessage_error = view_error.findViewById(R.id.textmessage);
    }

    public void showloading(){
        message_show.dismiss();
        show_loading.show();
    }

    public void hideall(){
        message_show.dismiss();
        show_loading.dismiss();
        show_error.dismiss();
    }

    public void showerror(String text){
        show_loading.dismiss();
        message_show.dismiss();
        textmessage_error.setText(text);
        show_error.show();
    }

    public void addData(){
        list = new ArrayList<>();

        String data = dataviewsurveyin.getString("data", "");
        if(!data.equals("")){
            try{
                JSONObject datajson_utama = new JSONObject(data);
                JSONArray dataarray = datajson_utama.getJSONArray("detail");

                String temp = "";
                for (int i=0; i<dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);

                    String idsurveydetail = datajson.getString("id_survey_detail");
                    String component = datajson.getString("component");
                    String posisi = datajson.getString("posisi");
                    boolean isHeader;

                    if(temp.equals(posisi)){
                        isHeader = false;
                    }else{
                        isHeader = true;
                        temp = posisi;
                    }

                    ArrayList<String> listkerusakan = new ArrayList<>();
                    ArrayList<String> listperbaikan = new ArrayList<>();

                    JSONArray dataarray_kerusakan = datajson.getJSONArray("foto");
                    for (int j=0; j < dataarray_kerusakan.length(); j++){
                        JSONObject datajson_kerusakan = dataarray_kerusakan.getJSONObject(j);
                        listkerusakan.add(datajson_kerusakan.getString("url"));
                    }

                    list.add(new ListAddRepairModel(
                            idsurveydetail,
                            posisi,
                            false,
                            component,
                            listkerusakan,
                            listperbaikan,
                            dataarray_kerusakan.length()
                    ));
                }
            }catch (Exception e){
                Log.d("TAGGAR", "list gagal "+e);
            }
        }

        adapter = new AddRepairAdapter(this, context, list);
        view.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == var.CODE_CAPTURE_IMAGE_REPAIR){
                String url = FILE_IMAGE;
                ArrayList<String> listperbaikan = list.get(posisi_list).getListrepair();
                listperbaikan.add(url);
                list.get(posisi_list).setListrepair(listperbaikan);
                adapter.notifyDataSetChanged();
            }
        }else{
            Log.d("TAGGAR", "gagal "+resultCode+" reques "+requestCode);
        }
    }

    Uri uriimage;
    int posisi_list;
    public void opencamera(int position){
        posisi_list = position;
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.d("TAGGAR", "g "+ex);
        }

        uriimage= FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uriimage);
        cameraintent.putExtra("file_path",uriimage);
        ((Activity) context).startActivityForResult(cameraintent, var.CODE_CAPTURE_IMAGE_REPAIR);
    }

    String FILE_IMAGE;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        FILE_IMAGE = image.getAbsolutePath();
        return image;
    }

    public void moveactiviy(){
        hideall();
        Intent intent = new Intent(context, DoneActivity.class);
        intent.putExtra("text", "Berhasil Menambahkan Foto Perbaikan");
        startActivity(intent);
        finish();
    }

    public void getAPI_InsertSurveyRepair(){
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

        JSONArray dataarray = new JSONArray();
        JSONArray dataarray_foto = new JSONArray();

        try{
            int po = 0;
            for(int i=0; i < list.size(); i++){

                JSONObject datajson = new JSONObject();
                datajson.put("id", list.get(i).getIdsurveydetail());
                dataarray.put(i, datajson);
                //FOR IMAGE
                ArrayList<String> datarepair = list.get(i).getListrepair();

                Log.d("TAGGAR", "datarepair "+datarepair.size());
                for(int j=0; j < datarepair.size(); j++){
                    JSONObject datajson_foto = new JSONObject();
                    datajson_foto.put("id", list.get(i).getIdsurveydetail());
                    datajson_foto.put("url", datarepair.get(j));
                    dataarray_foto.put(po, datajson_foto);
                    po++;
                }
            }

            String data_surveyin_photo = String.valueOf(dataarray);
            byte[] bytejson = data_surveyin_photo.getBytes("UTF-8");

            base64 = Base64.encodeToString(bytejson, Base64.DEFAULT);
        }catch (Exception e){
            Log.d("TAGGAR", "gagal try "+e);
        }

        RequestBody data_id = RequestBody.create(MediaType.parse("text/plain"), ID);
        RequestBody data_group = RequestBody.create(MediaType.parse("text/plain"), GROUP);
        RequestBody data_surveyin_id = RequestBody.create(MediaType.parse("text/plain"), ID_SURVEYIN);
        RequestBody data_survey_detail = RequestBody.create(MediaType.parse("text/plain"), base64);
//        List<FileNameModel> data_foto =  par.PARAM_INSERT_FOTO_SURVEY_IN_REPAIR_V2(context, dataarray_foto);
        Log.d("TAGGAR", "ADD REPAIR "+base64);

        Call<JsonObject> call = null;

        call = interfaceApi.getapi_surveyin_repair(
                par.PARAM_INSERT_FOTO_SURVEY_IN_REPAIR(context, dataarray_foto),
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
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            moveactiviy();
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            showerror(addmessage);
                        }

                    }catch (Exception e){
                        showerror(var.MESSAGE_ALERT);
                    }
                }else{
                    showerror(var.MESSAGE_ALERT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                showerror(var.MESSAGE_ALERT);
            }
        });
    }

    public void addDatabase(){
        Database database = new Database(context);
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        String base64 = "";

        JSONArray dataarray = new JSONArray();
        JSONArray dataarray_foto = new JSONArray();

        try{
            int po = 0;
            for(int i=0; i < list.size(); i++){

                JSONObject datajson = new JSONObject();
                datajson.put("id", list.get(i).getIdsurveydetail());
                dataarray.put(i, datajson);
                //FOR IMAGE
                ArrayList<String> datarepair = list.get(i).getListrepair();

                Log.d("TAGGAR", "datarepair "+datarepair.size());
                for(int j=0; j < datarepair.size(); j++){
                    JSONObject datajson_foto = new JSONObject();
                    datajson_foto.put("id", list.get(i).getIdsurveydetail());
                    datajson_foto.put("url", datarepair.get(j));
                    dataarray_foto.put(po, datajson_foto);
                    po++;
                }
            }

            String data_surveyin_photo = String.valueOf(dataarray);
            byte[] bytejson = data_surveyin_photo.getBytes("UTF-8");

            base64 = Base64.encodeToString(bytejson, Base64.DEFAULT);
        }catch (Exception e){
            Log.d("TAGGAR", "gagal try "+e);
        }

        try{
            JSONObject datajson = new JSONObject();
            datajson.put("surveyin_detail", base64);
            datajson.put("idakun", ID);
            datajson.put("group", GROUP);
            datajson.put("idsurveyin", ID_SURVEYIN);
            datajson.put("data_array_foto", dataarray_foto);

            values.put(database.COL_UPL_SIN_ID, ID_SURVEYIN);
            values.put(database.COL_UPL_TIPE, "W");
            values.put(database.COL_UPL_TIPE_UPLOAD, "REPAIR");
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
        Intent i= new Intent(context, CronUploadSurveyinRepair.class);
        context.startService(i);
    }
}