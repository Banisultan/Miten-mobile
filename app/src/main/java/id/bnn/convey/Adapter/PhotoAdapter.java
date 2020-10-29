package id.bnn.convey.Adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.ListFotoViewModel;
import id.bnn.convey.Model.ListPhoto;
import id.bnn.convey.R;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.Holder>{

    Context context;
    List<ListPhoto> list;
    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;

    public PhotoAdapter(
            Context context,
            List<ListPhoto> list
    ){
        this.context = context;
        this.list = list;

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v2, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textcount.setText(String.valueOf(position+1));

        String url_image = list.get(position).getUrl();
        String url_image_file = list.get(position).getFile_dir();
        if(url_image.equals("null")){
            Glide.with(context).load(url_image_file).into(holder.imageview);
        }else{
            Glide.with(context).load(url_image).into(holder.imageview);
        }

        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveremovedata(list.get(position).getId_survey(), list.get(position).getId_foto());
                list.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        TextView textcount;
        ImageView imageview;
        LinearLayout tombolhapus;

        public Holder(View itemview){
            super(itemview);

            textcount = itemview.findViewById(R.id.textcount);
            imageview = itemview.findViewById(R.id.image);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
        }
    }

    public void saveremovedata(String id_survey, String id_foto){
        if(!id_survey.equals("null")){
            String data = datainputsurveyin.getString("datahapus", "");
            try{
                JSONArray dataarray = null;
                int posisi = -1;

                JSONArray datafoto = null;
                if(!data.equals("")){
                    dataarray = new JSONArray(data);
                    for (int i=0; i< dataarray.length(); i++){
                        JSONObject datajson = dataarray.getJSONObject(i);

                        if(datajson.getString("id_survey_detail").equals(id_survey)){
                            datafoto = new JSONArray(datajson.getString("foto"));
                            datafoto.put(datafoto.length(), id_foto);
                            posisi = i;
                            break;
                        }
                    }
                }

                if(posisi == -1){
                    dataarray = new JSONArray();
                    datafoto = new JSONArray();
                    datafoto.put(0, id_foto);
                    posisi = 0;
                }

                JSONObject datajson_view = new JSONObject();
                datajson_view.put("id_survey_detail", id_survey);
                datajson_view.put("hapus", "false");
                datajson_view.put("foto", datafoto);

                dataarray.put(posisi, datajson_view);

                datainputsurveyin_edit.putString("datahapus", String.valueOf(dataarray));
                datainputsurveyin_edit.apply();
            }catch (Exception e){
                Log.d("TAGGAR", "ini error "+e);
            }
        }
    }
}
