package id.bnn.convey.FinalActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.LoadingScreenActivity;
import id.bnn.convey.R;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginUserActivity extends AppCompatActivity {

    Context context;

    LinearLayout tombollogin;
    LinearLayout tombolkembali;
    EditText inputusername;
    EditText inputpassword;

    Boolean BUTTON_EXECUTE = false;

    Dialog dialog_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    Dialog dialog_loading;
    TextView text_msg_loading;

    VariableText var = new VariableText();

    SharedPreferences.Editor datauser_edit;

    ImageView imagebg;
    ImageView imagelogo;
    ImageView imageprofile;
    ImageView imageeye;

    boolean EYE = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
        hide_warning();
        hide_loading();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        context = this;

        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);

        imagebg = findViewById(R.id.imagebg);
        imagelogo = findViewById(R.id.imagelogo);
        imageprofile = findViewById(R.id.imageprofile);
        imageeye = findViewById(R.id.imageeye);

        Glide.with(context).load(R.drawable.img_bg_white).into(imagebg);
        Glide.with(context).load(R.drawable.img_logo_warna).into(imagelogo);
        Glide.with(context).load(R.drawable.img_profile).into(imageprofile);
        Glide.with(context).load(R.drawable.img_eye_off).into(imageeye);

        imageeye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEye();
            }
        });

        tombollogin = findViewById(R.id.tombollogin);
        tombollogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        tombolkembali = findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endactivity();
            }
        });

        messagewarning();
        messageloading();
    }

    public void setEye(){
        if(EYE){
            inputpassword.setTransformationMethod(new PasswordTransformationMethod());
            Glide.with(context).load(R.drawable.img_eye_off).into(imageeye);
            EYE = false;
        }else{
            inputpassword.setTransformationMethod(null);
            Glide.with(context).load(R.drawable.img_eye_on).into(imageeye);
            EYE = true;
        }

        inputpassword.setSelection(inputpassword.getText().toString().length());
    }

    public void validation(){
        String username = inputusername.getText().toString().trim();
        String password = inputpassword.getText().toString().trim();

        if(username.isEmpty()){
            show_warning(getString(R.string.val_username_empty));
        }else
            if(password.isEmpty()){
                show_warning(getString(R.string.val_password_empty));
            }else{
                if(!BUTTON_EXECUTE){
                    BUTTON_EXECUTE = true;
                    show_loading(getString(R.string.msg_loading_login));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callAPI(username, password);
                        }
                    }, 600);
                }
            }
    }

    public void moveactivity(){
        Intent intent = new Intent(this, LoadingScreenActivity.class);
        startActivity(intent);
        LoginActivity.activity.finish();
        finish();
    }

    public void endactivity(){
        finish();
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
        dialog_warning.hide();
    }

    public void show_loading(String message){
        text_msg_loading.setText(message);
        dialog_loading.show();
    }

    public void hide_loading(){
        BUTTON_EXECUTE = false;
        dialog_loading.hide();
    }

    public void savedata(JsonObject datajson){
        String userid = datajson.get("userid").getAsString();
        String nama = datajson.get("nama").getAsString();
        String tipe = datajson.get("tipe").getAsString();
        String token = datajson.get("token").getAsString();
        String waktu = datajson.get("waktu").getAsString();

        datauser_edit = context.getSharedPreferences("datauser", Context.MODE_PRIVATE).edit();
        datauser_edit.putString("userid", userid);
        datauser_edit.putString("nama", nama);
        datauser_edit.putString("tipe", tipe);
        datauser_edit.putString("token", token);
        datauser_edit.putString("waktu", waktu);
        datauser_edit.apply();

        // SEMENTARA
        datauser_edit = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE).edit();
        datauser_edit.putString("idakun", userid);
        datauser_edit.putString("nama", nama);
        datauser_edit.putString("group", "CONVEY");
        datauser_edit.putString("tipe", tipe);
        datauser_edit.putString("token", token);
        datauser_edit.putString("waktu", waktu);
        datauser_edit.apply();

        subscribe(userid);
        moveactivity();
    }


    public void callAPI(String username, String password){
        JsonArray dataarray = new JsonArray();

        JsonObject datajson_1 = new JsonObject();
        datajson_1.addProperty("title", "Authorization");
        datajson_1.addProperty("value", "Bearer "+var.createSHA1(var.SECRET_AUTH, var.SECRET_SHA1));

        JsonObject datajson_2 = new JsonObject();
        datajson_2.addProperty("title", "Password");
        datajson_2.addProperty("value", password);

        dataarray.add(datajson_1);
        dataarray.add(datajson_2);

        OkHttpClient.Builder httpClient = new VariableRequest().setHTTPCLIENT(dataarray);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL_v2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI_v2 interfaceApi = retrofit.create(InterfaceAPI_v2.class);

        JsonObject datajson = new JsonObject();
        datajson.addProperty("mobid", var.createMobID());
        datajson.addProperty("username", username);

        Call<JsonObject> call = interfaceApi.login(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String message = obj_response.get("message").getAsString();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        savedata(obj_response.get("data").getAsJsonObject());
                        hide_loading();
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

    public void subscribe(String idakun){
        FirebaseMessaging.getInstance().subscribeToTopic("MOB_"+idakun)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.d("TAGGAR", "GAGAL SUBCREB");
                        }else{
                            Log.d("TAGGAR", "SUKSESS SUBCREB");
                        }
                    }
                });
    }
}