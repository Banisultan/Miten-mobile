package id.bnn.convey.FinalActivity.HomeFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.bnn.convey.Activity.LoginActivity;
import id.bnn.convey.Adapter.KategoriAdapter;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.Model.KategoriModel;
import id.bnn.convey.R;
import id.bnn.convey.VariableParam;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import id.bnn.convey.VariableVoid;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends Fragment {

    Context context;

    TextView texthari;
    TextView textwaktu;
    TextView textdate;
    TextView textnama;
    TextView textupdatedate;
    RecyclerView viewkategori;
    SwipeRefreshLayout refreshLayout;

    List<KategoriModel> list = new ArrayList<>();
    KategoriAdapter adapter;

    VariableText var = new VariableText();
    VariableRequest req = new VariableRequest();
    VariableParam par = new VariableParam();
    VariableVoid vod;

    SharedPreferences.Editor datauser_edit;
    SharedPreferences datauser;
    String USERID;
    String TOKEN;

    SharedPreferences dataakun;
    SharedPreferences datalist;
    SharedPreferences.Editor dataakun_edit;
    SharedPreferences.Editor datalist_edit;
    SharedPreferences.Editor datalistitem_edit;
    String IDAKUN;
    String NAMA;
    String GROUP;

    TextView textwaiting;
    TextView textdone;

    ImageView imagelogo;
    ImageView imagebg;
    ImageView imageiconleft;
    ImageView imageiconright;


    public HomeFragment() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
        refreshLayout.setEnabled(false);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_home, container, false);
        context = container.getContext();

        imagelogo = itemview.findViewById(R.id.imagelogo);
        imagebg = itemview.findViewById(R.id.imagebg);
        imageiconleft = itemview.findViewById(R.id.imageleft);
        imageiconright = itemview.findViewById(R.id.imageright);

        Glide.with(context).load(R.drawable.img_logo_home).into(imagelogo);
        Glide.with(context).load(R.drawable.img_bg).into(imagebg);
        Glide.with(context).load(R.drawable.img_write).into(imageiconleft);
        Glide.with(context).load(R.drawable.img_write).into(imageiconright);

        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");

        vod = new VariableVoid(context, USERID, TOKEN);

        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        datalist = context.getSharedPreferences("datalist", Context.MODE_PRIVATE);
        IDAKUN = dataakun.getString("idakun", "");
        NAMA = dataakun.getString("nama", "");
        GROUP = dataakun.getString("group", "");

        textwaiting = itemview.findViewById(R.id.textwaiting);
        textdone = itemview.findViewById(R.id.textdone);

        textnama = itemview.findViewById(R.id.textnama);
        textnama.setText("Hai, "+NAMA);

        textupdatedate = itemview.findViewById(R.id.textupdatedate);

        viewkategori = itemview.findViewById(R.id.viewkategori);
        viewkategori.setHasFixedSize(true);
        viewkategori.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        refreshLayout = itemview.findViewById(R.id.refreshhome);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loaddata();
            }
        });

        texthari = itemview.findViewById(R.id.texthari);
        textdate = itemview.findViewById(R.id.textdate);
        textwaktu = itemview.findViewById(R.id.textwaktu);

        loaddata();
        return itemview;
    }

    public void loaddata(){
        refreshLayout.setEnabled(true);
        callAPI_listkategori();
        vod.callAPI_owner();
        vod.callAPI_listposisi();
        vod.callAPI_listwash();
        vod.callAPI_listgrade();
    }

    public void next(){
        getAPI_getProfile(IDAKUN, GROUP);
    }

    public void savekategori(JsonArray dataarray){
        datalist_edit = context.getSharedPreferences("datalist", Context.MODE_PRIVATE).edit();
        datalist_edit.putString("datakategori", String.valueOf(dataarray));
        datalist_edit.apply();

        loadkategori();
    }

    public void loadkategori(){
        list = new ArrayList<>();
        JsonArray dataarray = new JsonParser().parse(datalist.getString("datakategori", "[]")).getAsJsonArray();
        for(int i=0; i<dataarray.size(); i++){
            JsonObject datajson = dataarray.get(i).getAsJsonObject();
            list.add(new KategoriModel(
                    datajson.get("id").getAsInt(),
                    datajson.get("nama").getAsString(),
                    datajson.get("image").getAsString()
            ));
        }


        next();
        adapter = new KategoriAdapter(context, list);
        viewkategori.setAdapter(adapter);
    }

    public void hideerefresh(){
        refreshLayout.setEnabled(false);
        refreshLayout.setEnabled(true);
        refreshLayout.setRefreshing(false);
    }

    public void saveprofile(JSONObject datajson){
        datauser_edit = context.getSharedPreferences("datauser", Context.MODE_PRIVATE).edit();
        dataakun_edit = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE).edit();

        try {
            JSONObject data = datajson.getJSONObject("data");
            String nama = data.getString("nama");
            String idakun = data.getString("idakun");
            String group = data.getString("group");
            String tipe = data.getString("tipe");

            dataakun_edit.putString("nama", nama);
            dataakun_edit.putString("idakun", idakun);
            dataakun_edit.putString("group", group);
            dataakun_edit.putString("tipe", tipe);

            datauser_edit.putString("nama", nama);
            datauser_edit.putString("userid", idakun);
            datauser_edit.putString("tipe", tipe);
            datauser_edit.apply();

        }catch (Exception e){

        }

        hideerefresh();
    }

    public void savecount(JSONObject datajson){
        try{
            datalist_edit.putString("count", String.valueOf(datajson));
            datalist_edit.apply();
            loadcount();
        }catch (Exception e){
            Log.d("TAGGAR", "ini savecount"+e);
        }
    }

    public void loadcount(){
        String data = datalist.getString("count", "");
        if(!data.equals("")){
            try{
                JSONObject datajson = new JSONObject(data);
                String waiting = datajson.getString("waiting");
                String done = datajson.getString("done");
                String date = datajson.getString("datetext");

                textupdatedate.setText(date);
                textwaiting.setText(waiting);
                textdone.setText(done);

            }catch (Exception e){

            }
        }
    }

    public void callAPI_listkategori(){
        JsonArray dataarray = new JsonArray();

        JsonObject datajson_1 = new JsonObject();
        datajson_1.addProperty("title", "Authorization");
        datajson_1.addProperty("value", "Bearer "+TOKEN);

        dataarray.add(datajson_1);

        OkHttpClient.Builder httpClient = new VariableRequest().setHTTPCLIENT(dataarray);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(var.URL_v2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI_v2 interfaceApi = retrofit.create(InterfaceAPI_v2.class);

        JsonObject datajson = new JsonObject();
        datajson.addProperty("mobid", var.createMobID());
        datajson.addProperty("userid", USERID);

        Call<JsonObject> call = interfaceApi.list_kategori(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        savekategori(obj_response.get("data").getAsJsonArray());
                    }else{
                        next();
                    }
                }else{
                    next();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                next();
            }
        });
    }

    public void getAPI_getProfile(String idakun, String group){
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
        call = interfaceApi.getapi_getProfile(par.PARAM_LIST_KATEGORI(idakun, group));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            JSONObject objdata = obj_convert.getJSONObject("resmessage");
                            saveprofile(objdata);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            hideerefresh();
                        }

                    }catch (Exception e){
                        hideerefresh();
                    }
                }else{
                    hideerefresh();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hideerefresh();
            }
        });
    }

    public void getAPI_count(String idakun, String group){
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
        call = interfaceApi.getapi_count(par.PARAM_DEFAULT(idakun, group));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    try{
                        JSONObject obj_convert = new JSONObject(obj_response.toString());
                        String rescode = obj_convert.getString("rescode");

                        if(rescode.equals("0")){
                            JSONObject datajson = obj_convert.getJSONObject("resmessage");
                            savecount(datajson);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                        }

                    }catch (Exception e){

                    }
                }else{

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}
