package ca326.com.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bq.markerseekbar.MarkerSeekBar;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Start_Drawing_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private static boolean temp = false;

    // Saving recycle view (Timeline) functionality
    private RecyclerView timeline_frames;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private LinearLayoutManager horizontalLayoutManager;
    private Parcelable mListState = null;

    // Views
    public static CanvasView canvasView;
    public static  CanvasView canvasView2;
    private RelativeLayout menu;

    // Object creations
    private Paint mDefaultPaint;
    private ImageButton colour_picker;
    public static MyRecyclerViewAdapter adapter;
    public MarkerSeekBar pen_size_adjuster;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Animation & Timeline Logic
    public static Integer pos = 0;
    private Integer frame_counter = 1;
    public static List<Integer> frames = new ArrayList<Integer>();
    private List<String> frameNums = new ArrayList<String>();

    // Input from user stored in these variables
    public static Integer frame_rate_value = 2;
    public static String ANIMATION_TITLE = null;

    // IMPORTANT
    public static Map<Integer, List <Pair<Path, Paint>>> pathways = new HashMap<Integer, List<Pair <Path, Paint>>>();

    // Login Credentials
    public static SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_ANIMATION_NAME = "animationName";

    // Image Buttons / Buttons
    private ImageButton play;
    private ImageButton ham_menu;
    private ImageButton add_frame;
    private Button background;

    // just used to check if its a video being uploaded for onStartActivity
    private static final int video_code = 1;
    private Bitmap tmp;
    public static String videoPath;
    public static String imagePath;
    private String backgroundPath;
    public static String video_description;
    private File newfile = null;
    private Random rand = new Random();

    public static Integer limitCounter=0;

    // Other Fields
    private boolean is_menu_open = false;
    private String backgroundTitle;
    public static int image_counter = 1;
    private int canvas_height;
    public int pen_size;

    private String value;

    public static Bitmap bitmap;
    public static Bitmap newBitmap;
    public static Drawable myDrawable;
    public static Drawable newDrawable;
    public Integer i=0;

    private ImageView imageView;

    public Context context;

    public static Map<Integer, Drawable> drawables = new HashMap<>();

    public static Integer adapterPosition = 200;
    public static boolean set = false;
    public static boolean set2 = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This function below creates the nice fade in / out transition between activities.
        overridePendingTransition(R.anim.fadein, R.anim.exit_to_left);
        setContentView(R.layout.activity_start__drawing__screen);

        imageView = (ImageView) findViewById(R.id.imageView);

        // Drawing Functionality
        this.canvasView = (CanvasView) findViewById(R.id.canvas);
        this.canvasView2 = (CanvasView) findViewById(R.id.canvas);
        this.menu = (RelativeLayout) findViewById(R.id.layout_menu);
        this.timeline_frames = (RecyclerView) findViewById(R.id.frames);
        this.play = (ImageButton) findViewById((R.id.play_button));
        // END

        // Image button / Button onClick Listeners (For Style effects)
        this.ham_menu = (ImageButton) findViewById(R.id.menu);
        this.ham_menu.setBackgroundColor(Color.TRANSPARENT);

        //Background button
        this.background = (Button) findViewById(R.id.set_background);

        // PLAY IMAGE BUTTON
        this.play = (ImageButton) findViewById(R.id.play_button);
        this.play.setBackgroundColor(Color.TRANSPARENT);
        this.play.setImageResource(R.drawable.play_background_colour);

        // Add Frame btn settings
        this.add_frame = (ImageButton) findViewById(R.id.add_frame_btn);
        this.add_frame.setBackgroundColor(Color.TRANSPARENT);
        this.add_frame.setImageResource(R.drawable.add_frame_bg);

        // Colour Picker Stuff
        this.mDefaultPaint = canvasView.mPaint;
        this.mDefaultPaint = canvasView2.mPaint;
        this.colour_picker = (ImageButton) findViewById(R.id.change_colour);
        this.colour_picker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // If the swap colour button is pressed then:
                openColourPick();
            }
        });

        // set up the RecyclerView
        add_frame(timeline_frames);

        // Seekbar (Change Pen Size)
        this.pen_size_adjuster = (MarkerSeekBar) findViewById(R.id.seekbar);
        this.pen_size_adjuster.setMarkerAnimationFrame(pen_size);
        this.pen_size = 8;
        this.pen_size_adjuster.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pen_size = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Log.i("Seekbar", "Started Changing Pen Size");

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                Log.i("Seekbar", "Finished Changing Pen Size");
                Log.i("Seekbar", "Pen Size: " + pen_size);

                // Update the canvasView paint stuff
                canvasView.adjustPenSize(pen_size);
            }
        });

        // Get Animation Title upon load since ASYNC by nature
        // Must delay it to let the pending transition finish (OverridePendingTransition)
        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Log.i("shared pref","pref name is " + mSharedPreferences.getAll());
        String temp = mSharedPreferences.getString("animationName",null);
        Log.i("shared pref", "TEMP: " + temp);

        if(temp==null) {
            canvasView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    get_animation_name();
                }
            }, 1000);
        }
        background.setText("Set Background");

        // Log settings tab by user for modifications
        Log.i("Settings: ", "Onion Skin Visibility? -- " + canvasView.settings_onion_skin);
    }

    // MAINTAIN / SAVE TIMELINE AFTER LEAVING ACTIVITY
    // Data is remembered however you must Re-apply the frames to timeline. (FUNCTIONING)
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.i("RESTORE TIMELINE", "SAVED INSTANCE");

        // Save list state
        mListState = horizontalLayoutManager.onSaveInstanceState();
        state.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.i("RESTORE TIMELINE", "RESTORE INSTANCE");

        // Retrieve list state and list/item positions
        if (state != null) {
            Log.i("RESTORE TIMELINE", "RESTORE INSTANCE NOT NULL *!*");
            mListState = state.getParcelable(KEY_RECYCLER_STATE);
        }
    }

    protected void onResume() {
        super.onResume();
        Log.i("RESTORE TIMELINE", "RESUMED INSTANCE");

        if (mListState != null) {
            Log.i("RESTORE TIMELINE", "RESUMED INSTANCE NOT NULL *!*");
            horizontalLayoutManager.onRestoreInstanceState(mListState);
        }
        else{
            Log.i("RESTORE TIMELINE", "RESUMED INSTANCE NOT NULL *!*");
            horizontalLayoutManager.onRestoreInstanceState(mListState);
            Log.i("RESTORE TIMELINE", "PATHWAYS: " + pathways);
            // READD EVERYTHING
            for(int i = 1; i < pathways.size(); i++) {
                add_frame(canvasView);
            }
        }
    }
    // END SAVE TIMELINE

    // When clicking a frame on the timeline, update some parameters
    public void onItemClick(View view, int position) {
        int correct_onion_frame;

        if (position == 0)
            correct_onion_frame = position;
        else
            correct_onion_frame = position-1;

        if (this.pos != position) {
            // Bitmap is hidden inside the method before the bitmap is saved.
            change_current_frame(position, correct_onion_frame);
            // Reshow Bitmap after bitmap saved
            canvasView.shouldShowOnionSkin = true;
        }
        else {
            assignTimelineIcon(position);
        }
    }

    private void change_current_frame(int position, int correct_onion_frame) {
        // Destroy previous onion cache
        this.canvasView.onionPaths.clear();
        if (background.getText().equals("Remove Background")) {
            this.canvasView2.setBackground(newDrawable);
        }
        else{
            this.canvasView2.setBackground(null);
        }

        // Add Onion Layering Functionality (Transparent previous frame, don't include in actual drawing)
        this.pathways.put(this.pos, this.canvasView.newPaths);
        this.canvasView.newPaths = this.pathways.get(position);
        // Set all paint objects to opaque.

        // If on Frame 0 (Don't onion skin)
        if (position != 0) {
            List<Pair<Path, Paint>> onionSkin = get_onion_skin(correct_onion_frame);
            Log.i("Onion Layering", "onions: " + onionSkin);
            // Display Onion layer
            this.canvasView.onionPaths.addAll(onionSkin);
        }
        this.canvasView.invalidate();

        // Hide Onion layer in bitmap frames
        canvasView.shouldShowOnionSkin = false;
        assignTimelineIcon(position);
        i++;

        this.pos = position;


    }

    private void assignTimelineIcon(int position) {
        if (!temp) {
            this.canvasView.setDrawingCacheEnabled(true);
            bitmap = this.canvasView.getDrawingCache();
            adapterPosition = position;
            myDrawable = new BitmapDrawable(getResources(), bitmap);
            adjust_timeline();
        }
        else{
            adapterPosition = 100;
            adapterPosition = position;
            myDrawable = new BitmapDrawable(getResources(), bitmap);
            adjust_timeline();
            adapterPosition = 99;
        }
    }

    // Save Animation Function, takes all frames in canvas
    // Converts them to bitmaps and encodes them to mp4.
    public void download_animation(View v) {
        // Create Directories if they already haven't been created
        build_local_directories();

        Log.i("Save Animation","Beginning Encoded / Decoding (Saving Animation /sdcard/AnimationDoodle");

        // Assign pathways (IMPORTANT)
        pathways.put(pos, this.canvasView.newPaths);
        checkCanvasSize(canvasView);

        // Extract all images (Bitmaps)
        verifyStoragePermissions(this);
        ArrayList<Bitmap> bit_frames = new ArrayList<Bitmap>();

        // Store canvas_width (Some reason gets overwritten, so store and then restore)
        canvas_height = canvasView.height;

        Log.i("Download", "Bitmap Conversion Starting...");
        for(int i = 0; i < pathways.size(); i++) {
            canvasView.newPaths = pathways.get(i);
            Bitmap frameX = prepare_animation_frame();
            bit_frames.add(frameX);
        }

        // restore canvasView.newPath before the loop
        canvasView.newPaths = pathways.get(pos);
        canvasView.invalidate();

        Log.i("Download", "Bitmap Conversion Complete");
        Log.i("Download", "Starting JPEG Conversion...");

        // ASYNC TASK (Save images in AnimationDoodle/Temp directory
        DownloadFrameTask download_images = new DownloadFrameTask(bit_frames, this);
        download_images.execute();
    }

    private void checkCanvasSize(View v) {
        // Method is just used to statically adjust width/height values to be even
        // Even values necessary for video encoding.
        if (canvasView.width % 2 != 0)
            canvasView.width++;

        if (canvasView.height % 2 != 0)
            canvasView.height++;
    }

    public Bitmap loadBitmapFromView(View v) {
        System.out.println(canvasView.width + "--" + canvasView.height);
        Bitmap b = Bitmap.createBitmap(canvasView.width, canvasView.height, Bitmap.Config.RGB_565);
        Log.i("Downloading", "OWidth: " + b.getWidth() + " --- " + "OHeight: " +  b.getHeight());

        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);   // Essential
        v.draw(c);
        return b;
    }

    private void adjust_timeline() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.frames);
        horizontalLayoutManager
                = new LinearLayoutManager(Start_Drawing_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new MyRecyclerViewAdapter(this, frames, frameNums);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    // Testing function
    public static CanvasView getCView() {
        return canvasView;
    }

    public void save_external(View v) {
        build_local_directories();
        Log.i("Save", "Pushed Save Button");

        // Store canvas_width (Some reason gets overwritten, so store and then restore)
        canvas_height = canvasView.height;

        // ASYNC call to file name
        get_background_name(this.canvasView);
    }

    private void save(String store_name) {
        build_local_directories();
        Log.i("Download Image", "Canvas Width: " + canvasView.width + " -- " + "Canvas Height: " + canvasView.height );

        try {
            // Hide onion skins
            canvasView.shouldShowOnionSkin = false;
            canvasView.invalidate();

            // Restore
            canvasView.height = canvas_height;

            // Method which creates the correct bitmap
            Bitmap bitmap = loadBitmapFromView(canvasView);

            File f = null;
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle/Backgrounds");
                f = new File(file.getAbsolutePath()+ file.separator + store_name +".jpg");
            }
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);

            //Reshow Onion Skin
            canvasView.shouldShowOnionSkin = true;
            outputStream.close();

           Toast toast = Toast.makeText(this, "Background Saved!", Toast.LENGTH_SHORT);
           toast.setGravity(Gravity.CENTER, 0, 0);
           toast.show();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Bitmap prepare_animation_frame() {
        Log.i("Download Image", "Canvas Width: " + canvasView.width + " -- " + "Canvas Height: " + canvasView.height );
        // Hide onion skins
        canvasView.shouldShowOnionSkin = false;
        canvasView.invalidate();

        // Restore
        canvasView.height = canvas_height;

        // Method which creates the correct bitmap
        Bitmap bitmap = loadBitmapFromView(canvasView);

        //Reshow Onion Skin
        canvasView.shouldShowOnionSkin = true;

        return bitmap;
    }

    public void get_background_name(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter a filename");
        alert.setMessage("Background Title: ");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        final int prev_h = canvasView.height;

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("Save", "Picture (" + image_counter + ") saved");
                backgroundTitle = input.getText().toString();
                image_counter++;

                // Do something with backgroundTitle!
                save(backgroundTitle);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.create().show();
    }

    // Adapt this function later to handle Integers rather than Strings more efficiently.
    public void get_file_input_frame_rate(View v) {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this, R.style.custom_alert_dialog);
        final MarkerSeekBar seek = new MarkerSeekBar(this);
        final int old_frame_rate = frame_rate_value;

        seek.setMax(30);
        seek.setProgress(frame_rate_value);

        popDialog.setIcon(R.drawable.frame_rate);
        popDialog.setTitle("Frame rate manager");
        popDialog.setMessage("Readjust your animation frame rate");
        popDialog.setView(seek);
        popDialog.setCancelable(true);

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
                Log.i("Frame Rate Button", "SeekBar no longer touched");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
                Log.i("Frame Rate Button", "SeekBar touched");
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                //add code here
                Log.i("Frame Rate Button", "SeekBar slider moved!");
                Log.i("Frame Rate Button", "Value: " + progress);
                if (progress != 0)
                    frame_rate_value = progress;
                else
                    frame_rate_value = 1;
            }
        });
        // Button OK
        popDialog.setPositiveButton("Accept",
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        popDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        frame_rate_value = old_frame_rate;
                    }
                });
        popDialog.create();
        popDialog.show();
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
                canvasView.setUpPaint(color, mDefaultPaint, pen_size);
                canvasView.colour = color;
            }
        });
        colour_picker.show();
    }

    // Check Storage permissions (Mandatory)
    public void verifyStoragePermissions(Activity activity) {
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

    public void set_video_description(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Enter a brief description of video (200 characters Max)");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);
        final int prev_h = canvasView.height;

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                value = input.getText().toString();
                image_counter++;
                video_description = value;
                upload();

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled.
            }
        });

        alert.create().show();
    }

    public void upload(){
        UploadVideo upload = new UploadVideo(this);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        //clear the shared preferences for the animation name
        mEditor.remove("animationName");
        mEditor.apply();
        Log.i("shared pref","new shared "+mSharedPreferences.getAll());
        upload.execute();
    }

    public void choose(View v) {
        SharedPreferences mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String check = mSharedPreferences.getString(PREF_EMAIL,null);
        Log.i("check","is "+ check);
        if (check == null) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please sign in or register to upload", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            return;
        }

        /*
        if (response == "5") {
            Toast toast = Toast.makeText(getApplicationContext(), "Reached your limit of uploaded videos!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        } else {
            limitCounter++;
            chooseImage();
        }
        */
        limitCounter=5;
        chooseImage();

    }
    public void chooseImage(){
        // the thumbnail of the video will be the frame halfway through the animation

        //this next line remembers the canvas that the user is on when uploading video so it doesn't get overwritten by the thumbnail
        this.pathways.put(this.pos, this.canvasView.newPaths);

        //first check if there is more than 1 frame in animation
        //if not then obviously the thumbnail is the first frame
        if (this.pathways.size() > 1) {
            System.out.println("size of list is " + this.pathways.size());
            Integer middle = (this.pathways.size() / 2) - 1;
            this.canvasView.newPaths = this.pathways.get(middle);
            System.out.println("size of list is " + this.canvasView.newPaths.size());
            //System.out.println("size of list is " + this.canvasView.newPaths.get(middle));
        }
        try {

            int value = rand.nextInt(10000);

            this.canvasView.setDrawingCacheEnabled(true);
            tmp = this.canvasView.getDrawingCache();
            Canvas canvas = new Canvas(tmp);
            canvas.drawColor(Color.WHITE);
            this.canvasView.draw(canvas);

            //now retrieve the image for the right frame
            this.canvasView.newPaths = this.pathways.get(this.pos);
            this.canvasView.invalidate();


            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle/Temp");

                //// make the file name the same as the video file name + .jpg to differentiate
                //// will change this later when save feature is done
                newfile = new File(file.getAbsolutePath()+ file.separator + "imageName" + value +".jpg");
                System.out.println("file path is " + newfile);
            }
            FileOutputStream outputStream = new FileOutputStream(newfile);
            tmp.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);
            outputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        chooseVideo();
    }

    public void onBackPressed() {
        System.out.println("Back Button Pushed <Returning to Homescreen>");
        Intent startMain = new Intent(Start_Drawing_Screen.this, Main_Menu_Screen.class);
        startActivity(startMain);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (set){
                Uri imageUri = data.getData();
                backgroundPath = getImagePath(imageUri);
                set=false;
                set_background(backgroundPath);
                return;
            }
            else if (!set & requestCode == video_code) {
                Uri imageUri = data.getData();
                System.out.println("image uri is : " + imageUri);
                videoPath = getPath(imageUri);
                imagePath = newfile.toString();
                System.out.println("imagePath == " + imagePath);
            }
        }

        set_video_description();

    }

    private void build_local_directories() {
        // BASE DIRECTORY
        File baseDirectory = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle");
        if(!baseDirectory.exists()){
            baseDirectory.mkdirs();
        }

        // IMPORTABLE BACKGROUNDS DIRECTORY
        File file = new File(Environment.getExternalStorageDirectory(),"AnimationDoodle/Backgrounds");
        if(!file.exists()) {
            file.mkdirs();
        }

        // TEMP IMAGE HOLDERS (USED TO ENCODE VIDEO) DIRECTORY
        File tempDirectory = new File(Environment.getExternalStorageDirectory(), "AnimationDoodle/Temp");
        if (!tempDirectory.exists()) {
            tempDirectory.mkdirs();
        }

        // ANIMATIONS DIRECTORY
        File animationDirectory = new File(Environment.getExternalStorageDirectory(), "AnimationDoodle/Animations");
        if (!animationDirectory.exists()) {
            animationDirectory.mkdirs();
        }

        Log.i("Building...", "Directories Built");
    }

    public void restart_animation(View v) {
        // Clear both pathways & frames
        Log.i("Restart Animation", "frames: " + frames.size());
        Log.i("Restart Animation", "Pathways: " + pathways.size());

        new FancyGifDialog.Builder(this)
                .setTitle("Animation Restart Manager")
                .setMessage("Are you sure you want to restart your animation?")
                .setNegativeBtnText("Cancel")
                .setPositiveBtnBackground("#FF4081")
                .setPositiveBtnText("Accept")
                .setNegativeBtnBackground("#FFA9A7A8")
                .setGifResource(R.drawable.gif16)   //Pass your Gif here
                .isCancellable(true)
                .OnPositiveClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                        reset_timeline();
                    }
                })
                .OnNegativeClicked(new FancyGifDialogListener() {
                    @Override
                    public void OnClick() {
                    }
                })
                .build();
    }

    public void reset_timeline() {
        // Clear Frames, FrameNums, Pathways & drawables
        // Also reset frame_counter back to 1 to restart.
        frames.clear();
        frameNums.clear();
        pathways.clear();
        drawables.clear();
        canvasView.onionPaths.clear();

        adapter.notifyDataSetChanged();
        timeline_frames.invalidate();

        frame_counter = 1; // Reset back to 1
        pos = 0;

        Log.i("Restart Animation", "Restarting... \nFrames reset\n" +
                "frameNums reset\nPathways reset\ndrawables reset\nframe_counter reset to 1");

        // Remove previous Animation Name
        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String temp = mSharedPreferences.getString("animationName",null);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();

        //clear the shared preferences for the animation name
        mEditor.remove("animationName");
        mEditor.apply();

        canvasView.newPaths.clear();

        //remove the final drawable background
        background.setText("Set Background");
        this.canvasView2.setBackground(null);

        canvasView.invalidate();

        // Add a new frame to begin with
        add_frame(timeline_frames);

        //  Prompt for new Animation Title
        get_animation_name();

        // DONE
    }

    public void chooseVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()
                + "/AnimationDoodle/Animations/");
        intent.setDataAndType(uri, "video/*");
        startActivityForResult(Intent.createChooser(intent, "Select a video "), video_code);

    }

    // TOOL BAR MODIFICATIONS
    public void shift_menu(View v) {
        if (!this.is_menu_open) {
            this.menu.setVisibility(View.VISIBLE);
            this.ham_menu.setImageResource(R.drawable.button_background_colour_inverse);
        }
        else {
            this.menu.setVisibility(View.INVISIBLE);
            this.ham_menu.setImageResource(R.drawable.button_background_colour);
        }

        this.is_menu_open = !(this.is_menu_open);
    }

    public void play_animation(View v) {
        // First Assign frame_rate (Default 1.5 frames per second)
        Play_Animation_Screen.frame_rate = (int) (1000 / (frame_rate_value));
        System.out.println("Frame rate set at: " + Integer.toString(Play_Animation_Screen.frame_rate));

        // Close Menu if open
        if (this.is_menu_open)
            shift_menu(menu);

        Log.i("Play Animation Log", "Pathways before begin: " + pathways);
        // Add to pathways current frame -- onItemClick() will handle the rest
        this.pathways.put(this.pos, this.canvasView.newPaths);
        Log.i("Play Animation Log", "Pathways after: " + pathways);

        // Transition to Play_Animation_Activity Intent
        Intent playing = new Intent(Start_Drawing_Screen.this, Play_Animation_Screen.class);
        startActivity(playing);
    }

    public void prompt_frame_rate(View v) {
        // Integer backgroundTitle stored in class field.
        get_file_input_frame_rate(canvasView);
    }

    public void add_frame(View v) {
        // will generate a frame object and frameNo and place it in the timeline.
        // data to populate the RecyclerView with
        Integer tmp = R.drawable.frame;
        frames.add(tmp);
        frameNums.add("Frame " + frame_counter);

        List <Pair <Path, Paint>> emptyArr =  new ArrayList <Pair <Path, Paint>>();

        // It is essential to -1 here from frame_counter.
        // IF STATEMENT IS ESSENTIAL FOR MAINTAINING TIMELINE THROUGH ACTIVITIES
        if(this.pathways.size() <= frame_counter)
            this.pathways.put(frame_counter-1, emptyArr);

        // Change view to next frame
        onItemClick(canvasView, frame_counter-1);

        frame_counter++;
        adjust_timeline();

        Log.i("Add Frame Button", "frames: " + (frames));
        Log.i("Add Frame Button", "Pathways: " + pathways);

    }

    //method called when background is to be set
    public void chooseImage(View v){
        if (background.getText().equals("Set Background")) {
            set = true;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/AnimationDoodle/Backgrounds/");
            Log.i("string","is  " + uri);
            intent.setDataAndType(uri, "image/*");
            //intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "Select an image in backgrounds "), video_code);
        }
        else{
            remove_background();
        }
    }

    public void remove_background(){
        this.canvasView2.setBackgroundColor(Color.WHITE);
        drawables.put(0,null);
        this.canvasView2.invalidate();
        background.setText("Set Background");
    }

    public void set_background(String backpath) {
        temp = false;

        Log.i("file2","file " + backgroundPath);
        String newBackground = backpath;
        File file2 = new File(newBackground);
        BitmapFactory.Options bit = new BitmapFactory.Options();
        Log.i("bitmap","bitmap: " +bit);
        Bitmap background2 = BitmapFactory.decodeFile(file2.getAbsolutePath(),bit);

        newBitmap = background2.copy(Bitmap.Config.ARGB_8888, true);
        Log.i("file2","file " + background);

        // used to test if loading bitmap is working.It is.
        //imageView.setImageBitmap(newBitmap);

        // This method isn't working. Need to figure out whats wrong. Bitmap is loading properly, just not setting on the canvas
        Canvas canvas = new Canvas(background2.copy(Bitmap.Config.ARGB_8888, true));

        newDrawable = new BitmapDrawable(getResources(), newBitmap);
        Log.i("set","set is " + set);
        this.canvasView2.setBackground(newDrawable);
        drawables.put(0,newDrawable);


        this.canvasView2.invalidate();
        background.setText("Remove Background");
    }

    public  List<Pair<Path, Paint>> get_onion_skin(int correct_onion_frame) {
        List<Pair<Path, Paint>> mixed_frame = new ArrayList<>();
        Log.i("Onion Skin", "Altering Paint Objs...");
        // Make sure user isn't on the first frame (No Onion Skin on first frame)
        for (int i = 0; i < pathways.get(correct_onion_frame).size(); i++) {
            Path tmp = pathways.get(correct_onion_frame).get(i).first;
            Paint tmpPaint = new Paint(pathways.get(correct_onion_frame).get(i).second);
            tmpPaint.setAlpha(20);

            Log.i("Onion Skin", "Paths: " + tmp + " -- " + "Paints: " + tmpPaint);
            Pair onion_skin_item = new Pair(tmp, tmpPaint);
            mixed_frame.add(onion_skin_item);
        }

        return mixed_frame;
    }

    public void previous_frame(View v) {
        Integer currentIndex = this.pos;
        List<Pair<Path, Paint>> mixed_frame = new ArrayList<>();

        if (currentIndex > 0) {

            List<Pair<Path, Paint>> prev_frame = pathways.get(currentIndex-1); // -1 for previous version
            mixed_frame.addAll(this.canvasView.newPaths);
            mixed_frame.addAll(prev_frame);


            pathways.put(pos, mixed_frame);
            this.canvasView.newPaths = mixed_frame;
            this.canvasView.invalidate();
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        //this is important to change depending on whether your uploading videos or images

        cursor = getContentResolver().query(
                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        System.out.println("cursor is  " + cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        System.out.println("the path is " + path);
        cursor.close();
        return path;

    }

    private void callStoragePermissions() {
        verifyStoragePermissions(this);
    }

    public String getImagePath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        //this is important to change depending on whether your uploading videos or images

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        System.out.println("cursor is  " + cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        System.out.println("the path is " + path);
        cursor.close();
        return path;

    }

    // Extract title from animation (Using MaterialStyleDialog Library)
    // Stores the title of the animation in ANIMATION_TITLE
    // Pass to ASYNC Download Animation Task after...
    public void get_animation_name() {
        // EMPTY POP UP DIALOG BOX
        Log.i("Dialog Box", "Initiating");
        Log.d("Dialog Box", "Animation name: " + ANIMATION_TITLE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.custom_dialog, null);

        final EditText customText = (EditText) customView.findViewById(R.id.animation_name);
        TextView text_help = (TextView) customView.findViewById(R.id.text_help);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Roboto-Light.ttf");
        Typeface custom_font_med = Typeface.createFromAsset(getAssets(),  "fonts/CaviarDreams.ttf");

        customText.setTypeface(custom_font);
        text_help.setTypeface(custom_font_med);

        // Dialog Box
        new MaterialStyledDialog.Builder(this)
                .setHeaderDrawable(R.drawable.sun_bg)
                .setStyle(Style.HEADER_WITH_ICON)
                .setIcon(R.drawable.black_tiger)

                .withIconAnimation(true)
                // Old standard padding: .setCustomView(your_custom_view, 20, 20, 20, 0)
                .setCustomView(customView, 20, 30, 20, 0)

                .withDialogAnimation(true)
                .withDialogAnimation(true, Duration.SLOW)
                .setPositiveText("Accept")

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        // Build necessary directories
                        Log.d("MaterialStyledDialogs", "Accept Button Pushed!");
                        ANIMATION_TITLE = customText.getText().toString();

                        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
                        mEditor.putString(PREF_ANIMATION_NAME,ANIMATION_TITLE);
                        mEditor.commit();

                        callStoragePermissions();
                        // Logging
                        Log.i("shared pref","pref name is " + mSharedPreferences.getAll());
                        Log.d("MaterialStyledDialogs", "Animation Name: " + ANIMATION_TITLE);
                    }
                })

                .withDivider(true)  // Divider between buttons
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Log.d("MaterialStyledDialogs", "Decline Button Pushed!");

                        // Logging
                        Log.d("MaterialStyledDialogs", "Animation Name: " + ANIMATION_TITLE);
                    }
                })

                // Must be here
                .setNegativeText("Decline")
                .show();
    }

}
