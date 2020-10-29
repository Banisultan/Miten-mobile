package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FinalActivity.SurveyinAddActivity;
import id.bnn.convey.Adapter.PositionPhotoAdapter_v2;
import id.bnn.convey.Adapter.ViewSurveyinAdapter;
import id.bnn.convey.Adapter.ViewSurveyinKerusakanAdapter;
import id.bnn.convey.Model.PositionPhotoModel_v2;
import id.bnn.convey.Model.ViewSurveyinKerusakanModel;
import id.bnn.convey.Model.ViewSurveyinModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ViewSurveyinActivity extends AppCompatActivity {

    Context context;

    LinearLayout layoututama;
    LinearLayout layoutdetail;
    LinearLayout layoutfotocontainer;

    ImageView tombolkembali_toolbar;
    TextView texttoolbar;
    String TEXT_TOOLBAR = "Detail Survey";
    ImageView tombolapprove;
    ImageView tombolapproverepair;
    ImageView tomboledit;
    ImageView tomboladd;
    ImageView tombollock;

    RecyclerView view;
    RecyclerView viewkerusakan;
    RecyclerView viewphoto;

    List<ViewSurveyinModel> list;
    List<ViewSurveyinKerusakanModel> listkerusakan;
    List<PositionPhotoModel_v2> listphoto;

    ViewSurveyinAdapter adapter;
    ViewSurveyinKerusakanAdapter adapterkerusakan;
    PositionPhotoAdapter_v2 adapterphoto;

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();

    SharedPreferences dataakun;
    SharedPreferences dataviewsurveyin;
    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor dataviewsurveyin_edit;
    SharedPreferences.Editor datainputsurveyin_edit;

    String ID;
    String GROUP;
    String ID_SURVEY;
    String STATUS;
    String STATUS_APPROVE;
    String TIPE;
    String LINK_CONTAINER;
    String LINK_CSC;

    Dialog dialog_image;
    ImageView image_view;
    ImageView imageclose_msg_image;
    LinearLayout tombolno_msg_image;

    AlertDialog.Builder message_alert;
    AlertDialog message_show;
    LayoutInflater message_inflate;
    View message_view;

    AlertDialog.Builder message_loading;
    AlertDialog message_show_loading;
    LayoutInflater inflate_loading;
    View view_loading;
    TextView textmessage_loading;

    AlertDialog.Builder message_error;
    AlertDialog message_show_error;
    LayoutInflater inflate_error;
    View view_error;
    TextView textmessage_error;
    LinearLayout tomboloke_error;

    TextView textjudul;
    TextView textmessage;
    TextView texttombolyes;
    LinearLayout tombolyes;
    LinearLayout tombolno;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
        hide_all();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        hide_all();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_surveyin);
        context = this;

        datainputsurveyin = getSharedPreferences("datainputsurveyin", MODE_PRIVATE);
        datainputsurveyin.edit().clear().apply();

        layoututama = findViewById(R.id.layoututama);
        layoutdetail = findViewById(R.id.layout_detail);
        layoutfotocontainer = findViewById(R.id.layoutfotocontainer);

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText(TEXT_TOOLBAR);
        tombolkembali_toolbar = findViewById(R.id.tombolkembali);
        tombolkembali_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        tombolapproverepair = findViewById(R.id.tombolapproverepair);
        tombolapproverepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STATUS_APPROVE = "repair";
                show_alert();
            }
        });

        tombolapprove = findViewById(R.id.tombolapprove);
        tombolapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STATUS_APPROVE = "default";
                show_alert();
            }
        });

        tomboledit = findViewById(R.id.tomboledit);
        tomboledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_all();
                savedata_toedit();
            }
        });

        tomboladd = findViewById(R.id.tomboladd);
        tomboladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveactivity_repair(ID_SURVEY);
            }
        });

        tombollock = findViewById(R.id.tombollock);
        tombollock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Glide.with(context).load(R.drawable.img_back).into(tombolkembali_toolbar);
        Glide.with(context).load(R.drawable.img_edit_v2).into(tomboledit);
        Glide.with(context).load(R.drawable.img_done_v2).into(tombolapprove);

        dataakun = getSharedPreferences("dataakun", MODE_PRIVATE);
        dataviewsurveyin = getSharedPreferences("dataviewsurveyin", MODE_PRIVATE);
        ID = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        ID_SURVEY = getIntent().getStringExtra("idsurvei");
        TIPE = dataakun.getString("tipe", "");

        view = findViewById(R.id.view);
        view.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);
        view.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });


        viewkerusakan = findViewById(R.id.viewkerusakan);
        viewkerusakan.setHasFixedSize(true);
        view.setNestedScrollingEnabled(false);
        viewkerusakan.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        viewphoto = findViewById(R.id.viewphoto);
        viewphoto.setHasFixedSize(true);
        viewphoto.setNestedScrollingEnabled(false);
        viewphoto.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        hideallbutton();
        messagedialog_image();
        showdialog();
        runAPI();
    }

    public void messagedialog_image(){
        dialog_image = new Dialog(context);
        dialog_image.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_image.setContentView(R.layout.layout_image_v2);
        dialog_image.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tombolno_msg_image = dialog_image.findViewById(R.id.tombolno);
        tombolno_msg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidemessage_image();
            }
        });

        image_view = dialog_image.findViewById(R.id.imageview);
        imageclose_msg_image = dialog_image.findViewById(R.id.imageclose);
        Glide.with(context).load(R.drawable.img_close).into(imageclose_msg_image);
    }

    public void showdialog_image(String image){
        Glide.with(context).load(image).into(image_view);
        dialog_image.show();
    }

    public void hidemessage_image(){
        dialog_image.dismiss();
    }

    public void showbutton(){
        if(TIPE.equals("SUPERVISOR")){
            switch (STATUS){
                case "BA":
                    tomboledit.setVisibility(View.VISIBLE);
                    tombolapprove.setVisibility(View.VISIBLE);
                    break;
                case "SP":
                    tombolapproverepair.setVisibility(View.VISIBLE);
                    break;
            }
        }else
            if(TIPE.equals("QC_REPAIR")){
                switch (STATUS){
                    case "AO":
                        tomboladd.setVisibility(View.VISIBLE);
                        break;
                }
            }
    }

    public void hideallbutton(){
        layoututama.setVisibility(View.GONE);
        tomboledit.setVisibility(View.GONE);
        tomboladd.setVisibility(View.GONE);
        tombolapprove.setVisibility(View.GONE);
        tombollock.setVisibility(View.GONE);
        tombolapproverepair.setVisibility(View.GONE);
    }

    public void runAPI(){
        show_loading("Sedang memuat data");
        // DELAY
        new CountDownTimer(var.DELAY_MESSAGE, 1000) {
            public void onFinish() {
                getAPI_viewsurvey(ID_SURVEY);
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    public void show_layout_utama(){
        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGEV2);
        transition.addTarget(layoututama);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoututama.setVisibility(View.VISIBLE);
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

        message_loading = new AlertDialog.Builder(context);
        inflate_loading = getLayoutInflater();
        message_show_loading = message_loading.create();
        view_loading = inflate_loading.inflate(R.layout.layout_message_loading, null, false);
        message_loading.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_loading.setView(view_loading);
        message_loading.setCancelable(false);
        message_show_loading = message_loading.create();
        textmessage_loading = view_loading.findViewById(R.id.textmessage);

        message_error = new AlertDialog.Builder(context);
        inflate_error = getLayoutInflater();
        message_show_error = message_error.create();
        view_error = inflate_error.inflate(R.layout.layout_message_error, null, false);
        message_error.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_error.setView(view_error);
        message_error.setCancelable(false);
        message_show_error = message_error.create();
        textmessage_error = view_error.findViewById(R.id.textmessage);
        tomboloke_error = view_error.findViewById(R.id.tombolyes);

        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_loading("Sedang memproses data");
                // DELAY
                new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                    public void onFinish() {
                        getAPI_approvesurvei(ID, GROUP, ID_SURVEY, STATUS_APPROVE);
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }
        });

        tomboloke_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_all();
            }
        });

        tombolno = message_view.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide_all();
            }
        });

        texttombolyes = message_view.findViewById(R.id.texttombolyes);
        texttombolyes.setText("Approve");

        textmessage = message_view.findViewById(R.id.textmessage);
        textmessage.setText("Yakin akan meng approve survey ini ?");

        textjudul = message_view.findViewById(R.id.textjudul);
        textjudul.setText("Perhatian");


    }

    public void hide_all(){
        message_show.dismiss();
        message_show_error.dismiss();
        message_show_loading.dismiss();
    }

    public void show_loading(String message){
        message_show.dismiss();
        message_show_error.dismiss();

        textmessage_loading.setText(message);
        message_show_loading.show();
    }

    public void show_error(String message){
        message_show.dismiss();
        message_show.dismiss();

        textmessage_error.setText(message);
        message_show_error.show();
    }

    public void show_alert(){
        message_show_loading.dismiss();
        message_show_error.dismiss();
        message_show.show();
    }

    public void endactivity(){
        hide_all();
        finish();
    }

    public void savedata_toedit(){
        dataviewsurveyin = context.getSharedPreferences("dataviewsurveyin", MODE_PRIVATE);
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", MODE_PRIVATE).edit();

        String dataview = dataviewsurveyin.getString("data", "");
        if(!dataview.equals("")){
            try {
                // GET DATA SURVEY IN
                JSONObject datajsonview = new JSONObject(dataview);

                JSONObject datajson = datajsonview.getJSONObject("datasurvey");
                String surveyid = datajson.getString("id_survey");
                String container = datajson.getString("container_num");
                String ownercode = datajson.getString("owner_code");
                String washcode = datajson.getString("wash_code");
                String size = datajson.getString("size");
                String tipe = datajson.getString("tipe");
                String haulier = datajson.getString("haulier");
                String grade = datajson.getString("grade");
                String[] dmf = datajson.getString("dmf").split("-");
                String bulan = dmf[0];
                String tahun = dmf[1];
                String payload = datajson.getString("payload");
                String tare = datajson.getString("tare");
                String maxgross = datajson.getString("maxgross");

                datainputsurveyin_edit.putString("surveyid", surveyid);
                datainputsurveyin_edit.putString("nocontainer", container);
                datainputsurveyin_edit.putString("haulier", haulier);
                datainputsurveyin_edit.putString("tare", tare);
                datainputsurveyin_edit.putString("payload", payload);
                datainputsurveyin_edit.putString("maxgross", maxgross);
                datainputsurveyin_edit.putString("ownercode", ownercode);
                datainputsurveyin_edit.putString("size", size);
                datainputsurveyin_edit.putString("tipe", tipe);
                datainputsurveyin_edit.putString("bulan", bulan);
                datainputsurveyin_edit.putString("tahun", tahun);
                datainputsurveyin_edit.putString("grade", grade);
                datainputsurveyin_edit.putString("washcode", washcode);

                // GET DATA SURVEY IN PHOTO
                JSONArray dataarrayphoto = datajsonview.getJSONArray("fotocontainer");
                JSONArray dataarrayphoto_new = new JSONArray();

                for(int i=0; i < dataarrayphoto.length(); i++){
                    JSONObject datajsonphoto = dataarrayphoto.getJSONObject(i);

                    String posisi = datajsonphoto.getString("posisi");

                    JSONArray dataarrayphoto_data = datajsonphoto.getJSONArray("url");
                    for (int j=0; j < dataarrayphoto_data.length(); j++) {
                        JSONObject datajsonphoto_data = dataarrayphoto_data.getJSONObject(j);
                        String photoid = datajsonphoto_data.getString("id_foto");
                        String filetemp = datajsonphoto_data.getString("image");

                        JSONObject datajsonphoto_new = new JSONObject();
                        datajsonphoto_new.put("mobid_photo", photoid);
                        datajsonphoto_new.put("mobid_damage", "");
                        datajsonphoto_new.put("file_temp", filetemp);
                        datajsonphoto_new.put("posisi", posisi);
                        datajsonphoto_new.put("tipe", "C");
                        datajsonphoto_new.put("action", "edit");
                        dataarrayphoto_new.put(datajsonphoto_new);
                    }
                }

                // GET DATA SURVEY IN KERUSAKAN
                JSONArray dataarraydamage = datajsonview.getJSONArray("detail");
                JSONArray dataarraydamage_new = new JSONArray();

                for(int i=0; i < dataarraydamage.length(); i++){
                    JSONObject datajsondamage = dataarraydamage.getJSONObject(i);

                    String damageid = datajsondamage.getString("id_survey_detail");
                    String component_name = datajsondamage.getString("component");
                    String component_code = datajsondamage.getString("component_code");
                    String location = datajsondamage.getString("location");
                    String dimension = datajsondamage.getString("dimensi");
                    String quantity = datajsondamage.getString("quantity");
                    String damage_name = datajsondamage.getString("damage");
                    String damage_code = datajsondamage.getString("damage_code");
                    String tpc = datajsondamage.getString("tpc");
                    String repair_name = datajsondamage.getString("repair");
                    String repair_code = datajsondamage.getString("repair_code");

                    JSONArray arrayfotodamage = datajsondamage.getJSONArray("foto");
                    for(int j=0; j<arrayfotodamage.length(); j++){
                        JSONObject jsonfotodamage = arrayfotodamage.getJSONObject(j);

                        String photoid = jsonfotodamage.getString("id_foto");
                        String filetemp = jsonfotodamage.getString("url");

                        JSONObject jsonfotodamage_new = new JSONObject();
                        jsonfotodamage_new.put("mobid_photo", photoid);
                        jsonfotodamage_new.put("mobid_damage", damageid);
                        jsonfotodamage_new.put("file_temp", filetemp);
                        jsonfotodamage_new.put("posisi", "");
                        jsonfotodamage_new.put("tipe", "D");
                        jsonfotodamage_new.put("action", "edit");
                        dataarrayphoto_new.put(jsonfotodamage_new);
                    }

                    JSONObject jsondamage = new JSONObject();
                    jsondamage.put("componentcode", component_code);
                    jsondamage.put("componentname", component_name);
                    jsondamage.put("damagecode", damage_code);
                    jsondamage.put("damagename", damage_name);
                    jsondamage.put("dimension", dimension);
                    jsondamage.put("repaircode", repair_code);
                    jsondamage.put("repairname", repair_name);
                    jsondamage.put("quantity", quantity);
                    jsondamage.put("location", location);
                    jsondamage.put("tpc", tpc);
                    jsondamage.put("countimage", arrayfotodamage.length());
                    jsondamage.put("mobid_damage", damageid);
                    jsondamage.put("action", "edit");

                    dataarraydamage_new.put(jsondamage);
                }
                datainputsurveyin_edit.putString("photo", String.valueOf(dataarrayphoto_new));
                datainputsurveyin_edit.putString("datadamage", String.valueOf(dataarraydamage_new));
                datainputsurveyin_edit.apply();

                moveactivity();
            }catch (Exception e){
                Log.d("TAGGAR", "error save "+e);
            }
        }
    }

    public void savedata(JSONObject datajson){
        dataviewsurveyin_edit = context.getSharedPreferences("dataviewsurveyin", MODE_PRIVATE).edit();
        dataviewsurveyin_edit.putString("data", String.valueOf(datajson));
        dataviewsurveyin_edit.apply();

        addData();
    }
    
    public void moveactivity(){
        Intent intent = new Intent(context, SurveyinAddActivity.class);
        intent.putExtra("toolbar", "Pengeditan Survey IN");
        intent.putExtra("action", "edit");
        startActivity(intent);
        finish();
    }

    public void moveactivity_repair(String idsurveyin){
        Intent intent = new Intent(context, AddRepairActivity.class);
        intent.putExtra("idsurveyin", idsurveyin);
        startActivity(intent);
        finish();
    }

    public void addData(){
        list = new ArrayList<>();
        listkerusakan = new ArrayList<>();
        listphoto = new ArrayList<>();

        String data = dataviewsurveyin.getString("data", "");
        if(!data.equals("")){
            try{
                JSONObject dataobj = new JSONObject(data);

                JSONObject datajson = dataobj.getJSONObject("datasurvey");
                String status_text = datajson.getString("status_text");
                String status = datajson.getString("status");
                String container = datajson.getString("container_num");
                String waktu = datajson.getString("created_text");
                String nama_pengisi = datajson.getString("pengisi");
                String link_container = datajson.getString("link_container");
                String link_csc = datajson.getString("link_csc");
                String owner = datajson.getString("owner_code");
                String size = datajson.getString("size");
                String tipe = datajson.getString("tipe");
                String haulier = datajson.getString("haulier");
                String dmf = datajson.getString("dmf_text");
                String payload = datajson.getString("payload");
                String tare = datajson.getString("tare");
                String maxgross = datajson.getString("maxgross");
                String grade  = datajson.getString("grade");
                String wash_code = datajson.getString("wash_code");

                LINK_CONTAINER = link_container;
                LINK_CSC =link_csc;

                STATUS = status;
                showbutton();

                list.add(new ViewSurveyinModel("Status", status_text));
                list.add(new ViewSurveyinModel("Nomor Container", container));
                list.add(new ViewSurveyinModel("Waktu Pengisian", waktu));
                list.add(new ViewSurveyinModel("Nama Pengisi", nama_pengisi));
                list.add(new ViewSurveyinModel("Owner Code", owner));
                list.add(new ViewSurveyinModel("Size", size));
                list.add(new ViewSurveyinModel("Tipe", tipe));
                list.add(new ViewSurveyinModel("Haulier", haulier));
                list.add(new ViewSurveyinModel("DMF", dmf));
                list.add(new ViewSurveyinModel("Payload", payload));
                list.add(new ViewSurveyinModel("Tare", tare));
                list.add(new ViewSurveyinModel("Maxgross", maxgross));
                list.add(new ViewSurveyinModel("Grade", grade));
                list.add(new ViewSurveyinModel("Wash Code", wash_code));

                JSONArray dataarray = dataobj.getJSONArray("detail");

                if(dataarray.length() == 0){
                    layoutdetail.setVisibility(View.GONE);
                }

                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson_detail = dataarray.getJSONObject(i);
                    String component = datajson_detail.getString("component_code") +" - "+ datajson_detail.getString("component");
                    String location = datajson_detail.getString("location");
                    String dimensi = datajson_detail.getString("dimensi");
                    String quantity = datajson_detail.getString("quantity");
                    String damage = datajson_detail.getString("damage_code")+" - "+datajson_detail.getString("damage");
                    String posisi = datajson_detail.getString("posisi");
                    String tpc = datajson_detail.getString("tpc");
                    String repair = datajson_detail.getString("repair_code") +" - "+datajson_detail.getString("repair");

                    ArrayList<String> foto = new ArrayList<>();
                    JSONArray datafoto = datajson_detail.getJSONArray("foto");
                    for(int j=0; j < datafoto.length(); j++){
                        JSONObject datajson_foto = datafoto.getJSONObject(j);
                        foto.add(datajson_foto.getString("url"));
                    }

                    ArrayList<String> fotorepair = new ArrayList<>();
                    JSONArray datafotorepair = datajson_detail.getJSONArray("fotorepair");
                    for(int j=0; j < datafotorepair.length(); j++){
                        JSONObject datajson_foto = datafotorepair.getJSONObject(j);
                        fotorepair.add(datajson_foto.getString("url"));
                    }

                    listkerusakan.add(new ViewSurveyinKerusakanModel(
                            component,
                            location,
                            quantity,
                            damage,
                            tpc,
                            repair,
                            dimensi,
                            posisi,
                            foto,
                            fotorepair));
                }

                JSONArray dataarray_photo = dataobj.getJSONArray("fotocontainer");

                if(dataarray_photo.length() == 0){
                    layoutfotocontainer.setVisibility(View.GONE);
                }

                for (int i=0; i < dataarray_photo.length(); i++){
                    JSONObject datajson_photo = dataarray_photo.getJSONObject(i);

                    JSONArray dataarray_photo_v2 = datajson_photo.getJSONArray("url");
                    ArrayList<String> listarray = new ArrayList<>();
                    for(int x=0; x < dataarray_photo_v2.length(); x++){
                        JSONObject datajson_photo_v2 = dataarray_photo_v2.getJSONObject(x);
                        listarray.add(datajson_photo_v2.getString("image"));
                    }

                    listphoto.add(new PositionPhotoModel_v2(
                            datajson_photo.getString("posisi"),
                            listarray
                    ));
                }

                adapterphoto = new PositionPhotoAdapter_v2(this, listphoto);
                viewphoto.setAdapter(adapterphoto);
            }catch (Exception e){
                Log.d("TAGGAR", "view "+e);
            }
        }

        adapter = new ViewSurveyinAdapter(context, list);
        adapterkerusakan = new ViewSurveyinKerusakanAdapter(this, listkerusakan);

        view.setAdapter(adapter);
        viewkerusakan.setAdapter(adapterkerusakan);

        show_layout_utama();
    }

    public void getAPI_viewsurvey(String idsurvey){
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
        call = interfaceApi.getapi_viewsurvey(par.PARAM_VIEW_SURVEY(ID, GROUP, idsurvey));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            JSONObject objdata = obj_convert.getJSONObject("resmessage");
                            savedata(objdata);
                            hide_all();
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            show_error(addmessage);
                        }

                    }catch (Exception e){
                        show_error(var.MESSAGE_ALERT);
                    }
                }else{
                    show_error(var.MESSAGE_ALERT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                show_error(var.MESSAGE_ALERT);
            }
        });
    }

    public void moveactivity_done(String text){
        hide_all();
        Intent intent = new Intent(context, DoneActivity.class);
        intent.putExtra("text", text);
        startActivity(intent);
        finish();
    }

    public void getAPI_approvesurvei(String idakun, String group, String idsurvei, String status){
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

        if(status.equals("repair")){
            call = interfaceApi.getapi_surveyin_repair_approve(par.PARAM_APPROVE_SURVEI(idakun, group, idsurvei));
        }else{
            call = interfaceApi.getapi_approvesurvey(par.PARAM_APPROVE_SURVEI(idakun, group, idsurvei));
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
                            String text = "";
                            if(status.equals("repair")){
                                text = "Berhasil Approve Foto Perbaikan";
                            }else{
                                text = "Berhasil Approve Survey in";
                            }

                            moveactivity_done(text);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            show_error(addmessage);
                        }

                    }catch (Exception e){
                        show_error(var.MESSAGE_ALERT);
                    }
                }else{
                    show_error(var.MESSAGE_ALERT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                show_error(var.MESSAGE_ALERT);
            }
        });
    }
}