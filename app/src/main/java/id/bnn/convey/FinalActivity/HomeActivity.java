package id.bnn.convey.FinalActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import id.bnn.convey.Activity.ViewSurveyActivity;
import id.bnn.convey.Adapter.HomeAdapter;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.R;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeActivity extends AppCompatActivity {
    Context context;

    ViewPager viewpagerhome;
    HomeAdapter homeAdapter;

    LinearLayout tombolhome;
    LinearLayout tombolprofile;
    LinearLayout tombolqr;

    IntentIntegrator scannerview;

    Dialog dialog_notif;
    TextView text_msg_notif;
    LinearLayout button_msg_notif;
    ImageView image_msg_notif;

    ImageView imagehome;
    ImageView imageprofile;
    ImageView imageqr;

    boolean backpressed = false;

    SharedPreferences datauser;
    String USERID;
    String TOKEN;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hide_notif();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

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
        setContentView(R.layout.activity_home_new);


        context = this;
        scannerview = new IntentIntegrator(this);

        viewpagerhome = findViewById(R.id.viewpagerhome);
        homeAdapter = new HomeAdapter(getSupportFragmentManager());
        viewpagerhome.setAdapter(homeAdapter);

        imagehome = findViewById(R.id.imagehome);
        imageprofile = findViewById(R.id.imageprofile);
        imageqr = findViewById(R.id.imageqr);

        Glide.with(context).load(R.drawable.img_home).into(imagehome);
        Glide.with(context).load(R.drawable.img_user_v2).into(imageprofile);
        Glide.with(context).load(R.drawable.img_qr).into(imageqr);

        tombolhome = findViewById(R.id.tombolhome);
        tombolhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpagerhome.setCurrentItem(0);
            }
        });

        tombolprofile = findViewById(R.id.tombolprofile);
        tombolprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewpagerhome.setCurrentItem(1);
            }
        });

        tombolqr = findViewById(R.id.tombolqr);
        tombolqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        datauser = getSharedPreferences("datauser", MODE_PRIVATE);
        USERID = datauser.getString("userid","");
        TOKEN = datauser.getString("token", "");

        message_notif();
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestcode, resultcode, data);
        if(result != null){
            if(result.getContents() != null){
                String textqr = "haloooo";
//                getAPI_searchsurvey(IDAKUN, GROUP, textqr);
            }
        }else{
            super.onActivityResult(requestcode, resultcode, data);
        }
    }

    public void message_notif(){
        dialog_notif = new Dialog(context);
        dialog_notif.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_notif.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_notif.setContentView(R.layout.layout_message_new);
        dialog_notif.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_notif.show();

        text_msg_notif = dialog_notif.findViewById(R.id.textmessage);

        image_msg_notif = dialog_notif.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_message).into(image_msg_notif);

        button_msg_notif = dialog_notif.findViewById(R.id.buttonoke);
        button_msg_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_notif();
            }
        });
    }

    public void hide_notif(){
        dialog_notif.dismiss();
    }
}
