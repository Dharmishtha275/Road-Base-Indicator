package com.example.roadbaseindicator;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Created by Cisco on 07-04-2016.
 */

public class Splash extends Activity implements AnimationListener {
    ImageView imgLogo;
    Animation animRotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


       // getSupportActionBar().setIcon(R.drawable.logo);
       // getSupportActionBar().setTitle("Road Base Indicator");
//
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#79BAEC"));
//       getSupportActionBar().setBackgroundDrawable(colorDrawable);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);

        // load the animation
        animRotate = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotate);

        // set animation listener
        animRotate.setAnimationListener(this);
        imgLogo.startAnimation(animRotate);

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // TODO Auto-generated method stub
        if (animation == animRotate) {

            Intent i=new Intent(getApplicationContext(),view_map.class);
            //i.putExtra("name", edt_1.getText().toString());
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }



}


