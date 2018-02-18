package ca326.com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

// __Author__ = Shane Creedon (Shane.creedon3@mail.dcu.ie)
// __Author__ = James Collins


public class Profile_Screen extends AppCompatActivity {

    private RelativeLayout drop_down_option_menu;
    private Button logout;

    private boolean button_colour_swap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_profile__screen);

        this.drop_down_option_menu = (RelativeLayout) findViewById(R.id.menu_layout);
        this.logout = (Button) findViewById(R.id.log_out_button);
    }


    public void menu(View v) {
        System.out.println("clicked");
        if (!button_colour_swap){
            this.drop_down_option_menu.setVisibility(View.VISIBLE);
            button_colour_swap = true;
        }
        else{
            this.drop_down_option_menu.setVisibility(View.INVISIBLE);
            button_colour_swap= false;
        }
    }

    public void logOut(View view) {
        //this.logout.setVisibility(View.VISIBLE);
        SharedPreferences.Editor editor = getSharedPreferences("preference",getApplicationContext().MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Profile_Screen.this, Sign_In_Screen.class);
        startActivity(intent);
    }


}
