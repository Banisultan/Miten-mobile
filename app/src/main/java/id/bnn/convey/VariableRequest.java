package id.bnn.convey;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.readystatesoftware.chuck.ChuckInterceptor;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

public class VariableRequest {

    SharedPreferences dataakun;
    public OkHttpClient.Builder setHTTPCLIENT(Context context){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        dataakun = context.getSharedPreferences("dataakun", Context.MODE_PRIVATE);
        String token = dataakun.getString("token", "");

        OkHttpClient httpClient2 = UnsafeOkHttpClient.getUnsafeOkHttpClient();
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Authorization", "Bearer "+token)
                        .method(original.method(), original.body());
                Request request = requestBuilder.build();
                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        });
//        httpClient.addInterceptor(new ChuckInterceptor(context));
//        httpClient.sslSocketFactory(ssl)
        httpClient.addInterceptor(logging);

        return httpClient;
    }

    public OkHttpClient.Builder setHTTPCLIENT(JsonArray dataarray){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                for(int i=0; i < dataarray.size(); i++){
                    JsonObject datajson = dataarray.get(i).getAsJsonObject();
                    String title = datajson.get("title").getAsString();
                    String value = datajson.get("value").getAsString();
                    requestBuilder.addHeader(title, value);
                }
                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();
                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        });
        httpClient.addInterceptor(logging);
        return httpClient;
    }

    public OkHttpClient.Builder setHTTPCLIENT(JSONArray dataarray){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @NotNull
            @Override
            public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder();
                for(int i=0; i < dataarray.length(); i++){
                    try {
                        JSONObject datajson = dataarray.getJSONObject(i);
                        String title = datajson.getString("title");
                        String value = datajson.getString("value");
                        requestBuilder.addHeader(title, value);
                    }catch (Exception e){

                    }

                }
                requestBuilder.method(original.method(), original.body());
                Request request = requestBuilder.build();
                okhttp3.Response response = chain.proceed(request);
                return response;
            }
        });
        httpClient.addInterceptor(logging);
        return httpClient;
    }

    public static class UnsafeOkHttpClient {
        public static OkHttpClient getUnsafeOkHttpClient() {
            try {
                // Create a trust manager that does not validate certificate chains
                final TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            @Override
                            public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                            }

                            @Override
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return new java.security.cert.X509Certificate[]{};
                            }
                        }
                };

                // Install the all-trusting trust manager
                final SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                // Create an ssl socket factory with our all-trusting manager
                final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
                builder.hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });

                OkHttpClient okHttpClient = builder.build();
                return okHttpClient;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}
