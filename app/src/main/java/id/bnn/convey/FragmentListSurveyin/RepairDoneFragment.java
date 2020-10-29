package id.bnn.convey.FragmentListSurveyin;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.ListContainerVipAdapter;
import id.bnn.convey.Database;
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

public class RepairDoneFragment extends Fragment {

    Context context;
    RecyclerView view;

    List<ListContainerModel> list;
    ListContainerVipAdapter adapterlist;

    SharedPreferences dataakun;
    String IDAKUN;
    String GROUP;
    String TIPE;
    TextView textempty;

    VariableText var = new VariableText();
    VariableParam par = new VariableParam();
    VariableRequest req = new VariableRequest();

    LinearLayout layoutbar;

    boolean GET_API = false;
    int PAGE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_repair_done, container, false);
        context = container.getContext();
        PAGE = 0;
        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        IDAKUN = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        TIPE = dataakun.getString("tipe", "");

        textempty = itemview.findViewById(R.id.textempty);
        layoutbar = itemview.findViewById(R.id.layoutbar);
        layoutbar.setVisibility(View.GONE);

        view = itemview.findViewById(R.id.viewdonerepair);
        view.setHasFixedSize(true);
        view.setLayoutManager(new LinearLayoutManager(context));

        list = new ArrayList<>();
        adapterlist = new ListContainerVipAdapter(context, list);
        view.setAdapter(adapterlist);

        view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    if(layoutbar.getVisibility() == View.GONE){
                        if(!GET_API){
                            PAGE += 1;
                            runapi(PAGE);
                        }
                    }
                }
            }
        });

        runapi(0);
        return itemview;
    }

    public void runapi(int page){
        layoutbar.setVisibility(View.VISIBLE);
        GET_API = true;
        getAPI(page);
    }

    public void stopapi(){
        layoutbar.setVisibility(View.GONE);
        GET_API = false;

        if(list.size() == 0){
            textempty.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        }else{
            textempty.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
        }

        adapterlist.notifyDataSetChanged();
    }

    public void getAPI(int page){
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
        call = interfaceApi.getapi_listsurvey_repairdone(par.PARAM_LIST_SURVEY(IDAKUN, GROUP, page));
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
                            addListSurveyIN(datajson);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            stopapi();
                        }

                    }catch (Exception e){
                        Log.d("TAGGAR", "ERROR "+e);
                        stopapi();
                    }
                }else{
                    stopapi();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                stopapi();
            }
        });
    }

    public void addListSurveyIN(JSONObject datajson){
        try{
            JSONArray dataarray = datajson.getJSONArray("data");
            for (int i=0; i < dataarray.length(); i++){
                JSONObject datajson_obj = dataarray.getJSONObject(i);
                String sin_id = datajson_obj.getString("sin_id");
                String sin_container = datajson_obj.getString("sin_container");
                String sin_condition = datajson_obj.getString("sin_condition");
                String sin_created = datajson_obj.getString("sin_created");
                String sin_status = datajson_obj.getString("sin_status");
                String sin_keterangan  = datajson_obj.getString("keterangan");

                boolean cari = false;
                for(int j=0; j < list.size(); j++){
                    if(list.get(j).getIdsurvei().equals(sin_id)){
                        list.set(j, new ListContainerModel(
                                sin_id,
                                sin_container,
                                sin_status,
                                sin_created,
                                sin_condition,
                                sin_keterangan,
                                var.TIPE_DB_WAITING
                        ));
                        cari = true;
                        break;
                    }
                }

                if(!cari){
                    list.add(new ListContainerModel(
                            sin_id,
                            sin_container,
                            sin_status,
                            sin_created,
                            sin_condition,
                            sin_keterangan,
                            var.TIPE_DB_WAITING
                    ));
                }
            }

        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR insert sqlite "+e);
        }

        stopapi();
    }
}