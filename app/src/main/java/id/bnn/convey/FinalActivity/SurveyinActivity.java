package id.bnn.convey.FinalActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import id.bnn.convey.Adapter.SurveyinAdapter;
import id.bnn.convey.Database;
import id.bnn.convey.InterfaceAPI;
import id.bnn.convey.ListPendingUploadActivity;
import id.bnn.convey.R;
import id.bnn.convey.SearchActivity;
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

public class SurveyinActivity extends AppCompatActivity {

    Context context;
    ViewPager viewPager;
    SurveyinAdapter adaptersurvey;

    RelativeLayout layoutdone;
    RelativeLayout layoutrepair;
    RelativeLayout layoutwaiting;
    RelativeLayout layoutapprove;
    RelativeLayout layoutdonerepair;

    TextView textwaiting;
    TextView textapprove;
    TextView textrepair;
    TextView textdonerepair;
    TextView textdone;

    ImageView tombolkembali;
    TextView texttoolbar;
    ImageView imagesearch;

    LinearLayout tomboladd;
    TextView textcountwaiting;
    TextView textcountapprove;

    VariableText var = new VariableText();
    VariableParam par = new VariableParam();
    VariableRequest req = new VariableRequest();

    String IDAKUN;
    String GROUP;
    String TIPE;

    SharedPreferences dataakun;
    SharedPreferences datainputsurveyin;

    SwipeRefreshLayout swipeRefreshLayout;

    TextView textcountwaitingv2;
    TextView textcountapprovev2;
    TextView textcountrepair;
    TextView textcountdonerepair;
    TextView textcountdone;

    LinearLayout layouttext;
    TextView textupload;

    LinearLayout tombolsearch;

    ImageView imageadd;

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveyin);
        context = this;

        imageadd = findViewById(R.id.imageadd);
        Glide.with(context).load(R.drawable.img_add).into(imageadd);

        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", MODE_PRIVATE);

        dataakun = getSharedPreferences("dataakun", MODE_PRIVATE);
        IDAKUN = dataakun.getString("idakun", "");
        GROUP = dataakun.getString("group", "");
        TIPE = dataakun.getString("tipe", "");

        layouttext = findViewById(R.id.layout_text);
        layouttext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveactivitiy(new Intent(context, ListPendingUploadActivity.class), "Pending Upload Survey IN");
            }
        });

        textupload = findViewById(R.id.text_upload);

        textcountwaitingv2 = findViewById(R.id.textcountwaitingv2);
        textcountapprovev2 = findViewById(R.id.textcountapprovev2);
        textcountrepair = findViewById(R.id.textcountrepair);
        textcountdonerepair = findViewById(R.id.textcountapproverepair);
        textcountdone = findViewById(R.id.textcountdone);

        layoutwaiting = findViewById(R.id.layoutwaiting);
        layoutapprove = findViewById(R.id.layoutapprove);
        layoutrepair = findViewById(R.id.layoutrepair);
        layoutdonerepair = findViewById(R.id.layoutdonerepair);
        layoutdone = findViewById(R.id.layoutdone);

        textwaiting = findViewById(R.id.textwaiting);
        textapprove = findViewById(R.id.textapprove);
        textrepair = findViewById(R.id.textrepair);
        textdonerepair = findViewById(R.id.textdonerepair);
        textdone = findViewById(R.id.textdone);

        texttoolbar = findViewById(R.id.texttoolbar);
        tomboladd = findViewById(R.id.tomboladd);
        tombolkembali = findViewById(R.id.tombolkembali);
        imagesearch = findViewById(R.id.imagesearch);

        Glide.with(context).load(R.drawable.img_back).into(tombolkembali);
        Glide.with(context).load(R.drawable.img_search).into(imagesearch);

        viewPager = findViewById(R.id.viewpagersurvey);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setbar(posisitipe(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_IDLE){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });

        adaptersurvey = new SurveyinAdapter(TIPE, getSupportFragmentManager());
        viewPager.setAdapter(adaptersurvey);

        textcountwaiting = findViewById(R.id.textcountwaiting);
        textcountapprove = findViewById(R.id.textcountapprove);

        layoutwaiting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(posisitipe("waiting"));
            }
        });

        layoutapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(posisitipe("approve"));
            }
        });

        layoutrepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(posisitipe("repair"));
            }
        });

        layoutdonerepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(posisitipe("donerepair"));
            }
        });

        layoutdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(posisitipe("done"));
            }
        });

        texttoolbar.setText(getIntent().getStringExtra("toolbar"));

        tomboladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveactivitiy(new Intent(context, SurveyinAddActivity.class), "Pengisian Survey IN");
            }
        });

        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getDatabaseUpload();
                runAPI(true);
            }
        });

        tombolsearch = findViewById(R.id.layoutbutton);
        tombolsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveactivity_search();
            }
        });

        activefirstbutton();
        validasirole();
        runAPI(false);
    }

    public String posisitipe(int posisi){
        if(TIPE.equals("SURVEYOR")){
            switch (posisi){
                case 0:
                    return "waiting";
                case 1:
                    return "done";
                default:
                    return "";
            }
        }else if(TIPE.equals("SUPERVISOR")){
            switch (posisi){
                case 0:
                    return "waiting";
                case 1:
                    return "approve";
                case 2:
                    return "donerepair";
                case 3:
                    return "done";
            }
        }else if(TIPE.equals("QC_REPAIR")){
            switch (posisi){
                case 0:
                    return "waiting";
                case 1:
                    return "donerepair";
                case 2:
                    return "done";
            }
        }

        return "";
    }

    public int posisitipe(String posisi){
        if(TIPE.equals("SURVEYOR")){
            switch (posisi){
                case "waiting":
                    return 0;
                case "done":
                    return 1;
                default:
                    return -1;
            }
        }else if(TIPE.equals("SUPERVISOR")){
            switch (posisi){
                case "waiting":
                    return 0;
                case "approve":
                    return 1;
                case "donerepair":
                    return 2;
                case "done":
                    return 3;
                default:
                    return -1;
            }
        }else if(TIPE.equals("QC_REPAIR")){
            switch (posisi){
                case "waiting":
                    return 0;
                case "donerepair":
                    return 1;
                case "done":
                    return 2;
                default:
                    return -1;
            }
        }
        return -1;
    }

    public void runAPI(boolean refresh){
        getAPI(refresh);
    }


    public void activefirstbutton(){
        layoutwaiting.setBackgroundResource(R.drawable.bg_shape_tab_left);
        textwaiting.setTextColor(getResources().getColor(R.color.colorblack));
    }

    public void validasirole(){
        if(TIPE.equals("SURVEYOR")){
            layoutapprove.setVisibility(View.GONE);
            layoutrepair.setVisibility(View.GONE);
            layoutdonerepair.setVisibility(View.GONE);
            tomboladd.setVisibility(View.VISIBLE);
        }else if(TIPE.equals("SUPERVISOR")){
            layoutrepair.setVisibility(View.GONE);
            tomboladd.setVisibility(View.GONE);
        }else if(TIPE.equals("QC_REPAIR")){
            layoutapprove.setVisibility(View.GONE);
            layoutrepair.setVisibility(View.GONE);
            tomboladd.setVisibility(View.GONE);
        }
    }

    public void resetbutton(){
        layoutwaiting.setBackgroundResource(R.drawable.bg_shape_tab_off);
        layoutapprove.setBackgroundResource(R.drawable.bg_shape_tab_off);
        layoutrepair.setBackgroundResource(R.drawable.bg_shape_tab_off);
        layoutdonerepair.setBackgroundResource(R.drawable.bg_shape_tab_off);
        layoutdone.setBackgroundResource(R.drawable.bg_shape_tab_off);

        textwaiting.setTextColor(getResources().getColor(R.color.colorgrey));
        textapprove.setTextColor(getResources().getColor(R.color.colorgrey));
        textrepair.setTextColor(getResources().getColor(R.color.colorgrey));
        textdonerepair.setTextColor(getResources().getColor(R.color.colorgrey));
        textdone.setTextColor(getResources().getColor(R.color.colorgrey));

        textcountwaitingv2.setTextColor(getResources().getColor(R.color.colorgrey));
        textcountapprovev2.setTextColor(getResources().getColor(R.color.colorgrey));
        textcountrepair.setTextColor(getResources().getColor(R.color.colorgrey));
        textcountdonerepair.setTextColor(getResources().getColor(R.color.colorgrey));
        textcountdone.setTextColor(getResources().getColor(R.color.colorgrey));

        textcountwaitingv2.setBackgroundResource(R.drawable.bg_shape_v2);
        textcountapprovev2.setBackgroundResource(R.drawable.bg_shape_v2);
        textcountrepair.setBackgroundResource(R.drawable.bg_shape_v2);
        textcountdonerepair.setBackgroundResource(R.drawable.bg_shape_v2);
        textcountdone.setBackgroundResource(R.drawable.bg_shape_v2);
    }

    public void setbar(String position){
        resetbutton();
        switch (position){
            case "waiting":
                layoutwaiting.setBackgroundResource(R.drawable.bg_shape_tab_left);
                textwaiting.setTextColor(getResources().getColor(R.color.colorblack));
                textcountwaitingv2.setTextColor(getResources().getColor(R.color.colororange));
                textcountwaitingv2.setBackgroundResource(R.drawable.bg_shape_v5);
                break;
            case "approve":
                layoutapprove.setBackgroundResource(R.drawable.bg_shape_tab_left);
                textapprove.setTextColor(getResources().getColor(R.color.colorblack));
                textcountapprovev2.setTextColor(getResources().getColor(R.color.colororange));
                textcountapprovev2.setBackgroundResource(R.drawable.bg_shape_v5);
                break;
            case "repair":
                layoutrepair.setBackgroundResource(R.drawable.bg_shape_tab_left);
                textrepair.setTextColor(getResources().getColor(R.color.colorblack));
                textcountrepair.setTextColor(getResources().getColor(R.color.colororange));
                textcountrepair.setBackgroundResource(R.drawable.bg_shape_v5);
                break;
            case "donerepair":
                layoutdonerepair.setBackgroundResource(R.drawable.bg_shape_tab_left);
                textdonerepair.setTextColor(getResources().getColor(R.color.colorblack));
                textcountdonerepair.setTextColor(getResources().getColor(R.color.colororange));
                textcountdonerepair.setBackgroundResource(R.drawable.bg_shape_v5);
                break;
            case "done":
                layoutdone.setBackgroundResource(R.drawable.bg_shape_tab_left);
                textdone.setTextColor(getResources().getColor(R.color.colorblack));
                textcountdone.setTextColor(getResources().getColor(R.color.colororange));
                textcountdone.setBackgroundResource(R.drawable.bg_shape_v5);
                break;
        }
    }

    public void moveactivitiy(Intent intent, String toolbar){
        datainputsurveyin.edit().clear().apply();
        intent.putExtra("toolbar", toolbar);
        intent.putExtra("action", "add");
        startActivity(intent);
    }

    public void moveactivity_search(){
        Intent intent = new Intent(context, SearchActivity.class);
        startActivity(intent);
    }

    public void endactivity(){
        this.finish();
    }

    public void getDatabaseUpload(){
        Database database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();
        db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_SURVEYIN_UPLOAD
                +" WHERE "+database.COL_UPL_TIPE+" = ?";

        String[] selectionArgs = {"W"};

        Cursor cursor = db.rawQuery(selection, selectionArgs);
        int i = 0;
        while (cursor.moveToNext()){
            i++;
        }

        if(i > 0){
            layouttext.setVisibility(View.VISIBLE);
            textupload.setText("PERHATIAN . "+i+" Data sedang dalam proses upload ke server");
        }else{
            layouttext.setVisibility(View.GONE);
        }
    }

    public void getAPI(boolean refresh){
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
        call = interfaceApi.getapi_listsurvey(par.PARAM_LIST_SURVEY(IDAKUN, GROUP, 0));
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
                            String countallwaiting = datajson.getString("count_all_waiting");
                            String countalldone = datajson.getString("count_all_done");
                            String countwaiting = datajson.getString("count_waiting");
                            String countapprove = datajson.getString("count_approve");
                            String countrepair = datajson.getString("count_repair");
                            String countrepairdone = datajson.getString("count_repairdone");
                            String countdone = datajson.getString("count_done");

                            textcountwaiting.setText(countallwaiting);
                            textcountapprove.setText(countalldone);
                            textcountwaitingv2.setText(countwaiting);
                            textcountapprovev2.setText(countapprove);
                            textcountrepair.setText(countrepair);
                            textcountdonerepair.setText(countrepairdone);
                            textcountdone.setText(countdone);

                            viewPager.setAdapter(new SurveyinAdapter(TIPE, getSupportFragmentManager()));
                            swipeRefreshLayout.setRefreshing(false);
                        }else{
                            String addmessage = obj_convert.getString("addmessage");
                            swipeRefreshLayout.setRefreshing(false);

                        }

                    }catch (Exception e){
                        Log.d("TAGGAR", "ERROR "+e);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
