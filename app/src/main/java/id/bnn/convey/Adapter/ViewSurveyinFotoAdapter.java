package id.bnn.convey.Adapter;

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

import id.bnn.convey.Model.ViewSurveyinModel;
import id.bnn.convey.R;

public class ViewSurveyinFotoAdapter extends RecyclerView.Adapter<ViewSurveyinFotoAdapter.Holder>{

    Context context;
    List<ViewSurveyinModel> list;

    public ViewSurveyinFotoAdapter(
            Context context,
            List<ViewSurveyinModel> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_view_surveyin_foto, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textnama.setText(list.get(position).getNama());
        Glide.with(context).load(list.get(position).getValue()).into(holder.imgview);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textnama;
        ImageView imgview;

        public Holder(View itemview){
            super(itemview);

            textnama = itemview.findViewById(R.id.textnama);
            imgview = itemview.findViewById(R.id.imgview);
        }
    }


}
