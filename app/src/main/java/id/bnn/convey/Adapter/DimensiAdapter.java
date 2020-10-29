package id.bnn.convey.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.DimensiModel;
import id.bnn.convey.Model.ListComponentCodeModel;
import id.bnn.convey.R;

public class DimensiAdapter extends RecyclerView.Adapter<DimensiAdapter.Holder>{

    Context context;
    List<DimensiModel> list;
    ArrayList<String> selectdimension;
    LinearLayout tombolyes;
    AlertDialog message_show;
    TextView tomboldimension;

    int position_on = -1;
    public DimensiAdapter(
            Context context,
            List<DimensiModel> list,
            ArrayList<String> selectdimension,
            LinearLayout tombolyes,
            AlertDialog message_show,
            TextView tomboldimension,
            TextView textpilihan

    ){
        this.context = context;
        this.list = list;
        this.selectdimension = selectdimension;
        this.tombolyes = tombolyes;
        this.message_show = message_show;
        this.tomboldimension = tomboldimension;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.layout_list_component_code, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String name = list.get(position).getName();
        holder.textnama.setText(name);

        holder.tombolview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectdimension.add("- Pilih Dimensi");

                selectdimension.set(0, list.get(position).getName());

                tomboldimension.setText(selectdimension.get(0));
                message_show.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout tombolview;
        TextView textnama;

        public Holder(View itemview){
            super(itemview);
            textnama = itemview.findViewById(R.id.textnama);
            tombolview = itemview.findViewById(R.id.tombolview);
        }
    }
}
