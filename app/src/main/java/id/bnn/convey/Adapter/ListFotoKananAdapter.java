package id.bnn.convey.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.ListFotoDepanModel;
import id.bnn.convey.Model.ListFotoKananModel;
import id.bnn.convey.Model.ListFotoKiriModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class ListFotoKananAdapter extends RecyclerView.Adapter<ListFotoKananAdapter.Holder>{

    Context context;
    FragmentActivity fragment;

    List<ListFotoDepanModel> listfoto;
    ArrayList<String> textdmg;
    RecyclerView viewlist;
    TextView textcount;

    VariableText var = new VariableText();

    int CODE_DEPAN = 100;
    int CODE_KIRI  = 200;
    int CODE_KANAN = 300;

    public ListFotoKananAdapter(
            FragmentActivity fragment,
            Context context,
            List<ListFotoDepanModel> listfoto,
            ArrayList<String> textdmg,
            RecyclerView viewlist,
            TextView textcount
    ){
        this.fragment = fragment;
        this.context = context;
        this.listfoto = listfoto;
        this.textdmg = textdmg;
        this.viewlist = viewlist;
        this.textcount = textcount;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_foto, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.layout_spinner,
                textdmg);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

        holder.textnomor.setText(String.valueOf(position+1));
        holder.listdmg.setAdapter(adapter);
        holder.listdmg.setSelection(listfoto.get(position).getPosition_dmg());
        holder.listdmg.setTag(position);
        holder.listdmg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posisi = Integer.valueOf(parent.getTag().toString());
                listfoto.set(posisi, new ListFotoDepanModel(
                        listfoto.get(posisi).getUriimage(),
                        listfoto.get(posisi).getBitmap(),
                        position,
                        parent.getSelectedItem().toString(),
                        listfoto.get(posisi).getUrl()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Bitmap imgbitmap = listfoto.get(position).getBitmap();
        holder.imagedepan.setImageBitmap(imgbitmap);
    }

    @Override
    public int getItemCount() {
        return listfoto.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoutnomor;
        Spinner listdmg;
        ImageView imagedepan;
        TextView textnomor;

        public Holder(View itemview){
            super(itemview);
            listdmg = itemview.findViewById(R.id.listdmg);
            imagedepan = itemview.findViewById(R.id.image);
            layoutnomor = itemview.findViewById(R.id.layoutnomor);
            textnomor = itemview.findViewById(R.id.textnomor);
        }
    }
}
