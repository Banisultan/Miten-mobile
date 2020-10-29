package id.bnn.convey.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Database;
import id.bnn.convey.Model.ListContainerModel;
import id.bnn.convey.R;
import id.bnn.convey.Activity.ViewSurveyActivity;
import id.bnn.convey.VariableText;
import id.bnn.convey.ViewSurveyinActivity;

public class ListContainerVipAdapter extends RecyclerView.Adapter<ListContainerVipAdapter.Holder>{

    Context context;
    List<ListContainerModel> listcontainer;
    boolean BUTTON_CLICK;
    VariableText var = new VariableText();
    public ListContainerVipAdapter(
            Context context,
            List<ListContainerModel> listcontainer
    ){
        this.context = context;
        this.listcontainer = listcontainer;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_container, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String status = listcontainer.get(position).getStatus();
        String idsurvey = listcontainer.get(position).getIdsurvei();
        String condition = listcontainer.get(position).getKondisi();

        if(listcontainer.get(position).getDb_tipe().equals(var.TIPE_DB_DONE)){
            holder.textketerangan.setVisibility(View.GONE);
            holder.imagestatus.setImageResource(R.drawable.vec_done);
            holder.layoutkondisi.setBackgroundResource(R.drawable.bg_shape_kondisi_approve);
        }else{
            holder.imagestatus.setImageResource(R.drawable.vec_waiting);
            holder.layoutkondisi.setBackgroundResource(R.drawable.bg_shape_kondisi_waiting);
        }

        holder.textketerangan.setText(listcontainer.get(position).getKeterangan());
        holder.textcontainer.setText(listcontainer.get(position).getNocontainer());
        holder.textcondition.setText(listcontainer.get(position).getKondisi());
        holder.listsurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveactivity(
                        listcontainer.get(position).getIdsurvei(),
                        listcontainer.get(position).getStatus()
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return listcontainer.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoutkondisi;
        TextView textcondition;
        TextView textcontainer;
        TextView textketerangan;
        ImageView imagestatus;

        RelativeLayout listsurvey;

        public Holder(View itemview){
            super(itemview);
            textcontainer = itemview.findViewById(R.id.textcontainer);
            textketerangan = itemview.findViewById(R.id.textketerangan);
            textcondition = itemview.findViewById(R.id.textcondition);
            imagestatus = itemview.findViewById(R.id.imagestatus);
            listsurvey = itemview.findViewById(R.id.listsurvei);
            layoutkondisi = itemview.findViewById(R.id.layoutkondisi);
        }
    }

    public void moveactivity(String idsurvei, String status){
        Intent intent = new Intent(context, ViewSurveyinActivity.class);
        intent.putExtra("idsurvei", idsurvei);
        intent.putExtra("status", status);

        context.startActivity(intent);
    }
}
