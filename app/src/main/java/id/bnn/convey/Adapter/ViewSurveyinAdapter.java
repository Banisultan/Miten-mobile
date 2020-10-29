package id.bnn.convey.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.bnn.convey.Model.ViewSurveyinModel;
import id.bnn.convey.R;

public class ViewSurveyinAdapter extends RecyclerView.Adapter<ViewSurveyinAdapter.Holder>{

    Context context;
    List<ViewSurveyinModel> list;

    public ViewSurveyinAdapter(
            Context context,
            List<ViewSurveyinModel> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_view_surveyin, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textnama.setText(list.get(position).getNama());
        holder.textvalue.setText(list.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textnama;
        TextView textvalue;

        public Holder(View itemview){
            super(itemview);

            textnama = itemview.findViewById(R.id.textnama);
            textvalue = itemview.findViewById(R.id.textvalue);
        }
    }


}
