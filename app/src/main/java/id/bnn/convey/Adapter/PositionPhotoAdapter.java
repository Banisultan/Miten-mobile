package id.bnn.convey.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FragmentSurvey.InputFotoSurveyin;
import id.bnn.convey.Model.ListPhoto;
import id.bnn.convey.Model.PhotoModel;
import id.bnn.convey.Model.PositionModel;
import id.bnn.convey.Model.PositionPhotoModel;
import id.bnn.convey.Model.ViewPhotoModel;
import id.bnn.convey.R;

public class PositionPhotoAdapter extends RecyclerView.Adapter<PositionPhotoAdapter.Holder>{

    Context context;
    List<PositionPhotoModel> list;

    public PositionPhotoAdapter(
            Context context,
            List<PositionPhotoModel> list
    ){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(context).inflate(R.layout.list_photo, parent, false);
        return new Holder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.textheader.setText(list.get(position).getPosition());
        List<PhotoModel> listphoto_view = list.get(position).getList_photo();

        if(listphoto_view.size() == 0){
            holder.layoututama.setVisibility(View.GONE);
        }else{
            holder.layoututama.setVisibility(View.VISIBLE);
        }

        List<ViewPhotoModel> listphoto =  new ArrayList<>();

        for(int i=0; i < listphoto_view.size(); i++){
            String file_image = listphoto_view.get(i).getImage_dict();
            String file_url = listphoto_view.get(i).getImage_url();

            if(file_image.equals("null")){
                listphoto.add(new ViewPhotoModel(file_url));
            }else{
                listphoto.add(new ViewPhotoModel(file_image));
            }
        }

        holder.adapterphoto = new ViewPhotoAdapter(context, listphoto, PositionPhotoAdapter.this, list, position);
        holder.viewphoto.setAdapter(holder.adapterphoto);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        LinearLayout layoututama;
        RecyclerView viewphoto;
        TextView textheader;

        ViewPhotoAdapter adapterphoto;

        public Holder(View itemview){
            super(itemview);

            textheader = itemview.findViewById(R.id.textheader);
            viewphoto = itemview.findViewById(R.id.viewphoto);
            viewphoto.setHasFixedSize(true);
            viewphoto.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

            layoututama = itemview.findViewById(R.id.layoututama);
        }
    }
}
