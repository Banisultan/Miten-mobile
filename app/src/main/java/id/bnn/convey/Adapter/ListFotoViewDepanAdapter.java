package id.bnn.convey.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.bnn.convey.Model.ListFotoDepanModel;
import id.bnn.convey.Model.ListFotoViewModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class ListFotoViewDepanAdapter extends RecyclerView.Adapter<ListFotoViewDepanAdapter.Holder>{

    Context context;
    List<ListFotoViewModel> listfoto;

    LinearLayout layoutimage;
    ImageView imageview;

    public ListFotoViewDepanAdapter(
            Context context,
            List<ListFotoViewModel> listfoto,
            LinearLayout layoutimage,
            ImageView imageView
    ){
        this.context = context;
        this.listfoto = listfoto;
        this.layoutimage = layoutimage;
        this.imageview = imageView;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_view_foto, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Picasso.get().load(listfoto.get(position).getUrl()).into(holder.imagedepan);
        holder.textdmg.setText(listfoto.get(position).getDmg());
        holder.imagedepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                layoutimage.setVisibility(View.VISIBLE);
//                Picasso.get().load(listfoto.get(position).getUrl()).into(imageview);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listfoto.size();
    }


    public class Holder extends RecyclerView.ViewHolder {
        LinearLayout layoutnomor;
        ImageView imagedepan;
        TextView textdmg;

        public Holder(View itemview){
            super(itemview);
            imagedepan = itemview.findViewById(R.id.image);
            layoutnomor = itemview.findViewById(R.id.layoutnomor);
            textdmg = itemview.findViewById(R.id.textdmg);
        }
    }
}
