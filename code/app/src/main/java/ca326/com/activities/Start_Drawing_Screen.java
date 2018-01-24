package ca326.com.activities;

import android.graphics.Color;
import android.graphics.Paint;
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
        DrawingView dv = new DrawingView(this);

        // Set up Paint object
        dv.mPaint = new Paint();
        dv.mPaint.setAntiAlias(true);
        dv.mPaint.setDither(true);
        dv.mPaint.setColor(Color.BLACK);
        dv.mPaint.setStyle(Paint.Style.STROKE);
        dv.mPaint.setStrokeJoin(Paint.Join.ROUND);
        dv.mPaint.setStrokeCap(Paint.Cap.ROUND);
        dv.mPaint.setStrokeWidth(5);

        setContentView(dv);
        Button clear = (Button) findViewById(R.id.clear);

        // Clear Drawing Functionality
        // clear.setOnClickListener(new View.OnClickListener() {

            //public void onClick(View v) {
                //dv.clearDrawing();
            //}
        //});
    }
}