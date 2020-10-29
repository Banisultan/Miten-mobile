package id.bnn.convey.FinalActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.NoContainerFragment;
import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.R;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SurveyinAddActivity extends AppCompatActivity {

    TextView texttoolbar;
    TextView viewstep1;
    TextView viewstep2;
    TextView viewstep3;
    TextView viewstep4;
    TextView viewstep5;

    View viewline1;
    View viewline2;
    View viewline3;
    View viewline4;
    ImageView viewv2;

    TextView[] viewstep = {null, viewstep1, viewstep2, viewstep3, viewstep4, viewstep5};
    View[] viewline = {null, viewline1, viewline2, viewline3, viewline4};

    Context context;
    Fragment fragment;

    boolean backpressed = false;
    String ACTION;

    VariableText var = new VariableText();

    Dialog dialog_warning;
    ImageView image_msg_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    Dialog dialog_loading;
    TextView text_msg_loading;

    SharedPreferences datainputsurveyin;
    SharedPreferences datauser;
    String USERID;
    String TOKEN;

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        hide_loading();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onBackPressed(){
        if(backpressed){
            endactivity();
            return;
        }

        backpressed = true;
        Toast.makeText(this, "Tekan sekali lagi untuk kembali", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backpressed = false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survei);

        context = this;

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");

        ACTION = getIntent().getStringExtra("action");

        viewv2 = findViewById(R.id.step2v2);
        viewstep[1] = findViewById(R.id.step1);
        viewstep[2] = findViewById(R.id.step2);
        viewstep[3] = findViewById(R.id.step3);
        viewstep[4] = findViewById(R.id.step4);
        viewstep[5] = findViewById(R.id.step5);

        viewline[1] = findViewById(R.id.linestep1);
        viewline[2] = findViewById(R.id.linestep2);
        viewline[3] = findViewById(R.id.linestep3);
        viewline[4] = findViewById(R.id.linestep4);

        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText(getIntent().getStringExtra("toolbar"));

        messageloading();
        messagewarning();

        if(ACTION.equals("add")){
            datainputsurveyin.edit().clear().apply();
            callAPI();
        }else{
            setFragment();
        }
    }

    public void messageloading(){
        dialog_loading = new Dialog(context);
        dialog_loading.setCancelable(false);
        dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setContentView(R.layout.layout_loading_v2);
        dialog_loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        text_msg_loading = dialog_loading.findViewById(R.id.textmessage);
        text_msg_loading.setText("Sedang mengecek saldo");
        dialog_loading.show();
    }

    public void hide_loading(){
        dialog_loading.dismiss();
    }

    public void messagewarning(){
        dialog_warning = new Dialog(context);
        dialog_warning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_warning.setContentView(R.layout.layout_warning);
        dialog_warning.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        image_msg_warning = dialog_warning.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(image_msg_warning);

        text_msg_warning = dialog_warning.findViewById(R.id.textmessage);
        button_msg_warning = dialog_warning.findViewById(R.id.buttonoke);
        button_msg_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_warning();
                endactivity();
            }
        });

    }

    public void show_warning(String message){
        text_msg_warning.setText(message);
        dialog_warning.show();
    }

    public void hide_warning(){
        dialog_warning.dismiss();
    }

    public void endactivity(){
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        for(Fragment fragment : getSupportFragmentManager().getFragments()){
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }

    public void addfragment(){
        SharedPreferences.Editor datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin_edit.putString("mobid", var.createMobID());
        datainputsurveyin_edit.putString("action", ACTION);
        datainputsurveyin_edit.apply();

        fragment = new NoContainerFragment(
                viewv2,
                viewstep,
                viewline
        );
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layoutfragment, fragment)
                .addToBackStack("awal")
                .commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void setFragment(){
        addfragment();
        hide_loading();
    }

    public void callAPI(){
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

        Call<JsonObject> call = interfaceApi.checkSaldo(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String message = obj_response.get("message").getAsString();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        setFragment();
                    }else{
                        hide_loading();
                        show_warning(message);
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
