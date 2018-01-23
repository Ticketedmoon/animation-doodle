package ca326.com.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Start_Drawing_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start__drawing__screen);
    }
    
}
