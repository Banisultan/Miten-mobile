package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoneActivity extends AppCompatActivity {

    LinearLayout tomboloke;
    TextView textketerangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        String text = getIntent().getStringExtra("text");
        textketerangan = findViewById(R.id.textketerangan);
        textketerangan.setText(text);

        tomboloke = findViewById(R.id.tomboloke);
        tomboloke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endactivity();
            }
        });
    }

    public void endactivity(){
        finish();
    }
}