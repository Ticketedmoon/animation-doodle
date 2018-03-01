package ca326.com.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.javiersantos.bottomdialogs.BottomDialog;

import java.io.File;

public class Main_Menu_Screen extends AppCompatActivity {

    // Login Credentials
    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    // Initialise buttons
    private Button start_drawing;
    private Button start_profile;
    private Button start_popular_animations;

    // Store Animation Title
    public static String ANIMATION_TITLE;

    // Main Menu standard onCreate function.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main_menu_screen);

        // Assign buttons
        start_drawing = (Button) findViewById(R.id.drawing_menu);
        start_profile = (Button) findViewById(R.id.profile_menu);
        start_popular_animations = (Button) findViewById(R.id.popAn_menu);

        // Assign Background Colour
        start_drawing.setBackgroundResource(R.drawable.main_menu_btn_colour);
        start_profile.setBackgroundResource(R.drawable.main_menu_btn_colour);
        start_popular_animations.setBackgroundResource(R.drawable.main_menu_btn_colour);
        // END

        // Change status bar (Transparent -> looks better)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Build necessary directories
        build_local_directories();
    }

    private void build_local_directories() {

        // BASE DIRECTORY
        File baseDirectory = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle");
        if(!baseDirectory.exists()){
            baseDirectory.mkdirs();
        }

        // IMPORTABLE BACKGROUNDS DIRECTORY
        File file = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle/Backgrounds");
        if(!file.exists()){
            file.mkdirs();
        }

        // TEMP IMAGE HOLDERS (USED TO ENCODE VIDEO) DIRECTORY
        File tempDirectory = new File(Environment.getExternalStorageDirectory(), "AnimationDoodle/Temp");
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        // ANIMATIONS DIRECTORY
        File animationDirectory = new File(Environment.getExternalStorageDirectory(), "AnimationDoodle/Animations");
        if (!animationDirectory.exists()) {
            animationDirectory.mkdirs();
        }
    }

    public void get_animation_name(View v) {
        // EMPTY POP UP DIALOG BOX
        Log.i("Dialog Box", "Initiating");
        Log.d("Dialog Box", "Animation name: " + ANIMATION_TITLE);
        Intent drawing_screen = new Intent(Main_Menu_Screen.this, Start_Drawing_Screen.class);
        startActivity(drawing_screen);
    }

    // When button pressed, call this function
    public void goToDrawingScreen(View v) {
        get_animation_name(v);
    }

    // When button pressed, call this function
    public void goToSignInOrProfile(View v) {
        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Intent intent;


        if(mSharedPreferences.contains(PREF_EMAIL)&& mSharedPreferences.contains(PREF_PASSWORD)) {
            intent = new Intent(Main_Menu_Screen.this, Profile_Screen.class);
        }
        else{
            intent = new Intent(Main_Menu_Screen.this, Sign_In_Screen.class);
        }

        startActivity(intent);
    }

    // When button pressed, call this function
    public void goToTopRated(View v) {
        Intent drawing_screen = new Intent(Main_Menu_Screen.this, Top_Rated_Screen.class);
        startActivity(drawing_screen);
    }

    // Disable back button on this screen
    public void onBackPressed() {
        System.out.println("Back Button Pushed <Returning to Homescreen>");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }
}
