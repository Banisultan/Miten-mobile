package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Testing2 extends AppCompatActivity {

    Context context;
    VariableRequest req = new VariableRequest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing2);

        context = this;

        getAPI();
    }

    public void getAPI(){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = req.setHTTPCLIENT(context);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://newsapi.org/")
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();

        InterfaceAPI interfaceApi = retrofit.create(InterfaceAPI.class);

        Call<JsonObject> call = null;
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("RESPONSE", "API sukses : "+response.body());

                try{
                    JSONObject objdata = new JSONObject(response.body().toString());
                    Log.d("RESPONSE", "Status : "+objdata.get("status"));
                }catch(Exception e){

                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("RESPONSE", "API gagal : "+t.getMessage());
            }
        });
    }
}
