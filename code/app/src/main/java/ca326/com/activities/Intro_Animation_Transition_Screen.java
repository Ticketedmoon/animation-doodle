package ca326.com.activities;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class Intro_Animation_Transition_Screen extends AwesomeSplash {

    //if you need to start some services do it in initSplash()!

    @Override
    public void initSplash(ConfigSplash configSplash) {

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorOrange); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(1500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Customize Logo
        configSplash.setLogoSplash(R.drawable.ad_gem); //or any other drawable
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
        configSplash.setTitleTextSize(60f); //float value
        configSplash.setAnimTitleDuration(1500);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);

        configSplash.setTitleFont("fonts/intro_transition_font.ttf"); //provide string to your font located in assets/fonts/
    }

    @Override
    public void animationsFinished() {
        //transit to another activity here
        startActivity(new Intent(Intro_Animation_Transition_Screen.this, Sign_In_Screen.class));
    }

}
