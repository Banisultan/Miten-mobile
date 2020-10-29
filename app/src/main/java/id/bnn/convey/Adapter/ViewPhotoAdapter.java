package id.bnn.convey.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.PositionPhotoModel;
import id.bnn.convey.Model.ViewPhotoModel;
import id.bnn.convey.R;

public class ViewPhotoAdapter extends RecyclerView.Adapter<ViewPhotoAdapter.Holder>{

    Context context;
    List<ViewPhotoModel> list;
    PositionPhotoAdapter adapterphoto;
    List<PositionPhotoModel> listphoto;
    int POSITION;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;

    public ViewPhotoAdapter(
            Context context,
            List<ViewPhotoModel> list,
            PositionPhotoAdapter adapterphoto,
            List<PositionPhotoModel> listphoto,
            int POSITION
    ){
        this.context = context;
        this.list = list;
        this.adapterphoto = adapterphoto;
        this.listphoto = listphoto;
        this.POSITION = POSITION;

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v2, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getImage_file()).into(holder.imageview);
        holder.textcount.setText(String.valueOf(position+1));
        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!listphoto.get(POSITION).getList_photo().get(position).getId_foto().equals("null")){
                    saveremovedata(listphoto.get(POSITION).getList_photo().get(position).getId_foto());
                }
                listphoto.get(POSITION).getList_photo().remove(position);
                adapterphoto.notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageview;
        TextView textcount;
        LinearLayout tombolhapus;
        public Holder(View itemview){
            super(itemview);
            imageview = itemview.findViewById(R.id.image);
            textcount = itemview.findViewById(R.id.textcount);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
        }
    }

    public void saveremovedata(String idfoto){
        String data = datainputsurveyin.getString("datahapusfoto", "[]");
        try{
            JSONArray dataarray = new JSONArray(data);

            JSONObject datajson = new JSONObject();
            datajson.put("id_foto", idfoto);

            dataarray.put(dataarray.length(), datajson);


            datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
            datainputsurveyin_edit.putString("datahapusfoto", String.valueOf(dataarray));
            datainputsurveyin_edit.apply();
        }catch (Exception e){

        }
    }
}
