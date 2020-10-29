package id.bnn.convey.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FinalActivity.SurveyinAddActivity;
import id.bnn.convey.Adapter.ListFotoViewDepanAdapter;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.Model.ListFotoViewModel;
import id.bnn.convey.R;
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

public class ViewSurveyActivity extends AppCompatActivity {

    Context context;

    LinearLayout layoutmessage_option;
    LinearLayout tombolleft;
    LinearLayout tombolright;
    TextView textmessage_option;
    TextView textjudul_option;
    TextView texttombolleft;
    TextView texttombolright;
    ImageView imagemessage_option;

    LinearLayout layoutimage;
    ImageView imageview;

    LinearLayout layoutmessage;
    LinearLayout tomboloke;
    ImageView imagemessage;
    TextView textmessage;
    TextView texttombol;

    TextView tomboledit;
    TextView tombolsimpan;
    TextView texttoolbar;
    ImageView tombolkembali;

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();

    String idakun;
    String group;
    String idsurvei;

    SharedPreferences dataakun;

    List<ListFotoViewModel> listfotodepan;
    List<ListFotoViewModel> listfotokiri;
    List<ListFotoViewModel> listfotokanan;

    RecyclerView viewfotodepan;
    RecyclerView viewfotokiri;
    RecyclerView viewfotokanan;

    ListFotoViewDepanAdapter adapterfotodepan;
    ListFotoViewDepanAdapter adapterfotokiri;
    ListFotoViewDepanAdapter adapterfotokanan;

    LinearLayout layoutstatus;

    TextView textstatus;
    TextView textnomorcontainer;
    TextView textowner;
    TextView textsize;
    TextView texttipe;
    TextView texthaulier;
    TextView textdmf;
    TextView textpayload;
    TextView texttare;
    TextView textmaxgross;
    TextView textcomp;
    TextView textlocation;
    TextView textdimensi;
    TextView textquantity;
    TextView texttpc;

    ImageView imagecontainer;

    String TEXT_TOOLBAR = "Detail Survei";
    String status = "";
    String nomorcontainer = "";
    String owner = "";
    String size = "";
    String tipe = "";
    String haulier = "";
    String dmf = "";
    String payload = "";
    String tare = "";
    String maxgross = "";
    String comp = "";
    String location = "";
    String dimension = "";
//    String dimensi = "";
//    String lebar = "";
//    String tinggi = "";
    String quantity = "";
    String tpc = "";
    String urlcontainer = "";
    int month = 0;
    int years = 0;

    JSONObject objectdepan = new JSONObject();
    JSONObject objectkiri = new JSONObject();
    JSONObject objectkanan = new JSONObject();

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_survey);

        context = this;

        dataakun = getSharedPreferences("dataakun", MODE_PRIVATE);
        idakun = dataakun.getString("idakun", "");
        group = dataakun.getString("group", "");
        idsurvei = getIntent().getExtras().getString("idsurvei");

        layoutmessage_option = findViewById(R.id.layoutmessageoption);
        tombolright = findViewById(R.id.tombolright);
        tombolleft = findViewById(R.id.tombolleft);
        texttombolright = findViewById(R.id.texttombolright);
        texttombolleft = findViewById(R.id.texttombolleft);
        textmessage_option = findViewById(R.id.textmessageoption);
        textjudul_option = findViewById(R.id.textjuduloption);
        imagemessage_option = findViewById(R.id.imagemessageoption);

        layoutimage = findViewById(R.id.layoutimage);
        imageview = findViewById(R.id.imageview);

        layoutmessage = findViewById(R.id.layoutmessage);
        textmessage = findViewById(R.id.textmessage);
        texttombol = findViewById(R.id.texttombol);
        tomboloke = findViewById(R.id.tomboloke);
        imagemessage = findViewById(R.id.imagemessage);

        tombolkembali = findViewById(R.id.tombolkembali);
        tombolsimpan = findViewById(R.id.tombolsimpan);
        tomboledit = findViewById(R.id.tomboledit);
        texttoolbar = findViewById(R.id.texttoolbar);

        viewfotodepan = findViewById(R.id.listfotodepan);
        viewfotokiri = findViewById(R.id.listfotokiri);
        viewfotokanan = findViewById(R.id.listfotokanan);

        viewfotodepan.setHasFixedSize(true);
        viewfotokiri.setHasFixedSize(true);
        viewfotokanan.setHasFixedSize(true);

        viewfotodepan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewfotokiri.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        viewfotokanan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        listfotodepan = new ArrayList<>();
        listfotokiri = new ArrayList<>();
        listfotokanan = new ArrayList<>();

        layoutstatus = findViewById(R.id.layoutstatus);
        textstatus = findViewById(R.id.textstatus);
        textnomorcontainer = findViewById(R.id.textnomorcontainer);
        textowner = findViewById(R.id.textowner);
        textsize = findViewById(R.id.textsize);
        texttipe = findViewById(R.id.texttipe);
        texthaulier = findViewById(R.id.texthaulier);
        textdmf = findViewById(R.id.textdmf);
        textpayload = findViewById(R.id.textpayload);
        texttare = findViewById(R.id.texttare);
        textmaxgross = findViewById(R.id.textmaxgross);
        textcomp = findViewById(R.id.textcomp);
        textlocation = findViewById(R.id.textlocation);
        textdimensi = findViewById(R.id.textdimensi);
        textquantity = findViewById(R.id.textquantity);
        texttpc = findViewById(R.id.texttpc);
        imagecontainer = findViewById(R.id.imagecontainer);

        texttoolbar.setText(TEXT_TOOLBAR);

        filterview();

        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmessage_option("Anda yakin akan menyetujui survei ini ?");
            }
        });

        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        tombolleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidemessage_option();
            }
        });

        tombolright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showmessage_loading()){
                    hidemessage_option();
                    getAPI_approvesurvei();
                }
            }
        });

        tomboledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata();
            }
        });

        tomboloke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidemessage();
            }
        });

        refresh();
    }

    public void refresh(){
        showmessage_loading();
        getAPI_viewsurvey(idsurvei);
    }

    public void moveactivity(){
        Intent intent = new Intent(context, SurveyinAddActivity.class);
        intent.putExtra("insert", false);
        intent.putExtra("toolbar", "Edit Survey");
        startActivity(intent);
    }

    public void savedata(){
        SharedPreferences datainputsurveymasuk = getSharedPreferences("Inputsurveymasuk", MODE_PRIVATE);
        datainputsurveymasuk.edit().clear().commit();

        SharedPreferences.Editor datainputsurveymasuk_edit = getSharedPreferences("Inputsurveymasuk", MODE_PRIVATE).edit();
        datainputsurveymasuk_edit.putString("tipeinput", "edit");
        datainputsurveymasuk_edit.putString("idsurvey", idsurvei);
        datainputsurveymasuk_edit.putString("containernumber", nomorcontainer);
        datainputsurveymasuk_edit.putString("owner_code", owner);
        datainputsurveymasuk_edit.putString("size_text", size);
        datainputsurveymasuk_edit.putString("tipe_text", tipe);
        datainputsurveymasuk_edit.putString("haulier", haulier);
        datainputsurveymasuk_edit.putInt("month", month);
        datainputsurveymasuk_edit.putString("years_text", String.valueOf(years));
        datainputsurveymasuk_edit.putString("payload", payload);
        datainputsurveymasuk_edit.putString("tare", tare);
        datainputsurveymasuk_edit.putString("maxgross", maxgross);
        datainputsurveymasuk_edit.putString("imagecontainer", "");
        datainputsurveymasuk_edit.putString("urlimagecontainer", urlcontainer);

        datainputsurveymasuk_edit.putString("comp", comp);
        datainputsurveymasuk_edit.putString("loc", location);
        datainputsurveymasuk_edit.putString("dimensi", dimension);
//        datainputsurveymasuk_edit.putString("lebar", lebar);
//        datainputsurveymasuk_edit.putString("tinggi", tinggi);
        datainputsurveymasuk_edit.putString("quantity", quantity);
        datainputsurveymasuk_edit.putString("tpc", tpc);

        datainputsurveymasuk_edit.putString("imagedepan", objectdepan.toString());
        datainputsurveymasuk_edit.putString("imagekiri", objectkiri.toString());
        datainputsurveymasuk_edit.putString("imagekanan", objectkanan.toString());

        datainputsurveymasuk_edit.commit();

        moveactivity();
    }

    public void filterview(){
        boolean status_approve = getIntent().getBooleanExtra("approve_status", false);
        boolean status_edit = getIntent().getBooleanExtra("edit_status", false);

        Log.d("RESPONSE filter", "ini status : "+status_approve);
        if(status_approve){
            tombolsimpan.setVisibility(View.VISIBLE);
        }else{
            tombolsimpan.setVisibility(View.GONE);
        }

        if(status_edit){
            tomboledit.setVisibility(View.VISIBLE);
        }else{
            tomboledit.setVisibility(View.GONE);
        }
    }

    public void actionactive(){

    }

    public void actionnonactive(){

    }

    public Boolean showmessage_loading(){
        actionactive();

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoutmessage.setVisibility(View.VISIBLE);
        textmessage.setText("Sedang melakukan pengecekan");
        texttombol.setText("");
        tomboloke.setVisibility(View.GONE);
        imagemessage.setImageResource(R.drawable.vec_loading);
        tomboloke.setEnabled(false);

        return true;
    }

    public void showmessage_alert(String message){
        actionactive();

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoutmessage.setVisibility(View.VISIBLE);
        textmessage.setText(message);
        texttombol.setText("OKE");
        tomboloke.setVisibility(View.VISIBLE);
        imagemessage.setImageResource(R.drawable.img_message);
        tomboloke.setEnabled(true);
    }

    public void showmessage_option(String message){
        actionactive();

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage_option);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoutmessage_option.setVisibility(View.VISIBLE);
        textjudul_option.setText("Pemberitahuan");
        textmessage_option.setText(message);
        texttombolleft.setText("TIDAK");
        texttombolright.setText("IYA");
        imagemessage_option.setImageResource(R.drawable.vec_notif);
        tombolleft.setEnabled(true);
        tombolright.setEnabled(true);
    }

    public void hidemessage(){
        tomboloke.setEnabled(false);
        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);
        layoutmessage.setVisibility(View.GONE);

        // DELAY
        new CountDownTimer(var.DELAY_MESSAGE, 1000) {
            public void onFinish() {
                actionnonactive();
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    public void hidemessage_option(){
        tombolleft.setEnabled(false);
        tombolright.setEnabled(false);

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage_option);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);
        layoutmessage_option.setVisibility(View.GONE);

        // DELAY
        new CountDownTimer(var.DELAY_MESSAGE, 1000) {
            public void onFinish() {
                actionnonactive();
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();
    }

    public void getAPI_approvesurvei(){
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
        call = interfaceApi.getapi_approvesurvey(par.PARAM_APPROVE_SURVEI(idakun, group, idsurvei));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            // DELAY
                            new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                                public void onFinish() {
                                    endactivity();
                                }

                                public void onTick(long millisUntilFinished) {

                                }
                            }.start();

                        }else{
                            String addmessage = obj_convert.getString("addmessage");

                            // DELAY
                            new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                                public void onFinish() {
                                    showmessage_alert(addmessage);
                                }

                                public void onTick(long millisUntilFinished) {

                                }
                            }.start();
                        }

                    }catch (Exception e){
                        // DELAY
                        new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                            public void onFinish() {
                                showmessage_alert(var.MESSAGE_ALERT);
                            }

                            public void onTick(long millisUntilFinished) {

                            }
                        }.start();
                    }
                }else{
                    // DELAY
                    new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                        public void onFinish() {
                            showmessage_alert(var.MESSAGE_ALERT);
                        }

                        public void onTick(long millisUntilFinished) {

                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // DELAY
                new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                    public void onFinish() {
                        showmessage_alert(var.MESSAGE_ALERT);
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }
        });
    }

    @Override
    public void onBackPressed(){
        endactivity();
    }

    public void endactivity(){
        finish();
    }

    public void loaddata(JSONObject obj){
        hidemessage();

        try{
            JSONArray arraysurvey = obj.getJSONArray("survey");
            JSONObject objsurvey = (JSONObject) arraysurvey.get(0);

            status = objsurvey.getString("sin_status");

            if(status.equals("A")){
                status = "Approve";
                layoutstatus.setBackgroundResource(R.drawable.bg_button_done);
            }else
                if(status.equals("BA")){
                    status = "Waiting";
                    layoutstatus.setBackgroundResource(R.drawable.bg_button_notdone);
            }

            String[] splitdmf = objsurvey.getString("sin_dmf").split("-");
            month = Integer.valueOf(splitdmf[0]);
            years = Integer.valueOf(splitdmf[1]);

            nomorcontainer = objsurvey.getString("sin_container");
            owner = objsurvey.getString("sin_owner");
            size = objsurvey.getString("sin_size");
            tipe = objsurvey.getString("sin_type");
            haulier = objsurvey.getString("sin_haulier");
            dmf = var.getmonth(month)+" - "+years;
            payload = objsurvey.getString("sin_payload");
            tare = objsurvey.getString("sin_tare");
            maxgross = objsurvey.getString("sin_maxgross");

            JSONArray arraydetail = obj.getJSONArray("detail");
            JSONObject objdetail = (JSONObject) arraydetail.get(0);

//            String[] splitdimension = objdetail.getString("sind_dimension").split("x");
//            lebar = splitdimension[0];
//            tinggi = splitdimension[1];

            comp = objdetail.getString("sind_comp");
            location = objdetail.getString("sind_loc");
            dimension = objdetail.getString("sind_dimension");
            quantity = objdetail.getString("sind_quantity");
            tpc = objdetail.getString("sind_tpc");

            // == NOMOR CONTAINER == //
            JSONArray arrayfoto = obj.getJSONArray("photo");

            // == FOTO DEPAN == //
            JSONArray arrayfotodepan = new JSONArray();
            JSONArray arraydmgdepan = new JSONArray();
            JSONArray arraycodedmgdepan = new JSONArray();
            JSONArray arrayurldepan = new JSONArray();

            // == FOTO KIRI == //
            JSONArray arrayfotokiri = new JSONArray();
            JSONArray arraydmgkiri = new JSONArray();
            JSONArray arraycodedmgkiri = new JSONArray();
            JSONArray arrayurlkiri = new JSONArray();

            // == FOTO KANAN == //
            JSONArray arrayfotokanan = new JSONArray();
            JSONArray arraydmgkanan = new JSONArray();
            JSONArray arraycodedmgkanan = new JSONArray();
            JSONArray arrayurlkanan = new JSONArray();


            for(int i=0; i < arrayfoto.length(); i++){
                JSONObject objfoto = (JSONObject) arrayfoto.get(i);
                String url = objfoto.getString("sinp_filepath");
                String file = objfoto.getString("sinp_filename");
                String fullurl = url+""+file;
                String dmg = objfoto.getString("sinp_dmg");

                String position = objfoto.getString("sinp_position");

                if(position.equals("FRONT")){
                    listfotodepan.add(new ListFotoViewModel(url+""+file, dmg));
                    arrayfotodepan.put("null");
                    arraydmgdepan.put(0);
                    arraycodedmgdepan.put(dmg);
                    arrayurldepan.put(fullurl);
                }else
                    if(position.equals("LEFT")){
                        listfotokiri.add(new ListFotoViewModel(url+""+file, dmg));
                        arrayfotokiri.put("null");
                        arraydmgkiri.put(0);
                        arraycodedmgkiri.put(dmg);
                        arrayurlkiri.put(fullurl);
                    }else
                        if(position.equals("RIGHT")){
                            listfotokanan.add(new ListFotoViewModel(url+""+file, dmg));
                            arrayfotokanan.put("null");
                            arraydmgkanan.put(0);
                            arraycodedmgkanan.put(dmg);
                            arrayurlkanan.put(fullurl);
                        }else
                            if(position.equals("NOMOR")){
                                urlcontainer = url+""+file;
                                Picasso.get().load(urlcontainer).into(imagecontainer);
                            }
            }

            objectdepan.put("fotodepan", arrayfotodepan);
            objectdepan.put("dmgdepan", arraydmgdepan);
            objectdepan.put("codedmgdepan", arraycodedmgdepan);
            objectdepan.put("urldepan", arrayurldepan);

            objectkiri.put("fotokiri", arrayfotokiri);
            objectkiri.put("dmgkiri", arraydmgkiri);
            objectkiri.put("codedmgkiri", arraycodedmgkiri);
            objectkiri.put("urlkiri", arrayurlkiri);

            objectkanan.put("fotokanan", arrayfotokanan);
            objectkanan.put("dmgkanan", arraydmgkanan);
            objectkanan.put("codedmgkanan", arraycodedmgkanan);
            objectkanan.put("urlkanan", arrayurlkanan);

        }catch (Exception e){
            Log.d("RESPONSE gagal", String.valueOf(e));
        }

        adapterfotodepan = new ListFotoViewDepanAdapter(context, listfotodepan, layoutimage, imageview);
        adapterfotokiri = new ListFotoViewDepanAdapter(context, listfotokiri, layoutimage, imageview);
        adapterfotokanan = new ListFotoViewDepanAdapter(context, listfotokanan, layoutimage, imageview);

        viewfotodepan.setAdapter(adapterfotodepan);
        viewfotokiri.setAdapter(adapterfotokiri);
        viewfotokanan.setAdapter(adapterfotokanan);

        textstatus.setText(status);
        textnomorcontainer.setText(nomorcontainer);
        textowner.setText(owner);
        textsize.setText(size);
        texttipe.setText(tipe);
        texthaulier.setText(haulier);
        textdmf.setText(dmf);
        textpayload.setText(payload);
        texttare.setText(tare);
        textmaxgross.setText(maxgross);
        textcomp.setText(comp);
        textlocation.setText(location);
        textdimensi.setText(dimension);
        textquantity.setText(quantity);
        texttpc.setText(tpc);
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
        call = interfaceApi.getapi_viewsurvey(par.PARAM_VIEW_SURVEY(idakun, group, idsurvey));
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
                            loaddata(objdata);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");

                            // DELAY
                            new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                                public void onFinish() {
                                    showmessage_alert(addmessage);
                                }

                                public void onTick(long millisUntilFinished) {

                                }
                            }.start();
                        }

                    }catch (Exception e){
                        // DELAY
                        new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                            public void onFinish() {
                                showmessage_alert(var.MESSAGE_ALERT);
                            }

                            public void onTick(long millisUntilFinished) {

                            }
                        }.start();
                    }
                }else{
                    // DELAY
                    new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                        public void onFinish() {
                            showmessage_alert(var.MESSAGE_ALERT);
                        }

                        public void onTick(long millisUntilFinished) {

                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // DELAY
                new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                    public void onFinish() {
                        showmessage_alert(var.MESSAGE_ALERT);
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }
        });
    }
}
