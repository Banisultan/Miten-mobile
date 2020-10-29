package id.bnn.convey.FragmentSurvey;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.ListFotoDepanAdapter;
import id.bnn.convey.Model.ListFotoDepanModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class UploadFotoFragment2 extends Fragment {

    Activity activity;
    RecyclerView listfotodepan;
    RecyclerView listfotokiri;
    RecyclerView listfotokanan;
    RecyclerView listfotobelakang;

    FragmentActivity fragment;

    Context context;
    ArrayList<String> textlist_dmg = new ArrayList<>();
    List<ListFotoDepanModel> listdepan;
    List<ListFotoDepanModel> listkiri;
    List<ListFotoDepanModel> listkanan;
    List<ListFotoDepanModel> listbelakang;

    ListFotoDepanAdapter adapterfotodepan;
    ListFotoDepanAdapter adapterfotokiri;
    ListFotoDepanAdapter adapterfotokanan;
    ListFotoDepanAdapter adapterfotobelakang;

    ImageView tombolfotodepan;
    ImageView tombolfotokiri;
    ImageView tombolfotokanan;
    ImageView tombolfotobelakang;

    LinearLayout layoutfotodepan;
    LinearLayout layoutfotokiri;
    LinearLayout layoutfotokanan;
    LinearLayout layoutfotobelakang;

    TextView textcountdepan;
    TextView textcountkiri;
    TextView textcountkanan;
    TextView textcountbelakang;

    LinearLayout tombollanjut;
    LinearLayout tombolprev;

    int MAXFOTO = 100;

    Uri uriimage;
    Uri[] imageuridepan = new Uri[MAXFOTO];
    Uri[] imageurikiri = new Uri[MAXFOTO];
    Uri[] imageurikanan = new Uri[MAXFOTO];
    Uri[] imageuribelakang = new Uri[MAXFOTO];

    SharedPreferences datalistsurvey;
    SharedPreferences datainputsurveymasuk;
    SharedPreferences.Editor datainputsurveymasuk_edit;

    VariableText var = new VariableText();

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    int CODE_DEPAN = 100;
    int CODE_KIRI = 200;
    int CODE_KANAN = 300;
    int CODE_BELAKANG = 400;

    LinearLayout layoutmessage;
    LinearLayout tomboloke;
    ImageView imagemessage;
    TextView textmessage;
    TextView texttombol;

    public UploadFotoFragment2(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline,
            LinearLayout layoutmessage,
            LinearLayout tomboloke,
            ImageView imagemessage,
            TextView textmessage,
            TextView texttombol
    ) {
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
        this.layoutmessage = layoutmessage;
        this.tomboloke = tomboloke;
        this.imagemessage = imagemessage;
        this.textmessage = textmessage;
        this.texttombol = texttombol;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View item = inflater.inflate(R.layout.fragment_upload_foto2, container, false);
        context = container.getContext();
        activity = getActivity();
        fragment = (FragmentActivity) context;

        datalistsurvey = context.getSharedPreferences("datalistsurvey", Context.MODE_PRIVATE);

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

        tombolfotodepan = item.findViewById(R.id.tombolfotodepan);
        tombolfotokiri = item.findViewById(R.id.tombolfotokiri);
        tombolfotokanan = item.findViewById(R.id.tombolfotokanan);
        tombolfotobelakang = item.findViewById(R.id.tombolfotobelakang);

        layoutfotodepan = item.findViewById(R.id.layoutfotodepan);
        layoutfotokiri = item.findViewById(R.id.layoutfotokiri);
        layoutfotokanan = item.findViewById(R.id.layoutfotokanan);
        layoutfotobelakang = item.findViewById(R.id.layoutfotobelakang);

        listfotodepan = item.findViewById(R.id.listfotodepan);
        listfotokiri = item.findViewById(R.id.listfotokiri);
        listfotokanan = item.findViewById(R.id.listfotokanan);
        listfotobelakang = item.findViewById(R.id.listfotobelakang);

        textcountdepan = item.findViewById(R.id.textcountdepan);
        textcountkiri = item.findViewById(R.id.textcountkiri);
        textcountkanan = item.findViewById(R.id.textcountkanan);
        textcountbelakang = item.findViewById(R.id.textcountbelakang);

        tombollanjut = item.findViewById(R.id.tombollanjut);
        tombolprev = item.findViewById(R.id.tombolprev);

        listfotodepan.setHasFixedSize(true);
        listfotokiri.setHasFixedSize(true);
        listfotokanan.setHasFixedSize(true);
        listfotobelakang.setHasFixedSize(true);

        listfotodepan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listfotokiri.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listfotokanan.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        listfotobelakang.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        listdepan = new ArrayList<>();
        listkiri = new ArrayList<>();
        listkanan = new ArrayList<>();
        listbelakang = new ArrayList<>();

        loaddata();
        loaddata_damage();

        adapterfotodepan = new ListFotoDepanAdapter(fragment, context, listdepan, textlist_dmg, listfotodepan, textcountdepan);
        adapterfotokiri = new ListFotoDepanAdapter(fragment, context, listkiri, textlist_dmg, listfotokiri, textcountkiri);
        adapterfotokanan = new ListFotoDepanAdapter(fragment, context, listkanan, textlist_dmg, listfotokanan, textcountkanan);
        adapterfotobelakang = new ListFotoDepanAdapter(fragment, context, listbelakang, textlist_dmg, listfotobelakang, textcountbelakang);

        listfotodepan.setAdapter(adapterfotodepan);
        listfotokiri.setAdapter(adapterfotokiri);
        listfotokanan.setAdapter(adapterfotokanan);
        listfotobelakang.setAdapter(adapterfotobelakang);

        tombolfotodepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencamera(CODE_DEPAN);
            }
        });

        tombolfotokiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencamera(CODE_KIRI);
            }
        });

        tombolfotokanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencamera(CODE_KANAN);
            }
        });

        tombolfotobelakang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opencamera(CODE_BELAKANG);
            }
        });

        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata(true);
            }
        });

        tombolprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata(false);
            }
        });

        return item;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CODE_DEPAN){
                try{
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage), (float) 0.05);
                    listdepan.add(new ListFotoDepanModel(uriimage, bitmap, 0, "", ""));
                    adapterfotodepan.notifyDataSetChanged();
                    layoutfotodepan.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Log.d("RESPONSE", "TRY gagal : "+e);
                }
            }else

            if(requestCode == CODE_KIRI){
                try{
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage), (float) 0.05);
                    listkiri.add(new ListFotoDepanModel(uriimage, bitmap, 0, "", ""));
                    adapterfotokiri.notifyDataSetChanged();
                    layoutfotokiri.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Log.d("RESPONSE", "TRY gagal : "+e);
                }
            }else

            if(requestCode == CODE_KANAN){
                try{
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage), (float) 0.05);
                    listkanan.add(new ListFotoDepanModel(uriimage, bitmap, 0, "", ""));
                    adapterfotokanan.notifyDataSetChanged();
                    layoutfotokanan.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Log.d("RESPONSE", "TRY gagal : "+e);
                }
            }else

            if(requestCode == CODE_BELAKANG){
                try{
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriimage), (float) 0.05);
                    listbelakang.add(new ListFotoDepanModel(uriimage, bitmap, 0, "", ""));
                    adapterfotobelakang.notifyDataSetChanged();
                    layoutfotobelakang.setVisibility(View.VISIBLE);
                }catch (Exception e){
                    Log.d("RESPONSE", "TRY gagal : "+e);
                }
            }
        }else{
            Log.d("RESPONSE", "GAGAL GET IMAGE CAMERA");
        }
    }

    public void savedata(boolean next){
        JSONArray arrayfotodepan = new JSONArray();
        JSONArray arrayfotokiri = new JSONArray();
        JSONArray arrayfotokanan = new JSONArray();

        JSONArray arraydmgdepan = new JSONArray();
        JSONArray arraydmgkiri = new JSONArray();
        JSONArray arraydmgkanan = new JSONArray();

        JSONArray arraycodedmgdepan = new JSONArray();
        JSONArray arraycodedmgkiri = new JSONArray();
        JSONArray arraycodedmgkanan = new JSONArray();

        JSONArray arrayurldepan = new JSONArray();
        JSONArray arrayurlkiri = new JSONArray();
        JSONArray arrayurlkanan = new JSONArray();

        JSONObject objectdepan = new JSONObject();
        JSONObject objectkiri = new JSONObject();
        JSONObject objectkanan = new JSONObject();

        // == SAVE DATA FOTO DEPAN == //
        try{
            for(int i=0; i < listdepan.size(); i++){
                arrayfotodepan.put(listdepan.get(i).getUriimage());
                arraydmgdepan.put(listdepan.get(i).getPosition_dmg());
                arraycodedmgdepan.put(listdepan.get(i).getCodedmg());
                arrayurldepan.put(listdepan.get(i).getUrl());
            }

            objectdepan.put("fotodepan", arrayfotodepan);
            objectdepan.put("dmgdepan", arraydmgdepan);
            objectdepan.put("codedmgdepan", arraycodedmgdepan);
            objectdepan.put("urldepan", arrayurldepan);
        }catch (Exception e){
            Log.d("RESPONSE", "GAGAL SAVE DATA");
        }

        // == SAVE DATA FOTO KIRI == //
        try{
            for(int i=0; i < listkiri.size(); i++){
                arrayfotokiri.put(listkiri.get(i).getUriimage());
                arraydmgkiri.put(listkiri.get(i).getPosition_dmg());
                arraycodedmgkiri.put(listkiri.get(i).getCodedmg());
                arrayurlkiri.put(listkiri.get(i).getUrl());
            }

            objectkiri.put("fotokiri", arrayfotokiri);
            objectkiri.put("dmgkiri", arraydmgkiri);
            objectkiri.put("codedmgkiri", arraycodedmgkiri);
            objectkiri.put("urlkiri", arrayurlkiri);
        }catch (Exception e){
            Log.d("RESPONSE", "GAGAL SAVE DATA");
        }

        // == SAVE DATA FOTO KANAN == //
        try{
            for(int i=0; i < listkanan.size(); i++){
                arrayfotokanan.put(listkanan.get(i).getUriimage());
                arraydmgkanan.put(listkanan.get(i).getPosition_dmg());
                arraycodedmgkanan.put(listkanan.get(i).getCodedmg());
                arrayurlkanan.put(listkanan.get(i).getUrl());
            }

            objectkanan.put("fotokanan", arrayfotokanan);
            objectkanan.put("dmgkanan", arraydmgkanan);
            objectkanan.put("codedmgkanan", arraycodedmgkanan);
            objectkanan.put("urlkanan", arrayurlkanan);
        }catch (Exception e){
            Log.d("RESPONSE", "GAGAL SAVE DATA");
        }

        datainputsurveymasuk_edit = context.getSharedPreferences("Inputsurveymasuk", Context.MODE_PRIVATE).edit();
        datainputsurveymasuk_edit.putString("imagedepan", objectdepan.toString());
        datainputsurveymasuk_edit.putString("imagekiri", objectkiri.toString());
        datainputsurveymasuk_edit.putString("imagekanan", objectkanan.toString());
        datainputsurveymasuk_edit.commit();

        if(next){
            nextfragment();
        }else{
            prevfragment();
        }
    }

    public void loaddata(){
        datainputsurveymasuk = context.getSharedPreferences("Inputsurveymasuk", Context.MODE_PRIVATE);
        String valueimagedepan = datainputsurveymasuk.getString("imagedepan","");
        String valueimagekiri = datainputsurveymasuk.getString("imagekiri","");
        String valueimagekanan = datainputsurveymasuk.getString("imagekanan","");

        try {
            if (!valueimagedepan.equals("")) {
                JSONObject objectfotodepan = new JSONObject(valueimagedepan);
                JSONArray arrayfotodepan = objectfotodepan.getJSONArray("fotodepan");
                JSONArray arraydmgdepan = objectfotodepan.getJSONArray("dmgdepan");
                JSONArray arraycodedmgdepan = objectfotodepan.getJSONArray("codedmgdepan");
                JSONArray arrayurldepan = objectfotodepan.getJSONArray("urldepan");

                for (int i = 0; i < arrayfotodepan.length(); i++) {
                    String value = arrayfotodepan.getString(i);
                    String url = arrayurldepan.getString(i);
                    int dmg_depan = arraydmgdepan.getInt(i);
                    String codedmg = arraycodedmgdepan.getString(i);

                    if (!value.equals("null")) {
                        Uri contentURI = Uri.parse(value);
                        Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                        listdepan.add(new ListFotoDepanModel(contentURI, bitmap, dmg_depan, codedmg, ""));
                    }else
                        if(!url.equals("")){
                            listdepan.add(new ListFotoDepanModel(null, null, 0, codedmg, url));
                        }
                }
            }
        }catch (Exception e) {
            Log.d("RESPONSE", "depan gagal : "+e);
        }

        try{
            if(!valueimagekiri.equals("")){
                JSONObject objectfotokiri = new JSONObject(valueimagekiri);
                JSONArray arrayfotokiri = objectfotokiri.getJSONArray("fotokiri");
                JSONArray arraydmgkiri = objectfotokiri.getJSONArray("dmgkiri");
                JSONArray arraycodedmgkiri = objectfotokiri.getJSONArray("codedmgkiri");
                JSONArray arrayurlkiri = objectfotokiri.getJSONArray("urlkiri");

                for(int i=0; i < arrayfotokiri.length(); i++){
                    String value = arrayfotokiri.getString(i);
                    String url = arrayurlkiri.getString(i);
                    int dmg = arraydmgkiri.getInt(i);
                    String codedmg = arraycodedmgkiri.getString(i);

                    if(!value.equals("null")){
                        Uri contentURI = Uri.parse(value);
                        Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                        listkiri.add(new ListFotoDepanModel(contentURI, bitmap, dmg, codedmg, ""));
                    }else
                        if(!url.equals("")){
                            listkiri.add(new ListFotoDepanModel(null, null, 0, codedmg, url));
                        }
                }
            }

            try {
                if (!valueimagekanan.equals("")) {
                    JSONObject objectfotokanan = new JSONObject(valueimagekanan);
                    JSONArray arrayfotokanan = objectfotokanan.getJSONArray("fotokanan");
                    JSONArray arraydmgkanan = objectfotokanan.getJSONArray("dmgkanan");
                    JSONArray arraycodedmgkanan = objectfotokanan.getJSONArray("codedmgkanan");
                    JSONArray arrayurlkanan = objectfotokanan.getJSONArray("urlkanan");

                    for (int i = 0; i < arrayfotokanan.length(); i++) {
                        String value = arrayfotokanan.getString(i);
                        String url = arrayurlkanan.getString(i);
                        int dmg = arraydmgkanan.getInt(i);
                        String codedmg = arraycodedmgkanan.getString(i);

                        if (!value.equals("null")) {
                            Uri contentURI = Uri.parse(value);
                            Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                            listkanan.add(new ListFotoDepanModel(contentURI, bitmap, dmg, codedmg, ""));
                        }else
                            if(!url.equals("")){
                                listkanan.add(new ListFotoDepanModel(null, null, 0, codedmg, url));
                            }
                    }
                }
            }catch (Exception e) {

            }

            listfotodepan.smoothScrollToPosition(listdepan.size());
            listfotokiri.smoothScrollToPosition(listkiri.size());
            listfotokanan.smoothScrollToPosition(listkanan.size());
        }catch (Exception e){
            Log.d("RESPONSE", "Gagal load : "+e);
        }
    }

    public void nextfragment(){
//        Fragment fragment = new ConfirmSurveyinFragment(
//                viewv2,
//                viewstep,
//                viewline,
//                layoutmessage,
//                tomboloke,
//                imagemessage,
//                textmessage,
//                texttombol
//        );
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_left_start, R.anim.slide_left_end)
//                .replace(R.id.layoutfragment, fragment)
//                .commit();
    }

    public void prevfragment(){
//        Fragment fragment = new DeskripsiSurveyFragment(
//                viewv2,
//                viewstep,
//                viewline,
//                layoutmessage,
//                tomboloke,
//                imagemessage,
//                textmessage,
//                texttombol
//        );
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_right_start, R.anim.slide_right_end)
//                .replace(R.id.layoutfragment, fragment)
//                .commit();
    }

    public void opencamera(int code){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        uriimage = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, uriimage);
        ((Activity) context).startActivityForResult(cameraintent, code);
    }



    public void loaddata_damage(){
        String data = datalistsurvey.getString("damage", "");
        textlist_dmg.add("- Pilih dmg");

        if(!data.equals("")){
            try{
                JSONArray arrayowner = new JSONArray(data);
                for(int i=0; i<arrayowner.length(); i++){
                    JSONObject objdata = arrayowner.getJSONObject(i);
                    String value = objdata.getString("dmg_code");
                    textlist_dmg.add(value);
                }

            }catch (Exception e){
                Log.d("RESPONSE error", "res : "+String.valueOf(e));
            }
        }
    }
}
