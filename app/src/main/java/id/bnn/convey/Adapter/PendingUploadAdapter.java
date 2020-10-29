package id.bnn.convey.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Model.ListContainerModel;
import id.bnn.convey.Model.PendingUploadModel;
import id.bnn.convey.R;

public class PendingUploadAdapter extends RecyclerView.Adapter<PendingUploadAdapter.Holder>{

    Context context;
    List<PendingUploadModel> list;

    public PendingUploadAdapter(
            Context context,
            List<PendingUploadModel> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_pending_upload, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textcontainer.setText(list.get(position).getNocontainer());
        holder.textketerangan.setText(list.get(position).getKeterangan());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView textcontainer;
        TextView textketerangan;

        public Holder(View itemview){
            super(itemview);
            textcontainer = itemview.findViewById(R.id.textcontainer);
            textketerangan = itemview.findViewById(R.id.textketerangan);
        }
    }
}
