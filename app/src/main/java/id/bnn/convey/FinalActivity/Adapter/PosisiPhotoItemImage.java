package id.bnn.convey.FinalActivity.Adapter;

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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.PhotoContainerFragment;
import id.bnn.convey.Model.PhotoModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class PosisiPhotoItemImage extends RecyclerView.Adapter<PosisiPhotoItemImage.Holder>{

    Context context;
    List<PhotoModel> list;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;

    PosisiPhotoItem adapter;

    PhotoContainerFragment activity;

    public PosisiPhotoItemImage(
            PhotoContainerFragment activity,
            List<PhotoModel> list,
            PosisiPhotoItem adapter
    ){
        this.activity = activity;
        this.list = list;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v2, parent, false);

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();

        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getImage_dict()).into(holder.imageview);
        Glide.with(context).load(R.drawable.img_remove).into(holder.imageremove);
        holder.textcount.setText(String.valueOf(position+1));
        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("photo","[]")).getAsJsonArray();
                JsonArray dataarray_remove = new JsonParser().parse(datainputsurveyin.getString("photo_remove","[]")).getAsJsonArray();

                for(int i=0; i < dataarray.size(); i++){
                    JsonObject datajson = dataarray.get(i).getAsJsonObject();
                    String mobid_photo = datajson.get("mobid_photo").getAsString();

                    if(list.get(position).getId_foto().equals(mobid_photo)){
                        dataarray.remove(i);
                        if(datajson.get("action").getAsString().equals("add")){
                            new VariableText().removePhoto(datajson.get("file_temp").getAsString());
                        }else{
                            dataarray_remove.add(mobid_photo);
                        }

                        savedata(String.valueOf(dataarray), String.valueOf(dataarray_remove));
                        break;
                    }
                }
                list.remove(position);
                activity.adapterposisi.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
            }
        });

        holder.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.showdialog_image(list.get(position).getImage_dict());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageview;
        ImageView imageremove;
        TextView textcount;
        LinearLayout tombolhapus;

        public Holder(View itemview){
            super(itemview);
            imageview = itemview.findViewById(R.id.image);
            textcount = itemview.findViewById(R.id.textcount);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
            imageremove = itemview.findViewById(R.id.imageremove);
        }
    }

    public void savedata(String dataphoto, String dataphotoremove){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin_edit.putString("photo", dataphoto);
        datainputsurveyin_edit.putString("photo_remove", dataphotoremove);
        datainputsurveyin_edit.apply();
    }
}
