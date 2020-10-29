package id.bnn.convey.FinalActivity.SurveyinFragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.ListContainerVipAdapter;
import id.bnn.convey.Database;
import id.bnn.convey.FinalActivity.Adapter.ListSurvey;
import id.bnn.convey.FinalActivity.Model.m_ListSurvey;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.Model.ListContainerModel;
import id.bnn.convey.Model.ListContainerVipModel;
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
import retrofit2.http.GET;

public class WaitingFragment extends Fragment {

    Context context;
    RecyclerView viewwaiting;

    List<m_ListSurvey> list;
    ListSurvey adapter;

    SharedPreferences datauser;
    String USERID;
    String TOKEN;

    TextView textempty;
    TextView textdone;
    VariableText var = new VariableText();
    ProgressBar progressBar;

    int PAGE = 1;
    boolean GET_API = false;
    boolean DISABLE_API = false;

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Glide.get(context).clearMemory();
//        Runtime.getRuntime().gc();
    }

    @Override
    public void onResume() {
        super.onResume();
        PAGE = 1;
        loaddata();
        Log.d("TAGGAR", "SIZE "+list.size());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_waiting2, container, false);
        context = container.getContext();

        textempty = itemview.findViewById(R.id.textempty);
        textdone = itemview.findViewById(R.id.textdone);

        progressBar = itemview.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");

        viewwaiting = itemview.findViewById(R.id.viewwaiting);
        viewwaiting.setHasFixedSize(true);
        viewwaiting.setLayoutManager(new LinearLayoutManager(context){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        viewwaiting.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!recyclerView.canScrollVertically(1)){
                    if(progressBar.getVisibility() == View.GONE){
                        if(PAGE > 1){
                            loaddata();
                        }
                    }
                }
            }
        });

        list = new ArrayList<>();
        adapter = new ListSurvey(list);
        viewwaiting.setAdapter(adapter);
        return itemview;
    }

    public void loaddata(){
        if(!DISABLE_API){
            if(!GET_API){
                progressBar.setVisibility(View.VISIBLE);
                GET_API = true;
                callAPI(PAGE);
            }
        }
    }

    public void adddata(JsonArray dataarray){
        if(dataarray.size() == 0){
            DISABLE_API = true;
            textdone.setVisibility(View.VISIBLE);
        }

        for(int i=0; i < dataarray.size(); i++){
            try{
                JsonObject datajson = dataarray.get(i).getAsJsonObject();
                String surveyid = datajson.get("surveyid").getAsString();
                for(int j=0; j<list.size(); j++){
                    if(list.get(j).getSurveyid().equals(surveyid)){
                        list.remove(j);
                        break;
                    }
                }

                list.add(new m_ListSurvey(
                        surveyid,
                        datajson.get("nocontainer").getAsString(),
                        datajson.get("condition").getAsString(),
                        datajson.get("time").getAsString()
                ));
            }catch (Exception e){

            }
        }
        PAGE += 1;
        adapter.notifyDataSetChanged();
        hide();
    }

    public void hide(){
        progressBar.setVisibility(View.GONE);
        GET_API = false;
    }

    public void callAPI(int page) {
        JsonArray dataarray = new JsonArray();

        JsonObject datajson_1 = new JsonObject();
        datajson_1.addProperty("title", "Authorization");
        datajson_1.addProperty("value", "Bearer " + TOKEN);

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
        datajson.addProperty("page", page);

        Call<JsonObject> call = interfaceApi.list_surveyin_waiting(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String message = obj_response.get("message").getAsString();
                    String rescode = obj_response.get("rescode").getAsString();

                    if (rescode.equals("0")) {
                        adddata(obj_response.get("data").getAsJsonArray());
                    }else{
                        hide();
                    }
                }else{
                    hide();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide();
            }
        });
    }
}
