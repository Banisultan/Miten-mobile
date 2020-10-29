package id.bnn.convey.Adapter;

import android.content.Context;
import android.telecom.Call;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Model.ListContainerModel;
import id.bnn.convey.R;

public class ListContainerAdapter extends RecyclerView.Adapter<ListContainerAdapter.Holder>{

    Context context;
    List<ListContainerModel> listcontainer;

    public ListContainerAdapter(
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

        if(status.equals("A")){
            holder.imagestatus.setImageResource(R.drawable.vec_done);
            holder.textstatus.setText("Selesai");
            holder.textketerangan.setText("Selesai di approve oleh supervisor");
        }else{
            holder.imagestatus.setImageResource(R.drawable.vec_warning);
            holder.textstatus.setText("Belum selesai");
            holder.textketerangan.setText("Belum di approve oleh supervisor");
        }

        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            Date date = new Date(Long.parseLong(listcontainer.get(position).getWaktu()));
            holder.textwaktu.setText(dateFormat.format(date));
        }catch (Exception e){
            Log.d("RESPONSE", "gagal set time "+e);
        }


        holder.textcontainer.setText(listcontainer.get(position).getNocontainer());
    }

    @Override
    public int getItemCount() {
        return listcontainer.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textcontainer;
        TextView textketerangan;
        TextView textwaktu;
        TextView textstatus;
        ImageView imagestatus;

        public Holder(View itemview){
            super(itemview);
            textcontainer = itemview.findViewById(R.id.textcontainer);
            textketerangan = itemview.findViewById(R.id.textketerangan);
            textstatus = itemview.findViewById(R.id.textstatus);
            imagestatus = itemview.findViewById(R.id.imagestatus);
        }
    }
}
