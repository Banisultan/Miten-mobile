package id.bnn.convey.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.bnn.convey.Model.ViewFotoModel;
import id.bnn.convey.Model.ViewSurveyinModel;
import id.bnn.convey.R;
import id.bnn.convey.ViewSurveyinActivity;

public class ViewFotoAdapter extends RecyclerView.Adapter<ViewFotoAdapter.Holder>{

    ViewSurveyinActivity activity;
    Context context;
    List<ViewFotoModel> list;

    public ViewFotoAdapter(
            ViewSurveyinActivity activity,
            List<ViewFotoModel> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v3, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Picasso.get().load(list.get(position).getUrl())
                .resize(500,500)
                .centerCrop()
                .into(holder.imgview);


        holder.tombolview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showdialog_image(list.get(position).getUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView imgview;
        LinearLayout tombolview;

        public Holder(View itemview){
            super(itemview);

            tombolview = itemview.findViewById(R.id.tombolview);
            imgview = itemview.findViewById(R.id.imgview);
        }
    }
}
