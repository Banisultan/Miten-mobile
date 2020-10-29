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

import id.bnn.convey.Model.ListComponentCodeModel;
import id.bnn.convey.R;

public class ComponentCodeAdapter extends RecyclerView.Adapter<ComponentCodeAdapter.Holder>{

    Context context;
    List<ListComponentCodeModel> list;
    ArrayList<String> textcomponent;
    ArrayList<String> textcomponentcode;
    LinearLayout tombolyes;
    AlertDialog message_show;
    TextView tombolcomponent;

    int position_on = -1;
    public ComponentCodeAdapter(
            Context context,
            List<ListComponentCodeModel> list,
            ArrayList<String> textcomponent,
            ArrayList<String> textcomponentcode,
            LinearLayout tombolyes,
            AlertDialog message_show,
            TextView tombolcomponent
    ){
        this.context = context;
        this.list = list;
        this.textcomponent = textcomponent;
        this.textcomponentcode = textcomponentcode;
        this.tombolyes = tombolyes;
        this.message_show = message_show;
        this.tombolcomponent = tombolcomponent;
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
        String code = list.get(position).getCode();
        holder.textnama.setText(code+" - "+name);

        holder.tombolview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textcomponent.add("00");
                textcomponentcode.add("00");

                textcomponent.set(0, list.get(position).getName());
                textcomponentcode.set(0, list.get(position).getCode());

                tombolcomponent.setText(textcomponentcode.get(0)+" - "+textcomponent.get(0));
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
