package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;


public class UnderDevelopmentActivity extends AppCompatActivity {

    Context context;
    TextView texttoolbar;
    ImageView tombolkembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_under_development);

        context = this;

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText("Under Development");

        tombolkembali = findViewById(R.id.tombolkembali);
        tombolkembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });

        AlphaAnimation tom = new AlphaAnimation(1F, 0.8F);


    }

    public void endactivity(){
        finish();
    }
}
