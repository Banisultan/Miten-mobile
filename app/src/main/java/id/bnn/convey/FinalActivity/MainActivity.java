package id.bnn.convey.FinalActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.bnn.convey.EducationActivity;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class MainActivity extends AppCompatActivity {

    Context context;

    VariableText var = new VariableText();

    SharedPreferences datadevice;
    SharedPreferences datauser;

    ImageView image_logo;
    ImageView image_bg;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(context).clearMemory();
        context.getCacheDir().delete();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        image_bg = findViewById(R.id.image_bg);
        image_logo = findViewById(R.id.image_logo);
//        Picasso.get().load(R.drawable.img_logo).into(image_logo);
//        Picasso.get().load(R.drawable.img_bg_new).into(image_bg);
        Glide.with(context).load(R.drawable.img_logo).into(image_logo);
        Glide.with(context).load(R.drawable.img_bg_new).into(image_bg);

        datauser = getSharedPreferences("datauser", MODE_PRIVATE);
        // DELAY
        new CountDownTimer(var.DELAY_SPLASH_SCREEN, 1000) {
            public void onFinish() {
                datadevice = getSharedPreferences("device", MODE_PRIVATE);
                String log = datadevice.getString("log", "");

                Intent intent = null;
                if(log.equals("")){
                    var.unsubscribeall();
                    intent = new Intent(MainActivity.this, EducationActivity.class);
                }else{
                    if(datauser.getString("userid", "").equals("")){
                        Log.d("TAGGAR", "masuke ");
                        var.unsubscribeall();
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                    }else{
                        Date date = new Date();
                        SimpleDateFormat formatdate = new SimpleDateFormat("yyyy-MM-dd");
                        String waktu_now = formatdate.format(date);

                        if(datauser.getString("waktu", "").equals(waktu_now)){
                            intent = new Intent(MainActivity.this, HomeActivity.class);
                        }else{
                            Log.d("TAGGAR", "malah masuk ke sini");
                            var.unsubscribeall();
                            datauser.edit().clear().commit();
                            intent = new Intent(MainActivity.this, LoginActivity.class);
                        }
                    }
                }
                moveactiviy(intent);
            }

            public void onTick(long millisUntilFinished) {

            }
        }.start();

        test();
    }

    public void moveactiviy(Intent intent){
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void test(){
        FirebaseMessaging.getInstance().subscribeToTopic("global_")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }
}
