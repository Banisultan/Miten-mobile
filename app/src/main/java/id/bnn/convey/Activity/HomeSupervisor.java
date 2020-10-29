package id.bnn.convey.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.ListContainerVipAdapter;
import id.bnn.convey.FinalActivity.SurveyinAddActivity;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.Model.ListContainerModel;
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

public class HomeSupervisor extends AppCompatActivity {

    Context context;
    List<ListContainerModel> listcontainer;
    RecyclerView viewcontainer;
    ListContainerVipAdapter listcontaineradapter;

    LinearLayout tombolsurvei;
    ScrollView scrollview;
    ImageView imagelogo;
    TextView textnama;

    RelativeLayout layoutsearch;
    EditText inputsearch;
    ImageView tombolsearch;
    ImageView tombolbatal;
    ImageView tombolscanqr;


    VariableText var = new VariableText();
    VariableParam par = new VariableParam();
    VariableRequest req = new VariableRequest();

    SharedPreferences dataakun;

    String namaakun;
    String idakun;
    String group;

    SwipeRefreshLayout swiperefresh;
    IntentIntegrator scannerview;

    LinearLayout layoutprogres;

    ProgressDialog a;

    public void sss(){
        a = new ProgressDialog(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_supervisor);


        context = this;
        scannerview = new IntentIntegrator(this);

        dataakun = getSharedPreferences("dataakun", MODE_PRIVATE);
        namaakun = dataakun.getString("nama", "");
        idakun = dataakun.getString("idakun", "");
        group = dataakun.getString("group", "");

        textnama = findViewById(R.id.textnama);

        layoutprogres = findViewById(R.id.layoutprogres);
        imagelogo = findViewById(R.id.imagelogo);
        scrollview = findViewById(R.id.scrollview);
        tombolsurvei = findViewById(R.id.tombolsurvei);
        viewcontainer = findViewById(R.id.viewcontainer);
        swiperefresh = findViewById(R.id.swiperefresh);
        inputsearch = findViewById(R.id.inputsearch);
        layoutsearch = findViewById(R.id.layoutsearch);

        tombolsearch = findViewById(R.id.tombolsearch);
        tombolbatal = findViewById(R.id.tombolbatal);
        tombolscanqr = findViewById(R.id.tombolscanqr);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                actionrefresh();
            }
        });

        viewcontainer.setHasFixedSize(true);
        viewcontainer.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically(){
                return false;
            }
        });

        scrollview.fullScroll(ScrollView.FOCUS_UP);
        scrollview.smoothScrollTo(0,0);

        tombolsurvei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences datainputsurveymasuk = getSharedPreferences("Inputsurveymasuk", MODE_PRIVATE);
                datainputsurveymasuk.edit().clear().commit();
                moveactivitiy(new Intent(context, SurveyinAddActivity.class));
            }
        });

        tombolsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionprogress();
                String container = inputsearch.getText().toString().trim();
                getAPI_searchsurvey(idakun, group, container);
            }
        });

        tombolbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionprogress();
                inputsearch.setText("");
                getAPI_listsurvey(idakun, group);
                tombolbatal.setVisibility(View.GONE);
            }
        });

        tombolscanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerview.initiateScan();
            }
        });

        getAPI_listsurvey(idakun, group);
        textnama.setText(namaakun);
    }

    @Override
    public void onActivityResult(int requestcode, int resultcode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestcode, resultcode, data);
        if(result != null){
            if(result.getContents() != null){
                actionprogress();
                inputsearch.setText(result.getContents());
                getAPI_searchsurvey(idakun, group, inputsearch.getText().toString().trim());
            }
        }else{
            super.onActivityResult(requestcode, resultcode, data);
        }
    }

    public void moveactivitiy(Intent intent){
        startActivity(intent);
    }

    public void actionprogress(){
        layoutprogres.setVisibility(View.VISIBLE);
        layoutsearch.setBackgroundResource(R.drawable.bg_shape_searching);
    }

    public void actionnotprogress(){
        layoutprogres.setVisibility(View.GONE);
        layoutsearch.setBackgroundResource(R.drawable.bg_shape);
    }

    public void actionactive(){

    }

    public void actionnonactive(){
        swiperefresh.setRefreshing(false);
        actionnotprogress();
    }

    public void getAPI_listsurvey(String idakun, String group){
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
        call = interfaceApi.getapi_listsurvey(par.PARAM_LIST_SURVEY_ALL(idakun, group, "admin"));
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
                            showdata(objdata);
                        }else{
                            actionnonactive();
                        }

                    }catch (Exception e){
                        actionnonactive();
                    }
                }else{
                    actionnonactive();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                actionnonactive();
            }
        });
    }

    public void getAPI_searchsurvey(String idakun, String group, String container){
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
        call = interfaceApi.getapi_searchsurvey(par.PARAM_SEARCH_SURVEY(idakun, group, "admin", container));
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
                            showdata(objdata);
                            tombolbatal.setVisibility(View.VISIBLE);
                        }else{
                            actionnonactive();
                        }

                    }catch (Exception e){
                        actionnonactive();
                    }
                }else{
                    actionnonactive();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                actionnonactive();
            }
        });
    }

    public void showdata(JSONObject objdata){
        try{
            JSONArray arraydata = objdata.getJSONArray("listsurvey");
            listcontainer = new ArrayList<>();

            for(int i=0; i< arraydata.length(); i++){
                JSONObject obj = (JSONObject) arraydata.get(i);
                String idsurvei = obj.getString("sin_id");
                String nomor = obj.getString("sin_container");
                String status = obj.getString("sin_status");
                String waktu = obj.getString("sin_updated");
                String kondisi = obj.getString("sin_condition");
                String keterangan = obj.getString("keterangan");

                listcontainer.add(new ListContainerModel(idsurvei, nomor, status, waktu, kondisi, keterangan, ""));
            }

            listcontaineradapter = new ListContainerVipAdapter(context, listcontainer);
            viewcontainer.setAdapter(listcontaineradapter);
        }catch (Exception e){

        }
        actionnonactive();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    public void actionrefresh(){
        getAPI_listsurvey(idakun, group);
    }
}
