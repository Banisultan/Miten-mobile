package id.bnn.convey.FinalActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import id.bnn.convey.R;

public class LoginActivity extends AppCompatActivity {

    static Activity activity;
    Context context;

    LinearLayout tombolscan;
    LinearLayout tombolusername;

    Dialog dialog_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    ImageView imagebg;
    ImageView imagelogo;
    ImageView imageprofile;
    ImageView imagebutton;

    @Override
    public void onDestroy() {
        super.onDestroy();
        hide_warning();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        activity = this;
        context = this;

        imagebg = findViewById(R.id.image_bg);
        imagelogo = findViewById(R.id.image_logo);
        imageprofile = findViewById(R.id.imageprofile);
        imagebutton = findViewById(R.id.image_button);

        Glide.with(context).load(R.drawable.img_bg_white).into(imagebg);
        Glide.with(context).load(R.drawable.img_logo_warna).into(imagelogo);
        Glide.with(context).load(R.drawable.img_profile).into(imageprofile);
        Glide.with(context).load(R.drawable.img_user).into(imagebutton);

        tombolscan = findViewById(R.id.tombolscan);
        tombolscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_warning(getString(R.string.msg_warning_onprogress));
            }
        });

        tombolusername = findViewById(R.id.tombolusername);
        tombolusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveactivity();
            }
        });

        messagewarning();
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

    public void show_warning(String message){
        text_msg_warning.setText(message);
        dialog_warning.show();
    }

    public void hide_warning(){
        dialog_warning.hide();
    }

    public void moveactivity(){
        Intent intent = new Intent(this, LoginUserActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}