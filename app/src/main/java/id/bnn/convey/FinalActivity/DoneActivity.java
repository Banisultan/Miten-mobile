package id.bnn.convey.FinalActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import id.bnn.convey.R;

public class DoneActivity extends AppCompatActivity {

    Context context;
    ImageView imagedone;

    LinearLayout tomboloke;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(context).clearMemory();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done2);

        context = this;

        tomboloke = findViewById(R.id.tomboloke);
        tomboloke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imagedone = findViewById(R.id.imagedone);
        Glide.with(context).load(R.drawable.img_done).into(imagedone);
    }
}