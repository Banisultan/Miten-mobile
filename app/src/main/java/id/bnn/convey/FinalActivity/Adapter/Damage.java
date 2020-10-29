package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.DamageContainerFragment;
import id.bnn.convey.FinalActivity.Model.m_Damage;
import id.bnn.convey.R;

public class Damage extends RecyclerView.Adapter<Damage.Holder>{

    Context context;
    List<m_Damage> list;
    DamageContainerFragment activity;

    public Damage(
            DamageContainerFragment activity,
            List<m_Damage> list
    ){
        this.list = list;
        this.activity = activity;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(context).inflate(R.layout.layout_adddamage, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String mobid_damage =  list.get(position).getMobid_damage();

        holder.textcomponent.setText(list.get(position).getComponent());
        holder.textcount.setText(String.valueOf(list.get(position).getCount()));

        Glide.with(context).load(R.drawable.img_camera_v2).into(holder.imagefoto);
        Glide.with(context).load(R.drawable.img_edit).into(holder.tomboledit);
        Glide.with(context).load(R.drawable.img_remove_v2).into(holder.tombolhapus);

        holder.tombolhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.showmessage_alert("Yakin akan menghapus data kerusakan ?", mobid_damage);
            }
        });

        holder.tomboledit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.editdata(mobid_damage);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView textcomponent;
        TextView textcount;
        ImageView tombolhapus;
        ImageView tomboledit;
        ImageView imagefoto;

        public Holder(View itemview){
            super(itemview);
            textcomponent = itemview.findViewById(R.id.textcomponent);
            textcount = itemview.findViewById(R.id.textcount);
            tombolhapus = itemview.findViewById(R.id.tombolhapus);
            tomboledit = itemview.findViewById(R.id.tomboledit);

            imagefoto = itemview.findViewById(R.id.imagefoto);
        }
    }
}
