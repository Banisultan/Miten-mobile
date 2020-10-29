package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.bnn.convey.FinalActivity.AddSurveyinFragment.DamageAddFragment;
import id.bnn.convey.FinalActivity.Model.m_Item;
import id.bnn.convey.R;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.Holder>{

    Context context;
    DamageAddFragment activity;
    List<m_Item> list;

    String dialog_activity;
    String codeitem;

    public ItemAdapter(
            String dialog_activity,
            List<m_Item> list,
            DamageAddFragment activity,
            String codeitem
    ){
        this.dialog_activity = dialog_activity;
        this.list = list;
        this.activity = activity;
        this.codeitem = codeitem;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_component_code, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String name = list.get(position).getName();
        String code = list.get(position).getCode();

        if(dialog_activity.equals("DIMENSI")){
            holder.textnama.setText(code);
        }else{
            holder.textnama.setText(code+" - "+name);
        }

        if(!codeitem.equals("")){
            if(list.get(position).getCode().equals(codeitem)){
                holder.tombollist.setBackgroundResource(R.drawable.bg_shape_v9);
                holder.textnama.setTextColor(ContextCompat.getColor(context, R.color.colorwhite));
            }else{
                holder.tombollist.setBackgroundResource(R.drawable.bg_shape_v7);
                holder.textnama.setTextColor(ContextCompat.getColor(context, R.color.colorblack_v2));
            }
        }else{
            holder.tombollist.setBackgroundResource(R.drawable.bg_shape_v7);
        }

        holder.tombollist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (dialog_activity){
                    case "COMPONENT":
                        activity.setComponent(code, name);
                        break;
                    case "DAMAGE":
                        activity.setDamage(code, name);
                        break;
                    case "REPAIR":
                        activity.setRepair(code, name);
                        break;
                    case "DIMENSI":
                        activity.setDimension(code, name);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout tombollist;
        TextView textnama;

        public Holder(View itemview){
            super(itemview);
            textnama = itemview.findViewById(R.id.textnama);
            tombollist = itemview.findViewById(R.id.tombollist);
        }
    }
}
