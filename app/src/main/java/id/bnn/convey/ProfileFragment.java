package id.bnn.convey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.messaging.FirebaseMessaging;

import id.bnn.convey.FinalActivity.LoginActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    Context context;
    TextView textnama;
    LinearLayout layoutakses;
    LinearLayout tombolkeluar;

    VariableText var = new VariableText();

    AlertDialog.Builder message_alert;
    AlertDialog show_alert;
    LayoutInflater inflate_alert;
    View view_alert;

    LinearLayout tombolno;
    LinearLayout tombolyes;
    TextView texttombolyes;
    TextView textmessage;
    TextView textjudul;

    SharedPreferences dataakun;
    SharedPreferences datauser;
    String ID;

    ImageView imageprofile;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPause(){
        super.onPause();
        hideall();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        hideall();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_profile, container, false);

        context = container.getContext();

        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        ID = dataakun.getString("idakun", "");

        textnama = itemview.findViewById(R.id.textnama);
        layoutakses = itemview.findViewById(R.id.layoutakses);

        tombolkeluar = itemview.findViewById(R.id.tombolkeluar);
        tombolkeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShow_alert();
            }
        });

        imageprofile = itemview.findViewById(R.id.imageprofile);
        Glide.with(context).load(R.drawable.img_profile).into(imageprofile);
        showdialog();
        getData();
        return itemview;
    }

    public void showdialog(){
        message_alert = new AlertDialog.Builder(context);
        inflate_alert = getLayoutInflater();
        show_alert = message_alert.create();
        view_alert = inflate_alert.inflate(R.layout.layout_message_option_v2, null, false);
        message_alert.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_alert.setView(view_alert);
        message_alert.setCancelable(true);
        show_alert = message_alert.create();

        tombolno = view_alert.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideall();
            }
        });

        tombolyes = view_alert.findViewById(R.id.tombolyes);
        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        texttombolyes = view_alert.findViewById(R.id.texttombolyes);
        texttombolyes.setText("Logout");

        textmessage = view_alert.findViewById(R.id.textmessage);
        textmessage.setText("Yakin akan keluar ?");

        textjudul = view_alert.findViewById(R.id.textjudul);
        textjudul.setText("Perhatian");

    }

    public void setShow_alert(){
        show_alert.show();
    }

    public void hideall(){
        show_alert.dismiss();
    }

    public void getData(){
        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        textnama.setText(dataakun.getString("nama", ""));
        String tipe = dataakun.getString("tipe", "");

        if(tipe.equals("SUPERVISOR")){
            layoutakses.setVisibility(View.VISIBLE);
        }else{
            layoutakses.setVisibility(View.GONE);
        }
    }

    public void endactivity(){
        var.unsubscribe(ID);
        dataakun.edit().clear().apply();
        datauser.edit().clear().apply();
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
        ((Activity)context).finish();
    }
}
