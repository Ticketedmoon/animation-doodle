package ca326.com.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Start_Drawing_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    // Views
    private CanvasView canvasView;
    private RelativeLayout menu;

    // Object creations
    private Paint mDefaultPaint;
    private ImageButton colour_picker;
    private MyRecyclerViewAdapter adapter;

    // Animation & Timeline Logic
    public static Integer pos = 0;
    private List<Integer> frames = new ArrayList<Integer>();
    private List<String> frameNums = new ArrayList<String>();
    private static String value;

    //IMPORTANT
    public Map<Integer, List <Pair<Path, Paint>>> pathways = new HashMap<Integer, List<Pair <Path, Paint>>>();

    // Login Credentials
    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    //Onion skinning button
    public static boolean onionSkinning = true;
    ImageButton onionButton;

    // Other Fields
    ImageButton play;
    public static boolean playButton = true;
    private boolean button_colour_swap = false;

    // Handlers / Timed events
    Handler m_handler;
    Runnable m_handlerTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This function below creates the nice fade in / out transition between activities.
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start__drawing__screen);

        // Drawing Functionality
        this.canvasView = (CanvasView) findViewById(R.id.canvas);
        this.menu = (RelativeLayout) findViewById(R.id.layout_menu);
        this.onionButton = (ImageButton)findViewById(R.id.onionSkinningButton);
        this.play = (ImageButton)findViewById((R.id.play_button));

        // END

        // Colour Picker Stuff
        this.mDefaultPaint = canvasView.mPaint;
        this.colour_picker = (ImageButton) findViewById(R.id.change_colour);
        this.colour_picker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // If the swap colour button is pressed then:
                openColourPick();
            }
        });


        // set up the RecyclerView
        this.setUpTimeline();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.frames);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(Start_Drawing_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new MyRecyclerViewAdapter(this, frames, frameNums);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        // END

        onionButton.setImageResource(R.drawable.onion);
        onionButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                //add onion layers
                if (onionSkinning) {
                    for (int i = 0; i < pathways.size(); i++) {
                        if (pos!=0) {
                            canvasView.newPaths.addAll(pathways.get(pos - 1));
                            canvasView.invalidate();
                        }
                    }
                    // here we will make the button fade out to indicate onion skinning feature is turned on
                    onionButton.setImageResource(R.drawable.onion);
                }
            }
        });
    }

    // When clicking a frame on the timeline, update some parameters
    public void onItemClick(View view, int position) {
        if (this.pos != position) {
            Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
            this.pathways.put(this.pos, this.canvasView.newPaths);
            this.canvasView.newPaths = this.pathways.get(position);

            this.pos = position;
            this.canvasView.invalidate();
        }

        // Debugging Function, Useful
        test();

    }

    private void test() {
        for(int i = 0; i < pathways.size(); i++) {
            System.out.println("========= DEBUGGING ========\nKey: " + i + "\nValue: " + this.pathways.get(i));
            System.out.println("========= END DEBUGGING ========");
        }
    }

    // Disable back button on this screen
    public void onBackPressed() {
        System.out.println("Back Button Pushed <Returning to Homescreen>");
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);

    }

    public void save_external(View v) {
        System.out.println("Pushed Save Button");
        get_file_input(this.canvasView);
        // Add and delete tmp file
        save("tmp");
        File file = new File("/sdcard/Animation_Doodle_Images/tmp.jpg");
        boolean deleted = file.delete();
        System.out.println("tmp deleted: " + deleted);
        // Print out to log
        System.out.println("File Saved");
    }

    private void save(String store_name) {

        verifyStoragePermissions(this);
        // Bitmap bitmap = Bitmap.createBitmap(this.canvasView.width, this.canvasView.height, Bitmap.Config.ARGB_8888);
        // this.canvasView.getDrawingCache();

        try {
            this.canvasView.setDrawingCacheEnabled(true);
            Bitmap bitmap = this.canvasView.getDrawingCache();
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            this.canvasView.draw(canvas);

            File f = null;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(),"Animation_Doodle_Images");
                if(!file.exists()){
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath()+ file.separator + store_name +".jpg");
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, ostream);
            ostream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void get_file_input(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter a filename");
        alert.setMessage("Background Title: ");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString();
                // Do something with value!
                save(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.create().show();
    }

    public void clearCanvas(View v) {
        this.canvasView.clearCanvas();
    }

    public void undo(View v) {
        this.canvasView.undoAction();
    }

    public void openColourPick() {
        AmbilWarnaDialog colour_picker = new AmbilWarnaDialog(this, this.mDefaultPaint.getColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {

            public void onCancel(AmbilWarnaDialog dialog) {}
            public void onOk(AmbilWarnaDialog dialog, int color) {
                mDefaultPaint = new Paint();
                canvasView.setUpPaint(color, mDefaultPaint);
            }
        });
        colour_picker.show();
    }

    private void setUpTimeline() {
        // data to populate the RecyclerView with
        Integer tmp = R.drawable.frame;

        frames.add(tmp);
        frames.add(tmp);
        frames.add(tmp);
        frames.add(tmp);
        frames.add(tmp);
        frames.add(tmp);

        frameNums.add("Frame 1");
        frameNums.add("Frame 2");
        frameNums.add("Frame 3");
        frameNums.add("Frame 4");
        frameNums.add("Frame 5");
        frameNums.add("Frame 6");

        for(int i = 0; i < frames.size(); i++) {
            List <Pair <Path, Paint>> emptyArr =  new ArrayList <Pair <Path, Paint>>();
            this.pathways.put(i, emptyArr);
        }

    }


    // Left Arrow & Right Arrow pushed
    // goLeft brings the user to the sign-in / profile screen for viewing
    // If already signed in, bring to profile... (We can make an IF statement check later to see if they are signed in)
    // Sync both activities
    // Animations

    public void goLeft(View view){
        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Intent intent;
        if(mSharedPreferences.contains(PREF_EMAIL)&& mSharedPreferences.contains(PREF_PASSWORD)) {
            intent = new Intent(Start_Drawing_Screen.this, Profile_Screen.class);
        }
        else{
            intent = new Intent(Start_Drawing_Screen.this, Register_Screen.class);
        }
        startActivity(intent);
        finish();
    }

    public void goRight(View view){
        Intent intent = new Intent (Start_Drawing_Screen.this, Top_Rated_Screen.class);
        startActivity(intent);
    }



    // Sync both activities
    // goRight brings the user to the top-rated animations page
    // Only signed in users can upload, rate etc...


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Check Storage permissions (Mandatory)
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    // TOOL BAR MODIFICATIONS
    public void open_menu(View v) {
        if (!this.button_colour_swap)
            this.menu.setVisibility(View.VISIBLE);
        else
            this.menu.setVisibility(View.INVISIBLE);

        this.button_colour_swap = !(this.button_colour_swap);
    }

    public void play_animation(View v) {
        // Logcat Information
        if (playButton) {
            playButton = false;
            play.setImageResource(R.drawable.pause);
            System.out.println("Play Button Pushed\nPlaying Animation");

            // Play Animation Begin Logic
            m_handler = new Handler();
            m_handlerTask = new Runnable() {
                public void run() {
                    //Put code here to run after 1 seconds
                    m_handler.postDelayed(m_handlerTask, 500); // instead of 1000 mention the delay in milliseconds

                    canvasView.newPaths = pathways.get(pos);
                    canvasView.invalidate();
                    pos++;
                    if (pos == frames.size()) {
                        pos = 0;
                    }
                }
            };
            m_handlerTask.run();
            this.pos = 0;
        }
        else {
            // Keep this, needed to make sure first frame isn't removed.
            this.pos = 0;
            canvasView.newPaths = pathways.get(pos);
            canvasView.invalidate();
            // END

            play.setImageResource(R.drawable.play);
            m_handler.removeCallbacks(m_handlerTask);
            playButton = true;
        }
    }

    public void previous_frame(View v) {
        Integer currentIndex = this.pos;
        List<Pair <Path, Paint>> mixed_frame = new ArrayList<>();

        if (currentIndex > 0) {
            List<Pair<Path, Paint>> prev_frame = pathways.get(currentIndex-1); // -1 for previous version
            // Combine Both the previous frame with the current frame
            mixed_frame.addAll(this.canvasView.newPaths);
            mixed_frame.addAll(prev_frame);
        }
        this.canvasView.newPaths = mixed_frame;
        canvasView.invalidate();
    }
}
