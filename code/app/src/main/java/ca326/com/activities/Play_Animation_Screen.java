package ca326.com.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import static ca326.com.activities.Start_Drawing_Screen.drawables;

public class Play_Animation_Screen extends AppCompatActivity {

    // Private fields
    private CanvasViewNonEditable cv;
    private Integer pos;
    private RelativeLayout myView;
    private RelativeLayout myTopView;

    private Boolean isUp;
    private Boolean isPlaying = true;

    private SeekBar seek;
    private ImageButton change_canvas;
    private ImageButton pause_play_btn;

    // Public Static
    public static Integer frame_rate;
    public static Map<Integer, List <Pair<Path, Paint>>> pathways = Start_Drawing_Screen.pathways;

    // Handlers / Timed events
    private Handler m_handler;
    private Runnable m_handlerTask;
    private RelativeLayout play_canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.exit_to_left);
        setContentView(R.layout.activity_play__animation__screen);

        // Initialisation
        this.pos = 0;
        this.cv = (CanvasViewNonEditable) findViewById(R.id.canvas2);

        // Background adjustment
        play_canvas = (RelativeLayout) findViewById(R.id.play_canvas);
        play_canvas.setBackgroundResource(0);

        // Create important textViews
        TextView valueTV = new TextView(this);
        valueTV.setText(Start_Drawing_Screen.ANIMATION_TITLE);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/CaviarDreams.ttf");
        valueTV.setTypeface(face);
        valueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        valueTV.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        myView = findViewById(R.id.my_view_bottom);
        myView.setVisibility(View.VISIBLE);

        myTopView = findViewById(R.id.my_view_top);
        myTopView.setVisibility(View.VISIBLE);
        myTopView.setGravity(Gravity.CENTER);
        myTopView.addView(valueTV);

        // Pause / Play Button (DONE)
        pause_play_btn = (ImageButton) findViewById(R.id.playPauseButton);
        pause_play_btn.setBackgroundColor(Color.TRANSPARENT);
        pause_play_btn.setImageResource(R.drawable.exomedia_ic_pause_white);
        pause_play_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (isPlaying)
                    pause_play_btn.setImageResource(R.drawable.exomedia_ic_play_arrow_white);
                else
                    pause_play_btn.setImageResource(R.drawable.exomedia_ic_pause_white);

                isPlaying = !isPlaying;
            }
        });

        // Change Canvas Button
        change_canvas = (ImageButton) findViewById(R.id.changeBG);
        change_canvas.setBackgroundColor(Color.TRANSPARENT);
        change_canvas.setImageResource(R.drawable.change_bg_click);
        change_canvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence colors[] = new CharSequence[] {"Blank Canvas", "Crumpled Paper", "Ancient Scroll", "School Paper"};
                isPlaying = false;
                pause_play_btn.setImageResource(R.drawable.exomedia_ic_play_arrow_white);
                AlertDialog.Builder builder = new AlertDialog.Builder(cv.getContext());
                builder.setTitle("Choose Animation Background");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        Log.i("Choice Dialog", which + " <--");
                        switch (which) {
                            case 0:
                                play_canvas.setBackgroundResource(0);
                                break;
                            case 1:
                                play_canvas.setBackground(getDrawable(R.drawable.paper));
                                break;
                            case 2:
                                play_canvas.setBackground(getDrawable(R.drawable.ancient_bg));
                                break;
                            case 3:
                                play_canvas.setBackground(getDrawable(R.drawable.school_bg));
                                break;
                            default:
                                Log.i("Choice Dialog", "No Choice Chosen");
                        }

                    }
                });
                builder.show();
            }
        });

        // SeekBar manipulations
        seek = (SeekBar) findViewById(R.id.playAnimationSeekBar);
        seek.setMax(pathways.size()-1);
        seek.setProgress(0);
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
                Log.i("Play Animation SeekBar", "SeekBar no longer touched");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
                Log.i("Play Animation SeekBar", "SeekBar touched");
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                //add code here
                Log.i("Play Animation SeekBar", "SeekBar slider moved!");
                Log.i("Play Animation SeekBar", "Value: " + (progress));

                if (progress != seekBark.getMax()) {
                    pos = progress;
                    cv.newPaths = pathways.get(pos);
                    cv.invalidate();
                }
            }
        });

        isUp = true;

        // Runnable post delayed events.
        // play animation once screen is fully loaded
        cv.postDelayed(new Runnable() {

            @Override
            public void run() {
                play_animation(cv);
            }
        }, 1000);

        // Hide Both nav bars after 1.5 seconds
        cv.postDelayed(new Runnable() {

            @Override
            public void run() {
                onSlideViewClick(cv);
            }
        }, 2500);
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
                Log.i("Running", "isPlaying: " + isPlaying);
                if (isPlaying) {
                    seek.setProgress(pos);
                    System.out.println(pos.toString() + "----" + Integer.toString(pathways.size()));
                    cv.newPaths = pathways.get(pos);
                    cv.setBackground(drawables.get(0));
                    cv.invalidate();
                    pos++;
                }
                    // Delay needs to be here for pause / play button reasons
                    // instead of 1000 mention the delay in milliseconds
                    m_handler.postDelayed(m_handlerTask, frame_rate);

                    // Doesn't require {} body since only 1 line following the IF conditional.
                    if (pos == pathways.size())
                        pos = 0;
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
    public void slideUpBottom(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDownBottom(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void slideUpTop(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,
                -view.getHeight());             // toYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDownTop(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                -view.getHeight(),  // fromYDelta
                0);                 // fromYDelta
        animate.setDuration(300);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewClick(View view) {
        if (isUp) {
            slideDownBottom(myView);
            slideUpTop(myTopView);
        } else {
            slideUpBottom(myView);
            slideDownTop(myTopView);
        }
        isUp = !isUp;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == android.view.MotionEvent.ACTION_UP)
            onSlideViewClick(cv);

        Log.i("OnTouch", "Delaying...");
        return true;
    }
}
