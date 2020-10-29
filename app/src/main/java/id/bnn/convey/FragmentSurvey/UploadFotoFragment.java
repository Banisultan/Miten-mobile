package id.bnn.convey.FragmentSurvey;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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
import org.w3c.dom.Text;

import java.io.File;
import java.util.Map;

import id.bnn.convey.R;
import id.bnn.convey.VariableText;
import okhttp3.MediaType;


public class UploadFotoFragment extends Fragment {

    Context context;
    ImageView imagedepan1;
    ImageView imagedepan2;
    ImageView imagedepan3;
    ImageView imagekiri1;
    ImageView imagekiri2;
    ImageView imagekiri3;
    ImageView imagekanan1;
    ImageView imagekanan2;
    ImageView imagekanan3;

    ImageView[] imagedepan = {imagedepan1, imagedepan2, imagedepan3};
    ImageView[] imagekiri = {imagekiri1, imagekiri2, imagekiri3};
    ImageView[] imagekanan = {imagekanan1, imagekanan2, imagekanan3};

    int MAXFOTO = 3;

    Uri image;
    Uri[] imageuridepan = new Uri[MAXFOTO];
    Uri[] imageurikiri = new Uri[MAXFOTO];
    Uri[] imageurikanan = new Uri[MAXFOTO];

    Bitmap[] bitmapdepan = new Bitmap[MAXFOTO];

    TextView[] viewstep;
    View[] viewline;

    SharedPreferences datainputsurveymasuk;
    SharedPreferences.Editor datainputsurveymasuk_edit;

    LinearLayout tombollanjut;
    LinearLayout tombolprev;

    VariableText var = new VariableText();

    int URUTAN;
    int CODE_DEPAN = 100;
    int CODE_KIRI = 200;
    int CODE_KANAN = 300;

    boolean REPLACE = false;

    String POSISI_DEPAN = "depan";
    String POSISI_KIRI = "kiri";
    String POSISI_KANAN = "kanan";

    public UploadFotoFragment(
            TextView[] viewstep,
            View[] viewline
    ) {
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        viewstep[2].setBackgroundResource(R.drawable.bg_step_on);
        viewline[2].setBackgroundResource(R.drawable.bg_line_on);

        View itemview = inflater.inflate(R.layout.fragment_upload_foto2, container, false);
        imagedepan[0] = itemview.findViewById(R.id.imagedepan1);
        imagedepan[1] = itemview.findViewById(R.id.imagedepan2);
        imagedepan[2] = itemview.findViewById(R.id.imagedepan3);
        imagekiri[0] = itemview.findViewById(R.id.imagekiri1);
        imagekiri[1] = itemview.findViewById(R.id.imagekiri2);
        imagekiri[2] = itemview.findViewById(R.id.imagekiri3);
        imagekanan[0] = itemview.findViewById(R.id.imagekanan1);
        imagekanan[1] = itemview.findViewById(R.id.imagekanan2);
        imagekanan[2] = itemview.findViewById(R.id.imagekanan3);
        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombolprev = itemview.findViewById(R.id.tombolprev);

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

        imagedepan[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuridepan[0] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_DEPAN, 0);
            }
        });

        imagedepan[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuridepan[1] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_DEPAN, 1);
            }
        });

        imagedepan[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageuridepan[2] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_DEPAN, 2);
            }
        });

        imagekiri[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikiri[0] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KIRI, 0);
            }
        });

        imagekiri[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikiri[1] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KIRI, 1);
            }
        });

        imagekiri[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikiri[2] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KIRI, 2);
            }
        });

        imagekanan[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikanan[0] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KANAN, 0);
            }
        });

        imagekanan[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikanan[1] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KANAN, 1);
            }
        });


        imagekanan[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageurikanan[2] != null){
                    REPLACE = true;
                }
                checkpermission(POSISI_KANAN, 2);
            }
        });


        loaddata();
        return itemview;
    }

    public void checkpermission(String posisi, int urutan){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                String[] permission = {
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                };

                requestPermissions(permission, 1000);
            }else{
                opencamera(posisi, urutan);
            }
        }
    }

    public void opencamera(String posisi, int urutan){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from the camera");

        URUTAN = urutan;
        int code = 0;
        if(posisi.equals(POSISI_DEPAN)){
            code = CODE_DEPAN;
        }else if(posisi.equals(POSISI_KIRI)){
            code = CODE_KIRI;
        }else if(posisi.equals(POSISI_KANAN)){
            code = CODE_KANAN;
        }

        image = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraintent.putExtra(MediaStore.EXTRA_OUTPUT, image);
        startActivityForResult(cameraintent, code);
    }

    Bitmap bitimage;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("RESPONSE", "ini apa "+REPLACE);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CODE_DEPAN) {
                if(REPLACE){
                    imageuridepan[URUTAN] = image;
                    imagedepan[URUTAN].setImageURI(image);
                }else{
                    boolean oke = false;
                    for (int i = 0; i < imageuridepan.length; i++) {
                        if(imageuridepan[i] == null){
                            imageuridepan[i] = image;
                            oke = true;
                            break;
                        }
                    }

                    if(!oke){
                        imageuridepan[URUTAN] = image;
                    }

                    for (int i = 0; i < imageuridepan.length; i++) {
                        if (imageuridepan[i] != null) {
                            imagedepan[i].setImageURI(imageuridepan[i]);
                        }
                    }
                }
            }else if(requestCode == CODE_KIRI) {
                if(REPLACE){
                    imageurikiri[URUTAN] = image;
                    imagekiri[URUTAN].setImageURI(image);
                }else{
                    boolean oke = false;
                    for (int i = 0; i < imageurikiri.length; i++) {
                        if(imageurikiri[i] == null){
                            imageurikiri[i] = image;
                            oke = true;
                            break;
                        }
                    }

                    if(!oke){
                        imageurikiri[URUTAN] = image;
                    }

                    for (int i = 0; i < imageurikiri.length; i++) {
                        if (imageurikiri[i] != null) {
                            imagekiri[i].setImageURI(imageurikiri[i]);
                        }
                    }
                }
            }else if(requestCode == CODE_KANAN) {
                if(REPLACE){
                    imageurikanan[URUTAN] = image;
                    imagekanan[URUTAN].setImageURI(image);
                }else{
                    boolean oke = false;
                    for (int i = 0; i < imageurikanan.length; i++) {
                        if(imageurikanan[i] == null){
                            imageurikanan[i] = image;
                            oke = true;
                            break;
                        }
                    }

                    if(!oke){
                        imageurikanan[URUTAN] = image;
                    }

                    for (int i = 0; i < imageurikanan.length; i++) {
                        if (imageurikanan[i] != null) {
                            imagekanan[i].setImageURI(imageurikanan[i]);
                        }
                    }
                }
            }
        }

        REPLACE = false;

        Log.d("RESPONSE req"+requestCode, "INI res"+resultCode);
    }

    public void prevfragment(){
//        Fragment fragment = new DeskripsiSurveyFragment(viewstep, viewline);
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_right_start, R.anim.slide_right_end)
//                .replace(R.id.layoutfragment, fragment)
//                .commit();
    }

    public void nextfragment(){
//        Fragment fragment = new ConfirmSurveyinFragment(viewstep, viewline);
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_left_start, R.anim.slide_left_end)
//                .replace(R.id.layoutfragment, fragment)
//                .commit();
    }

    public void savedata(boolean next){
        JSONArray arrayfotodepan = new JSONArray();
        JSONArray arrayfotokiri = new JSONArray();
        JSONArray arrayfotokanan = new JSONArray();

        JSONObject objectfotodepan = new JSONObject();
        JSONObject objectfotokiri = new JSONObject();
        JSONObject objectfotokanan = new JSONObject();

        try{
            for(int i = 0; i< MAXFOTO; i++){
                arrayfotodepan.put(imageuridepan[i]);
                arrayfotokiri.put(imageurikiri[i]);
                arrayfotokanan.put(imageurikanan[i]);
            }

            objectfotodepan.put("fotodepan", arrayfotodepan);
            objectfotokiri.put("fotokiri", arrayfotokiri);
            objectfotokanan.put("fotokanan", arrayfotokanan);
        }catch (Exception e){

        }

        datainputsurveymasuk_edit = context.getSharedPreferences("Inputsurveymasuk", Context.MODE_PRIVATE).edit();
        datainputsurveymasuk_edit.putString("imagedepan", objectfotodepan.toString());
        datainputsurveymasuk_edit.putString("imagekiri", objectfotokiri.toString());
        datainputsurveymasuk_edit.putString("imagekanan", objectfotokanan.toString());
        datainputsurveymasuk_edit.commit();

        if(next){
            nextfragment();
        }else{
            prevfragment();
        }
    }

    public void loaddata(){
        datainputsurveymasuk = context.getSharedPreferences("Inputsurveymasuk", Context.MODE_PRIVATE);

        Map<String, ?> sd = datainputsurveymasuk.getAll();
        Log.d("RESPONSE all", "exp : "+String.valueOf(sd));

        String valueimagedepan = datainputsurveymasuk.getString("imagedepan","");
        String valueimagekiri = datainputsurveymasuk.getString("imagekiri","");
        String valueimagekanan = datainputsurveymasuk.getString("imagekanan","");

        JSONArray arrayfotodepan = new JSONArray();
        JSONArray arrayfotokiri = new JSONArray();
        JSONArray arrayfotokanan = new JSONArray();
        try {
            JSONObject objectfotodepan = new JSONObject(valueimagedepan);
            JSONObject objectfotokiri = new JSONObject(valueimagekiri);
            JSONObject objectfotokanan = new JSONObject(valueimagekanan);

            arrayfotodepan = objectfotodepan.getJSONArray("fotodepan");
            arrayfotokiri = objectfotokiri.getJSONArray("fotokiri");
            arrayfotokanan = objectfotokanan.getJSONArray("fotokanan");

            for(int i=0; i < MAXFOTO; i++){
                String value_depan = arrayfotodepan.getString(i);
                if(!value_depan.equals("null")){
                    Uri contentURI = Uri.parse(value_depan);
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                    imagedepan[i].setImageBitmap(bitmap);
                    imageuridepan[i] = Uri.parse(value_depan);
                }

                String value_kiri = arrayfotokiri.getString(i);
                if(!value_kiri.equals("null")){
                    Uri contentURI = Uri.parse(value_kiri);
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                    imagekiri[i].setImageBitmap(bitmap);
                    imageurikiri[i] = Uri.parse(value_kiri);
                }

                String value_kanan = arrayfotokanan.getString(i);
                if(!value_kanan.equals("null")){
                    Uri contentURI = Uri.parse(value_kiri);
                    Bitmap bitmap = var.BitmapCompress(MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI), (float) 0.05);
                    imagekanan[i].setImageBitmap(bitmap);
                    imageurikanan[i] = Uri.parse(value_kanan);
                }
            }
        }catch (Exception e){
            Log.d("RESPONSE gagal", "haha "+String.valueOf(e));
        }



    }


}
