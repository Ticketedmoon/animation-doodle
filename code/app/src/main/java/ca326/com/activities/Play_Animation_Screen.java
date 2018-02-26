package ca326.com.activities;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;

import java.util.List;
import java.util.Map;

public class Play_Animation_Screen extends AppCompatActivity {

    // Normal fields
    private CanvasViewNonEditable cv;
    private Integer pos;
    public static Integer frame_rate;

    public static Map<Integer, List <Pair<Path, Paint>>> pathways = Start_Drawing_Screen.pathways;

    // Handlers / Timed events
    private Handler m_handler;
    private Runnable m_handlerTask;
    //private BottomSheetBehavior mBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play__animation__screen);

        // Initialisation
        this.pos = 0;
        this.cv = (CanvasViewNonEditable) findViewById(R.id.canvas2);
        //View bottomSheet = findViewById(R.id.play_bar);

        /* mBottomSheet = BottomSheetBehavior.from(bottomSheet);
        mBottomSheet.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.i("BottomSheet", "Collapsed");
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.i("BottomSheet", "Dragging");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.i("BottomSheet", "Expanded");
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.i("BottomSheet", "Hidden");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.i("BottomSheet", "Settling");
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        */
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
}
