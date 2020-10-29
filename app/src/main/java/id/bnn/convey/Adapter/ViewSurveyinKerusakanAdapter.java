package id.bnn.convey.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.ViewFotoModel;
import id.bnn.convey.Model.ViewSurveyinKerusakanModel;
import id.bnn.convey.Model.ViewSurveyinModel;
import id.bnn.convey.R;
import id.bnn.convey.ViewSurveyinActivity;

public class ViewSurveyinKerusakanAdapter extends RecyclerView.Adapter<ViewSurveyinKerusakanAdapter.Holder>{

    ViewSurveyinActivity activity;
    Context context;
    List<ViewSurveyinKerusakanModel> list;

    public ViewSurveyinKerusakanAdapter(
            ViewSurveyinActivity activity,
            List<ViewSurveyinKerusakanModel> list
    ){
        this.activity = activity;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_view_surveyin_kerusakan, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textcomponent.setText(list.get(position).getComponent());
        holder.textlocation.setText(list.get(position).getLocation());
        holder.textquantity.setText(list.get(position).getQuantity());
        holder.textdamage.setText(list.get(position).getDamage());
        holder.texttpc.setText(list.get(position).getTpc());
        holder.textrepair.setText(list.get(position).getRepair());
        holder.textcount.setText(String.valueOf(position+1));
        String posisi = list.get(position).getPosisi();
        posisi = posisi.substring(0, 1).toUpperCase() + posisi.substring(1).toLowerCase();
        holder.textposisi.setText(posisi);
        holder.textdimensi.setText(list.get(position).getDimensi());

        // FOTO KERUSAKAN
        holder.listfoto = new ArrayList<>();
        ArrayList<String> foto = list.get(position).getListfoto();

        if(foto.size() == 0){
            holder.layoutfoto.setVisibility(View.GONE);
        }

        for(int i=0; i < foto.size(); i++){
            holder.listfoto.add(new ViewFotoModel(foto.get(i)));
        }
        holder.adapter = new ViewFotoAdapter(activity, holder.listfoto);
        holder.viewfoto.setAdapter(holder.adapter);

        // FOTO REPAIR
        holder.listfotorepair = new ArrayList<>();
        ArrayList<String> fotorepair = list.get(position).getListfotorepair();

        if(fotorepair.size() == 0){
            holder.layoutperbaikan.setVisibility(View.GONE);
        }

        for(int i=0; i < fotorepair.size(); i++){
            holder.listfotorepair.add(new ViewFotoModel(fotorepair.get(i)));
        }
        holder.adapterrepair = new ViewFotoAdapter(activity, holder.listfotorepair);
        holder.viewfotorepair.setAdapter(holder.adapterrepair);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoutfoto;
        LinearLayout layoutperbaikan;

        TextView textcomponent;
        TextView textlocation;
        TextView textquantity;
        TextView textdamage;
        TextView texttpc;
        TextView textrepair;
        TextView textcount;
        TextView textposisi;
        TextView textdimensi;

        RecyclerView viewfoto;
        List<ViewFotoModel> listfoto;
        ViewFotoAdapter adapter;

        RecyclerView viewfotorepair;
        List<ViewFotoModel> listfotorepair;
        ViewFotoAdapter adapterrepair;

        public Holder(View itemview){
            super(itemview);

            textcomponent = itemview.findViewById(R.id.textcomponent);
            textlocation = itemview.findViewById(R.id.textlocation);
            textquantity = itemview.findViewById(R.id.textquantity);
            textdamage = itemview.findViewById(R.id.textdamage);
            texttpc = itemview.findViewById(R.id.texttpc);
            textrepair = itemview.findViewById(R.id.textrepair);
            textcount = itemview.findViewById(R.id.textcount);
            textposisi = itemview.findViewById(R.id.textposisi);
            textdimensi = itemview.findViewById(R.id.textdimensi);

            viewfoto = itemview.findViewById(R.id.viewfoto);
            viewfoto.setHasFixedSize(true);
            viewfoto.setNestedScrollingEnabled(false);
            viewfoto.setLayoutManager(new GridLayoutManager(context, 3));

            viewfotorepair = itemview.findViewById(R.id.viewfotorepair);
            viewfotorepair.setHasFixedSize(true);
            viewfotorepair.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });

            layoutfoto = itemview.findViewById(R.id.layoutfoto);
            layoutperbaikan = itemview.findViewById(R.id.layoutperbaikan);
        }
    }


}
