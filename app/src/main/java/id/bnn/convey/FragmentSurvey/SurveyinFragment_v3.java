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
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.R;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class SurveyinFragment_v3 extends Fragment {

    Uri uriimage;

    Spinner listowner;
    Spinner listsize;
    Spinner listtipe;
    Spinner listmonth;
    Spinner listyears;
    Spinner listwash;
    Spinner listgrade;

    ImageView imagecontainer;
    ImageView imagecsc;

    Context context;

    LinearLayout tombollanjut;
    LinearLayout tombolcontainer;
    LinearLayout tombolcsc;

    EditText inputcontainernumber;
    EditText inputhaulier;
    EditText inputpayload;
    EditText inputtare;
    EditText inputmaxgross;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    ArrayList<String> textowner_nama = new ArrayList<>();
    ArrayList<String> textowner_code = new ArrayList<>();
    ArrayList<String> textsize = new ArrayList<>();
    ArrayList<String> texttipe = new ArrayList<>();
    ArrayList<String> textgrade_nama = new ArrayList<>();
    ArrayList<String> textgrade_code = new ArrayList<>();
    ArrayList<String> textmonth_nama = new ArrayList<>();
    ArrayList<String> textmonth_code = new ArrayList<>();
    ArrayList<String> textyears = new ArrayList<>();
    ArrayList<String> textwashing_nama = new ArrayList<>();
    ArrayList<String> textwashing_code = new ArrayList<>();
    ArrayList<String> textwashing_show = new ArrayList<>();

    SharedPreferences dataakun;
    String IDAKUN;
    String GROUP;

    SharedPreferences datalistitem;
    SharedPreferences datainputsurveyin;
    SharedPreferences.Editor datainputsurveyin_edit;

    String FILE_IMAGE_CONTAINER = "";
    String FILE_IMAGE_CSC = "";
    String FILE_TEMP = "";

    String FILE_IMAGE_CONTAINER_URL = "";
    String FILE_IMAGE_CSC_URL = "";

    String TIPE_ACTIVITY = "";
    String ID_SURVEY = "";

    int CODE_PHOTO = 0;

    AlertDialog.Builder message_error;
    AlertDialog show_error;
    LayoutInflater inflate_error;
    View view_error;

    AlertDialog.Builder message_loading;
    AlertDialog show_loading;
    LayoutInflater inflate_loading;
    View view_loading;
    TextView textmessage_loading;

    LinearLayout tombolyes;
    TextView textmessage;

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();

    TextView textnocontainer;

    @Override
    public void onDestroy(){
        super.onDestroy();
        hideall();
    }

    @Override
    public void onPause(){
        super.onPause();
        hideall();
    }

    public SurveyinFragment_v3(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ) {
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_surveyin, container, false);

        for(int i=1; i<viewline.length; i++){
            if(i <= 1){
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
        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        IDAKUN = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");

        textnocontainer = itemview.findViewById(R.id.textnomorcontainer);
        textnocontainer.setTextSize(getResources().getDimension(R.dimen.textsize_normal));

        listowner = itemview.findViewById(R.id.listowner);
        listsize = itemview.findViewById(R.id.listsize);
        listtipe = itemview.findViewById(R.id.listtipe);
        listmonth = itemview.findViewById(R.id.listmonth);
        listyears = itemview.findViewById(R.id.listyears);
        listwash = itemview.findViewById(R.id.listwash);
        listgrade = itemview.findViewById(R.id.listgrade);

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombolcontainer = itemview.findViewById(R.id.tombolnocontainer);
        tombolcsc = itemview.findViewById(R.id.tombolcsc);

        inputcontainernumber = itemview.findViewById(R.id.inputcontainernumber);
        inputcontainernumber.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        inputhaulier = itemview.findViewById(R.id.inputhaulier);
        inputhaulier.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        inputpayload = itemview.findViewById(R.id.inputpayload);
        inputpayload.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputpayload.removeTextChangedListener(this);
                try{
                    String text = inputpayload.getText().toString().trim().replace(",","").replace(".","");
                    Log.d("TAGGAR", "ini "+text.length());
                    String hasil = var.setFormatNominal(Integer.valueOf(text));
                    inputpayload.setText(hasil);
                    inputpayload.setSelection(hasil.length());
                }catch (Exception e){

                }
                inputpayload.addTextChangedListener(this);
            }
        });

        inputtare = itemview.findViewById(R.id.inputtare);
        inputtare.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputtare.removeTextChangedListener(this);
                try{
                    String text = inputtare.getText().toString().trim().replace(",","").replace(".","");
                    String hasil = var.setFormatNominal(Integer.valueOf(text));
                    inputtare.setText(hasil);
                    inputtare.setSelection(hasil.length());
                }catch (Exception e){

                }
                inputtare.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        inputmaxgross = itemview.findViewById(R.id.inputmaxgross);
        inputmaxgross.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputmaxgross.removeTextChangedListener(this);
                try{
                    String text = inputmaxgross.getText().toString().trim().replace(",","").replace(".","");
                    String hasil = var.setFormatNominal(Integer.valueOf(text));
                    inputmaxgross.setText(hasil);
                    inputmaxgross.setSelection(hasil.length());
                }catch (Exception e){

                }
                inputmaxgross.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imagecontainer = itemview.findViewById(R.id.imagenocontainer);
        imagecsc = itemview.findViewById(R.id.imagecsc);

        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });

        tombolcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CODE_PHOTO = var.CODE_CAPTURE_IMAGE_CONTAINER;
                checkpermision();
            }
        });

        tombolcsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CODE_PHOTO = var.CODE_CAPTURE_IMAGE_CSC;
                checkpermision();
            }
        });

        showdialog();
        showdialog_loading();
        addData();
        loaddata_input();
        return itemview;
    }

    public void showdialog_loading(){
        message_loading = new AlertDialog.Builder(context);
        inflate_loading = getLayoutInflater();
        show_loading = message_loading.create();
        view_loading = inflate_loading.inflate(R.layout.layout_message_loading, null, false);
        message_loading.setView(view_loading);
        message_loading.setCancelable(false);
        show_loading = message_loading.create();
        textmessage_loading = view_loading.findViewById(R.id.textmessage);
    }

    public void showdialog(){
        message_error = new AlertDialog.Builder(context);
        inflate_error = getLayoutInflater();
        show_error = message_error.create();
        view_error = inflate_error.inflate(R.layout.layout_message_error, null, false);
        message_error.create().getWindow().setBackgroundDrawableResource(R.drawable.bg_shape);
        message_error.setView(view_error);
        message_error.setCancelable(false);
        show_error = message_error.create();

        tombolyes = view_error.findViewById(R.id.tombolyes);
        tombolyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideall();
            }
        });

        textmessage = view_error.findViewById(R.id.textmessage);
    }

    public void setShow_loading(String message){
        show_loading.show();
        textmessage.setText(message);
    }

    public void setShow_error(String message){
        show_loading.dismiss();
        show_error.show();
        textmessage.setText(message);
    }

    public void hideall(){
        show_error.dismiss();
        show_loading.dismiss();
    }

    public void addData(){
        loaddata_owner();
        loaddata_washing();
        loaddata_size();
        loaddata_tipe();
        loaddata_grade();
        loaddata_month();
        loaddata_years();
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
            opencamera(CODE_PHOTO);
        }
    }

    public boolean ceknomorcontainer(String nomorcontainer){
        try{
            int[] map = {10, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                    21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36, 37, 38};

            int[] weights = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};

            int digittocheck = Integer.parseInt(nomorcontainer.substring(nomorcontainer.length()-1));

            int[] digit = new int[10];
            for (int i=0; i<4; i++) {
                digit[i] = map[Character.getNumericValue(nomorcontainer.charAt(i)) - 10];
            }

            for (int i=4; i<10; i++) {
                digit[i] = Integer.parseInt(nomorcontainer.substring(i, i+1));
            }

            for (int i=0; i<10; i++){
                digit[i] = digit[i] * weights[i];
            }

            int total = 0;
            for (int i=0; i<10; i++){
                total = total + digit[i];
            }

            String digitresult = String.valueOf(total % 11);

            if(digittocheck == Integer.parseInt(digitresult.substring(digitresult.length() - 1))){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR "+e);
            return false;
        }
    }

    public void validation(){
        String text = inputcontainernumber.getText().toString().trim();
        if(!ceknomorcontainer(text)){
            setShow_error("Nomor Container tidak valid");
        }else
        if(text.equals("")){
            setShow_error("Nomor Container tidak boleh kosong");
        }else
        if(FILE_IMAGE_CONTAINER_URL == "" && FILE_IMAGE_CONTAINER == ""){
            setShow_error("Foto Nomor Container tidak boleh kosong");
        }else
        if(FILE_IMAGE_CSC == "" && FILE_IMAGE_CSC_URL == ""){
            setShow_error("Foto CSC Plate tidak boleh kosong");
        }else
        if(listowner.getSelectedItemPosition() == 0){
            setShow_error("Owner tidak boleh kosong");
        }else
        if(listsize.getSelectedItemPosition() == 0){
            setShow_error("Size tidak boleh kosong");
        }else
        if(listtipe.getSelectedItemPosition() == 0){
            setShow_error("Tipe tidak boleh kosong");
        }else
        if(inputhaulier.getText().toString().trim().equals("")){
            setShow_error("Haulier tidak boleh kosong");
        }else
        if(listgrade.getSelectedItemPosition() == 0){
            setShow_error("Grade tidak boleh kosong");
        }else
        if(listmonth.getSelectedItemPosition() == 0){
            setShow_error("Bulan tidak boleh kosong");
        }else
        if(listyears.getSelectedItemPosition() == 0){
            setShow_error("Tahun tidak boleh kosong");
        }else
        if(inputpayload.getText().toString().trim().equals("")){
            setShow_error("Payload tidak boleh kosong");
        }else
        if(inputtare.getText().toString().trim().equals("")){
            setShow_error("Tare tidak boleh kosong");
        }else
        if(inputmaxgross.getText().toString().trim().equals("")){
            setShow_error("Maxgross tidak boleh kosong");
        }else
        if(listwash.getSelectedItemPosition() == 0){
            setShow_error("Washing tidak boleh kosong");
        }else{
            if(!EDIT) {
                setShow_loading("Sedang memuat data");
                // DELAY
                new CountDownTimer(var.DELAY_MESSAGE, 1000) {
                    public void onFinish() {
                        getAPI_cekNoContainer(text);
                    }

                    public void onTick(long millisUntilFinished) {

                    }
                }.start();
            }else{
                savedata();
            }
        }
    }

    public void nextfragment(){
        Fragment fragment = new InputFotoSurveyin(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();

        datainputsurveyin_edit.putString("datahapus", "");
        datainputsurveyin_edit.apply();
    }

    public void savedata(){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();

        String containernumber = inputcontainernumber.getText().toString().trim();

        int position_owner = listowner.getSelectedItemPosition();
        String owner = textowner_nama.get(position_owner);
        String owner_code = textowner_code.get(position_owner);

        int position_washing = listwash.getSelectedItemPosition();
        String wash_name = textwashing_nama.get(position_washing);
        String wash_code = textwashing_code.get(position_washing);

        String size = listsize.getSelectedItem().toString();
        String tipe = listtipe.getSelectedItem().toString();
        String haulier = inputhaulier.getText().toString().trim();

        int position_bulan = listmonth.getSelectedItemPosition();
        String bulan = textmonth_nama.get(position_bulan);
        String bulan_code = textmonth_code.get(position_bulan);

        String grade = listgrade.getSelectedItem().toString();
        String tahun = listyears.getSelectedItem().toString();
        String payload = inputpayload.getText().toString().trim();
        String tare = inputtare.getText().toString().trim();
        String maxgross = inputmaxgross.getText().toString().trim();

        try{
            JSONObject datajson = new JSONObject();
            datajson.put("id_survey", ID_SURVEY);
            datajson.put("tipe_activity", TIPE_ACTIVITY);
            datajson.put("no_container", containernumber);
            datajson.put("owner", owner);
            datajson.put("owner_code", owner_code);
            datajson.put("wash_name", wash_name);
            datajson.put("wash_code", wash_code);
            datajson.put("size", size);
            datajson.put("tipe", tipe);
            datajson.put("haulier", haulier);
            datajson.put("grade", grade);
            datajson.put("bulan", bulan);
            datajson.put("bulan_code", bulan_code);
            datajson.put("tahun", tahun);
            datajson.put("payload", payload);
            datajson.put("tare", tare);
            datajson.put("maxgross", maxgross);
            datajson.put("file_no_container", FILE_IMAGE_CONTAINER);
            datajson.put("file_no_container_url", FILE_IMAGE_CONTAINER_URL);
            datajson.put("file_csc", FILE_IMAGE_CSC);
            datajson.put("file_csc_url", FILE_IMAGE_CSC_URL);

            datainputsurveyin_edit.putString("surveyin", String.valueOf(datajson));
            datainputsurveyin_edit.apply();
        }catch (Exception e){
            Log.d("TAGGAR", "error save "+e);
        }

        nextfragment();
    }

    boolean EDIT = false;
    public void loaddata_input(){
        String data = datainputsurveyin.getString("surveyin", "");
        String dataedit = datainputsurveyin.getString("surveyintipe", "");

        if(dataedit.equals("edit")){
            EDIT = true;
        }

        if(!data.equals("")){
            try{
                JSONObject datajson = new JSONObject(data);

                ID_SURVEY = datajson.getString("id_survey");
                TIPE_ACTIVITY = datajson.getString("tipe_activity");

                inputcontainernumber.setText(datajson.getString("no_container"));
                FILE_IMAGE_CONTAINER = datajson.getString("file_no_container");
                FILE_IMAGE_CSC = datajson.getString("file_csc");

                if(!FILE_IMAGE_CONTAINER.equals("null")){
                    File file_no = new File(FILE_IMAGE_CONTAINER);
                    Bitmap bitmap_no = var.BitmapCompress(BitmapFactory.decodeFile(file_no.getPath()), (float) 0.05);
                    imagecontainer.setImageBitmap(bitmap_no);
                }else{
                    FILE_IMAGE_CONTAINER_URL = datajson.getString("file_no_container_url");
                    Glide.with(context).load(FILE_IMAGE_CONTAINER_URL).into(imagecontainer);
                }

                if(!FILE_IMAGE_CSC.equals("null")){
                    File file_csc = new File(FILE_IMAGE_CSC);
                    Bitmap bitmap_csc = var.BitmapCompress(BitmapFactory.decodeFile(file_csc.getPath()), (float) 0.05);
                    imagecsc.setImageBitmap(bitmap_csc);
                }else{
                    FILE_IMAGE_CSC_URL = datajson.getString("file_csc_url");
                    Glide.with(context).load(FILE_IMAGE_CSC_URL).into(imagecsc);
                }

                int position_owner = getIndex(textowner_code, datajson.getString("owner_code"));
                listowner.setSelection(position_owner);

                int position_wash = getIndex(textwashing_code, datajson.getString("wash_code"));
                listwash.setSelection(position_wash);

                int position_size = getIndex(textsize, datajson.getString("size"));
                listsize.setSelection(position_size);

                int position_tipe = getIndex(texttipe, datajson.getString("tipe"));
                listtipe.setSelection(position_tipe);

                int position_grade = getIndex(textgrade_nama, datajson.getString("grade"));
                listgrade.setSelection(position_grade);

                int position_bulan = getIndex(textmonth_code, datajson.getString("bulan_code"));
                listmonth.setSelection(position_bulan);

                int position_tahun = getIndex(textyears, datajson.getString("tahun"));
                listyears.setSelection(position_tahun);

                inputhaulier.setText(datajson.getString("haulier"));
                inputpayload.setText(datajson.getString("payload"));
                inputtare.setText(datajson.getString("tare"));
                inputmaxgross.setText(datajson.getString("maxgross"));

            }catch (Exception e){
                Log.d("TAGGAR", "ini error "+e);
            }
        }
    }

    private int getIndex(ArrayList<String> textarray, String value){
        for(int i=0; i<textarray.size(); i++){
            if(textarray.get(i).equals(value)){
                return i;
            }
        }
        return 0;
    }

    public void loaddata_owner(){
        String data = datalistitem.getString("owner", "");
        textowner_nama.add("- Pilih owner");
        textowner_code.add("00");
        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    JSONObject objdata = arrayowner.getJSONObject(i);
                    String value = objdata.getString("ow_nama");
                    String code = objdata.getString("ow_code");
                    textowner_nama.add(value);
                    textowner_code.add(code);
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    textowner_nama);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listowner.setAdapter(adapter);
        }
    }

    public void loaddata_washing(){
        String data = datalistitem.getString("washing", "");
        textwashing_nama.add("- Pilih Washing");
        textwashing_code.add("00");
        textwashing_show.add("- Pilih Washing");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    JSONObject objdata = arrayowner.getJSONObject(i);
                    String code = objdata.getString("was_code");
                    String value = objdata.getString("was_name");
                    textwashing_nama.add(value);
                    textwashing_code.add(code);
                    textwashing_show.add(code+" - "+value);
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    textwashing_show);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listwash.setAdapter(adapter);
        }
    }

    public void loaddata_size(){
        String data = datalistitem.getString("size", "");
        textsize.add("- Pilih size");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    JSONObject objdata = arrayowner.getJSONObject(i);
                    String value = objdata.getString("siz_text");
                    textsize.add(value);
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    textsize);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listsize.setAdapter(adapter);
        }
    }

    public void loaddata_tipe(){
        String data = datalistitem.getString("tipe", "");
        texttipe.add("- Pilih tipe");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    JSONObject objdata = arrayowner.getJSONObject(i);
                    String value = objdata.getString("tip_text");
                    texttipe.add(value);
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    texttipe);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listtipe.setAdapter(adapter);
        }
    }

    public void loaddata_grade(){
        String data = datalistitem.getString("grade", "");
        textgrade_nama.add("- Pilih Grade");
        textgrade_code.add("0");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    textgrade_nama.add(arrayowner.getString(i));
                    textgrade_code.add(String.valueOf(i+1));
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.layout_spinner,
                textgrade_nama);
        adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

        listgrade.setAdapter(adapter);
    }

    public void loaddata_month(){
        String data = datalistitem.getString("month", "");
        textmonth_nama.add("- Pilih bulan");
        textmonth_code.add("0");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    textmonth_nama.add(arrayowner.getString(i));
                    textmonth_code.add(String.valueOf(i+1));
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    textmonth_nama);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listmonth.setAdapter(adapter);
        }
    }

    public void loaddata_years(){
        String data = datalistitem.getString("years", "");
        textyears.add("- Pilih tahun");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    textyears.add(arrayowner.getString(i));
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    context,
                    R.layout.layout_spinner,
                    textyears);
            adapter.setDropDownViewResource(R.layout.layout_spinner_popup);

            listyears.setAdapter(adapter);
        }
    }

    public void opencamera(int code){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        File photoFile = null;
        try {
            photoFile = createImageFile(code);
        } catch (IOException ex) {
            Log.d("TAGGAR", "g "+ex);
        }

        uriimage = FileProvider.getUriForFile(context, "com.example.android.fileprovider", photoFile);

        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uriimage);

        ((Activity) context).startActivityForResult(cameraintent, code);
    }

    File filetemp;
    private File createImageFile(int code) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        filetemp = image;
        FILE_TEMP = image.getAbsolutePath();
        Log.d("TAGGAR2", "LOKASI "+FILE_TEMP);

        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAGGAR", "masukk ke request");
        if(requestCode == var.CODE_PERMISSION_CAMERA){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                opencamera(CODE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if (resultCode == Activity.RESULT_OK) {
            // Compress Image Testing
            Bitmap bitmapcheck_num = BitmapFactory.decodeFile(FILE_TEMP);
            bitmapcheck_num = var.modifyOrientation(bitmapcheck_num, FILE_TEMP);

            float size_num = 1;
            if(var.byteSizeOfv2(bitmapcheck_num) / 1024 > 1000){
                size_num = 0.2f;
            }
            Bitmap bitmap_num = var.BitmapCompress(bitmapcheck_num, size_num);

            if(requestCode == var.CODE_CAPTURE_IMAGE_CONTAINER){
                try{
                    FILE_IMAGE_CONTAINER = FILE_TEMP;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage);
                    Bitmap baru = var.modifyOrientation(bitmap, FILE_IMAGE_CONTAINER);
                    imagecontainer.setImageBitmap(baru);
                }catch (Exception e){
                    Log.d("TAGGAR", "TRY gagal : "+e);
                }
            }

            if(requestCode == var.CODE_CAPTURE_IMAGE_CSC){
                try{
                    FILE_IMAGE_CSC = FILE_TEMP;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage);
                    Bitmap baru = var.modifyOrientation(bitmap, FILE_IMAGE_CSC);
                    imagecsc.setImageBitmap(baru);
                }catch (Exception e){
                    Log.d("TAGGAR", "TRY gagal : "+e);
                }
            }
        }else{
            Log.d("TAGGAR", "gagal "+resultCode+" reques "+FILE_IMAGE_CSC);
        }
    }
    public void getAPI_cekNoContainer(String nomor){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = req.setHTTPCLIENT(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI interfaceApi = retrofit.create(InterfaceAPI.class);

        Call<JsonObject> call = null;
        call = interfaceApi.getapi_surveyin_ceknocontainer(par.PARAM_SURVEY_IN_CEK_NOMOR_CONTAINER(IDAKUN, GROUP, nomor));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            savedata();
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            setShow_error(addmessage);
                        }

                    }catch (Exception e){
                        Log.d("TAGGAR", "ERROR "+e);
                        setShow_error(var.MESSAGE_ALERT);
                    }
                }else{
                    setShow_error(var.MESSAGE_ALERT);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                setShow_error(var.MESSAGE_ALERT);
            }
        });
    }
}
