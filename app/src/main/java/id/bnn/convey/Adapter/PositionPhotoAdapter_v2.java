package id.bnn.convey.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.PositionPhotoModel;
import id.bnn.convey.Model.PositionPhotoModel_v2;
import id.bnn.convey.Model.ViewPhotoModel;
import id.bnn.convey.R;
import id.bnn.convey.ViewSurveyinActivity;

public class PositionPhotoAdapter_v2 extends RecyclerView.Adapter<PositionPhotoAdapter_v2.Holder>{

    ViewSurveyinActivity activity;
    Context context;
    List<PositionPhotoModel_v2> list;

    public PositionPhotoAdapter_v2(
            ViewSurveyinActivity activity,
            List<PositionPhotoModel_v2> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_photo_v2, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textheader.setText("Photo "+list.get(position).getPosition());

        ArrayList<String> listdata = list.get(position).getList_photo();
        if(listdata.size() == 0){
            holder.layoututama.setVisibility(View.GONE);
        }else{
            holder.layoututama.setVisibility(View.VISIBLE);
        }

        for(int i=0; i<listdata.size(); i++){
            holder.listphoto.add(new ViewPhotoModel(listdata.get(i)));
        }
        holder.adapterphoto = new ViewPhotoAdapter_v2(activity, holder.listphoto);
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
        List<ViewPhotoModel> listphoto;
        ViewPhotoAdapter_v2 adapterphoto;

        public Holder(View itemview){
            super(itemview);

            textheader = itemview.findViewById(R.id.textheader);
            viewphoto = itemview.findViewById(R.id.viewphoto);
            viewphoto.setHasFixedSize(true);
            viewphoto.setNestedScrollingEnabled(false);
            viewphoto.setLayoutManager(new GridLayoutManager(context, 3));

            listphoto = new ArrayList<>();

            layoututama = itemview.findViewById(R.id.layoututama);
        }
    }
}
