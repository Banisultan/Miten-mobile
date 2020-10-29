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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.LongFunction;

import id.bnn.convey.Adapter.ComponentCodeAdapter;
import id.bnn.convey.Adapter.DimensiAdapter;
import id.bnn.convey.Adapter.PhotoAdapter;
import id.bnn.convey.LoginScan;
import id.bnn.convey.Model.DamageModel;
import id.bnn.convey.Model.DimensiModel;
import id.bnn.convey.Model.ListComponentCodeModel;
import id.bnn.convey.Model.ListFotoDepanModel;
import id.bnn.convey.Model.ListPhoto;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class DeskripsiSurveyFragment extends Fragment {

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    Context context;

    RecyclerView view;
    List<ListPhoto> list;
    PhotoAdapter adapter;

    RelativeLayout tomboladd;
    LinearLayout tombolsimpan;
    LinearLayout tombolprev;

    EditText inputquantity;
    EditText inputlocation;
    TextView tombolcomponent;
    TextView tombolrepair;
    TextView tomboldimensi;
    TextView tomboldamage;

    Spinner listtpc;

    ArrayList<String> textposition = new ArrayList<>();

    ArrayList<String> DIRECTORY_URL = new ArrayList<>();

    String FILE_IMAGE;

    VariableText var = new VariableText();

    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;
    SharedPreferences datalistsurvey;

    String TIPE;
    String ID;
    String ID_SURVEY = "null";
    String ID_FOTO = "null";

    AlertDialog.Builder message_alert;
    AlertDialog message_show;
    LayoutInflater message_inflate;
    View message_view;

    EditText inputsearch;
    LinearLayout tombolyes;
    LinearLayout tombolno;
    TextView textpilihan;

    RecyclerView viewrecyler;
    List<ListComponentCodeModel> listcomponent;
    ComponentCodeAdapter adaptercomponent;

    List<DimensiModel> listdimensi;
    DimensiAdapter adapterdimensi;

    List<ListComponentCodeModel> listdamage;
    ComponentCodeAdapter adapterdamage;

    List<ListComponentCodeModel> listrepair;
    ComponentCodeAdapter adapterrepair;

    ArrayList<String> textcomponent = new ArrayList<>();
    ArrayList<String> textcomponentcode = new ArrayList<>();

    ArrayList<String> selectdimension = new ArrayList<>();
    ArrayList<String> selectdamage = new ArrayList<>();
    ArrayList<String> selectdamagecode = new ArrayList<>();
    ArrayList<String> selecttpc = new ArrayList<>();
    ArrayList<String> selectrepair = new ArrayList<>();
    ArrayList<String> selectrepaircode = new ArrayList<>();

    AlertDialog.Builder message_error;
    AlertDialog show_error;
    LayoutInflater inflate_error;
    View view_error;
    TextView textjudul;

    LinearLayout tombolyes_error;
    TextView textmessage;

    String STATUS_SEARCH = "";
    String TEXT_DIMENSION = "DIMENSION";
    String TEXT_DAMAGE = "DAMAGE";
    String TEXT_COMPONENT = "COMPONENT";
    String TEXT_REPAIR = "REPAIR";
    String TEXT_SELECT_DIMENSION = "- Pilih Dimensi";

    public DeskripsiSurveyFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline,
            String tipe
    ) {
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
        this.TIPE = tipe;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        message_show.dismiss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_deskripsi_survey, container, false);
        viewv2.setImageResource(R.drawable.vec_warning_v3);

        context = container.getContext();
        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        datalistsurvey = context.getSharedPreferences("datalistitem", Context.MODE_PRIVATE);

        inputquantity = itemview.findViewById(R.id.inputquantity);
        inputlocation = itemview.findViewById(R.id.inputlocation);

        listtpc = itemview.findViewById(R.id.listtpc);

        list = new ArrayList<>();

        textcomponent.add("- Pilih Component Code");
        textcomponentcode.add("00");

        selectdimension.add(TEXT_SELECT_DIMENSION);
        selectdamage.add("- Pilih Damage Code");
        selectdamagecode.add("00");

        selectrepair.add("- Pilih Repair Code");
        selectrepaircode.add("00");

        view = itemview.findViewById(R.id.viewphoto);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        tomboladd = itemview.findViewById(R.id.tomboladd);
        tomboladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkpermision();
            }
        });

        tombolsimpan = itemview.findViewById(R.id.tombolsimpan);
        tombolsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        tombolprev = itemview.findViewById(R.id.tombolprev);
        tombolprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevfragment();
            }
        });

        tombolcomponent = itemview.findViewById(R.id.tombolcomponent);
        tombolcomponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowComponent();
            }
        });

        tomboldimensi = itemview.findViewById(R.id.inputdimensi);
        tomboldimensi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowDimensi();
            }
        });

        tomboldamage = itemview.findViewById(R.id.inputdamage);
        tomboldamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowDamage();
            }
        });

        tombolrepair = itemview.findViewById(R.id.tombolrepair);
        tombolrepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShowRepair();
            }
        });

        showdialog();
        showdialog_error();
        addData();
        return itemview;
    }

    public void setShowDimensi(){
        inputsearch.setText("");
        viewrecyler.setAdapter(adapterdimensi);
        textjudul.setText("Dimensi");
        textpilihan.setText(selectdimension.get(0));
        STATUS_SEARCH = TEXT_DIMENSION;
        message_show.show();
    }

    public void setShowComponent(){
        inputsearch.setText("");
        viewrecyler.setAdapter(adaptercomponent);
        textjudul.setText("Component Code");
        if(selectdamagecode.get(0).equals("00")){
            textpilihan.setText(textcomponent.get(0));
        }else{
            textpilihan.setText(textcomponentcode.get(0)+" - "+textcomponent.get(0));
        }

        STATUS_SEARCH = TEXT_COMPONENT;
        message_show.show();
    }

    public void setShowDamage(){
        inputsearch.setText("");
        viewrecyler.setAdapter(adapterdamage);
        textjudul.setText("Damage Code");
        if(selectdamagecode.get(0).equals("00")){
            textpilihan.setText(selectdamage.get(0));
        }else{
            textpilihan.setText(selectdamagecode.get(0)+" - "+selectdamage.get(0));
        }

        STATUS_SEARCH = TEXT_DAMAGE;
        message_show.show();
    }

    public void setShowRepair(){
        inputsearch.setText("");
        viewrecyler.setAdapter(adapterrepair);
        textjudul.setText("Repair Code");
        if(selectrepaircode.get(0).equals("00")){
            textpilihan.setText(selectrepair.get(0));
        }else{
            textpilihan.setText(selectrepaircode.get(0)+" - "+selectrepair.get(0));
        }

        STATUS_SEARCH = TEXT_REPAIR;
        message_show.show();
    }

    public void validation(){
        if(textcomponentcode.get(0).equals("00")){
            setShow_error("Component tidak boleh kosong");
        }else
            if(inputlocation.getText().toString().trim().equals("")){
                setShow_error("Location tidak boleh kosong");
            }else
                if(selectdimension.get(0).equals(TEXT_SELECT_DIMENSION)){
                    setShow_error("Dimensi tidak boleh kosong");
                }else
                    if(inputquantity.getText().toString().trim().equals("")){
                        setShow_error("Quantity tidak boleh kosong");
                    }else
                        if(selectdamagecode.get(0).equals("00")){
                            setShow_error("Damage tidak boleh kosong");
                        }else
                            if(listtpc.getSelectedItemPosition() == 0){
                                setShow_error("TPC tidak boleh kosong");
                            }else
                                if(selectrepaircode.get(0).equals("00")){
                                    setShow_error("Repair tidak boleh kosong");
                                }else
                                    if(list.size() == 0) {
                                        setShow_error("Minimal harus melampirkan 1 foto");
                                    }else{
                                        if(TIPE.equals("edit")){
                                            savedata(replaceposition());
                                        }else{
                                            savedata(-1);
                                        }
                                    }
    }

    public void hideall(){
        show_error.dismiss();
        message_show.dismiss();
    }

    public void showdialog_error(){
        message_error = new AlertDialog.Builder(context);
        inflate_error = getLayoutInflater();
        show_error = message_error.create();
        view_error = inflate_error.inflate(R.layout.layout_message_error, null, false);
        message_error.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_error.setView(view_error);
        message_error.setCancelable(true);
        show_error = message_error.create();

        tombolyes_error = view_error.findViewById(R.id.tombolyes);
        tombolyes_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideall();
            }
        });

        textmessage = view_error.findViewById(R.id.textmessage);
    }

    public void setShow_error(String message){
        textmessage.setText(message);
        show_error.show();
    }

    public void showdialog(){
        message_alert = new AlertDialog.Builder(context);
        message_inflate = getLayoutInflater();
        message_view = message_inflate.inflate(R.layout.list_component_code, null, false);
        message_alert.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_alert.setView(message_view);
        message_alert.setCancelable(false);
        message_show = message_alert.create();

        viewrecyler = message_view.findViewById(R.id.view);
        viewrecyler.setHasFixedSize(true);
        viewrecyler.setLayoutManager(new LinearLayoutManager(context));
        viewrecyler.setAdapter(adaptercomponent);

        inputsearch = message_view.findViewById(R.id.inputsearch);
        inputsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = inputsearch.getText().toString().trim();
                if(STATUS_SEARCH.equals(TEXT_COMPONENT)){
                    loaddata_component(text);
                }
                else if(STATUS_SEARCH.equals(TEXT_DIMENSION)){
                    loaddata_dimension(text);
                }
                else if(STATUS_SEARCH.equals(TEXT_DAMAGE)){
                    loaddata_damage(text);
                }
                else if(STATUS_SEARCH.equals(TEXT_REPAIR)){
                    loaddata_repair(text);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textjudul = message_view.findViewById(R.id.textjudul);

        textpilihan = message_view.findViewById(R.id.textpilihan);
        tombolyes = message_view.findViewById(R.id.tombolyes);

        tombolno = message_view.findViewById(R.id.tombolno);
        tombolno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_show.dismiss();
            }
        });
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
        ((Activity) context).startActivityForResult(cameraintent, var.CODE_CAPTURE_IMAGE_RUSAK);
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

    public void checkpermision(){
        String[] permision = new String[]{
                Manifest.permission.CAMERA
        };

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permision, var.CODE_PERMISSION_CAMERA);
        }else{
            opencamera();
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
            File file_in = var.createTempFile_v2(context, bitmap_num, filetemp);

            if(requestCode == var.CODE_CAPTURE_IMAGE_RUSAK){
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage);
                    addPhoto(bitmap, FILE_IMAGE, ID_SURVEY, "null");
                }catch (Exception e){
                    Log.d("TAGGAR", "TRY gagal : "+e);
                }
            }
        }else{
            Log.d("TAGGAR", "gagal "+resultCode+" reques "+requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAGGAR", "masukk ke request");
        if(requestCode == var.CODE_PERMISSION_CAMERA){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera();
            }
        }
    }

    public void addData(){
        loaddata_repair("");
        loaddata_tpc();
        loaddata_damage("");
        loaddata_dimension("");
        loaddata_component("");

        if(TIPE.equals("edit")){
            loaddata_edit();
            loaddata_foto();
        }
    }

    public void loaddata_dimension(String search){
        listdimensi = new ArrayList<>();
        String data = datalistsurvey.getString("dimension", "");

        search = search.toLowerCase();

        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i<dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    String nama = datajson.getString("dim_name");

                    if(search.equals("")){
                        listdimensi.add(new DimensiModel(nama));
                    }else{
                        if(nama.toLowerCase().matches("\\A"+search+".*")){
                            listdimensi.add(new DimensiModel(nama));
                        }
                    }
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            adapterdimensi = new DimensiAdapter(
                    context,
                    listdimensi,
                    selectdimension,
                    tombolyes,
                    message_show,
                    tomboldimensi,
                    textpilihan);
        }

        if(STATUS_SEARCH.equals(TEXT_DIMENSION)){
            viewrecyler.setAdapter(adapterdimensi);
        }
    }

    public void loaddata_edit(){
        String data = datainputsurveyin.getString("dataedit", "");
        Log.d("TAGGAR", "DATA EDIT "+data);
        if(!data.equals("")){
            try{
                JSONObject datajson = new JSONObject(data);

                ID = datajson.getString("id");
                String component = datajson.getString("component");
                String componentcode = datajson.getString("componentcode");
                String location = datajson.getString("location");
                String dimensi = datajson.getString("dimensi");
                String quantity = datajson.getString("quantity");
                String damage = datajson.getString("damage");
                String damagecode = datajson.getString("damagecode");
                String repair = datajson.getString("repair");
                String repaircode = datajson.getString("repaircode");
                String tpc = datajson.getString("tpc");
                ID_SURVEY = datajson.getString("id_survey_detail");

                textcomponent.set(0, component);
                textcomponentcode.set(0, componentcode);
                selectdimension.set(0, dimensi);
                selectdamage.set(0, damage);
                selectdamagecode.set(0, damagecode);
                selectrepair.set(0, repair);
                selectrepaircode.set(0, repaircode);

                tomboldimensi.setText(dimensi);
                tombolcomponent.setText(componentcode+" - "+component);
                tomboldamage.setText(damagecode+" - "+damage);
                tombolrepair.setText(repaircode+" - "+repair);

                inputlocation.setText(location);
                inputquantity.setText(quantity);

                listtpc.setSelection(var.getIndexSpinner(selecttpc, tpc));

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }
        }
    }

    public void loaddata_foto(){
        String data = datainputsurveyin.getString("dataedit", "");
        Log.d("TAGGAR", "FOTO "+data);
        if(!data.equals("")){
            try{
                JSONObject datajson = new JSONObject(data);
                JSONArray dataarray = datajson.getJSONArray("foto");
                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson_baru = dataarray.getJSONObject(i);

                    String url_file = datajson_baru.getString("directory");
                    String url_file_api = datajson_baru.getString("directory_url");
                    ID_FOTO = datajson_baru.getString("id_foto");

                    if(url_file.equals("null")){
                        list.add(new ListPhoto(
                                url_file_api,
                                null,
                                "null",
                                ID_SURVEY, ID_FOTO));
                        DIRECTORY_URL.add(url_file_api);
                    }else{
                        File file = new File(url_file);
                        Bitmap bitmap = var.BitmapCompress(BitmapFactory.decodeFile(file.getPath()), (float) 0.01);
                        list.add(new ListPhoto(
                                "null",
                                bitmap,
                                url_file,
                                ID_SURVEY,
                                "null"));
                        DIRECTORY_URL.add("null");
                    }

                }
            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            adapter = new PhotoAdapter(context, list);
            view.setAdapter(adapter);
        }

        Log.d("TAGGAR", "ISI DATA size "+list.size());
    }

    public void loaddata_damage(String search){
        listdamage = new ArrayList<>();
        String data = datalistsurvey.getString("damage", "");
        search = search.toLowerCase();
        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i<dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);

                    String nama = datajson.getString("dmg_nama");
                    String code = datajson.getString("dmg_code");

                    if(search.equals("")){
                        listdamage.add(new ListComponentCodeModel(code, nama));
                    }else{
                        if(code.toLowerCase().matches("\\A"+search+".*")){
                            listdamage.add(new ListComponentCodeModel(code, nama));
                        }
                    }
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            adapterdamage = new ComponentCodeAdapter(
                    context,
                    listdamage,
                    selectdamage,
                    selectdamagecode,
                    tombolyes,
                    message_show,
                    tomboldamage);
        }

        if(STATUS_SEARCH.equals(TEXT_DAMAGE)){
            viewrecyler.setAdapter(adapterdamage);
        }
    }

    public void loaddata_component(String search){
        listcomponent = new ArrayList<>();
        String data = datalistsurvey.getString("component", "");
        search = search.toLowerCase();

        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i<dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    String nama = datajson.getString("comc_name");
                    String code = datajson.getString("comc_code");

                    if(search.equals("")){
                        listcomponent.add(new ListComponentCodeModel(code, nama));
                    }else{
                        if(code.toLowerCase().matches("\\A"+search+".*")){
                            listcomponent.add(new ListComponentCodeModel(code, nama));
                        }
                    }
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            adaptercomponent = new ComponentCodeAdapter(
                    context,
                    listcomponent,
                    textcomponent,
                    textcomponentcode,
                    tombolyes,
                    message_show,
                    tombolcomponent);
        }

        if(STATUS_SEARCH.equals(TEXT_COMPONENT)){
            viewrecyler.setAdapter(adaptercomponent);
        }
    }

    public void loaddata_repair(String search){
        listrepair = new ArrayList<>();
        String data = datalistsurvey.getString("repair", "");
        search = search.toLowerCase();

        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i<dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);

                    String nama = datajson.getString("rep_desc");
                    String code = datajson.getString("rep_code");

                    if(search.equals("")){
                        listrepair.add(new ListComponentCodeModel(code, nama));
                    }else{
                        if(code.toLowerCase().matches("\\A"+search+".*")){
                            listrepair.add(new ListComponentCodeModel(code, nama));
                        }
                    }
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            adapterrepair = new ComponentCodeAdapter(
                    context,
                    listrepair,
                    selectrepair,
                    selectrepaircode,
                    tombolyes,
                    message_show,
                    tombolrepair);
        }

        if(STATUS_SEARCH.equals(TEXT_REPAIR)){
            viewrecyler.setAdapter(adapterrepair);
        }
    }

    public void loaddata_tpc(){
        String data = datalistsurvey.getString("tpc", "");
        selecttpc.add("- Pilih TPC");

        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i<dataarray.length(); i++){
                    String value = dataarray.getString(i);
                    selecttpc.add(value);
                }

            }catch (Exception e){
                Log.d("TAGGAR error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    selecttpc);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listtpc.setAdapter(adapter);
        }
    }

    public int replaceposition(){
        String data = datainputsurveyin.getString("surveyindetail", "");

        if(!data.equals("")){
            try{
                JSONArray dataarray = new JSONArray(data);
                for(int i=0; i< dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    if(datajson.getString("id").equals(ID)){
                        return i;
                    }
                }
            }catch (Exception e){

            }

        }
        return -1;
    }

    public void addPhoto(Bitmap bitmap, String file_dir, String id_survey, String id_foto){
        list.add(new ListPhoto("null", bitmap, file_dir, id_survey, id_foto));
        DIRECTORY_URL.add("null");
        adapter = new PhotoAdapter(context, list);
        view.setAdapter(adapter);
    }

    public void savedata(int position_replace){
        String data = datainputsurveyin.getString("surveyindetail", "[]");

        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        String component = textcomponent.get(0);
        String componentcode = textcomponentcode.get(0);
        String location = inputlocation.getText().toString().trim();
        String dimensi = selectdimension.get(0);
        String quantity = inputquantity.getText().toString().trim();

        String damage = selectdamage.get(0);
        Log.d("TAGGAR", "DAMAGE "+damage);
        String damagecode = selectdamagecode.get(0);

        String repair = selectrepair.get(0);
        Log.d("TAGGAR", "REPAIR "+repair);
        String repaircode = selectrepaircode.get(0);

        String tpc = listtpc.getSelectedItem().toString();

        Date date = new Date();
        long id = date.getTime();

        ID = damagecode+"_"+id+"_"+var.getRandomNumberInRange(100, 999);

        try{
            JSONArray dataarrayall = new JSONArray(data);

            JSONObject datajsonall = new JSONObject();
            datajsonall.put("id", ID);
            datajsonall.put("component", component);
            datajsonall.put("componentcode", componentcode);
            datajsonall.put("location", location);
            datajsonall.put("dimensi", dimensi);
            datajsonall.put("quantity", quantity);
            datajsonall.put("damage", damage);
            datajsonall.put("damagecode", damagecode);
            datajsonall.put("repair", repair);
            datajsonall.put("repaircode", repaircode);
            datajsonall.put("tpc", tpc);
            datajsonall.put("id_survey_detail", ID_SURVEY);

            JSONArray dataarray = new JSONArray();
            for(int i=0; i < list.size(); i++){
                JSONObject datajson = new JSONObject();
                Log.d("TAGGAR", "file dir "+list.get(i).getFile_dir());
                datajson.put("directory", list.get(i).getFile_dir());
                datajson.put("directory_url", list.get(i).getUrl());
                datajson.put("id_foto", list.get(i).getId_foto());
                dataarray.put(i, datajson);
            }

            datajsonall.put("foto", dataarray);

            if(position_replace == -1){
                dataarrayall.put(dataarrayall.length(), datajsonall);
            }else{
                dataarrayall.put(position_replace, datajsonall);
            }

            datainputsurveyin_edit.putString("surveyindetail", String.valueOf(dataarrayall));
            datainputsurveyin_edit.commit();

            Log.d("TAGGAR", "ini json HAHA "+datainputsurveyin.getString("surveyindetail", "kosong"));
        }catch (Exception e){
            Log.d("TAGGAR", "gagal "+e);
        }

        prevfragment();
    }

    public void prevfragment(){
        Fragment fragment = new InputKerusakan(
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
