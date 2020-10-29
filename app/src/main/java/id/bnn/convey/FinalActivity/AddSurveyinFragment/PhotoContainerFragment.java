package id.bnn.convey.FinalActivity.AddSurveyinFragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.FinalActivity.Adapter.PosisiPhoto;
import id.bnn.convey.FinalActivity.Adapter.PosisiPhotoItem;
import id.bnn.convey.Model.PhotoModel;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhotoItem;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhoto;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;

public class PhotoContainerFragment extends Fragment {

    Context context;
    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    LinearLayout tombolkembali;
    LinearLayout tombollanjut;
    LinearLayout tombolcapture;
    ImageView imageadd;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;
    JsonArray DATAPHOTO;
    String MOBID;

    int CODE_IMAGE = 300;
    String FILE_TEMP;
    String POSISI_TEMP;
    int POSITION_LIST;
    Uri URI_TEMP;

    VariableText var = new VariableText();
    VariableVoid vod;

    RecyclerView viewitem;
    List<m_PosisiPhotoItem> listitem;
    PosisiPhotoItem adapteritem;

    Dialog dialog_image;
    ImageView image_view;
    ImageView imageclose_msg_image;
    LinearLayout tombolno_msg_image;

    Dialog dialog;
    LinearLayout tombolno;
    ImageView imageclose_msg_dialog;

    RecyclerView viewposisi;
    List<m_PosisiPhoto> listposisi;
    public PosisiPhoto adapterposisi;

    public PhotoContainerFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidemessage();
        hidemessage_image();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_photo_container, container, false);
        context = container.getContext();
        vod = new VariableVoid(context);

        for(int i=1; i<viewline.length; i++){
            if(i<=3){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);
        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        viewitem = itemview.findViewById(R.id.viewlist);
        viewitem.setHasFixedSize(true);
        viewitem.setLayoutManager(new LinearLayoutManager(context));

        imageadd = itemview.findViewById(R.id.imageadd);
        Glide.with(context).load(R.drawable.img_add).into(imageadd);

        tombolcapture = itemview.findViewById(R.id.tombolcapture);
        tombolcapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        tombolkembali = itemview.findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevfragment();
            }
        });

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextfragment();
            }
        });

        messagedialog_image();
        messagedialog();
        loaddata();
        return itemview;
    }

    public void prevfragment(){
        Fragment fragment = new DataSurveyFragment(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void nextfragment(){
        Fragment fragment = new DamageContainerFragment(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void messagedialog_image(){
        dialog_image = new Dialog(context);
        dialog_image.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_image.setContentView(R.layout.layout_image_v2);
        dialog_image.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tombolno_msg_image = dialog_image.findViewById(R.id.tombolno);
        tombolno_msg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidemessage_image();
            }
        });

        image_view = dialog_image.findViewById(R.id.imageview);
        imageclose_msg_image = dialog_image.findViewById(R.id.imageclose);
        Glide.with(context).load(R.drawable.img_close).into(imageclose_msg_image);
    }

    public void showdialog_image(String image){
        Glide.with(context).load(image).into(image_view);
        dialog_image.show();
    }

    public void messagedialog(){
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_view_posisi);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        tombolno = dialog.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hidemessage();
            }
        });

        imageclose_msg_dialog = dialog.findViewById(R.id.imageclose);
        Glide.with(context).load(R.drawable.img_close).into(imageclose_msg_dialog);

        viewposisi = dialog.findViewById(R.id.viewposisi);
        viewposisi.setHasFixedSize(true);
        viewposisi.setLayoutManager(new LinearLayoutManager(context));
    }

    public void hidemessage(){
        dialog.dismiss();
    }

    public void hidemessage_image(){
        dialog_image.dismiss();
    }

    public void loaddata(){
        listitem = new ArrayList<>();

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        MOBID = datainputsurveyin.getString("mobid", "");
        DATAPHOTO = new JsonParser().parse(datainputsurveyin.getString("photo", "[]")).getAsJsonArray();
        Log.d("TAGGAR", "dataphotoawal "+DATAPHOTO);

        listposisi = new ArrayList<>();

        JsonArray dataarray = vod.getDatabase_posisi();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String name = datajson.get("name").getAsString();
            String code = datajson.get("code").getAsString();

            int count = 0;

            List<PhotoModel> listphoto = new ArrayList<>();
            for(int j=0; j < DATAPHOTO.size();j++){
                JsonObject dataisi = DATAPHOTO.get(j).getAsJsonObject();
                if(dataisi.get("posisi").getAsString().equals(code)){
                    listphoto.add(new PhotoModel(dataisi.get("file_temp").getAsString(), "", dataisi.get("mobid_photo").getAsString()));
                    count++;
                }
            }

            listposisi.add(new m_PosisiPhoto(code, name,count));
            listitem.add(new m_PosisiPhotoItem(name, listphoto));
        }

        adapterposisi = new PosisiPhoto(listposisi, listitem, this);
        viewposisi.setAdapter(adapterposisi);

        adapteritem = new PosisiPhotoItem(this, listitem);
        viewitem.setAdapter(adapteritem);
    }

    public void checkpermision(String posisi, int position_list){
        String[] permision = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        POSISI_TEMP = posisi;
        POSITION_LIST = position_list;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permision, var.CODE_PERMISSION_CAMERA);
        }else{
            opencamera();
        }
    }

    public void opencamera(){
        File photoFile = null;
        try {
            photoFile = var.createImageFile(context);
            FILE_TEMP = photoFile.getAbsolutePath();
        } catch (IOException ex) {
            Log.d("TAGGAR", "g "+ex);
        }

        URI_TEMP = FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, URI_TEMP);

        ((Activity) context).startActivityForResult(cameraintent, CODE_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_IMAGE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CODE_IMAGE){
                Bitmap bitmap = BitmapFactory.decodeFile(FILE_TEMP);
                bitmap = var.modifyOrientation(bitmap, FILE_TEMP);

                float size_num = 1;
                if(var.byteSizeOfv2(bitmap) / 1024 > 1000){
                    size_num = 0.2f;
                }

                Bitmap bitmap_new = null;
                try{
                    bitmap_new = var.BitmapCompress(bitmap, size_num);
                    FileOutputStream fileOutputStream = new FileOutputStream(FILE_TEMP);
                    bitmap_new.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                }catch (Exception e){
                    Log.d("TAGGAR", "TRY gagal : "+e);
                }

                String tipe = "C";
                String mobid_photo = var.createMobID();

                DATAPHOTO = new JsonParser().parse(datainputsurveyin.getString("photo","[]")).getAsJsonArray();

                JsonObject datajson = new JsonObject();
                datajson.addProperty("mobid_photo", mobid_photo);
                datajson.addProperty("mobid_damage", "");
                datajson.addProperty("file_temp", FILE_TEMP);
                datajson.addProperty("posisi", POSISI_TEMP);
                datajson.addProperty("tipe", tipe);
                datajson.addProperty("action", "add");
                DATAPHOTO.add(datajson);

                hidemessage();
                adddata(mobid_photo);
            }else{
                Log.d("TAGGAR", "gagal "+resultCode+" reques ");
            }
        }
    }

    public void adddata(String mobid_photo){
        savedata();
        List<PhotoModel> listphoto = listitem.get(POSITION_LIST).getList_photo();
        listphoto.add(new PhotoModel(FILE_TEMP,"", mobid_photo));

        adapteritem.notifyDataSetChanged();
        adapterposisi.notifyDataSetChanged();
    }

    public void savedata(){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin_edit.putString("photo", String.valueOf(DATAPHOTO));

        Log.d("TAGGAR", "datafotoadd "+DATAPHOTO);
        datainputsurveyin_edit.apply();
    }
}