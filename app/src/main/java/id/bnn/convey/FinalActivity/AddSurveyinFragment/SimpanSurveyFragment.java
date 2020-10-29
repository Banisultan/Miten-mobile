package id.bnn.convey.FinalActivity.AddSurveyinFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
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
import com.google.gson.JsonParser;


import id.bnn.convey.FinalActivity.DoneActivity;
import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.R;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SimpanSurveyFragment extends Fragment {

    Context context;
    SharedPreferences datainputsurveyin;
    SharedPreferences datauser;

    VariableText var = new VariableText();
    VariableVoid vod;

    String SURVEYID;
    String USERID;
    String TOKEN;
    String MOBID;
    String NOCONTAINER;
    String SIZE;
    String TIPE;
    String OWNERCODE;
    String WASHCODE;
    String DMF;
    String PAYLOAD;
    String TARE;
    String MAXGROSS;
    String HAULIER;
    String GRADE;
    JsonArray DATADAMAGE;
    JsonArray DATA_PHOTO;
    JsonArray REMOVE_DATA_PHOTO;
    JsonArray REMOVE_DATA_DAMAGE;

    LinearLayout tombolsimpan;

    Dialog dialog_warning;
    ImageView image_msg_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    Dialog dialog_loading;
    TextView text_msg_loading;

    boolean BUTTON_EXECUTE = false;
    String ACTION;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    ImageView imagewarning;
    LinearLayout tombolkembali;

    public SimpanSurveyFragment(
        ImageView viewv2,
        TextView[] viewstep,
        View[] viewline
    ){
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hide_warning();
        hide_loading();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_simpan_survey, container, false);
        context = container.getContext();
        vod = new VariableVoid(context);

        for(int i=1; i<viewline.length; i++){
            viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
            viewline[i].setBackgroundResource(R.drawable.bg_line_on);
        }
        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        imagewarning = itemview.findViewById(R.id.imagewarning);
        Glide.with(context).load(R.drawable.img_warning).into(imagewarning);

        tombolsimpan = itemview.findViewById(R.id.tombolsimpan);
        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_loading("Sedang mengupload data");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callAPI();
                    }
                }, 600);
            }
        });

        tombolkembali = itemview.findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevfragment();
            }
        });

        loaddata();
        messageloading();
        messagewarning();
        return itemview;
    }

    public void prevfragment(){
        Fragment fragment = new DamageContainerFragment(
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

    public void loaddata(){
        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        ACTION = datainputsurveyin.getString("action", "add");

        int bulan = Integer.valueOf(datainputsurveyin.getString("bulan", "0"));
        String bulantext = String.valueOf(bulan);
        if(bulan < 10){
            bulantext = "0"+bulan;
        }

        SURVEYID = datainputsurveyin.getString("surveyid", "");
        MOBID = datainputsurveyin.getString("mobid", "");
        NOCONTAINER = datainputsurveyin.getString("nocontainer", "");
        SIZE = datainputsurveyin.getString("size", "");
        TIPE = datainputsurveyin.getString("tipe", "");
        OWNERCODE = datainputsurveyin.getString("ownercode", "");
        WASHCODE = datainputsurveyin.getString("washcode", "");
        DMF = bulantext+"-"+ datainputsurveyin.getString("tahun", "");
        PAYLOAD = datainputsurveyin.getString("payload", "");
        TARE = datainputsurveyin.getString("tare", "");
        MAXGROSS = datainputsurveyin.getString("maxgross", "");
        HAULIER = datainputsurveyin.getString("haulier", "");
        GRADE = datainputsurveyin.getString("grade", "");

        DATADAMAGE = new JsonParser().parse(datainputsurveyin.getString("datadamage", "[]")).getAsJsonArray();
        DATA_PHOTO = new JsonParser().parse(datainputsurveyin.getString("photo", "[]")).getAsJsonArray();
        REMOVE_DATA_PHOTO = new JsonParser().parse(datainputsurveyin.getString("photo_remove", "[]")).getAsJsonArray();
        REMOVE_DATA_DAMAGE = new JsonParser().parse(datainputsurveyin.getString("damage_remove", "[]")).getAsJsonArray();
    }

    public void messagewarning(){
        dialog_warning = new Dialog(context);
        dialog_warning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_warning.setContentView(R.layout.layout_warning);
        dialog_warning.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        text_msg_warning = dialog_warning.findViewById(R.id.textmessage);
        button_msg_warning = dialog_warning.findViewById(R.id.buttonoke);
        button_msg_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_warning();
            }
        });

        imagewarning = dialog_warning.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(imagewarning);
    }

    public void messageloading(){
        dialog_loading = new Dialog(context);
        dialog_loading.setCancelable(false);
        dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setContentView(R.layout.layout_loading_v2);
        dialog_loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        text_msg_loading = dialog_loading.findViewById(R.id.textmessage);
    }

    public void show_warning(String message){
        text_msg_warning.setText(message);
        dialog_warning.show();
    }

    public void hide_warning(){
        dialog_warning.dismiss();
    }

    public void show_loading(String message){
        text_msg_loading.setText(message);
        dialog_loading.show();
    }

    public void hide_loading(){
        BUTTON_EXECUTE = false;
        dialog_loading.dismiss();
    }

    public void addtodatabase(String idsurvey){
        for(int i=0; i < DATA_PHOTO.size(); i++){
            JsonObject datajson = DATA_PHOTO.get(i).getAsJsonObject();
            String action = datajson.get("action").getAsString();
            if(action.equals("add")) {
                String mobid_photo = datajson.get("mobid_photo").getAsString();
                String mobid_damage = datajson.get("mobid_damage").getAsString();
                String filetemp = datajson.get("file_temp").getAsString();
                String tipe = datajson.get("tipe").getAsString();
                String posisi = datajson.get("posisi").getAsString();
                long id = vod.addDatabase_photo(MOBID, mobid_photo, mobid_damage, filetemp, posisi, tipe, idsurvey, USERID);
            }
        }

        moveactivity();
    }

    public void moveactivity(){
        Intent intent = new Intent(context, DoneActivity.class);
        startActivity(intent);
        ((Activity)context).finish();
    }

    public void callAPI(){
        JsonArray dataarray = new JsonArray();
        JsonObject datajson_1 = new JsonObject();

        try{
            datajson_1.addProperty("title", "Authorization");
            datajson_1.addProperty("value", "Bearer "+TOKEN);
        }catch (Exception e){
            Log.d("TAGGAR", "Gagal datajson_1"+e);
        }
        dataarray.add(datajson_1);

        OkHttpClient.Builder httpClient = new VariableRequest().setHTTPCLIENT(dataarray);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL_v2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI_v2 interfaceApi = retrofit.create(InterfaceAPI_v2.class);

        JsonObject datajson = new JsonObject();
        try{
            datajson.addProperty("surveyid", SURVEYID);
            datajson.addProperty("mobid", MOBID);
            datajson.addProperty("userid", USERID);
            datajson.addProperty("nocontainer", NOCONTAINER);
            datajson.addProperty("size", SIZE);
            datajson.addProperty("tipe", TIPE);
            datajson.addProperty("ownercode", OWNERCODE);
            datajson.addProperty("washcode", WASHCODE);
            datajson.addProperty("dmf", DMF);
            datajson.addProperty("payload", PAYLOAD);
            datajson.addProperty("tare", TARE);
            datajson.addProperty("maxgross", MAXGROSS);
            datajson.addProperty("haulier", HAULIER);
            datajson.addProperty("grade", GRADE);
            datajson.add("datadamage", DATADAMAGE);
        }catch (Exception e){
            Log.d("TAGGAR", "Gagal datajson "+e);
        }

        Call<JsonObject> call = null;
        if(ACTION.equals("add")){
            call = interfaceApi.insertsurveyin(datajson);
        }else{
            datajson.add("photo_remove", REMOVE_DATA_PHOTO);
            datajson.add("damage_remove", REMOVE_DATA_DAMAGE);
            call = interfaceApi.editsurveyin(datajson);
        }

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){

                    try {
                        JsonObject obj_response = new JsonParser().parse(response.body().toString()).getAsJsonObject();
                        String message = obj_response.get("message").getAsString();
                        String rescode = obj_response.get("rescode").getAsString();

                        if(rescode.equals("0")){
                            JsonObject data = obj_response.get("data").getAsJsonObject();
                            hide_loading();
                            addtodatabase(data.get("surveyid").getAsString());
                        }else{
                            hide_loading();
                            show_warning(message);
                        }
                    }catch (Exception e){
                        Log.d("TAGGAR", "Gagal get api "+e);
                        hide_loading();
                        show_warning(getString(R.string.error_default));
                    }
                }else{
                    hide_loading();
                    show_warning(getString(R.string.error_default));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide_loading();
                show_warning(getString(R.string.error_default));
            }
        });
    }
}