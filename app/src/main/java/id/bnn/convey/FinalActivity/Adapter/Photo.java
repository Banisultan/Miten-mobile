package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.DamageAddFragment;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhotoItemImage;
import id.bnn.convey.R;

public class Photo extends RecyclerView.Adapter<Photo.Holder>{

    Context context;
    List<m_PosisiPhotoItemImage> list;
    DamageAddFragment activity;

    public Photo(
            DamageAddFragment activity,
            List<m_PosisiPhotoItemImage> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v2, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getImage_dict()).into(holder.imageview);
        Glide.with(context).load(R.drawable.img_remove_v2).into(holder.imageremove);
        holder.textcount.setText(String.valueOf(position+1));
        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removefoto(list.get(position).getImage_dict(), list.get(position).getId_foto(), list.get(position).getAction());
                list.remove(position);
                notifyDataSetChanged();
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
            imageremove = itemview.findViewById(R.id.imageremove);
            textcount = itemview.findViewById(R.id.textcount);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
        }
    }
}
