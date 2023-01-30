package com.rcdevelopment.phsensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NonSwipeableViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private ImageButton forwardButton, backButton;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentList = new ArrayList<>();
        fragmentList.add(new MainActivityCode());
        fragmentList.add(new CalculateAdjustment());

        viewPager = findViewById(R.id.MainViewPager);
        pagerAdapter = new PhPagerAdapter(getSupportFragmentManager(), fragmentList);

        viewPager.setAdapter(pagerAdapter);

        forwardButton = findViewById(R.id.forwardPageButton);
        backButton = findViewById(R.id.BackPageButton);


        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();

                if(position < fragmentList.size()-1){
                    position++;
                    viewPager.setCurrentItem(position);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = viewPager.getCurrentItem();

                if(position > 0){
                    position--;
                    viewPager.setCurrentItem(position);
                }
            }
        });


        // USE THIS IF YOU WANT TO UPDATE WHEN A PAGE CHANGES
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//               // UPDATE CODE GOES HERE
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });



    }
}