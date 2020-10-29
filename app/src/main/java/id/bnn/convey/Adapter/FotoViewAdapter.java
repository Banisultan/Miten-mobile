package id.bnn.convey.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import id.bnn.convey.Model.ListFotoView;
import id.bnn.convey.R;
import id.bnn.convey.FinalActivity.SurveyinActivity;
import id.bnn.convey.UnderDevelopmentActivity;

public class FotoViewAdapter extends RecyclerView.Adapter<FotoViewAdapter.Holder>{

    Context context;
    List<ListFotoView> list;

    public FotoViewAdapter(
            Context context,
            List<ListFotoView> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto_v4, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Glide.with(context).load(list.get(position).getUrl()).into(holder.imageview);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        ImageView imageview;

        public Holder(View itemview){
            super(itemview);
            imageview = itemview.findViewById(R.id.imgview);
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
