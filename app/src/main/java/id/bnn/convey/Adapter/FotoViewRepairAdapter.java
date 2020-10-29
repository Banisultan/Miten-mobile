package id.bnn.convey.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bnn.convey.Model.ListAddRepairModel;
import id.bnn.convey.Model.ListFotoView;
import id.bnn.convey.R;

public class FotoViewRepairAdapter extends RecyclerView.Adapter<FotoViewRepairAdapter.Holder>{

    Context context;
    List<ListFotoView> list;
    List<ListAddRepairModel> listperbaikan;
    int position_utama;
    AddRepairAdapter adapterperbaikan;

    public FotoViewRepairAdapter(
            Context context,
            List<ListFotoView> list,
            List<ListAddRepairModel> listperbaikan,
            AddRepairAdapter adapterperbaikan,
            int position_utama
    ){
        this.context = context;
        this.list = list;
        this.listperbaikan = listperbaikan;
        this.position_utama = position_utama;
        this.adapterperbaikan = adapterperbaikan;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v5, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getUrl()).into(holder.imageview);
        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listperbaikan.get(position_utama).getListrepair().remove((position));
                adapterperbaikan.notifyDataSetChanged();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout tombolhapus;
        ImageView imageview;

        public Holder(View itemview){
            super(itemview);
            imageview = itemview.findViewById(R.id.imgview);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
        }
    }

    public void moveactivity(Intent intent){
        context.startActivity(intent);
    }
}
