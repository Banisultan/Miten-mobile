package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.PhotoContainerFragment;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhoto;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhotoItem;
import id.bnn.convey.R;

public class PosisiPhoto extends RecyclerView.Adapter<PosisiPhoto.Holder>{

    PhotoContainerFragment activity;
    Context context;
    List<m_PosisiPhoto> list;
    List<m_PosisiPhotoItem> listitem;

    public PosisiPhoto(
            List<m_PosisiPhoto> list,
            List<m_PosisiPhotoItem> listitem,
            PhotoContainerFragment activity
    ){
        this.list = list;
        this.listitem = listitem;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_position_v2, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String name = list.get(position).getName();
        String code = list.get(position).getCode();
        int count = listitem.get(position).getList_photo().size();

        Glide.with(context).load(R.drawable.img_camera).into(holder.imageview);

        if(count == 0){
            holder.textcount.setVisibility(View.GONE);
        }else{
            holder.textcount.setVisibility(View.VISIBLE);
            holder.textcount.setText(String.valueOf(count));
        }

        holder.textname.setText(name);
        holder.tombolview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.checkpermision(code, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        RelativeLayout tombolview;
        TextView textname;
        TextView textcount;
        ImageView imageview;

        public Holder(View itemview){
            super(itemview);
            textname = itemview.findViewById(R.id.textnama);
            tombolview = itemview.findViewById(R.id.tombolview);
            textcount = itemview.findViewById(R.id.textcount);
            imageview = itemview.findViewById(R.id.imageview);
        }
    }
}
