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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

import id.bnn.convey.FinalActivity.Adapter.ItemAdapter;
import id.bnn.convey.FinalActivity.Adapter.Photo;
import id.bnn.convey.FinalActivity.Model.m_PosisiPhotoItemImage;
import id.bnn.convey.FinalActivity.Model.m_Item;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;

public class DamageAddFragment extends Fragment {

    Context context;

    VariableText var = new VariableText();

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    LinearLayout tombolcomponent;
    LinearLayout tomboldimensi;
    LinearLayout tomboldamage;
    LinearLayout tombolrepair;
    LinearLayout tombolsimpan;
    LinearLayout tombolkembali;
    RelativeLayout tombolphoto;
    EditText inputquantity;
    EditText inputlocation;

    TextView textcomponent;
    TextView textdamage;
    TextView textrepair;
    TextView textdimensi;

    ImageView imageadd;

    VariableVoid vod;
    Dialog dialog;
    EditText inputsearch;
    ImageView image_msg_dialog;
    TextView textjudul_msg_dialog;
    TextView textsubjudul_msg_dialog;
    LinearLayout tombolcancel_msg_dialog;

    Dialog dialog_image;
    ImageView image_view;
    ImageView image_msg_image;
    LinearLayout tombolclose_msg_image;

    Dialog dialog_alert;
    TextView text_msg_alert;
    ImageView image_msg_alert;
    LinearLayout tombolok_msg_alert;
    LinearLayout tombolbatal_msg_alert;

    Dialog dialog_warning;
    ImageView image_msg_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    Spinner spintpc;
    List<m_Item> listcomponent = new ArrayList<>();
    List<m_Item> listdamage = new ArrayList<>();
    List<m_Item> listsearch = new ArrayList<>();
    List<m_Item> listdimensi = new ArrayList<>();
    List<m_Item> listrepair = new ArrayList<>();
    ArrayList<String> listtpc = new ArrayList<>();

    RecyclerView viewlist;
    ItemAdapter adapter;

    String DIALOG_ACTIVE;
    String COMPONENT_CODE = "";
    String COMPONENT_NAME = "";
    String DAMAGE_CODE = "";
    String DAMAGE_NAME = "";
    String REPAIR_CODE = "";
    String REPAIR_NAME = "";
    String DIMENSION = "";
    String ACTION = "";

    String FILE_TEMP;
    Uri URI_TEMP;
    int CODE_IMAGE = 400;
    boolean EDIT = false;
    String MOBID_DAMAGE;
    public JsonArray FILE_TEMP_REMOVE = new JsonArray();
    public JsonArray FOTO_TEMP_REMOVE = new JsonArray();
    public JsonArray REMOVE_PHOTO = new JsonArray();

    RecyclerView viewphoto;
    List<m_PosisiPhotoItemImage> listphoto = new ArrayList<>();
    Photo adapterphoto;

    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;

    @Override
    public void onDestroy() {
        super.onDestroy();
        hide_dialog();
        hide_alert();
        hide_warning();
        hide_image();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    public DamageAddFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_damage_add, container, false);
        context = container.getContext();
        vod = new VariableVoid(context);

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);

        for(int i=1; i<viewline.length; i++){
            if(i<=4){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);
        Glide.with(context).load(R.drawable.img_warning).into(viewv2);

        viewphoto = itemview.findViewById(R.id.viewphoto);
        viewphoto.setHasFixedSize(true);
        viewphoto.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        adapterphoto = new Photo(this, listphoto);
        viewphoto.setAdapter(adapterphoto);

        textcomponent = itemview.findViewById(R.id.textcomponent);
        textdamage = itemview.findViewById(R.id.textdamage);
        textdimensi = itemview.findViewById(R.id.textdimensi);
        textrepair = itemview.findViewById(R.id.textrepair);

        spintpc = itemview.findViewById(R.id.spintpc);

        imageadd = itemview.findViewById(R.id.imageadd);
        Glide.with(context).load(R.drawable.img_add).into(imageadd);

        inputquantity = itemview.findViewById(R.id.inputquantity);
        inputquantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputquantity.removeTextChangedListener(this);
                String text = inputquantity.getText().toString().trim().replace(",", "").replace(".","");
                if(!text.equals("")){
                    String texthasil = var.setFormatNominal(Integer.valueOf(text));
                    inputquantity.setText(texthasil);
                    inputquantity.setSelection(texthasil.length());
                }

                inputquantity.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        inputlocation = itemview.findViewById(R.id.inputlocation);

        tombolcomponent = itemview.findViewById(R.id.tombolcomponent);
        tombolcomponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_component();
            }
        });

        tomboldamage = itemview.findViewById(R.id.tomboldamage);
        tomboldamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_damage();
            }
        });

        tomboldimensi = itemview.findViewById(R.id.tomboldimensi);
        tomboldimensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_dimensi();
            }
        });

        tombolrepair = itemview.findViewById(R.id.tombolrepair);
        tombolrepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_repair();
            }
        });

        tombolsimpan = itemview.findViewById(R.id.tombolsimpan);
        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata();
            }
        });

        tombolkembali = itemview.findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmessage_alert("Yakin akan membatalkan pengisian data kerusakan ?");
            }
        });

        tombolphoto = itemview.findViewById(R.id.tombolphoto);
        tombolphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkpermision();
            }
        });

        loaddata();
        messagedialog();
        messagedialog_image();
        messagedialog_alert();
        messagedialog_warning();
        return itemview;
    }

    public void checkpermision(){
        String[] permision = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

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

                String mobid_photo = var.createMobID();
                listphoto.add(new m_PosisiPhotoItemImage(
                        FILE_TEMP,
                        "",
                        mobid_photo,
                        "add"));
                adapterphoto.notifyDataSetChanged();
            }else{
                Log.d("TAGGAR", "gagal "+resultCode+" reques ");
            }
        }
    }

    public void messagedialog_warning(){
        dialog_warning = new Dialog(context);
        dialog_warning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_warning.setContentView(R.layout.layout_warning);
        dialog_warning.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        image_msg_warning = dialog_warning.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(image_msg_warning);

        text_msg_warning = dialog_warning.findViewById(R.id.textmessage);
        button_msg_warning = dialog_warning.findViewById(R.id.buttonoke);
        button_msg_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_warning();
            }
        });
    }

    public void show_warning(String message){
        text_msg_warning.setText(message);
        dialog_warning.show();
    }

    public void hide_warning(){
        dialog_warning.dismiss();
    }

    public void messagedialog_alert(){
        dialog_alert = new Dialog(context);
        dialog_alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_alert.setContentView(R.layout.layout_alert);
        dialog_alert.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        text_msg_alert = dialog_alert.findViewById(R.id.textmessage);
        image_msg_alert = dialog_alert.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(image_msg_alert);

        tombolok_msg_alert = dialog_alert.findViewById(R.id.tomboloke);
        tombolok_msg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_alert();
                prevfragment();
            }
        });

        tombolbatal_msg_alert = dialog_alert.findViewById(R.id.tombolbatal);
        tombolbatal_msg_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_alert();
            }
        });
    }

    public void hide_alert(){
        dialog_alert.dismiss();
    }

    public void showmessage_alert(String text){
        text_msg_alert.setText(text);
        dialog_alert.show();
    }

    public void messagedialog_image(){
        dialog_image = new Dialog(context);
        dialog_image.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_image.setContentView(R.layout.layout_image_v2);
        dialog_image.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        image_view = dialog_image.findViewById(R.id.imageview);

        image_msg_image = dialog_image.findViewById(R.id.imageclose);
        Glide.with(context).load(R.drawable.img_close).into(image_msg_image);

        tombolclose_msg_image = dialog_image.findViewById(R.id.tombolno);
        tombolclose_msg_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_image();
            }
        });
    }

    public void hide_image(){
        dialog_image.dismiss();
    }

    public void showdialog_image(String image){
        Glide.with(context).load(image).into(image_view);
        dialog_image.show();
    }

    public void messagedialog(){
        dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_damage);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        viewlist = dialog.findViewById(R.id.view);
        viewlist.setHasFixedSize(true);
        viewlist.setLayoutManager(new LinearLayoutManager(context));

        inputsearch = dialog.findViewById(R.id.inputsearch);
        inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputsearch.removeTextChangedListener(this);
                searchitem(inputsearch.getText().toString());
                inputsearch.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        textjudul_msg_dialog = dialog.findViewById(R.id.textjudul);
        textsubjudul_msg_dialog = dialog.findViewById(R.id.textsubjudul);

        image_msg_dialog = dialog.findViewById(R.id.imagesearch);
        Glide.with(context).load(R.drawable.img_search_v2).into(image_msg_dialog);

        tombolcancel_msg_dialog = dialog.findViewById(R.id.tombolcancel);
        tombolcancel_msg_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_dialog();
            }
        });
    }

    public void show_component(){
        DIALOG_ACTIVE = "COMPONENT";
        if(COMPONENT_CODE.equals("")){
            textsubjudul_msg_dialog.setText("Pilih component code");
        }else{
            textsubjudul_msg_dialog.setText(textcomponent.getText());
        }

        inputsearch.setText("");
        textjudul_msg_dialog.setText("Component Code");

        adapter = new ItemAdapter(DIALOG_ACTIVE, listcomponent, this, COMPONENT_CODE);
        viewlist.setAdapter(adapter);
        dialog.show();
    }

    public void show_damage(){
        DIALOG_ACTIVE = "DAMAGE";
        if(DAMAGE_CODE.equals("")){
            textsubjudul_msg_dialog.setText("Pilih damage code");
        }else{
            textsubjudul_msg_dialog.setText(textdamage.getText());
        }

        inputsearch.setText("");
        textjudul_msg_dialog.setText("Damage Code");

        adapter = new ItemAdapter(DIALOG_ACTIVE, listdamage, this, DAMAGE_CODE);
        viewlist.setAdapter(adapter);
        dialog.show();
    }

    public void show_repair(){
        DIALOG_ACTIVE = "REPAIR";
        if(REPAIR_CODE.equals("")){
            textsubjudul_msg_dialog.setText("Pilih repair code");
        }else{
            textsubjudul_msg_dialog.setText(textrepair.getText());
        }

        inputsearch.setText("");
        textjudul_msg_dialog.setText("Repair Code");

        adapter = new ItemAdapter(DIALOG_ACTIVE, listrepair, this, REPAIR_CODE);
        viewlist.setAdapter(adapter);
        dialog.show();
    }

    public void show_dimensi(){
        DIALOG_ACTIVE = "DIMENSI";
        if(DIMENSION.equals("")){
            textsubjudul_msg_dialog.setText("Pilih dimension");
        }else{
            textsubjudul_msg_dialog.setText(textdimensi.getText());
        }

        inputsearch.setText("");
        textjudul_msg_dialog.setText("Dimension");

        adapter = new ItemAdapter(DIALOG_ACTIVE, listdimensi, this, DIMENSION);
        viewlist.setAdapter(adapter);
        dialog.show();
    }

    public void hide_dialog(){
        dialog.dismiss();
    }

    public void loaddata(){
        loaddata_component();
        loaddata_damage();
        loaddata_dimensi();
        loaddata_repair();
        loaddata_tpc("");

        String dataedit = datainputsurveyin.getString("datadamage_edit", "");

        if(!dataedit.equals("")){
            EDIT = true;
            JsonObject datajson = new JsonParser().parse(dataedit).getAsJsonObject();
            MOBID_DAMAGE = datajson.get("mobid_damage").getAsString();
            COMPONENT_CODE = datajson.get("componentcode").getAsString();
            COMPONENT_NAME = datajson.get("componentname").getAsString();
            DIMENSION = datajson.get("dimension").getAsString();
            DAMAGE_CODE = datajson.get("damagecode").getAsString();
            DAMAGE_NAME = datajson.get("damagename").getAsString();
            REPAIR_CODE = datajson.get("repaircode").getAsString();
            REPAIR_NAME = datajson.get("repairname").getAsString();
            ACTION = datajson.get("action").getAsString();

            textcomponent.setText(COMPONENT_CODE+" - "+COMPONENT_NAME);
            textdamage.setText(DAMAGE_CODE+" - "+DAMAGE_NAME);
            textrepair.setText(REPAIR_CODE+" - "+REPAIR_NAME);
            textdimensi.setText(DIMENSION);

            spintpc.setSelection(var.getIndexSpinner(listtpc, datajson.get("tpc").getAsString()));

            inputlocation.setText(datajson.get("location").getAsString());
            inputquantity.setText(datajson.get("quantity").getAsString());

            JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("photo", "[]")).getAsJsonArray();
            Log.d("TAGGAR2", "data damage "+dataarray);
            for(int i=0; i< dataarray.size(); i++){
                JsonObject datajson_photo = dataarray.get(i).getAsJsonObject();
                if(datajson_photo.get("mobid_damage").getAsString().equals(MOBID_DAMAGE)){
                    listphoto.add(new m_PosisiPhotoItemImage(
                            datajson_photo.get("file_temp").getAsString(),
                            "",
                            datajson_photo.get("mobid_photo").getAsString(),
                            ACTION
                    ));
                }
            }

            adapterphoto.notifyDataSetChanged();
        }else{
            MOBID_DAMAGE = var.createMobID();
            ACTION = "add";
        }
    }

    public void loaddata_component(){
        JsonArray dataarray = vod.getDatabase_component();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            String name = datajson.get("name").getAsString();
            listcomponent.add(new m_Item(code, name));
        }
    }

    public void loaddata_dimensi(){
        JsonArray dataarray = vod.getDatabase_dimension();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            String name = datajson.get("name").getAsString();
            listdimensi.add(new m_Item(code, name));
        }
    }

    public void loaddata_damage(){
        JsonArray dataarray = vod.getDatabase_damage();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            String name = datajson.get("name").getAsString();
            listdamage.add(new m_Item(code, name));
        }
    }

    public void loaddata_repair(){
        JsonArray dataarray = vod.getDatabase_repair();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            String name = datajson.get("name").getAsString();
            listrepair.add(new m_Item(code, name));
        }
    }

    public void loaddata_tpc(String tpc){
        listtpc.add("- Pilih TPC");

        JsonArray dataarray = vod.getDatabase_tpc();
        for(int i=0; i < dataarray.size(); i++){
            listtpc.add(dataarray.get(i).getAsString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listtpc);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spintpc.setAdapter(adapter);
        spintpc.setSelection(var.getIndexSpinner(listtpc, tpc));
    }

    public void setComponent(String code, String name){
        hide_dialog();
        COMPONENT_CODE = code;
        COMPONENT_NAME = name;
        textcomponent.setText(code+" - "+name);
    }

    public void setDamage(String code, String name){
        hide_dialog();
        DAMAGE_CODE = code;
        DAMAGE_NAME = name;
        textdamage.setText(code+" - "+name);
    }

    public void setRepair(String code, String name){
        hide_dialog();
        REPAIR_CODE = code;
        REPAIR_NAME = name;
        textrepair.setText(code+" - "+name);
    }

    public void setDimension(String code, String name){
        hide_dialog();
        DIMENSION = code;
        textdimensi.setText(code);
    }

    public void searchitem(String text){
        List<m_Item> pencarian = new ArrayList<>();
        String code = "";
        switch (DIALOG_ACTIVE){
            case "COMPONENT":
                pencarian = listcomponent;
                code = COMPONENT_CODE;
                break;
            case "DAMAGE":
                pencarian = listdamage;
                code = DAMAGE_CODE;
                break;
            case "DIMENSI":
                pencarian = listdimensi;
                code = DIMENSION;
                break;
            case "REPAIR":
                pencarian = listrepair;
                code = REPAIR_CODE;
                break;
        }

        text = text.trim().toUpperCase();
        listsearch = new ArrayList<>();
        for(int i=0; i<pencarian.size(); i++){
            String cari = pencarian.get(i).getCode();
            if(cari.length() >= text.length()){
                if(cari.substring(0, text.length()).equals(text)){
                    listsearch.add(pencarian.get(i));
                }
            }
        }

        adapter = new ItemAdapter(DIALOG_ACTIVE, listsearch, this, code);
        viewlist.setAdapter(adapter);
    }

    public void savedata(){
        int tpc_position = spintpc.getSelectedItemPosition();
        String quantity = inputquantity.getText().toString().trim();
        String location = inputlocation.getText().toString().trim();
        String tpc = listtpc.get(tpc_position);

        boolean lanjut = false;

        String message = "";
        if(COMPONENT_CODE.equals("")){
            message = "Component code tidak boleh kosong";
        }else
        if(location.equals("")){
            message = "Location tidak boleh kosong";
        }else
        if(DIMENSION.equals("")){
            message = "Dimension tidak boleh kosong";
        }else
        if(quantity.equals("")){
            message = "Quantity tidak boleh kosong";
        }else
        if(DAMAGE_CODE.equals("")){
            message = "Damage code tidak boleh kosong";
        }else
        if(tpc_position == 0){
            message = "Tpc tidak boleh kosong";
        }else
        if(REPAIR_CODE.equals("")){
            message = "Repair code tidak boleh kosong";
        }else
        if(listphoto.size() == 0){
            message = "Foto kerusakan tidak boleh kosong";
        }else{
            lanjut = true;
        }

        if(lanjut){
            datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
            JsonArray dataarray = new JsonParser().parse(datainputsurveyin.getString("photo", "[]")).getAsJsonArray();
            JsonArray datadamage = new JsonParser().parse(datainputsurveyin.getString("datadamage", "[]")).getAsJsonArray();
            JsonArray dataarray_remove = new JsonParser().parse(datainputsurveyin.getString("photo_remove","[]")).getAsJsonArray();

            for(int i=0; i<REMOVE_PHOTO.size(); i++){
                dataarray_remove.add(REMOVE_PHOTO.get(i).getAsString());
            }

            if(EDIT){
                for(int l=0; l < FOTO_TEMP_REMOVE.size(); l++){
                    for(int k=0; k < dataarray.size(); k++){
                        JsonObject datajson = dataarray.get(k).getAsJsonObject();
                        if(datajson.get("mobid_photo").getAsString().equals(FOTO_TEMP_REMOVE.get(l))){
                            dataarray.remove(k);
                            break;
                        }
                    }
                }
            }

            for (int i=0; i <listphoto.size(); i++){
                if(listphoto.get(i).getAction().equals("add")){
                    JsonObject datajson = new JsonObject();
                    datajson.addProperty("mobid_photo", listphoto.get(i).getId_foto());
                    datajson.addProperty("mobid_damage", MOBID_DAMAGE);
                    datajson.addProperty("file_temp", listphoto.get(i).getImage_dict());
                    datajson.addProperty("posisi", "");
                    datajson.addProperty("tipe", "D");
                    datajson.addProperty("action", "add");
                    dataarray.add(datajson);
                }
            }

            JsonObject jsondamage = new JsonObject();
            jsondamage.addProperty("componentcode", COMPONENT_CODE);
            jsondamage.addProperty("componentname", COMPONENT_NAME);
            jsondamage.addProperty("damagecode", DAMAGE_CODE);
            jsondamage.addProperty("damagename", DAMAGE_NAME);
            jsondamage.addProperty("dimension", DIMENSION);
            jsondamage.addProperty("repaircode", REPAIR_CODE);
            jsondamage.addProperty("repairname", REPAIR_NAME);
            jsondamage.addProperty("quantity", quantity);
            jsondamage.addProperty("location", location);
            jsondamage.addProperty("tpc", tpc);
            jsondamage.addProperty("countimage", listphoto.size());
            jsondamage.addProperty("mobid_damage", MOBID_DAMAGE);
            jsondamage.addProperty("action", ACTION);

            if(EDIT){
                for(int j=0; j<datadamage.size(); j++){
                    JsonObject datajson = datadamage.get(j).getAsJsonObject();
                    if(datajson.get("mobid_damage").getAsString().equals(MOBID_DAMAGE)){
                        datadamage.set(j, jsondamage);
                    }
                }
            }else{
                datadamage.add(jsondamage);
            }

            datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
            datainputsurveyin_edit.putString("photo", String.valueOf(dataarray));
            datainputsurveyin_edit.putString("photo_remove", String.valueOf(dataarray_remove));
            datainputsurveyin_edit.putString("datadamage", String.valueOf(datadamage));
            datainputsurveyin_edit.apply();

            for (int j=0; j<FILE_TEMP_REMOVE.size(); j++){
                var.removePhoto(FILE_TEMP_REMOVE.get(j).getAsString());
            }

            prevfragment();
        }else{
            show_warning(message);
        }

    }

    public void removefoto(String file_temp, String mobid_photo, String action){
        if(EDIT){
            if(action.equals("edit")){
                REMOVE_PHOTO.add(mobid_photo);
            }
            FILE_TEMP_REMOVE.add(file_temp);
        }else{
            var.removePhoto(file_temp);
        }

        FOTO_TEMP_REMOVE.add(mobid_photo);
    }

    public void prevfragment(){
        Fragment fragment = new DamageContainerFragment(
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
}