package ca326.com.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Play_Animation_Screen extends AppCompatActivity {

    // Normal
    private CanvasView cv;
    private Integer pos;

    // Handlers / Timed events
    private Handler m_handler;
    private Runnable m_handlerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play__animation__screen);

        // Initialisation
        this.pos = Start_Drawing_Screen.pos;

        // Identify views
        this.cv = (CanvasView) findViewById(R.id.canvas);

        // methods
        play_animation();

    }

    public void play_animation() {
        // Remember Frame user is on & Time
        pos = 0;

        // Play Animation Begin Logic
        m_handler = new Handler();
        m_handlerTask = new Runnable()
        {
            public void run() {
                //Put code here to run after 1 seconds
                m_handler.postDelayed(m_handlerTask, 500); // instead of 1000 mention the delay in milliseconds
                cv.newPaths = Start_Drawing_Screen.pathways.get(pos);

                cv.invalidate();
                pos++;

                if (pos == Start_Drawing_Screen.frames.size()) {
                    m_handler.removeCallbacks(m_handlerTask);
                    System.out.println("-- Animation Play End --");
                }

            }
        };
        m_handlerTask.run();
        this.pos = 0;
    }
}
