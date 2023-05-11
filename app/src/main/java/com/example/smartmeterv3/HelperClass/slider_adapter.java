package com.example.smartmeterv3.HelperClass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.example.smartmeterv3.R;

public class slider_adapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public slider_adapter(Context context) {
        this.context = context;
    }

    int lottie[] = {
            R.raw.security,
            R.raw.live_data,
            R.raw.notification_anim
    };

    int headings[] = {
            R.string.capital_otp,
            R.string.live,
            R.string.modal
    };
    int description[] = {
            R.string.sentence,
            R.string.sentence2,
            R.string.sentence3
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_lottie,container,false);

        LottieAnimationView lottieAnimationView = view.findViewById(R.id.on_lottie);
        TextView title,desp;
        title = view.findViewById(R.id.slider_title);
        desp = view.findViewById(R.id.sentences);

        lottieAnimationView.setAnimation(lottie[position]);
        title.setText(headings[position]);
        desp.setText(description[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
