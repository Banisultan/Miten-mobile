package id.bnn.convey.FinalActivity.AddSurveyinFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import id.bnn.convey.InterfaceAPI_v2;
import id.bnn.convey.R;
import id.bnn.convey.VariableRequest;
import id.bnn.convey.VariableText;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoContainerFragment extends Fragment {

    Context context;
    LinearLayout buttoninput;
    EditText inputnocontainer;
    LinearLayout tombollanjut;

    InputMethodManager imm;

    SharedPreferences.Editor datainputsurveyin_edit;
    SharedPreferences datainputsurveyin;
    SharedPreferences datauser;

    Dialog dialog_warning;
    ImageView image_msg_warning;
    TextView text_msg_warning;
    LinearLayout button_msg_warning;

    Dialog dialog_loading;
    TextView text_msg_loading;

    VariableText var = new VariableText();

    String USERID;
    String TOKEN;

    boolean BUTTON_EXECUTE = false;

    ImageView viewv2;
    TextView[] viewstep;
    View[] viewline;

    String NO_CONTAINER = "";

    public NoContainerFragment(
            ImageView viewv2,
            TextView[] viewstep,
            View[] viewline
    ){
        this.viewv2 = viewv2;
        this.viewstep = viewstep;
        this.viewline = viewline;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hide_warning();
        hide_loading();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View itemview = inflater.inflate(R.layout.fragment_no_container, container, false);
        context = container.getContext();
        imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        for(int i=1; i<viewline.length; i++){
            if(i <= 1){
                viewstep[i].setBackgroundResource(R.drawable.bg_step_on);
                viewline[i].setBackgroundResource(R.drawable.bg_line_on);
            }else{
                viewstep[i].setBackgroundResource(R.drawable.bg_step_off);
                viewline[i].setBackgroundResource(R.drawable.bg_line_off);
            }
        }
        viewstep[viewline.length].setBackgroundResource(R.drawable.bg_step_off);
        Glide.with(context).load(R.drawable.img_warning_off).into(viewv2);

        inputnocontainer = itemview.findViewById(R.id.inputcontainernumber);
        inputnocontainer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputnocontainer.removeTextChangedListener(this);
                String text = inputnocontainer.getText().toString().trim().toUpperCase().replace(" ", "");
                inputnocontainer.setText(text);
                inputnocontainer.setSelection(text.length());
                inputnocontainer.addTextChangedListener(this);
            }
        });

        buttoninput = itemview.findViewById(R.id.buttoninput);
        buttoninput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputnocontainer.requestFocus();
                openkeyboard(inputnocontainer);
            }
        });

        tombollanjut = itemview.findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validation();
            }
        });

        messagewarning();
        messageloading();
        loadData();
        return itemview;
    }

    public void openkeyboard(EditText editText){
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public void loadData(){
        datainputsurveyin = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE);
        NO_CONTAINER = datainputsurveyin.getString("nocontainer", "");
        inputnocontainer.setText(NO_CONTAINER);
        inputnocontainer.setSelection(NO_CONTAINER.length());

        datauser = context.getSharedPreferences("datauser", Context.MODE_PRIVATE);
        USERID = datauser.getString("userid", "");
        TOKEN = datauser.getString("token", "");
    }

    public void savedata(String nocontainer){
        datainputsurveyin_edit = context.getSharedPreferences("datainputsurveyin", Context.MODE_PRIVATE).edit();
        datainputsurveyin_edit.putString("nocontainer", nocontainer.toUpperCase());
        datainputsurveyin_edit.apply();

        nextfragment();
    }

    public void nextfragment(){
        Fragment fragment = new DataSurveyFragment(
                viewv2,
                viewstep,
                viewline
        );
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.layoutfragment, fragment)
                .commit();
    }

    public void messagewarning(){
        dialog_warning = new Dialog(context);
        dialog_warning.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_warning.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_warning.setContentView(R.layout.layout_warning);
        dialog_warning.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        image_msg_warning = dialog_warning.findViewById(R.id.imagemessage);
        Glide.with(context).load(R.drawable.img_warning).into(image_msg_warning);

        text_msg_warning = dialog_warning.findViewById(R.id.textmessage);
        button_msg_warning = dialog_warning.findViewById(R.id.buttonoke);
        button_msg_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hide_warning();
            }
        });
    }

    public void messageloading(){
        dialog_loading = new Dialog(context);
        dialog_loading.setCancelable(false);
        dialog_loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_loading.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_loading.setContentView(R.layout.layout_loading_v2);
        dialog_loading.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        text_msg_loading = dialog_loading.findViewById(R.id.textmessage);
    }

    public void show_warning(String message){
        text_msg_warning.setText(message);
        dialog_warning.show();
    }

    public void hide_warning(){
        dialog_warning.dismiss();
    }

    public void show_loading(String message){
        text_msg_loading.setText(message);
        dialog_loading.show();
    }

    public void hide_loading(){
        BUTTON_EXECUTE = false;
        dialog_loading.dismiss();
    }

    public void validation(){
        String nocontainer = inputnocontainer.getText().toString().trim();
        if(nocontainer.isEmpty()) {
            show_warning(getString(R.string.val_container_empty));
        }else
            if(NO_CONTAINER.equals(nocontainer)){
                nextfragment();
            }
            else if(!ceknomorcontainer(nocontainer)){
                show_warning(getString(R.string.val_container_format));
            }else{
                if(!BUTTON_EXECUTE){
                    BUTTON_EXECUTE = true;
                    show_loading(getString(R.string.msg_loading_text));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callAPI(nocontainer);
                        }
                    }, 600);
                }
            }
    }

    public boolean ceknomorcontainer(String nomorcontainer){
        try{
            int[] map = {10, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                    21, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 34, 35, 36, 37, 38};

            int[] weights = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};

            int digittocheck = Integer.parseInt(nomorcontainer.substring(nomorcontainer.length()-1));

            int[] digit = new int[10];
            for (int i=0; i<4; i++) {
                digit[i] = map[Character.getNumericValue(nomorcontainer.charAt(i)) - 10];
            }

            for (int i=4; i<10; i++) {
                digit[i] = Integer.parseInt(nomorcontainer.substring(i, i+1));
            }

            for (int i=0; i<10; i++){
                digit[i] = digit[i] * weights[i];
            }

            int total = 0;
            for (int i=0; i<10; i++){
                total = total + digit[i];
            }

            String digitresult = String.valueOf(total % 11);

            if(digittocheck == Integer.parseInt(digitresult.substring(digitresult.length() - 1))){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.d("TAGGAR ", "ERROR "+e);
            return false;
        }
    }

    public void callAPI(String nocontainer){
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
        datajson.addProperty("nocontainer", nocontainer);

        Call<JsonObject> call = interfaceApi.checkNumber(datajson);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()){
                    JsonObject obj_response = response.body().getAsJsonObject();
                    String message = obj_response.get("message").getAsString();
                    String rescode = obj_response.get("rescode").getAsString();

                    if(rescode.equals("0")){
                        savedata(nocontainer);
                        hide_loading();
                    }else{
                        hide_loading();
                        show_warning(message);
                    }
                }else{
                    hide_loading();
                    show_warning(getString(R.string.error_default));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                hide_loading();
                show_warning(getString(R.string.error_default));
            }
        });
    }
}