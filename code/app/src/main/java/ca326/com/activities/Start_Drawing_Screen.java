package ca326.com.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Start_Drawing_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This functiion below creates the nice fade in / out transition between activities.
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start__drawing__screen);

        // Drawing Functinality
        CanvasView dv = new CanvasView(this);
        Button clear = (Button) findViewById(R.id.clear);
        setContentView(dv);
        // Clear Drawing Functionality
        // clear.setOnClickListener(new View.OnClickListener() {

            //public void onClick(View v) {
                //dv.clearDrawing();
            //}
        //});
    }
}