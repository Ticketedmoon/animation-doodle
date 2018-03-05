package ca326.com.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.WindowManager;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import static ca326.com.activities.Start_Drawing_Screen.PREFERENCE;

public class Intro_Animation_Transition_Screen extends AwesomeSplash {

    //if you need to start some services do it in initSplash()!

    @Override
    public void initSplash(ConfigSplash configSplash) {

        // Change status bar (Transparent -> looks better)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorOrange); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.black_tiger); //or any other drawable
        configSplash.setAnimLogoSplashDuration(750); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.DropOut); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        configSplash.setOriginalHeight(400); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(400); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(1200);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.colorWhite); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(3000);
        configSplash.setPathSplashFillColor(R.color.colorWhite); //path object filling color

        //Customize Title
        configSplash.setTitleSplash("Animation Doodle");
        configSplash.setTitleTextColor(R.color.colorBlack);
        configSplash.setTitleTextSize(35f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.FadeIn);

        configSplash.setTitleFont("fonts/Roboto-Light.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        SharedPreferences mSharedPreferences;
        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String temp = mSharedPreferences.getString("animationName",null);
        if(temp!=null) {
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();

            //clear the shared preferences for the animation name
            mEditor.remove("animationName");
            mEditor.apply();
        }
        //transit to another activity here
        // clear Animation Title

        Intent main_menu = new Intent(Intro_Animation_Transition_Screen.this, Main_Menu_Screen.class);
        startActivity(main_menu);
    }

}
