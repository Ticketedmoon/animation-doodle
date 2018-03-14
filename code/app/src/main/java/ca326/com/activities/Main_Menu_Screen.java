package ca326.com.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

public class Main_Menu_Screen extends AppCompatActivity {

    // Login Credentials
    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    // Main Menu standard onCreate function.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.fragment_layout_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Start Fragment Operations
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = ItemOneFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
                                Intent intent;

                                if(mSharedPreferences.contains(PREF_EMAIL)&& mSharedPreferences.contains(PREF_PASSWORD)) {
                                    intent = new Intent(Main_Menu_Screen.this, Profile_Screen.class);
                                    startActivity(intent);
                                }
                                else{
                                    selectedFragment = ItemTwoFragment.newInstance();
                                    break;
                                }
                                selectedFragment = ItemTwoFragment.newInstance();
                                break;
                            case R.id.action_item3:
                                selectedFragment = ItemThreeFragment.newInstance();
                                break;
                        }

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, ItemOneFragment.newInstance());
        transaction.commit();

    }

    public void goToDrawingScreen(View v) {
        Log.i("Button", "Pressed");
        Intent intent = new Intent(Main_Menu_Screen.this, Start_Drawing_Screen.class);
        startActivity(intent);
    }

    public void goToSettingsScreen(View v) {
        Log.i("Button Settings", "Pressed");
        Intent intent = new Intent(Main_Menu_Screen.this, Settings_Screen.class);
        startActivity(intent);
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
