package id.bnn.convey.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;

import id.bnn.convey.EducationActivity;
import id.bnn.convey.EducationActivity2;
import id.bnn.convey.FinalActivity.LoginActivity;
import id.bnn.convey.R;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater inflater;
    ViewPager viewpager;

    String[] listjudul = {
            "TRUK CONTAINER",
            "CONTAINER",
            "KAPAL CONTAINER"
    };
    int[] listimage = {R.drawable.img_bg_edu1, R.drawable.img_bg_edu2, R.drawable.img_bg_edu3};

    ImageView imagebg;

    TextView textjudul;
    TextView texttombol;
    LinearLayout tombollanjut;

    public SliderAdapter(
            Context context,
            ViewPager viewpager){

        this.context = context;
        this.viewpager = viewpager;
    }

    @Override
    public int getCount() {
        return listjudul.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_slide,container, false);

        textjudul = view.findViewById(R.id.textjudul);
        texttombol = view.findViewById(R.id.texttombol);
        tombollanjut = view.findViewById(R.id.tombollanjut);
        imagebg = view.findViewById(R.id.img_bg);

        imagebg.setBackgroundResource(listimage[position]);
        Glide.with(context).load(listimage[position]).into(imagebg);

        textjudul.setText(listjudul[position]);
        if(position == listjudul.length-1){
            texttombol.setText("Get Started");
        }

        tombollanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position < listjudul.length-1){
                    viewpager.setCurrentItem(viewpager.getCurrentItem()+1, true);
                }else{
                    actionactive();
                    savedata();
                }

            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }

    public void moveactivity(){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public void actionactive(){
        tombollanjut.setEnabled(false);
    }

    public void actionnonactive(){
        tombollanjut.setEnabled(true);
    }

    public void savedata(){
        SharedPreferences.Editor datadevice = context.getSharedPreferences("device", Context.MODE_PRIVATE).edit();
        datadevice.putString("log", "done");
        datadevice.apply();

        moveactivity();
    }
}
