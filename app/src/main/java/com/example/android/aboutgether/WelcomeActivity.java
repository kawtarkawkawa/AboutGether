package com.example.android.aboutgether;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mpager;
    private int[] layouts = {R.layout.first_slide,R.layout.second_slide,R.layout.third_slide};
    private MpagerAdapter mpagerAdapter;

    private LinearLayout Dots_Layout;
    private ImageView[] dots;

    private Button BnNext,BnSkip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (new PreferenceManager(this).checkPreference())
        {
            loadHome();
        }

        if (Build.VERSION.SDK_INT >=19)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_welcome);

        mpager = (ViewPager)findViewById(R.id.viewpager);
        mpagerAdapter = new MpagerAdapter(layouts,this);
        mpager.setAdapter(mpagerAdapter);

        Dots_Layout = (LinearLayout)findViewById(R.id.dotsLayout);
        BnNext = (Button)findViewById(R.id.btNext);
        BnSkip = (Button)findViewById(R.id.bnSkip);
        BnNext.setOnClickListener(this);
        BnSkip.setOnClickListener(this);
        creteDots(0);

        mpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                creteDots(position);
                if (position==layouts.length-1)
                {
                    BnNext.setText("GOT IT");
                    BnSkip.setVisibility(View.INVISIBLE);
                }
                else
                {
                    BnNext.setText("Next");
                    BnSkip.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void creteDots(int current_position) {
        if (Dots_Layout != null)
            Dots_Layout.removeAllViews();
        dots = new ImageView[layouts.length];

        for (int i = 0; i < layouts.length; i++) {
            dots[i] = new ImageView(this);
            if (i == current_position) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dots_selected));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.dots_default));
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 0, 4, 0);

            Dots_Layout.addView(dots[i], params);


        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btNext:
                loadNextSlide();
            break;
            case R.id.bnSkip:
                loadHome();
                new PreferenceManager(this).writePreference();
                break;
        }

    }
    private void loadHome()
    {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
    private void loadNextSlide()
    {
        int next_slide = mpager.getCurrentItem()+1;
        if (next_slide<layouts.length)
        {
            mpager.setCurrentItem(next_slide);
        }
        else
        {
            loadHome();
            new PreferenceManager(this).writePreference();
        }
    }
}
