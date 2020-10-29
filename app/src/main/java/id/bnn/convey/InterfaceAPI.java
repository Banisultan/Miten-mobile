package id.bnn.convey;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface InterfaceAPI {
    @POST("login/")
    Call<JsonObject> getapi(
            @Body JsonObject requestBody
    );

    @Multipart
    @POST("identity/")
    Call<JsonObject> getapi_identity(
            @Part MultipartBody.Part[] image,
            @Part("group") RequestBody data
    );

    @POST("listsurvey/step1/")
    Call<JsonObject> getapi_listowner(
            @Body JsonObject requestBody
    );

    @POST("countsurvey/")
    Call<JsonObject> getapi_count(
            @Body JsonObject requestBody
    );

    @POST("searchsurvey/")
    Call<JsonObject> getapi_searchsurvey(
            @Body JsonObject requestBody
    );

    @POST("survey_in/view/")
    Call<JsonObject> getapi_viewsurvey(
            @Body JsonObject requestBody
    );

    @POST("survey_in/search/")
    Call<JsonObject> getapi_search(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/")
    Call<JsonObject> getapi_listsurvey(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/waiting/")
    Call<JsonObject> getapi_listsurvey_waiting(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/approve/")
    Call<JsonObject> getapi_listsurvey_approve(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/repair/")
    Call<JsonObject> getapi_listsurvey_repair(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/repairdone/")
    Call<JsonObject> getapi_listsurvey_repairdone(
            @Body JsonObject requestBody
    );

    @POST("survey_in/list/done/")
    Call<JsonObject> getapi_listsurvey_done(
            @Body JsonObject requestBody
    );

    @POST("listkategori/")
    Call<JsonObject> getapi_listkategori(
            @Body JsonObject requestBody
    );

    @POST("getprofile/")
    Call<JsonObject> getapi_getProfile(
            @Body JsonObject requestBody
    );

    @POST("countsurvey/")
    Call<JsonObject> getapi_countsurvey(
            @Body JsonObject requestBody
    );

    @POST("approvesurvey/")
    Call<JsonObject> getapi_approvesurvey(
            @Body JsonObject requestBody
    );

    @POST("v1/validationSaldo/")
    Call<JsonObject> getapi_validationSaldo(
            @Body JsonObject requestBody
    );

    @POST("survey_in/repair/approve/")
    Call<JsonObject> getapi_surveyin_repair_approve(
            @Body JsonObject requestBody
    );

    @POST("survey_in/cek/nomorcontainer/")
    Call<JsonObject> getapi_surveyin_ceknocontainer(
            @Body JsonObject requestBody
    );

    @POST("survey_in/validation/")
    Call<JsonObject> getapi_surveyin_validation(
            @Body JsonObject requestBody
    );

    @Multipart
    @POST("api/v2/surveyin/insert/")
    Call<JsonObject> getapi_insertsurveyin(
            @Part MultipartBody.Part[] image,
            @Part("data_id") RequestBody id,
            @Part("data_group") RequestBody group,
            @Part("data_survey") RequestBody datasurvey,
            @Part("data_survey_detail") RequestBody datasurvey_de,
            @Part("data_foto") RequestBody datafoto
            );

    @Multipart
    @POST("api/v3/surveyin/insert/")
    Call<JsonObject> getapi_insertsurveyin_v3(
            @Part("fotocon") String datafoto
    );

    @Multipart
    @POST("survey_in/edit/")
    Call<JsonObject> getapi_editsurveyin(
            @Part MultipartBody.Part[] image,
            @Part("data_id") RequestBody id,
            @Part("data_group") RequestBody group,
            @Part("data_survey") RequestBody datasurvey,
            @Part("data_survey_detail") RequestBody datasurvey_de,
            @Part("data_survey_hapus") RequestBody datasurvey_hapus,
            @Part("data_foto") RequestBody datafoto,
            @Part("data_hapus_foto") RequestBody datahapusfoto
    );

    @Multipart
    @POST("survey_in/repair/")
    Call<JsonObject> getapi_surveyin_repair(
            @Part MultipartBody.Part[] image,
            @Part("data_id") RequestBody id,
            @Part("data_id_surveyin") RequestBody id_survey,
            @Part("data_id_detail") RequestBody id_survey_detail,
            @Part("data_group") RequestBody group
    );
}
