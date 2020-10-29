package id.bnn.convey.FragmentSurvey;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Adapter.PositionAdapter;
import id.bnn.convey.Adapter.PositionPhotoAdapter;
import id.bnn.convey.Model.PhotoModel;
import id.bnn.convey.Model.PositionModel;
import id.bnn.convey.Model.PositionPhotoModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;


public class InputFotoSurveyin extends Fragment {

    Context context;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    LinearLayout tombolcapture;
    LinearLayout tombollanjut;
    LinearLayout tombolprev;

    String FILE_IMAGE;

    VariableText var = new VariableText();

    AlertDialog.Builder posisi_alert;
    AlertDialog posisi_show;
    LayoutInflater posisi_inflate;
    View posisi_view;

    LinearLayout tombolno;

    RecyclerView viewposisi;
    List<PositionModel> listposisi;
    PositionAdapter adapterposisi;

    SharedPreferences datalistitem;

    RecyclerView viewphoto;
    List<PositionPhotoModel> listphoto;
    PositionPhotoAdapter adapterphoto;

    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;

    public InputFotoSurveyin(
            ImageView view2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.viewv2 = view2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_input_foto_surveyin, container, false);
        for(int i=1; i<viewline.length; i++){
            if(i<=2){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);

        context = container.getContext();
        datalistitem = context.getSharedPreferences("datalistitem", Context.MODE_PRIVATE);

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);

        viewphoto = itemview.findViewById(R.id.viewlist);
        viewphoto.setHasFixedSize(true);
        viewphoto.setLayoutManager(new LinearLayoutManager(context));

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata(true);
            }
        });

        tombolprev = itemview.findViewById(R.id.tombolprev);
        tombolprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata(false);
            }
        });

        tombolcapture = itemview.findViewById(R.id.tomboladdimage);
        tombolcapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPosisi_show();
            }
        });

        showdialog();
        loadData();
        return itemview;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == var.CODE_PERMISSION_CAMERA){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmapcheck_num = BitmapFactory.decodeFile(FILE_IMAGE);
            bitmapcheck_num = var.modifyOrientation(bitmapcheck_num, FILE_IMAGE);

            float size_num = 1;
            if(var.byteSizeOfv2(bitmapcheck_num) / 1024 > 1000){
                size_num = 0.2f;
            }
            Bitmap bitmap_num = var.BitmapCompress(bitmapcheck_num, size_num);
            File file_in_num = var.createTempFile_v2(context, bitmap_num, filetemp);

            if(requestCode == var.CODE_CAPTURE_IMAGE_POSISI){
                try{
                    hideall();
                    for(int i=0; i < listphoto.size(); i++){
                        if(listphoto.get(i).getPosition().equals(TEXT_POSISI)){
                            List<PhotoModel> list = listphoto.get(i).getList_photo();
                            list.add(new PhotoModel(FILE_IMAGE, "null", "null"));
                            listphoto.get(i).setList_photo(list);
                            break;
                        }
                    }
                    adapterphoto.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d("TAGGAR", "TRY gagal : "+e);
                }
            }
        }else{
            Log.d("TAGGAR", "gagal "+resultCode+" reques "+requestCode);
        }
    }

    public void setPosisi_show(){
        posisi_show.show();
    }

    public void showdialog(){
        posisi_alert = new AlertDialog.Builder(context);
        posisi_inflate = getLayoutInflater();
        posisi_view = posisi_inflate.inflate(R.layout.layout_view_posisi, null, false);
        posisi_alert.setView(posisi_view);
        posisi_alert.setCancelable(true);
        posisi_show = posisi_alert.create();

        viewposisi = posisi_view.findViewById(R.id.viewposisi);
        viewposisi.setHasFixedSize(true);
        viewposisi.setLayoutManager(new LinearLayoutManager(context));

        tombolno = posisi_view.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideall();
            }
        });
    }

    public void hideall(){
        posisi_show.dismiss();
    }

    public void nextfragment(){
        Fragment fragment = new InputKerusakan(
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

    public void prevfragment(){
        Fragment fragment = new SurveyinFragment(
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

    String TEXT_POSISI;
    public void checkpermision(String POSISI){
        TEXT_POSISI = POSISI;
        String[] permision = new String[]{
                Manifest.permission.CAMERA
        };

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permision, var.CODE_PERMISSION_CAMERA);
        }else{
            opencamera();
        }
    }

    Uri uriimage;
    public void opencamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.d("TAGGAR", "g "+ex);
        }

        uriimage= FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uriimage);
        ((Activity) context).startActivityForResult(cameraintent, var.CODE_CAPTURE_IMAGE_POSISI);
    }

    File filetemp;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        filetemp = image;
        FILE_IMAGE = image.getAbsolutePath();
        return image;
    }

    public void loadData(){
        addData();
    }

    JSONArray dataarray_utama = new JSONArray();
    public void addData(){
        listphoto = new ArrayList<>();
        listposisi = new ArrayList<>();

        String datalist = datalistitem.getString("position", "");
        if(!datalist.equals("")){
            try{
                JSONArray dataarray = new JSONArray(datalist);
                for(int i=0; i<dataarray.length(); i++){
                    String value = dataarray.getString(i).replace(" ", "_");
                    listposisi.add(new PositionModel(value));
                    listphoto.add(new PositionPhotoModel(value, new ArrayList<>()));

                    JSONObject datajson = new JSONObject();
                    datajson.put("posisi", value);
                    datajson.put("foto", new JSONArray());
                    dataarray_utama.put(i, datajson);
                }

            }catch (Exception e) {
                Log.d("TAGGAR error", "res : " + String.valueOf(e));
            }
        }

        String data = datainputsurveyin.getString("datafoto", "");
        Log.d("TAGGAR", "datafoto "+data);
        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i< dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    String posisi = datajson.getString("posisi");

                    for(int x=0; x < listphoto.size(); x++){
                        if(listphoto.get(x).getPosition().equals(posisi)){
                            JSONArray dataarray_data = datajson.getJSONArray("data");

                            List<PhotoModel> listphoto_view = new ArrayList<>();
                            for(int y=0; y< dataarray_data.length(); y++){
                                JSONObject datajson_data = dataarray_data.getJSONObject(y);
                                String file_image = datajson_data.getString("file_image");
                                String file_url = datajson_data.getString("file_url");
                                String id_foto = datajson_data.getString("id_foto");

                                listphoto_view.add(new PhotoModel(file_image, file_url, id_foto));
                            }
                            listphoto.get(x).setList_photo(listphoto_view);
                            break;
                        }
                    }
                }
            }catch (Exception e){

            }
        }

        adapterphoto = new PositionPhotoAdapter(context, listphoto);
        viewphoto.setAdapter(adapterphoto);

        adapterposisi = new PositionAdapter(context, listposisi, this);
        viewposisi.setAdapter(adapterposisi);
    }

    public void savedata(boolean nextfragment){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        try{
            JSONArray dataarray = new JSONArray();
            for(int i=0; i < listphoto.size(); i++){
                JSONObject datajson = new JSONObject();
                datajson.put("posisi", listphoto.get(i).getPosition());

                JSONArray dataarrayphoto = new JSONArray();
                List<PhotoModel> listphoto_view = listphoto.get(i).getList_photo();
                for(int x=0; x < listphoto_view.size(); x++){
                    JSONObject datajsonphoto = new JSONObject();
                    String file_image = listphoto_view.get(x).getImage_dict();
                    String file_url = listphoto_view.get(x).getImage_url();
                    String id_foto = listphoto_view.get(x).getId_foto();

                    datajsonphoto.put("file_image", file_image);
                    datajsonphoto.put("file_url", file_url);
                    datajsonphoto.put("id_foto", id_foto);
                    dataarrayphoto.put(x, datajsonphoto);
                }
                datajson.put("data", dataarrayphoto);
                dataarray.put(i, datajson);
            }
            datainputsurveyin_edit.putString("datafoto", String.valueOf(dataarray));
            datainputsurveyin_edit.commit();
        }catch (Exception e){
            Log.d("TAGGAR", "error : "+e);
        }

        if(nextfragment){
            nextfragment();
        }else{
            prevfragment();
        }
    }
}