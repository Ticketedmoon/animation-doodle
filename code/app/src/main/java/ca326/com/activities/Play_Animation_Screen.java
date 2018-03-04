package ca326.com.activities;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import java.util.List;
import java.util.Map;

import static ca326.com.activities.Start_Drawing_Screen.drawables;

public class Play_Animation_Screen extends AppCompatActivity {

    // Private fields
    private CanvasViewNonEditable cv;
    private Integer pos;
    private View myView;
    private View myTopView;
    private Boolean isUp;
    private Button myButton;

    // Public Static
    public static Integer frame_rate;
    public static Map<Integer, List <Pair<Path, Paint>>> pathways = Start_Drawing_Screen.pathways;

    // Handlers / Timed events
    private Handler m_handler;
    private Runnable m_handlerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.exit_to_left);
        setContentView(R.layout.activity_play__animation__screen);

        // Initialisation
        this.pos = 0;
        this.cv = (CanvasViewNonEditable) findViewById(R.id.canvas2);

        myView = findViewById(R.id.my_view_bottom);
        myView.setVisibility(View.VISIBLE);

        myTopView = findViewById(R.id.my_view_top);
        myTopView.setVisibility(View.VISIBLE);

        myButton = findViewById(R.id.my_button);
        isUp = true;

        play_animation(cv);
    }

    public void play_animation(View v) {
        // Logcat Information
        System.out.println("Transition to Play_Animation Activity\nPlay Button Pushed / paused\nPlaying Animation");
        System.out.println(Start_Drawing_Screen.pathways);
        System.out.println("--------");
        System.out.println(pathways);
        // END

        // Play Animation Begin Logic
        m_handler = new Handler();
        m_handlerTask = new Runnable() {
            public void run() {
                System.out.println(pos.toString() + "----" + Integer.toString(pathways.size()));
                cv.newPaths = pathways.get(pos);
                cv.setBackground(drawables.get(pos));
                cv.invalidate();
                pos++;

                // Delay needs to be here for pause / play button reasons
                // instead of 1000 mention the delay in milliseconds
                m_handler.postDelayed(m_handlerTask, frame_rate);

                // Doesn't require {} body since only 1 line following the IF conditional.
                if (pos == pathways.size())
                    pos=0;

            }
        };
        m_handlerTask.run();
    }

    // Import method, controls back logic and remembers previously drawn frames.
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // do something
            Log.i("Play Animation Log", "Back button pushed while animation playing");
            Log.i("Play Animation Log", "Resetting pos...");
            pos=0;
            cv.invalidate();

            m_handler.removeCallbacks(m_handlerTask);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // Animation Methods for Sliding toolbar play button menu
    // slide the view from below itself to the current position
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (isUp) {
            slideDown(myView);
            myButton.setText("Slide up");
        } else {
            slideUp(myView);
            myButton.setText("Slide down");
        }
        isUp = !isUp;
    }
}
