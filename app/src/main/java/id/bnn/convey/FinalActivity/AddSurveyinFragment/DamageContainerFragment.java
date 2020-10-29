package id.bnn.convey.FinalActivity.AddSurveyinFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FinalActivity.Adapter.Damage;
import id.bnn.convey.FinalActivity.Model.m_Damage;
import id.bnn.convey.R;

public class DamageContainerFragment extends Fragment {

    Context context;

    LinearLayout tomboladd;
    LinearLayout tombollanjut;
    LinearLayout tombolkembali;
    ImageView imageadd;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;

    List<m_Damage> list;
    RecyclerView view;
    Damage adapter;

    Dialog dialog_alert;
    TextView text_msg_alert;
    ImageView image_msg_alert;
    LinearLayout tombolok_msg_alert;
    LinearLayout tombolbatal_msg_alert;

    String MOBID_DAMAGE;

    public DamageContainerFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hide_alert();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        View itemview = inflater.inflate(R.layout.fragment_damage_container, container, false);

        for(int i=1; i<viewline.length; i++){
            if(i<=4){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);
        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        imageadd = itemview.findViewById(R.id.imageadd);
        Glide.with(context).load(R.drawable.img_add).into(imageadd);

        view = itemview.findViewById(R.id.view);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextfragment();
            }
        });

        tombolkembali = itemview.findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevfragment();
            }
        });

        tomboladd = itemview.findViewById(R.id.tomboladd);
        tomboladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adddamagefragment();
            }
        });

        messagedialog_alert();
        loaddata();
        return itemview;
    }

    public void messagedialog_alert(){
        dialog_alert = new Dialog(context);
        dialog_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_alert.setContentView(R.layout.layout_alert);
        dialog_alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        text_msg_alert = dialog_alert.findViewById(R.id.textmessage);
        image_msg_alert = dialog_alert.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(image_msg_alert);

        tombolok_msg_alert = dialog_alert.findViewById(R.id.tomboloke);
        tombolok_msg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MOBID_DAMAGE != null){
                    removedata(MOBID_DAMAGE);
                }
            }
        });

        tombolbatal_msg_alert = dialog_alert.findViewById(R.id.tombolbatal);
        tombolbatal_msg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_alert();
            }
        });
    }

    public void hide_alert(){
        dialog_alert.dismiss();
    }

    public void showmessage_alert(String text, String mobid_damage){
        MOBID_DAMAGE = mobid_damage;
        text_msg_alert.setText(text);
        dialog_alert.show();
    }

    public void loaddata(){
        list = new ArrayList<>();
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin_edit.putString("datadamage_edit", "");
        datainputsurveyin_edit.apply();

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("datadamage", "[]")).getAsJsonArray();


        for(int i=0; i<dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            list.add(new m_Damage(
                    datajson.get("mobid_damage").getAsString(),
                    datajson.get("componentcode").getAsString()+" - "+datajson.get("componentname").getAsString(),
                    datajson.get("countimage").getAsInt()));
        }

        adapter = new Damage(this, list);
        view.setAdapter(adapter);
    }

    public void editdata(String mobid_damage){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("datadamage", "[]")).getAsJsonArray();

        JsonObject datajson_edit = new JsonObject();
        for(int i=0; i<dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            if(datajson.get("mobid_damage").getAsString().equals(mobid_damage)){
                datajson_edit = datajson;
                break;
            }
        }

        datainputsurveyin_edit.putString("datadamage_edit", String.valueOf(datajson_edit));
        datainputsurveyin_edit.apply();

        hide_alert();
        adddamagefragment();
    }

    public void removedata(String mobid_damage){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("datadamage", "[]")).getAsJsonArray();
        JsonArray dataarray_remove = new JsonParser().parse(datainputsurveyin.getString("damage_remove", "[]")).getAsJsonArray();

        for(int i=0; i<dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            if(datajson.get("mobid_damage").getAsString().equals(mobid_damage)){
                dataarray.remove(i);
                list.remove(i);

                if(datajson.get("action").getAsString().equals("edit")){
                    dataarray_remove.add(mobid_damage);
                }
                break;
            }
        }

        datainputsurveyin_edit.putString("datadamage", String.valueOf(dataarray));
        datainputsurveyin_edit.putString("damage_remove", String.valueOf(dataarray_remove));
        datainputsurveyin_edit.apply();

        hide_alert();
        adapter.notifyDataSetChanged();
    }

    public void nextfragment(){
        Fragment fragment = new SimpanSurveyFragment(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void prevfragment(){
        Fragment fragment = new PhotoContainerFragment(
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

    public void adddamagefragment(){
        Fragment fragment = new DamageAddFragment(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }
}