package id.bnn.convey;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Model.FileNameModel;
import id.bnn.convey.Test.ProgressRequestBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VariableParam {

    VariableText var = new VariableText();

    // PARAM TO CALL API LOGIN
    public JsonObject PARAM_LOGIN(String idakun, String password, String group, String username, String role){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("username", username);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("role", role);
        return jsonObject;
    }

    // PARAM TO CALL API LOGIN
    public MultipartBody.Part[] PARAM_IDENTITY(Context context, Bitmap bitmap){
        MultipartBody.Part[] bodyvalue = null;
        List<MultipartBody.Part> body = new ArrayList<>();

        // === IMAGE CONTAINER === //
        try{
            File filecontainer = var.createTempFile(context, var.BitmapCompressFace(bitmap, (float) 0.1));
            RequestBody requestcontainer = RequestBody.create(MediaType.parse("image/*"), filecontainer);
            body.add(MultipartBody.Part.createFormData("fotoface", filecontainer.getName(), requestcontainer));
        }catch (Exception e){
            Log.d("RESPONSE", "bitmap error "+String.valueOf(e));
        }

        bodyvalue = new MultipartBody.Part[body.size()];
        body.toArray(bodyvalue);

        return bodyvalue;
    }

    // PARAM TO CALL API LOGIN
    public JsonObject PARAM_VIEW_SURVEY(String idakun, String group, String idsurvey){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("idsurvey", idsurvey);
        return jsonObject;
    }

    // PARAM TO CALL API APPROVE SURVEI
    public JsonObject PARAM_APPROVE_SURVEI(String idakun, String group, String idsurvei){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("idsurvei", idsurvei);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_LIST_SURVEY_ALL(String idakun, String group, String tipe){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("tipe", tipe);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_SEARCH_SURVEY(String idakun, String group, String tipe, String container){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("tipe", tipe);
        jsonObject.addProperty("container", container);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_LIST_SURVEY(String idakun, String group, int page){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("page", page);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_SURVEY_IN_SEARCH(String idakun, String group, String nomor){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("nomorcontainer", nomor);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_SURVEY_IN_CEK_NOMOR_CONTAINER(String idakun, String group, String nomor){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        jsonObject.addProperty("nocontainer", nomor);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_DEFAULT(String idakun, String group){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_VALIDATION_SALDO(String idakun, String group){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        return jsonObject;
    }

    // PARAM TO CALL API SURVEY IN
    public JsonObject PARAM_LIST_KATEGORI(String idakun, String group){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("idakun", idakun);
        jsonObject.addProperty("group", group);
        return jsonObject;
    }

    // PARAM TO CALL API INSERT SURVEY IN
    public List<FileNameModel> PARAM_INSERT_FOTO_SURVEY_IN(
            Context context,
            String data,
            String datadetail,
            String datafoto,
            ArrayList<String> listfoto
    )
    {
        List<FileNameModel> filebody = new ArrayList<>();

        // === IMAGE SURVEY IN === //
        try{
            JSONObject datajson = new JSONObject(data);

            String text_file_num = datajson.getString("file_no_container");
            if(!text_file_num.equals("null")){
                File file_num = new File(text_file_num);
                listfoto.add(text_file_num);
                Bitmap bitmapcheck_num = BitmapFactory.decodeFile(file_num.getPath());
                bitmapcheck_num = var.modifyOrientation(bitmapcheck_num, file_num.getPath());

                float size_num = 1;
                if(var.byteSizeOfv2(bitmapcheck_num) / 1024 > 1000){
                    size_num = 0.2f;
                }
                Bitmap bitmap_num = var.BitmapCompress(bitmapcheck_num, size_num);
                File file_in_num = var.createTempFile(context, bitmap_num);
                listfoto.add(file_in_num.getPath());
                filebody.add(new FileNameModel("foto_container", file_in_num));
            }

            String text_file_csc = datajson.getString("file_csc");
            if(!text_file_csc.equals("null")){
                File file_csc = new File(text_file_csc);
                listfoto.add(text_file_csc);
                Bitmap bitmapcheck_csc = BitmapFactory.decodeFile(file_csc.getPath());
                bitmapcheck_csc = var.modifyOrientation(bitmapcheck_csc, file_csc.getPath());
                float size_csc = 1;
                if(var.byteSizeOfv2(bitmapcheck_csc) / 1024 > 1000){
                    size_csc = 0.2f;
                }

                Bitmap bitmap_csc = var.BitmapCompress(bitmapcheck_csc, size_csc);
                File file_in_csc = var.createTempFile(context, bitmap_csc);
                listfoto.add(file_in_csc.getPath());
                filebody.add(new FileNameModel("foto_csc", file_in_csc));
            }


        }catch (Exception e){
            Log.d("TAGGAR", "foto error "+e);
        }

        // === IMAGE CONTAINER === //
        try{
            if(!datafoto.equals("")){
                JSONArray dataarray = new JSONArray(datafoto);
                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    String posisi = datajson.getString("posisi");

                    JSONArray dataarray_foto = datajson.getJSONArray("data");
                    for(int x=0; x < dataarray_foto.length(); x++){
                        JSONObject datajsonphoto = dataarray_foto.getJSONObject(x);

                        String file_image = datajsonphoto.getString("file_image");
                        listfoto.add(file_image);
                        if(!file_image.equals("null")){
                            File file = new File(file_image);
                            Bitmap bitmapcheck = BitmapFactory.decodeFile(file.getPath());
                            bitmapcheck = var.modifyOrientation(bitmapcheck, file.getPath());
                            float size = 1;
                            if(var.byteSizeOfv2(bitmapcheck) / 1024 > 1000){
                                size = 0.25f;
                            }

                            Bitmap bitmap = var.BitmapCompress(bitmapcheck, size);
                            File file_in = var.createTempFile(context, bitmap);
                            listfoto.add(file_in.getPath());
                            filebody.add(new FileNameModel(posisi, file_in));
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR", "data foto error "+e);
        }

        // === IMAGE SURVEY IN === //
        try{
            if(!datadetail.equals("")){
                JSONArray dataarray = new JSONArray(datadetail);
                for(int i =0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);

                    JSONArray dataarray_foto = datajson.getJSONArray("foto");
                    for(int j=0; j < dataarray_foto.length(); j++){
                        JSONObject datajson_foto = dataarray_foto.getJSONObject(j);

                        String id_foto = datajson_foto.getString("id_foto");

                        if(id_foto.equals("null")){
                            String directori = datajson_foto.getString("directory");
                            listfoto.add(directori);
                            if(!directori.equals("null")){
                                File file = new File(datajson_foto.getString("directory"));
                                Bitmap bitmapcheck = BitmapFactory.decodeFile(file.getPath());
                                bitmapcheck = var.modifyOrientation(bitmapcheck, file.getPath());

                                float size = 1;
                                if(var.byteSizeOfv2(bitmapcheck) / 1024 > 1000){
                                    size = 0.25f;
                                }

                                Bitmap bitmap = var.BitmapCompress(bitmapcheck, size);
                                File file_in = var.createTempFile(context, bitmap);
                                listfoto.add(file_in.getPath());
                                filebody.add(new FileNameModel(datajson.getString("id"), file_in));
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            Log.d("TAGGAR", "foto error detail"+e);
        }
        return filebody;
    }

    // PARAM TO CALL API INSERT SURVEY IN
    public MultipartBody.Part[] PARAM_INSERT_FOTO_SURVEY_IN_REPAIR(
            Context context,
            JSONArray dataarray)
    {
        MultipartBody.Part[] bodyvalue = null;
        List<MultipartBody.Part> body = new ArrayList<>();
        // === IMAGE SURVEY IN === //
        try{
            for(int i =0; i < dataarray.length(); i++){
                JSONObject datajson = dataarray.getJSONObject(i);

                String url = datajson.getString("url");
                if(!url.equals("null")){
                    File file = new File(url);
                    Bitmap bitmapcheck = BitmapFactory.decodeFile(file.getPath());
                    bitmapcheck = var.modifyOrientation(bitmapcheck, file.getPath());

                    float size = 1;
                    if(var.byteSizeOfv2(bitmapcheck) / 1024 > 1000){
                        size = 0.25f;
                    }

                    Bitmap bitmap = var.BitmapCompress(bitmapcheck, size);
                    File file_in = var.createTempFile(context, bitmap);

                    RequestBody request = RequestBody.create(MediaType.parse("image/*"), file_in);
                    body.add(MultipartBody.Part.createFormData(datajson.getString("id"), file_in.getName(), request));
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR", "foto error detail"+e);
        }

        bodyvalue = new MultipartBody.Part[body.size()];
        body.toArray(bodyvalue);

        return bodyvalue;
    }

    public List<FileNameModel> PARAM_INSERT_FOTO_SURVEY_IN_REPAIR_V2(
            Context context,
            JSONArray dataarray)
    {
        List<FileNameModel> filemodel = new ArrayList<>();

        Log.d("TAGGAR", "data array "+dataarray);

        // === IMAGE SURVEY IN === //
        try{
            for(int i =0; i < dataarray.length(); i++){
                JSONObject datajson = dataarray.getJSONObject(i);

                String url = datajson.getString("url");
                if(!url.equals("null")){
                    File file = new File(url);
                    Bitmap bitmapcheck = BitmapFactory.decodeFile(file.getPath());

                    float size = 1;
                    if(var.byteSizeOfv2(bitmapcheck) / 1024 > 1000){
                        size = 0.25f;
                    }

                    Bitmap bitmap = var.BitmapCompress(bitmapcheck, size);
                    File file_in = var.createTempFile(context, bitmap);

//                    filemodel.add(new FileNameModel());
                    RequestBody request = RequestBody.create(MediaType.parse("image/*"), file_in);
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR", "foto error detail"+e);
        }

        return filemodel;
    }

    // PARAM TO CALL API INSERT SURVEY IN
    public List<FileNameModel> PARAM_INSERT_FOTO_SURVEY_IN_v2(
            Context context,
            String data,
            String datadetail,
            String datafoto
    )
    {
        List<FileNameModel> filebody = new ArrayList<>();

        // === IMAGE SURVEY IN === //
        try{
            JSONObject datajson = new JSONObject(data);

            String text_file_num = datajson.getString("file_no_container");
            if(!text_file_num.equals("null")){
                File file_num = new File(text_file_num);
                filebody.add(new FileNameModel("foto_container", file_num));
            }

            String text_file_csc = datajson.getString("file_csc");
            if(!text_file_csc.equals("null")){
                File file_csc = new File(text_file_csc);
                filebody.add(new FileNameModel("foto_csc", file_csc));
            }


        }catch (Exception e){
            Log.d("TAGGAR", "foto error "+e);
        }

        // === IMAGE CONTAINER === //
        try{
            if(!datafoto.equals("")){
                JSONArray dataarray = new JSONArray(datafoto);
                for(int i=0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);
                    String posisi = datajson.getString("posisi");

                    JSONArray dataarray_foto = datajson.getJSONArray("data");
                    for(int x=0; x < dataarray_foto.length(); x++){
                        JSONObject datajsonphoto = dataarray_foto.getJSONObject(x);

                        String file_image = datajsonphoto.getString("file_image");
                        if(!file_image.equals("null")){
                            File file = new File(file_image);
                            filebody.add(new FileNameModel(posisi, file));
                        }
                    }
                }
            }
        }catch (Exception e){
            Log.d("TAGGAR", "data foto error "+e);
        }

        // === IMAGE SURVEY IN === //
        try{
            if(!datadetail.equals("")){
                JSONArray dataarray = new JSONArray(datadetail);
                for(int i =0; i < dataarray.length(); i++){
                    JSONObject datajson = dataarray.getJSONObject(i);

                    JSONArray dataarray_foto = datajson.getJSONArray("foto");
                    for(int j=0; j < dataarray_foto.length(); j++){
                        JSONObject datajson_foto = dataarray_foto.getJSONObject(j);

                        String id_foto = datajson_foto.getString("id_foto");

                        if(id_foto.equals("null")){
                            String directori = datajson_foto.getString("directory");
                            if(!directori.equals("null")){
                                File file = new File(datajson_foto.getString("directory"));
                                filebody.add(new FileNameModel(datajson.getString("id"), file));
                            }
                        }
                    }
                }
            }

        }catch (Exception e){
            Log.d("TAGGAR", "foto error detail"+e);
        }
        return filebody;
    }
}
