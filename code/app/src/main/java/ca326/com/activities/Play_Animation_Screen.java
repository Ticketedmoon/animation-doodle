package ca326.com.activities;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;

import java.util.List;
import java.util.Map;

public class Play_Animation_Screen extends AppCompatActivity {

    // Normal fields
    private CanvasViewNonEditable cv;
    private Integer pos;
    private boolean playButton = true;

    public static Map<Integer, List <Pair<Path, Paint>>> pathways = Start_Drawing_Screen.pathways;

    // Handlers / Timed events
    private Handler m_handler;
    private Runnable m_handlerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play__animation__screen);

        // Initialisation
        this.pos = 0;
        this.cv = (CanvasViewNonEditable) findViewById(R.id.canvas2);

        play_animation(cv);
    }

    // THIS FUNCTION IS NOT WORKING CORRECTLY -> REMEMBER TO FIX
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
                cv.invalidate();
                pos++;

                // Delay needs to be here for pause / play button reasons
                // instead of 1000 mention the delay in milliseconds
                m_handler.postDelayed(m_handlerTask, 500);

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
            m_handler.removeCallbacks(m_handlerTask);
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
}
