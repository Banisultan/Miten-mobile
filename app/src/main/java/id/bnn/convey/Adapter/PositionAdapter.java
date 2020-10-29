package id.bnn.convey.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FragmentSurvey.InputFotoSurveyin;
import id.bnn.convey.Model.DimensiModel;
import id.bnn.convey.Model.PositionModel;
import id.bnn.convey.R;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.Holder>{

    InputFotoSurveyin activity;
    Context context;
    List<PositionModel> list;

    public PositionAdapter(
            Context context,
            List<PositionModel> list,
            InputFotoSurveyin activity
    ){
        this.context = context;
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_position, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String posisi = list.get(position).getPosition();
        holder.textname.setText(posisi);
        holder.tombolview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.checkpermision(posisi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout tombolview;
        TextView textname;

        public Holder(View itemview){
            super(itemview);
            textname = itemview.findViewById(R.id.textnama);
            tombolview = itemview.findViewById(R.id.tombolview);
        }
    }
}
