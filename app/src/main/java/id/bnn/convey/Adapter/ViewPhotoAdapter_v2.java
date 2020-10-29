package id.bnn.convey.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.bnn.convey.Model.PositionPhotoModel;
import id.bnn.convey.Model.ViewPhotoModel;
import id.bnn.convey.R;
import id.bnn.convey.ViewSurveyinActivity;

public class ViewPhotoAdapter_v2 extends RecyclerView.Adapter<ViewPhotoAdapter_v2.Holder>{

    ViewSurveyinActivity activity;
    Context context;
    List<ViewPhotoModel> list;

    public ViewPhotoAdapter_v2(
            ViewSurveyinActivity activity,
            List<ViewPhotoModel> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v6, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Picasso.get().load(list.get(position).getImage_file())
                .resize(500,500)
                .centerCrop()
                .into(holder.imageview);
        holder.tombolfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showdialog_image(list.get(position).getImage_file());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView imageview;
        RelativeLayout tombolfoto;

        public Holder(View itemview){
            super(itemview);
            imageview = itemview.findViewById(R.id.image);
            tombolfoto = itemview.findViewById(R.id.tombolfoto);
        }
    }
}
