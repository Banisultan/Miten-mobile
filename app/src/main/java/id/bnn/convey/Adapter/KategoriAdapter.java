package id.bnn.convey.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.bnn.convey.Model.KategoriModel;
import id.bnn.convey.R;
import id.bnn.convey.FinalActivity.SurveyinActivity;
import id.bnn.convey.UnderDevelopmentActivity;

public class KategoriAdapter extends RecyclerView.Adapter<KategoriAdapter.Holder>{

    Context context;
    List<KategoriModel> list;

    public KategoriAdapter(
            Context context,
            List<KategoriModel> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.layout_kategori, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getUrl()).into(holder.imagekategori);

        holder.textkategori.setText(list.get(position).getNama());
        holder.tombolkategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = list.get(position).getId();
                String nama = list.get(position).getNama();
                switchingintent(id, nama);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout tombolkategori;
        TextView textkategori;
        ImageView imagekategori;

        public Holder(View itemview){
            super(itemview);

            tombolkategori = itemview.findViewById(R.id.tombolkategori);
            textkategori = itemview.findViewById(R.id.textkategori);
            imagekategori = itemview.findViewById(R.id.imgkategori);
        }
    }

    public void switchingintent(int id, String nama){
        Intent intent = null;
        switch (id){
            case 1:
                intent = new Intent(context, SurveyinActivity.class);
                intent.putExtra("toolbar", nama);
                break;
            default:
                intent = new Intent(context, UnderDevelopmentActivity.class);
                break;
        }

        if(intent != null){
            moveactivity(intent);
        }
    }

    public void moveactivity(Intent intent){
        context.startActivity(intent);
    }
}
