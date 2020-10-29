package id.bnn.convey.FinalActivity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.bnn.convey.FinalActivity.Model.m_ListSurvey;
import id.bnn.convey.R;
import id.bnn.convey.ViewSurveyinActivity;

public class ListSurvey extends RecyclerView.Adapter<ListSurvey.Holder>{

    Context context;
    List<m_ListSurvey> list;

    public ListSurvey(
            List<m_ListSurvey> list
    ){
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_container, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textketerangan.setText(list.get(position).getTime());
        holder.textcondition.setText(list.get(position).getCondition());
        holder.textcontainer.setText(list.get(position).getNocontainer());
        holder.tombollist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveactivity(
                        list.get(position).getSurveyid()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        RelativeLayout tombollist;
        TextView textcontainer;
        TextView textketerangan;
        TextView textcondition;

        public Holder(View itemview){
            super(itemview);
            textcontainer = itemview.findViewById(R.id.textcontainer);
            textketerangan = itemview.findViewById(R.id.textketerangan);
            textcondition = itemview.findViewById(R.id.textcondition);
            tombollist = itemview.findViewById(R.id.listsurvei);
        }
    }

    public void moveactivity(String idsurvei){
        Intent intent = new Intent(context, ViewSurveyinActivity.class);
        intent.putExtra("idsurvei", idsurvei);
        context.startActivity(intent);
    }
}
