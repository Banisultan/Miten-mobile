package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.PhotoContainerFragment;
import id.bnn.convey.Model.PhotoModel;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhotoItem;
import id.bnn.convey.R;

public class PosisiPhotoItem extends RecyclerView.Adapter<PosisiPhotoItem.Holder>{

    PhotoContainerFragment activity;
    Context context;
    List<m_PosisiPhotoItem> list;

    public PosisiPhotoItem(
            PhotoContainerFragment activity,
            List<m_PosisiPhotoItem> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_photo, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textheader.setText(list.get(position).getPosition());
        List<PhotoModel> listphoto = list.get(position).getList_photo();

        if(listphoto.size() == 0){
            holder.layoututama.setVisibility(View.GONE);
        }else {
            holder.layoututama.setVisibility(View.VISIBLE);
        }

        holder.adapterphoto = new PosisiPhotoItemImage(activity, listphoto, PosisiPhotoItem.this);
        holder.viewphoto.setAdapter(holder.adapterphoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoututama;
        RecyclerView viewphoto;
        TextView textheader;

        PosisiPhotoItemImage adapterphoto;

        public Holder(View itemview){
            super(itemview);

            textheader = itemview.findViewById(R.id.textheader);
            viewphoto = itemview.findViewById(R.id.viewphoto);
            viewphoto.setHasFixedSize(true);
            viewphoto.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            layoututama = itemview.findViewById(R.id.layoututama);
        }
    }
}
