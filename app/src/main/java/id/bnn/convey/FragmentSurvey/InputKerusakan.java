package id.bnn.convey.FragmentSurvey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.KerusakanAdapter;
import id.bnn.convey.Model.ListKerusakan;
import id.bnn.convey.R;

public class InputKerusakan extends Fragment {

    Context context;

    LinearLayout tombolkerusakan;
    LinearLayout tombollanjut;
    LinearLayout tombolprev;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    RecyclerView view;
    List<ListKerusakan> list;
    KerusakanAdapter adapter;

    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;
    SharedPreferences datalist;

    AlertDialog.Builder message_alert;
    AlertDialog message_show;
    LayoutInflater message_inflate;
    View message_view;

    LinearLayout tombolyes;
    LinearLayout tombolno;


    public InputKerusakan(
            ImageView view2,
            TextView[] viewstep,
            View[] viewline
    ) {
        this.viewv2 = view2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        message_show.dismiss();
    }

    @Override
    public void onPause(){
        super.onPause();
        message_show.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_input_kerusakan, container, false);

        for(int i=1; i<viewline.length; i++){
            if(i<=3){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewv2.setImageResource(R.drawable.vec_warning_v2);
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);

        context = container.getContext();

        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datalist = context.getSharedPreferences("datalistitem", Context.MODE_PRIVATE);

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextfragment();
            }
        });

        tombolprev = itemview.findViewById(R.id.tombolprev);
        tombolprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevfragment();
            }
        });

        tombolkerusakan = itemview.findViewById(R.id.tomboladdkerusakan);
        tombolkerusakan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openfragment();
            }
        });

        view = itemview.findViewById(R.id.viewlist);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));

        showdialog();
        addData();
        return itemview;
    }

    public void showdialog(){
        message_alert = new AlertDialog.Builder(context);
        message_inflate = getLayoutInflater();
        message_view = message_inflate.inflate(R.layout.layout_message_option_v2, null, false);
        message_alert.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_alert.setView(message_view);
        message_alert.setCancelable(true);
        message_show = message_alert.create();

        tombolyes = message_view.findViewById(R.id.tombolyes);
        tombolno = message_view.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_show.dismiss();
            }
        });
    }

    public void addData(){
        list = new ArrayList<>();

        List<String> position_list = new ArrayList<String>();
        position_list.add("Damage");

        String[] position_array = new String[position_list.size()];
        position_list.toArray(position_array);
        String data = datainputsurveyin.getString("surveyindetail", "");
        Log.d("TAGGAR", "surveyin detail "+data);
        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    JSONArray datafoto = datajson.getJSONArray("foto");
                    String component = datajson.getString("componentcode")+" - "+datajson.getString("component");
                    int countimage = datafoto.length();
                    String idsurvey = datajson.getString("id_survey_detail");

                    list.add(new ListKerusakan("null", false, component, countimage, idsurvey));
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res  2: "+String.valueOf(e));
            }
        }

        adapter = new KerusakanAdapter(
                context,
                list,
                message_show,
                tombolyes,
                viewv2,
                viewstep,
                viewline);
        view.setAdapter(adapter);
    }


    public void openfragment(){
        Fragment fragment = new DeskripsiSurveyFragment(
                viewv2,
                viewstep,
                viewline,
                "default"
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void nextfragment(){
        Fragment fragment = new ConfirmSurveyinFragment(
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
        Fragment fragment = new InputFotoSurveyin(
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

}