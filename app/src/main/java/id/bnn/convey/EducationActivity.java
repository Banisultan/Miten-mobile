package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;

import id.bnn.convey.Adapter.SliderAdapter;

public class EducationActivity extends AppCompatActivity {

    Context context;

    ViewPager viewpager;
    SliderAdapter adapterslide;

    View viewdot1;
    View viewdot2;
    View viewdot3;

    View[] viewdot = {viewdot1, viewdot2, viewdot3};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.get(context).clearMemory();
        context.getCacheDir().delete();
        Runtime.getRuntime().gc();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education);
        context = this;

        viewpager = findViewById(R.id.viewpager);
        adapterslide = new SliderAdapter(this, viewpager);
        viewpager.setAdapter(adapterslide);

        viewdot[0] = findViewById(R.id.viewdot1);
        viewdot[1] = findViewById(R.id.viewdot2);
        viewdot[2] = findViewById(R.id.viewdot3);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setdot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setdot(int position){
        Log.d("RESPONSE", "ini position : "+position);
        for(int i=0; i<viewdot.length; i++){
            if(i == position){
                viewdot[i].setBackgroundResource(R.drawable.bg_dot_on);
            }else{
                viewdot[i].setBackgroundResource(R.drawable.bg_dot_off);
            }
        }
    }



    public void endactivity(){

    }

    @Override
    public void onBackPressed(){
        viewpager.setCurrentItem(viewpager.getCurrentItem() - 1);
    }
}
