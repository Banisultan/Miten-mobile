package id.bnn.convey.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FragmentSurvey.DeskripsiSurveyFragment;
import id.bnn.convey.FragmentSurvey.InputKerusakan;
import id.bnn.convey.Model.ListKerusakan;
import id.bnn.convey.Model.ListPhoto;
import id.bnn.convey.R;

public class KerusakanAdapter extends RecyclerView.Adapter<KerusakanAdapter.Holder>{

    Context context;
    List<ListKerusakan> list;
    AlertDialog message_alert;
    LinearLayout tombolyes;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datalist;

    int position_remove = -1;
    String ID_SURVEY = "";

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    LinearLayout layoutmessage;
    LinearLayout tomboloke;
    ImageView imagemessage;
    TextView textmessage;
    TextView texttombol;

    public KerusakanAdapter(
            Context context,
            List<ListKerusakan> list,
            AlertDialog message_alert,
            LinearLayout tombolyes,
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.context = context;
        this.list = list;
        this.message_alert = message_alert;
        this.tombolyes = tombolyes;
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datalist = context.getSharedPreferences("datalistsurvey", Context.MODE_PRIVATE);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_kerusakan, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if(list.get(position).isHeader()){
            holder.layoutheader.setVisibility(View.VISIBLE);
            holder.textheader.setText(list.get(position).getHeader());
        }else{
            holder.layoutheader.setVisibility(View.GONE);
        }

        holder.textcomponent.setText(list.get(position).getComponent());
        holder.textcount.setText(String.valueOf(list.get(position).getCountimage()));

        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_alert.show();
                position_remove = position;
                ID_SURVEY = list.get(position).getIdsurvey();
            }
        });

        holder.tomboledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveedit(position);
            }
        });

        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removedata(position_remove);
                saveremovedata(ID_SURVEY);

                notifyDataSetChanged();
                message_alert.dismiss();
                position_remove = -1;
                ID_SURVEY = "";
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoutheader;
        TextView textheader;
        TextView textcomponent;
        TextView textcount;
        ImageView tombolhapus;
        ImageView tomboledit;

        public Holder(View itemview){
            super(itemview);
            layoutheader = itemview.findViewById(R.id.layoutheader);
            textheader = itemview.findViewById(R.id.textheader);
            textcomponent = itemview.findViewById(R.id.textcomponent);
            textcount = itemview.findViewById(R.id.textcount);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
            tomboledit = itemview.findViewById(R.id.tomboledit);
        }
    }

    public void removedata(int position_remove){
        String data = datainputsurveyin.getString("surveyindetail", "");
        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                dataarray.remove(position_remove);

                list = new ArrayList<>();

                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    JSONArray datafoto = datajson.getJSONArray("foto");
                    String component = datajson.getString("componentcode")+" - "+datajson.getString("component");
                    int countimage = datafoto.length();
                    String idsurvey = datajson.getString("id_survey_detail");

                    list.add(new ListKerusakan("null", false, component, countimage, idsurvey));
                }
                datainputsurveyin_edit.putString("surveyindetail", String.valueOf(dataarray));
                datainputsurveyin_edit.apply();

            }catch (Exception e){
                Log.d("TAGGAR", "remove ggaga "+e);
            }
        }
    }

    public void openfragment(){
        Fragment fragment = new DeskripsiSurveyFragment(
                viewv2,
                viewstep,
                viewline,
                "edit"
        );
        ((FragmentActivity)context).getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("")
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void saveedit(int position){
        String data = datainputsurveyin.getString("surveyindetail", "");
        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                JSONObject datajson = dataarray.getJSONObject(position);

                datainputsurveyin_edit.putString("dataedit", String.valueOf(datajson));
                datainputsurveyin_edit.apply();
            }catch (Exception e){
                Log.d("TAGGAR", "error dink "+e);
            }
        }

        openfragment();
    }

    public void saveremovedata(String id_survey){
        if(!id_survey.equals("null")){
            String data = datainputsurveyin.getString("datahapus", "");
            try{
                JSONArray dataarray;
                int posisi = -1;

                if(!data.equals("")){
                    dataarray = new JSONArray(data);
                    for (int i=0; i< dataarray.length(); i++){
                        JSONObject datajson = dataarray.getJSONObject(i);

                        if(datajson.getString("id_survey_detail").equals(id_survey)){
                            posisi = i;
                            break;
                        }
                    }
                }else{
                    dataarray = new JSONArray();
                }

                if(posisi == -1){
                    posisi = dataarray.length();
                }

                JSONObject datajson_view = new JSONObject();
                datajson_view.put("id_survey_detail", id_survey);
                datajson_view.put("hapus", "true");
                datajson_view.put("foto", "[]");

                dataarray.put(posisi, datajson_view);

                datainputsurveyin_edit.putString("datahapus", String.valueOf(dataarray));
                datainputsurveyin_edit.apply();
            }catch (Exception e){
                Log.d("TAGGAR", "ini error "+e);
            }
        }
    }
}
