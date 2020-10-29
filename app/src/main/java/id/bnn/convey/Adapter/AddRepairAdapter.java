package id.bnn.convey.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.AddRepairActivity;
import id.bnn.convey.Model.ListAddRepairModel;
import id.bnn.convey.Model.ListFotoView;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class AddRepairAdapter extends RecyclerView.Adapter<AddRepairAdapter.Holder>{

    AddRepairActivity activity;
    Context context;
    List<ListAddRepairModel> list;

    VariableText var = new VariableText();
    public AddRepairAdapter(
            AddRepairActivity activity,
            Context context,
            List<ListAddRepairModel> list
    ){
        this.activity = activity;
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_repair, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textcomponent.setText(list.get(position).getComponent());
        holder.textcount.setText(String.valueOf(list.get(position).getCountfoto()));

        if(list.get(position).isHeader()){
            holder.layoutheader.setVisibility(View.VISIBLE);
            holder.textheader.setText(list.get(position).getHeader());
        }else{
            holder.layoutheader.setVisibility(View.GONE);
        }

        ArrayList<String> listkerusakan = list.get(position).getListkerusakan();
        holder.listkerusakan = new ArrayList<>();

        if(listkerusakan.size() == 0){
            holder.textempty.setVisibility(View.VISIBLE);
        }

        for (int i=0; i < listkerusakan.size(); i++){
            holder.listkerusakan.add(new ListFotoView(listkerusakan.get(i)));
        }

        holder.adapter = new FotoViewAdapter(context, holder.listkerusakan);
        holder.viewkerusakan.setAdapter(holder.adapter);

        holder.tomboladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.opencamera(position);
            }
        });

        ArrayList<String> listperbaikan = list.get(position).getListrepair();
        holder.listperbaikan = new ArrayList<>();
        if(listperbaikan.size() == 0){
            holder.layoutperbaikan.setVisibility(View.GONE);
        }else{
            for (int i=0; i < listperbaikan.size(); i++){
                holder.listperbaikan.add(new ListFotoView(listperbaikan.get(i)));
            }
            holder.layoutperbaikan.setVisibility(View.VISIBLE);
            holder.countperbaikan.setText(String.valueOf(listperbaikan.size()));
        }

        holder.adapterperbaikan = new FotoViewRepairAdapter(context, holder.listperbaikan, list, this, position);
        holder.viewperbaikan.setAdapter(holder.adapterperbaikan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoutheader;
        TextView textheader;
        TextView textcomponent;
        TextView textcount;
        LinearLayout tomboladd;
        TextView textempty;

        RecyclerView viewkerusakan;
        List<ListFotoView> listkerusakan;
        FotoViewAdapter adapter;

        List<ListFotoView> listperbaikan;
        LinearLayout layoutperbaikan;
        RecyclerView viewperbaikan;
        FotoViewRepairAdapter adapterperbaikan;
        TextView countperbaikan;

        public Holder(View itemview){
            super(itemview);
            layoutheader = itemview.findViewById(R.id.layoutheader);
            textheader = itemview.findViewById(R.id.textheader);
            textcomponent = itemview.findViewById(R.id.textcomponent);
            textcount = itemview.findViewById(R.id.textcount);
            textempty = itemview.findViewById(R.id.textempty);
            textempty.setVisibility(View.GONE);

            viewkerusakan = itemview.findViewById(R.id.viewkerusakan);
            viewkerusakan.setHasFixedSize(true);
            viewkerusakan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            viewperbaikan = itemview.findViewById(R.id.viewperbaikan);
            viewperbaikan.setHasFixedSize(true);
            viewperbaikan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            countperbaikan = itemview.findViewById(R.id.textcountperbaikan);

            tomboladd = itemview.findViewById(R.id.layoutbutton);
            layoutperbaikan = itemview.findViewById(R.id.layoutperbaikan);
        }
    }
}
