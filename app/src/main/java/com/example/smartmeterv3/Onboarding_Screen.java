package com.example.smartmeterv3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.smartmeterv3.HelperClass.slider_adapter;

public class Onboarding_Screen extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dot_layout;
    Button lets,next,skip;
    TextView[] dots;

    int current_position;

    slider_adapter slider_adapter;
    Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);

        viewPager = findViewById(R.id.slider);
        dot_layout = findViewById(R.id.dot);
        lets = findViewById(R.id.lets_btn);
        next = findViewById(R.id.next);
        skip = findViewById(R.id.skip_btn);

        slider_adapter = new slider_adapter(this);
        viewPager.setAdapter(slider_adapter);

        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    public void skip_func(View view) {

        Intent intent = new Intent(getApplicationContext(),Login_Screen.class);
        startActivity(intent);
        finish();
    }

    public void next_func(View view) {
        viewPager.setCurrentItem(current_position + 1);
    }

    public void lets_func(View view) {
        Intent intent = new Intent(getApplicationContext(),Login_Screen.class);
        startActivity(intent);
    }

    private  void addDots(int position){

        dots = new TextView[3];
        dot_layout.removeAllViews();

        for (int i =0;i<dots.length;i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dot_layout.addView(dots[i]);
        }
        if (dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.blue));
        }
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDots(position);
            current_position = position;

            if (position == 0){
                lets.setVisibility(View.INVISIBLE);
            }
            else if (position == 1){
                lets.setVisibility(View.INVISIBLE);
            }
            else{
                animation = AnimationUtils.loadAnimation(Onboarding_Screen.this,R.anim.bottom_anim);
                lets.setAnimation(animation);
                skip.setVisibility(View.INVISIBLE);
                next.setVisibility(View.INVISIBLE);
                lets.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}