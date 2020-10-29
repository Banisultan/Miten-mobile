package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import id.bnn.convey.Activity.LoginActivity;

public class EducationActivity3 extends AppCompatActivity {

    Context context;
    LinearLayout tombollanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education3);

        context = this;
        tombollanjut = findViewById(R.id.tombollanjut);
        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveactivity();
            }
        });
    }

    public void moveactivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void endactivity(){
        Intent intent = new Intent(this, EducationActivity2.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        endactivity();
    }
}
