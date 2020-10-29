package id.bnn.convey;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfaceAPI_v2 {
    @POST("surveyin_checknumber")
    Call<JsonObject> checkNumber(
            @Body JsonObject requestBody
    );

    @POST("surveyin_checksaldo")
    Call<JsonObject> checkSaldo(
            @Body JsonObject requestBody
    );

    @POST("login")
    Call<JsonObject> login(
            @Body JsonObject requestBody
    );

    @POST("list_all")
    Call<JsonObject> list_all(
            @Body JsonObject requestBody
    );

    @POST("list_owner")
    Call<JsonObject> list_owner(
            @Body JsonObject requestBody
    );

    @POST("list_wash")
    Call<JsonObject> list_wash(
            @Body JsonObject requestBody
    );

    @POST("list_kategori")
    Call<JsonObject> list_kategori(
            @Body JsonObject requestBody
    );

    @POST("list_posisi")
    Call<JsonObject> list_posisi(
            @Body JsonObject requestBody
    );

    @POST("list_size")
    Call<JsonObject> list_size(
            @Body JsonObject requestBody
    );

    @POST("list_tipe")
    Call<JsonObject> list_tipe(
            @Body JsonObject requestBody
    );

    @POST("list_grade")
    Call<JsonObject> list_grade(
            @Body JsonObject requestBody
    );

    @POST("list_year")
    Call<JsonObject> list_year(
            @Body JsonObject requestBody
    );

    @POST("list_component")
    Call<JsonObject> list_component(
            @Body JsonObject requestBody
    );

    @POST("list_damage")
    Call<JsonObject> list_damage(
            @Body JsonObject requestBody
    );

    @POST("list_dimension")
    Call<JsonObject> list_dimension(
            @Body JsonObject requestBody
    );

    @POST("list_repair")
    Call<JsonObject> list_repair(
            @Body JsonObject requestBody
    );

    @POST("list_tpc")
    Call<JsonObject> list_tpc(
            @Body JsonObject requestBody
    );

    @POST("list_surveyin_waiting")
    Call<JsonObject> list_surveyin_waiting(
            @Body JsonObject requestBody
    );

    @POST("surveyin_insert")
    Call<JsonObject> insertsurveyin(
            @Body JsonObject requestBody
    );

    @POST("surveyin_edit")
    Call<JsonObject> editsurveyin(
            @Body JsonObject requestBody
    );

    @Multipart
    @POST("surveyin_photo")
    Call<JsonObject> insertsurveyin_photo(
            @Part MultipartBody.Part photo,
            @Part("userid") RequestBody userid,
            @Part("mobid") RequestBody mobid,
            @Part("surveyid") RequestBody surveyid,
            @Part("tipe") RequestBody tipe,
            @Part("posisi") RequestBody posisi,
            @Part("mobiddamage") RequestBody mobid_damage,
            @Part("mobidphoto") RequestBody mobid_photo
    );
}
