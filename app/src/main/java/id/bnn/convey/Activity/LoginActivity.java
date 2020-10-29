package id.bnn.convey.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
//import com.microsoft.projectoxford.vision.VisionServiceClient;
//import com.microsoft.projectoxford.vision.VisionServiceRestClient;
//import com.microsoft.projectoxford.vision.contract.AnalysisInDomainResult;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import id.bnn.convey.FinalActivity.HomeActivity;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.LoadingScreenActivity;
import id.bnn.convey.R;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
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


public class LoginActivity extends AppCompatActivity{

    Context context;

    LinearLayout layoutusername;
    LinearLayout tombolusername;
    LinearLayout tombollogin;
//    LinearLayout tomboloke;
    LinearLayout tombolscan;
    LinearLayout tombolpindai;
    LinearLayout layoutmessage;
    LinearLayout layoutafterproses;
    LinearLayout layoutbeforeproses;
    EditText inputusername;
    EditText inputpassword;
    TextView textmessage;
    TextView texttombol;
    TextView textnama;
    TextView textpindai;
    ImageView imagemessage;
    CircleImageView imageprofile;
    ImageView tomboleye;
    SharedPreferences.Editor datalogin_editor;

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();

    SharedPreferences.Editor dataakun_edit;
    SharedPreferences datalistitem;

    int REQUEST_CAMERA = 2000;

    String TAG = "TAGGAR";

    boolean STATUS_FACE = true;

    boolean backpressed = false;
    @Override
    public void onBackPressed(){
        if(backpressed){
            super.onBackPressed();
            return;
        }

        backpressed = true;
        Toast.makeText(this, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();

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
        setContentView(R.layout.activity_login);

        context = this;

        datalogin_editor = getSharedPreferences("login", MODE_PRIVATE).edit();
        datalistitem = getSharedPreferences("datalistitem", Context.MODE_PRIVATE);

        tombollogin = findViewById(R.id.tombollogin);
//        tomboloke = findViewById(R.id.tomboloke);
        tombolscan = findViewById(R.id.tombolscan);
        tombolpindai = findViewById(R.id.tombolpindai);
        layoutmessage = findViewById(R.id.layoutmessage);
        layoutafterproses = findViewById(R.id.layoutafterproses);
        layoutbeforeproses = findViewById(R.id.layoutbeforeproses);
        layoutusername = findViewById(R.id.layoutusername);
        inputusername = findViewById(R.id.inputusername);
        inputpassword = findViewById(R.id.inputpassword);
        textmessage = findViewById(R.id.textmessage);
        texttombol = findViewById(R.id.texttombol);
        textnama = findViewById(R.id.textnama);
        textpindai = findViewById(R.id.textpindai);
        imagemessage = findViewById(R.id.imagemessage);
        imageprofile = findViewById(R.id.imageprofile);
        tomboleye = findViewById(R.id.tomboleye);
        tombolusername = findViewById(R.id.tombolusername);

        tomboleye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tomboleye.getDrawable().getConstantState() ==
                        ContextCompat.getDrawable(context, R.drawable.vec_eye_on).getConstantState()){
                    tomboleye.setImageResource(R.drawable.vec_eye_off);
                    inputpassword.setTransformationMethod(null);
                    inputpassword.setSelection(inputpassword.length());
                }else{
                    tomboleye.setImageResource(R.drawable.vec_eye_on);
                    inputpassword.setTransformationMethod(new PasswordTransformationMethod());
                    inputpassword.setSelection(inputpassword.length());
                }
            }
        });

        tombolpindai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!STATUS_FACE){
                    hideusername();
                }else{
                    checkpermision();
                }
            }
        });

        tombolscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermision();
            }
        });

        tombollogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

//        tomboloke.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                hidemessage();
//            }
//        });
//        tomboloke.setEnabled(false);

        tombolusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                STATUS_FACE = false;
                showusername();
            }
        });
    }

    public void showusername(){
        inputusername.setText("");
        inputpassword.setText("");
        layoutbeforeproses.setVisibility(View.GONE);
        layoutafterproses.setVisibility(View.VISIBLE);
        layoutusername.setVisibility(View.VISIBLE);
        textnama.setVisibility(View.GONE);
        textpindai.setText("Pindai Wajah");
    }

    public void hideusername(){
        layoutbeforeproses.setVisibility(View.VISIBLE);
        layoutafterproses.setVisibility(View.GONE);
        layoutusername.setVisibility(View.GONE);
        textnama.setVisibility(View.VISIBLE);
        textpindai.setText("Pindai Kembali");

        STATUS_FACE = true;
    }

    public void validation(){
        String username = inputusername.getText().toString().trim();
        String password = inputpassword.getText().toString().trim();

        if(username.equals("") && !STATUS_FACE){
            showmessage_alert("Username anda belum diisi");
        }else
            if(password.equals("")){
                showmessage_alert("Password anda belum diisi");
            }else
                if(password.length() <= 5){
                    showmessage_alert("Password terlalu pendek");
                }else{
                    showmessage_loading();
                    getAPI_login(idakun, password, username);
                }
    }

    public void checkpermision(){
        String[] permision = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(LoginActivity.this, permision, REQUEST_CAMERA);
        }else{
            opencamera();
        }
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        super.onActivityResult(requestcode, resultcode, data);
        if(resultcode == Activity.RESULT_OK){
            Bitmap bitmap = null;
            try{
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage);
                showmessage_loading();
                getAPI_facedetection(bitmap);
            }catch (Exception e){
                Log.d("RESPONSE", "ini bitmap : "+String.valueOf(e));
            }
        }
    }

    Uri uriimage;
    public void opencamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        uriimage = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uriimage);
        cameraintent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        cameraintent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startActivityForResult(cameraintent, REQUEST_CAMERA);
    }

    public void moveactivity(JSONObject objdata){
        try{
            JSONObject data = objdata.getJSONObject("data");
            String nama = data.getString("nama");
            String idakun = data.getString("idakun");
            String group = data.getString("group");
            String tipe = data.getString("tipe");
            String token = data.getString("token");

            Date date = new Date();
            SimpleDateFormat formatdate = new SimpleDateFormat("yyyy/MM/dd");
            String waktu = formatdate.format(date);

            dataakun_edit = getSharedPreferences("dataakun", MODE_PRIVATE).edit();
            dataakun_edit.putString("nama", nama);
            dataakun_edit.putString("idakun", idakun);
            dataakun_edit.putString("group", group);
            dataakun_edit.putString("tipe", tipe);
            dataakun_edit.putString("waktu", waktu);
            dataakun_edit.putString("token", token);
            dataakun_edit.commit();

            subscribe(idakun);

        }catch (Exception e){

        }

        Intent intent = null;
        String owner = datalistitem.getString("owner", "");
        if(owner.equals("")){
            intent = new Intent(context, LoadingScreenActivity.class);
        }else{
            intent = new Intent(context, HomeActivity.class);
        }

        startActivity(intent);
        finish();
    }

    public void actionactive(){
        tomboleye.setEnabled(false);
        tombollogin.setEnabled(false);
        inputpassword.setEnabled(false);
        inputusername.setEnabled(false);
        tombolpindai.setEnabled(false);

        inputpassword.clearFocus();
        inputusername.clearFocus();
    }

    public void actionnonactive(){
        tomboleye.setEnabled(true);
        tombollogin.setEnabled(true);
        inputpassword.setEnabled(true);
        inputusername.setEnabled(true);
        tombolpindai.setEnabled(true);
    }

    public void showmessage_alert(String message){
        actionactive();

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoutmessage.setVisibility(View.VISIBLE);
        textmessage.setText(message);
        texttombol.setText("OK");
//        tomboloke.setVisibility(View.VISIBLE);
        imagemessage.setImageResource(R.drawable.img_message);
//        tomboloke.setEnabled(true);
    }

    public void showmessage_loading(){
        actionactive();

        Transition transition = new Fade();
        transition.setDuration(var.DELAY_MESSAGE);
        transition.addTarget(layoutmessage);
        TransitionManager.beginDelayedTransition((ViewGroup) getWindow().getDecorView().getRootView(), transition);

        layoutmessage.setVisibility(View.VISIBLE);
        textmessage.setText("Sedang melakukan pengecekan");
        texttombol.setText("");
//        tomboloke.setVisibility(View.GONE);
        imagemessage.setImageResource(R.drawable.vec_loading);
//        tomboloke.setEnabled(false);
    }

    public void hidemessage(){
//        tomboloke.setEnabled(false);
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

    String idakun = "";
    public void prosesdata(JSONObject obj, Bitmap bitmap){
        hidemessage();
        try{
            String nama = obj.getString("nama");
            idakun = obj.getString("idakun");
            textnama.setText(nama);
            layoutafterproses.setVisibility(View.VISIBLE);
            layoutbeforeproses.setVisibility(View.GONE);

            imageprofile.setImageURI(uriimage);
        }catch (Exception e){

        }
    }

    public void getAPI_login(String idakun, String password, String username){
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

        if(STATUS_FACE){
            call = interfaceApi.getapi(par.PARAM_LOGIN(idakun, password, var.DEF_GROUP, "", "FACE"));
        }else{
            call = interfaceApi.getapi(par.PARAM_LOGIN(idakun, password, var.DEF_GROUP, username, "bukan FACE"));
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
                            JSONObject objdata = obj_convert.getJSONObject("resmessage");
                            moveactivity(objdata);
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
                Log.d("RESPONSE", "messege : "+t.getMessage());

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

    public void getAPI_facedetection(Bitmap bitmap){
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
        RequestBody databody = RequestBody.create(MediaType.parse("text/plain"), var.DEF_GROUP);
        call = interfaceApi.getapi_identity(par.PARAM_IDENTITY(context, bitmap), databody);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("RESPONSE", "ini L "+response.body());
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            JSONObject objdata = obj_convert.getJSONObject("resmessage");
                            prosesdata(objdata, bitmap);
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

    public void subscribe(String idakun){
        FirebaseMessaging.getInstance().subscribeToTopic("MOB_"+idakun)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "GAGAL SUBCREB");
                        }else{
                            Log.d(TAG, "SUKSESS SUBCREB");
                        }
                    }
                });
    }
}
