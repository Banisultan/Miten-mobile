package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.ListContainerVipAdapter;
import id.bnn.convey.Model.ListContainerModel;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SearchActivity extends AppCompatActivity {

    Context context;

    SharedPreferences dataakun;
    String IDAKUN;
    String GROUP;
    String TIPE;

    VariableText var = new VariableText();
    VariableParam par = new VariableParam();
    VariableRequest req = new VariableRequest();

    ImageView tombolcari;
    EditText inputsearch;

    LinearLayout layoutbar;
    TextView textempty;

    RecyclerView view;
    List<ListContainerModel> list;
    ListContainerVipAdapter adapter;

    ImageView tombolkembali;
    TextView texttoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        IDAKUN = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        TIPE = dataakun.getString("tipe", "");

        inputsearch = findViewById(R.id.inputsearch);
        tombolcari = findViewById(R.id.tombolcari);
        tombolcari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startsearch();
            }
        });

        view = findViewById(R.id.view);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));
        view.setVisibility(View.GONE);

        textempty = findViewById(R.id.textempty);
        textempty.setText("Cari Data Survey IN");

        layoutbar = findViewById(R.id.layoutbar);
        layoutbar.setVisibility(View.GONE);

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText("Pencarian Survey IN");
        tombolkembali = findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });
    }

    public void endactivity(){
        finish();
    }

    public void startsearch(){
        layoutbar.setVisibility(View.VISIBLE);
        String text = inputsearch.getText().toString().trim();
        getAPI(text);
    }

    public void datafound(JSONObject datajson){
        layoutbar.setVisibility(View.GONE);
        textempty.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        try {
            JSONObject datajson_obj = datajson.getJSONObject("data");
            String sin_id = datajson_obj.getString("sin_id");
            String sin_container = datajson_obj.getString("sin_container");
            String sin_condition = datajson_obj.getString("sin_condition");
            String sin_created = datajson_obj.getString("sin_created");
            String sin_status = datajson_obj.getString("sin_status");
            String sin_keterangan  = datajson_obj.getString("keterangan");

            list.add(new ListContainerModel(
                    sin_id,
                    sin_container,
                    sin_status,
                    sin_created,
                    sin_condition,
                    sin_keterangan,
                    var.TIPE_DB_WAITING
            ));
        }catch (Exception e){
            Log.d("TAGGAR", "LAH "+e);
        }

        adapter = new ListContainerVipAdapter(context, list);
        view.setAdapter(adapter);
    }

    public void datanotfound(){
        layoutbar.setVisibility(View.GONE);
        view.setVisibility(View.GONE);
        textempty.setText("Data Tidak Ditemukan");
        textempty.setVisibility(View.VISIBLE);
    }

    public void getAPI(String nomor){
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
        call = interfaceApi.getapi_search(par.PARAM_SURVEY_IN_SEARCH(IDAKUN, GROUP, nomor));
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
                            datafound(datajson);
                        }else{
                            datanotfound();
                        }

                    }catch (Exception e){
                        datanotfound();
                    }
                }else{
                    datanotfound();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                datanotfound();
            }
        });
    }
}