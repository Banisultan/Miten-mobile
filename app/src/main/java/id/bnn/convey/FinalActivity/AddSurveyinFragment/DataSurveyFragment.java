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

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import id.bnn.convey.R;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;

public class DataSurveyFragment extends Fragment {

    Context context;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    LinearLayout tombolkembali;
    LinearLayout tombollanjut;
    LinearLayout tombolfotocontainer;
    LinearLayout tombolfotocsc;
    ImageView imagefotocontainer;
    ImageView imagefotocsc;
    EditText inputhaulier;
    EditText inputtare;
    EditText inputpayload;
    EditText inputmaxgross;

    int CODE_CONTAINER = 100;
    int CODE_CSC = 200;
    int CODE = 0;

    boolean FILE_CONTAINER = false;
    boolean FILE_CSC = false;

    String FILE_TEMP;
    Uri URI_TEMP;

    VariableText var = new VariableText();
    VariableVoid vod;

    String MOBID;
    JsonArray DATAPHOTO;

    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;

    Spinner spinowner;
    Spinner spinsize;
    Spinner spintipe;
    Spinner spingrade;
    Spinner spinbulan;
    Spinner spintahun;
    Spinner spinwash;

    ArrayList<String> listowner_code = new ArrayList<>();
    ArrayList<String> listowner_name = new ArrayList<>();
    ArrayList<String> listsize = new ArrayList<>();
    ArrayList<String> listtipe = new ArrayList<>();
    ArrayList<String> listgrade = new ArrayList<>();
    ArrayList<String> listbulan = new ArrayList<>();
    ArrayList<String> listtahun = new ArrayList<>();
    ArrayList<String> listwash_code = new ArrayList<>();
    ArrayList<String> listwash_name = new ArrayList<>();

    Dialog dialog_warning;
    ImageView image_msg_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    public DataSurveyFragment(
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
        hide_warning();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_data_survey, container, false);
        context = container.getContext();

        for(int i=1; i<viewline.length; i++){
            if(i <= 2){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);
        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        vod = new VariableVoid(context);
        spinowner = itemview.findViewById(R.id.spinowner);
        spinsize = itemview.findViewById(R.id.spinsize);
        spintipe = itemview.findViewById(R.id.spintipe);
        spingrade = itemview.findViewById(R.id.spingrade);
        spinbulan = itemview.findViewById(R.id.spinbulan);
        spintahun = itemview.findViewById(R.id.spintahun);
        spinwash = itemview.findViewById(R.id.spinwash);

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        MOBID = datainputsurveyin.getString("mobid", "");

        tombolfotocontainer = itemview.findViewById(R.id.tombolfotocontainer);
        tombolfotocontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CODE = CODE_CONTAINER;
                checkpermision();
            }
        });

        tombolfotocsc = itemview.findViewById(R.id.tombolfotocsc);
        tombolfotocsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CODE = CODE_CSC;
                checkpermision();
            }
        });

        imagefotocontainer = itemview.findViewById(R.id.imagefotocontainer);
        imagefotocsc = itemview.findViewById(R.id.imagefotocsc);

        Glide.with(context).load(R.drawable.img_add_image).into(imagefotocontainer);
        Glide.with(context).load(R.drawable.img_add_image).into(imagefotocsc);

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata(true   );
            }
        });

        tombolkembali = itemview.findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savedata(false);
            }
        });

        inputhaulier = itemview.findViewById(R.id.inputhaulier);

        inputtare = itemview.findViewById(R.id.inputtare);
        inputtare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputtare.removeTextChangedListener(this);
                String text = inputtare.getText().toString().trim().replace(",","").replace(".","");
                if(!text.equals("")){
                    String textbaru = var.setFormatNominal(Integer.valueOf(text));
                    inputtare.setText(textbaru);
                    inputtare.setSelection(textbaru.length());
                }
                inputtare.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputpayload = itemview.findViewById(R.id.inputpayload);
        inputpayload.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputpayload.removeTextChangedListener(this);
                String text = inputpayload.getText().toString().trim().replace(",","").replace(".","");
                if(!text.equals("")){
                    String textbaru = var.setFormatNominal(Integer.valueOf(text));
                    inputpayload.setText(textbaru);
                    inputpayload.setSelection(textbaru.length());
                }
                inputpayload.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputmaxgross = itemview.findViewById(R.id.inputmaxgross);
        inputmaxgross.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                inputmaxgross.removeTextChangedListener(this);
                String text = inputmaxgross.getText().toString().trim().replace(",","").replace(".","");
                if(!text.equals("")){
                    String textbaru = var.setFormatNominal(Integer.valueOf(text));
                    inputmaxgross.setText(textbaru);
                    inputmaxgross.setSelection(textbaru.length());
                }
                inputmaxgross.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loaddata();
        messagewarning();
        return itemview;
    }

    public void messagewarning(){
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

    public void savedata(boolean next){
        int owner_position = spinowner.getSelectedItemPosition();
        int size_position = spinsize.getSelectedItemPosition();
        int tipe_position = spintipe.getSelectedItemPosition();
        int grade_position = spingrade.getSelectedItemPosition();
        int wash_position = spinwash.getSelectedItemPosition();
        int bulan_position = spinbulan.getSelectedItemPosition();
        int tahun_position = spintahun.getSelectedItemPosition();

        String bulan = listbulan.get(spinbulan.getSelectedItemPosition());
        if(bulan_position == 0){
            bulan = "0";
        }
        String haulier = inputhaulier.getText().toString().trim();
        String tare = inputtare.getText().toString().trim();
        String payload = inputpayload.getText().toString().trim();
        String maxgross = inputmaxgross.getText().toString().trim();
        String ownercode = listowner_code.get(owner_position);
        String size = listsize.get(spinsize.getSelectedItemPosition());
        String tipe = listtipe.get(spintipe.getSelectedItemPosition());

        String tahun = listtahun.get(spintahun.getSelectedItemPosition());
        String grade = listgrade.get(spingrade.getSelectedItemPosition());
        String washcode = listwash_code.get(spinwash.getSelectedItemPosition());

        boolean lanjut = false;
        String message = "";
        if(next){
            if(!FILE_CONTAINER){
                message = "Foto no container tidak boleh kosong";
            }else
            if(!FILE_CSC){
                message = "Foto csc tidak boleh kosong";
            }else
            if(owner_position == 0){
                message = "Owner tidak boleh kosong";
            }else
            if(size_position == 0){
                message = "Size tidak boleh kosong";
            }else
            if(tipe_position == 0){
                message = "Tipe tidak boleh kosong";
            }else
            if(haulier.equals("")){
                message = "Haulier tidak boleh kosong";
            }else
            if(bulan_position == 0){
                message = "Bulan tidak boleh kosong";
            }else
            if(tahun_position == 0){
                message = "Tahun tidak boleh kosong";
            }else
            if(tare.equals("")){
                message = "Tare tidak boleh kosong";
            }else
            if(payload.equals("")){
                message = "Payload tidak boleh kosong";
            }else
            if(maxgross.equals("")){
                message = "Maxgross tidak boleh kosong";
            }else
            if(grade_position == 0){
                message = "Grade tidak boleh kosong";
            }else
            if(wash_position == 0){
                message = "Wash tidak boleh kosong";
            }else{
                lanjut = true;
            }
        }else{
            lanjut = true;
        }

        if(lanjut){
            datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
            datainputsurveyin_edit.putString("haulier", haulier);
            datainputsurveyin_edit.putString("tare", tare);
            datainputsurveyin_edit.putString("payload", payload);
            datainputsurveyin_edit.putString("maxgross", maxgross);
            datainputsurveyin_edit.putString("ownercode", ownercode);
            datainputsurveyin_edit.putString("size", size);
            datainputsurveyin_edit.putString("tipe", tipe);
            datainputsurveyin_edit.putString("bulan", bulan);
            datainputsurveyin_edit.putString("tahun", tahun);
            datainputsurveyin_edit.putString("grade", grade);
            datainputsurveyin_edit.putString("washcode", washcode);
            datainputsurveyin_edit.putString("photo", String.valueOf(DATAPHOTO));
            datainputsurveyin_edit.apply();

            if(next){
                nextfragment();
            }else{
                prevfragment();
            }
        }else{
            show_warning(message);
        }
    }

    public void loaddata(){
        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        DATAPHOTO = new JsonParser().parse(datainputsurveyin.getString("photo", "[]")).getAsJsonArray();

        String haulier = datainputsurveyin.getString("haulier", "");
        String tare = datainputsurveyin.getString("tare", "");
        String payload = datainputsurveyin.getString("payload", "");
        String maxgross = datainputsurveyin.getString("maxgross", "");
        String ownercode = datainputsurveyin.getString("ownercode", "");
        String size = datainputsurveyin.getString("size", "");
        String tipe = datainputsurveyin.getString("tipe", "");
        String grade = datainputsurveyin.getString("grade", "");
        String bulan = datainputsurveyin.getString("bulan", "0");
        String tahun = datainputsurveyin.getString("tahun", "");
        String washcode = datainputsurveyin.getString("washcode", "");

        inputhaulier.setText(haulier);
        inputtare.setText(tare);
        inputpayload.setText(payload);
        inputmaxgross.setText(maxgross);

        for(int i=0; i<DATAPHOTO.size(); i++){
            JsonObject datajson = DATAPHOTO.get(i).getAsJsonObject();
            if(datajson.get("posisi").getAsString().equals("CONTAINER")){
                FILE_CONTAINER = true;
                Glide.with(context).load(datajson.get("file_temp").getAsString()).into(imagefotocontainer);
            }else
                if(datajson.get("posisi").getAsString().equals("CSC")){
                    FILE_CSC = true;
                    Glide.with(context).load(datajson.get("file_temp").getAsString()).into(imagefotocsc);
                }
        }

        loaddata_owner(ownercode);
        loaddata_size(size);
        loaddata_tipe(tipe);
        loaddata_grade(grade);
        loaddata_bulan(bulan);
        loaddata_tahun(tahun);
        loaddata_wash(washcode);
    }

    public void loaddata_owner(String ownercode){
        listowner_name.add("- Pilih Owner");
        listowner_code.add("");

        JsonArray dataarray = vod.getDatabase_owner();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            listowner_name.add(code +" - "+ datajson.get("name").getAsString());
            listowner_code.add(code);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listowner_name);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spinowner.setAdapter(adapter);
        spinowner.setSelection(var.getIndexSpinner(listowner_code, ownercode));
    }

    public void loaddata_wash(String washcode){
        listwash_name.add("- Pilih Wash");
        listwash_code.add("");

        JsonArray dataarray = vod.getDatabase_wash();
        for(int i=0; i < dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            String code = datajson.get("code").getAsString();
            listwash_name.add(code +" - "+ datajson.get("name").getAsString());
            listwash_code.add(code);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listwash_name);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spinwash.setAdapter(adapter);
        spinwash.setSelection(var.getIndexSpinner(listwash_code, washcode));
    }

    public void loaddata_size(String size){
        listsize.add("- Pilih Size");

        JsonArray dataarray = vod.getDatabase_size();
        for(int i=0; i < dataarray.size(); i++){
            listsize.add(dataarray.get(i).getAsString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listsize);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spinsize.setAdapter(adapter);
        spinsize.setSelection(var.getIndexSpinner(listsize, size));
    }

    public void loaddata_tipe(String tipe){
        listtipe.add("- Pilih Tipe");

        JsonArray dataarray = vod.getDatabase_tipe();
        for(int i=0; i < dataarray.size(); i++){
            listtipe.add(dataarray.get(i).getAsString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listtipe);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spintipe.setAdapter(adapter);
        spintipe.setSelection(var.getIndexSpinner(listtipe, tipe));
    }

    public void loaddata_grade(String grade){
        listgrade.add("- Pilih Grade");

        JsonArray dataarray = vod.getDatabase_grade();
        for(int i=0; i < dataarray.size(); i++){
            listgrade.add(dataarray.get(i).getAsString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listgrade);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spingrade.setAdapter(adapter);
        spingrade.setSelection(var.getIndexSpinner(listgrade, grade));
    }

    public void loaddata_bulan(String bulan){
        listbulan.add("- Pilih Bulan");
        for(int i=1; i <= 12; i++){
            listbulan.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listbulan);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spinbulan.setAdapter(adapter);
        spinbulan.setSelection(var.getIndexSpinner(listbulan, String.valueOf(Integer.valueOf(bulan))));
    }

    public void loaddata_tahun(String tahun){
        listtahun.add("- Pilih Tahun");

        JsonArray dataarray = vod.getDatabase_year();
        for(int i=0; i < dataarray.size(); i++){
            listtahun.add(dataarray.get(i).getAsString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(
                context,
                R.layout.layout_spinner,
                listtahun);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);
        spintahun.setAdapter(adapter);
        spintahun.setSelection(var.getIndexSpinner(listtahun, tahun));
    }

    public void nextfragment(){
        Fragment fragment = new PhotoContainerFragment(
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
        Fragment fragment = new NoContainerFragment(
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

        ((Activity) context).startActivityForResult(cameraintent, CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CODE_CONTAINER){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK) {
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

            String posisi = "";
            if(requestCode == CODE_CONTAINER){
                Glide.with(context).load(bitmap_new).into(imagefotocontainer);
                posisi = "CONTAINER";
                FILE_CONTAINER = true;
            }else if(requestCode == CODE_CSC){
                Glide.with(context).load(bitmap_new).into(imagefotocsc);
                posisi = "CSC";
                FILE_CSC = true;
            }

            JsonObject datajson = new JsonObject();
            datajson.addProperty("mobid_photo", var.createMobID());
            datajson.addProperty("mobid_damage", "");
            datajson.addProperty("file_temp", FILE_TEMP);
            datajson.addProperty("posisi", posisi);
            datajson.addProperty("tipe", "C");
            datajson.addProperty("action", "add");

            boolean ketemu = false;
            for(int i=0; i < DATAPHOTO.size(); i++){
                JsonObject searchjson = DATAPHOTO.get(i).getAsJsonObject();
                if(searchjson.get("posisi").getAsString().equals(posisi)){
                    ketemu = true;
                    DATAPHOTO.set(i, datajson);
                    var.removePhoto(searchjson.get("file_temp").getAsString());
                    break;
                }
            }

            if(!ketemu){
                DATAPHOTO.add(datajson);
            }
        }else{
            Log.d("TAGGAR", "gagal "+resultCode+" reques ");
        }
    }
}