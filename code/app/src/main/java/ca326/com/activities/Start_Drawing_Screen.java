package ca326.com.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;
import org.jcodec.scale.BitmapUtil;
import org.json.JSONException;
import org.json.JSONObject;

import yuku.ambilwarna.AmbilWarnaDialog;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.bq.markerseekbar.MarkerSeekBar;

import javax.microedition.khronos.opengles.GL10;

import static ca326.com.activities.Sign_In_Screen.user_id;


public class Start_Drawing_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    // Saving recycle view (Timeline) functionality
    private RecyclerView timeline_frames;
    private final String KEY_RECYCLER_STATE = "recycler_state";

    // Views
    public static CanvasView canvasView;
    private RelativeLayout menu;

    // Object creations
    private Paint mDefaultPaint;
    private ImageButton colour_picker;
    private MyRecyclerViewAdapter adapter;
    public Map<Integer, Bitmap> canvas_bitmaps = new HashMap<Integer, Bitmap>();            // Initialise bitmap cache memory
    public MarkerSeekBar pen_size_adjuster;

    // Animation & Timeline Logic
    public static Integer pos = 0;
    private Integer frame_counter = 1;
    public static List<Integer> frames = new ArrayList<Integer>();
    private List<String> frameNums = new ArrayList<String>();

    // Input from user stored in these variables
    public static Integer frame_rate_value = 2;

    // IMPORTANT
    public static Map<Integer, List <Pair<Path, Paint>>> pathways = new HashMap<Integer, List<Pair <Path, Paint>>>();

    // Login Credentials
    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    // Image Buttons / Buttons
    private ImageButton play;
    private ImageButton ham_menu;
    private ImageButton profile;

    //Upload feature
    public static TextView textView;
    public static TextView textViewResponse;

    //just used to check if its a video being uploaded for onStartActivity
    private static final int video_code = 1;
    private Bitmap tmp;
    public static String videoPath;
    public static String imagePath;
    private File newfile = null;

    // Other Fields
    private boolean is_menu_open = false;
    public int pen_size;
    public static int image_counter = 1;
    private String value;
    private int canvas_height;

    public static ImageView imageView;
    public static Bitmap bitmap;
    public static Drawable myDrawable;

    public Context context;
    public static Integer adapterPosition = 20;
    public static boolean set = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This function below creates the nice fade in / out transition between activities.
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start__drawing__screen);

        textView = (TextView) findViewById(R.id.textView);
        textViewResponse = (TextView) findViewById(R.id.textViewResponse);


        // Drawing Functionality
        this.canvasView = (CanvasView) findViewById(R.id.canvas);
        this.menu = (RelativeLayout) findViewById(R.id.layout_menu);
        this.timeline_frames = (RecyclerView) findViewById(R.id.frames);
        this.play = (ImageButton) findViewById((R.id.play_button));
        // END

        // Image button / Button onClick Listeners (For Style effects)
        this.ham_menu = (ImageButton) findViewById(R.id.menu);
        this.ham_menu.setBackgroundColor(Color.TRANSPARENT);

        this.profile = (ImageButton) findViewById(R.id.profile_menu);
        this.profile.setBackgroundColor(Color.TRANSPARENT);
        this.profile.setImageResource(R.drawable.profile_background_colour);

        this.play = (ImageButton) findViewById(R.id.play_button);
        this.play.setBackgroundColor(Color.TRANSPARENT);
        this.play.setImageResource(R.drawable.play_background_colour);

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
    }

    // When clicking a frame on the timeline, update some parameters
    public void onItemClick(View view, int position) {
        int correct_onion_frame;

        if (position == 0)
            correct_onion_frame = position;
        else
            correct_onion_frame = position-1;

        if (this.pos != position) {
            set = true;

            // Moved all logic to a method (REFACTORING)
            change_current_frame(position, correct_onion_frame);

            // Hide Onion layer in bitmap frames
            canvasView.shouldShowOnionSkin = false;
            canvasView.invalidate();

            this.canvasView.setDrawingCacheEnabled(true);
            bitmap = this.canvasView.getDrawingCache();
            //Canvas canvas = new Canvas(bitmap);
            //canvas.drawColor(Color.WHITE);
            //this.canvasView.draw(canvas);
            adapterPosition = position;
            myDrawable = new BitmapDrawable(getResources(), bitmap);
            //myDrawable = getResources().getDrawable(R.drawable.play);
            adapter.notifyDataSetChanged();
            Log.i("bitmap","is " + bitmap);

            // Reshow Bitmap after bitmap saved
            canvasView.shouldShowOnionSkin = true;

        }
    }
    public void getImageView(CanvasView view){}

    private void change_current_frame(int position, int correct_onion_frame) {
        // Destroy previous onion cache
        this.canvasView.onionPaths.clear();

        // Add Onion Layering Functionality
        // Make Paint Relatively transparent
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

        this.pos = position;
        this.canvasView.invalidate();
    }

    // Save Animation Function, takes all frames in canvas
    // Converts them to bitmaps and encodes them to mp4.
    public void download_animation(View v) {
        Log.i("Save Animation","Beginning Encoded / Decoding (Saving Animation /sdcard/AnimationDoodle");

        // Assign pathways (IMPORTANT)
        pathways.put(pos, this.canvasView.newPaths);
        checkCanvasSize(canvasView);

        // Build an array of bitmap images from different pathways
        for(int i = 0; i < pathways.size(); i++) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.canvasView.newPaths = pathways.get(i);
            Bitmap bitmap = loadBitmapFromView(canvasView);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 5, stream);

            byte[] imageInByte = stream.toByteArray();
            Bitmap bitmapM = BitmapFactory.decodeByteArray(imageInByte, 0, imageInByte.length);
            canvas_bitmaps.put(i, bitmapM);
        }

        // Save all images
        int old_pos = pos;

        for(int i = 0; i < pathways.size(); i++) {
            canvasView.newPaths = pathways.get(i);
            save_external(canvasView);
        }

        canvasView.newPaths = pathways.get(old_pos);
        canvasView.invalidate();
        // END

        Log.i("Download","Bitmap values: " + this.canvas_bitmaps.values());

        // restore canvasView.newPath before the loop
        this.canvasView.newPaths = pathways.get(pos);
        Log.i("Download", "Bitmap Conversion Complete");

        // ASYNC TASK HERE (Encode Images)
        //DownloadFilesTask task = new DownloadFilesTask(this);
        //task.execute();
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
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(Start_Drawing_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new MyRecyclerViewAdapter(this, frames, frameNums);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public static CanvasView getCView() {
        return canvasView;
    }

    public void save_external(View v) {
        Log.i("Save", "Pushed Save Button");

        // Store canvas_width (Some reason gets overwritten, so store and then restore)
        canvas_height = canvasView.height;

        // ASYCN call to file name
        get_file_input(this.canvasView);
    }

    private void save(String store_name) {
        verifyStoragePermissions(this);
        Log.i("Download Image", "Canvas Width: " + canvasView.width + " -- " + "Canvas Height: " + canvasView.height );

        try {
            this.canvasView.setDrawingCacheEnabled(true);
            // remove and then restore onion skin

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
                if(!file.exists()){
                    file.mkdirs();
                }
                f = new File(file.getAbsolutePath()+ file.separator + store_name +".jpg");
            }
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);

            //Reshow Onion Skin
            canvasView.shouldShowOnionSkin = true;
            outputStream.close();

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
        final int prev_h = canvasView.height;

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("Save", "Picture (" + image_counter + ") saved");
                value = input.getText().toString();
                image_counter++;

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


    // Adapt this function later to handle Integers rather than Strings more efficiently.
    public void get_file_input_frame_rate(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Frame Rate Manager");
        alert.setMessage("Enter how many Frames to display per second (FPS): ");

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    Integer v = Integer.parseInt(input.getText().toString());
                    if(v > 0)
                        frame_rate_value = v;
                    else
                        Toast.makeText(getApplication(), "Impossible frame rate entered. Try Again.", Toast.LENGTH_SHORT).show();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Entered a string value rather than an Integer");
                    Toast.makeText(getApplication(), "Frame rate unchanged (Entered a non-number)", Toast.LENGTH_SHORT).show();
                }
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
                canvasView.setUpPaint(color, mDefaultPaint, pen_size);
                canvasView.colour = color;
            }
        });
        colour_picker.show();
    }

    // Left Arrow & Right Arrow pushed
    // goToProfile brings the user to the sign-in / profile screen for viewing
    // If already signed in, bring to profile... (We can make an IF statement check later to see if they are signed in)
    // Sync both activities
    // Animations

    public void goToProfile(View view){
        // OnTouch change background colour to red

        mSharedPreferences = getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        Intent intent;

        if(mSharedPreferences.contains(PREF_EMAIL)&& mSharedPreferences.contains(PREF_PASSWORD)) {
            intent = new Intent(Start_Drawing_Screen.this, Profile_Screen.class);
        }
        else{
            intent = new Intent(Start_Drawing_Screen.this, Sign_In_Screen.class);
        }

        startActivity(intent);
    }

    // Sync both activities
    // goToTopRatedAnimations brings the user to the top-rated animations page
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

    public void upload(View v){
        UploadVideo upload = new UploadVideo(this);
        upload.execute();
    }

    public void choose(View v){

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

            this.canvasView.setDrawingCacheEnabled(true);
            tmp = this.canvasView.getDrawingCache();
            Canvas canvas = new Canvas(tmp);
            canvas.drawColor(Color.WHITE);
            this.canvasView.draw(canvas);

            //now retrieve the image for the right frame
            this.canvasView.newPaths = this.pathways.get(this.pos);
            this.canvasView.invalidate();


            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(),"Animation_Doodle_Images");
                if(!file.exists()){
                    file.mkdirs();
                }

                //// make the file name the same as the video file name + .jpg to differentiate
                //// will change this later when save feature is done
                newfile = new File(file.getAbsolutePath()+ file.separator + "nnnnn" +".jpg");
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
            if (requestCode == video_code) {
                Uri imageUri = data.getData();
                System.out.println("image uri is : " + imageUri);
                videoPath = getPath(imageUri);
                imagePath = newfile.toString();
                System.out.println("imagePath == " + imagePath);
            }
        }

    }

    public void chooseVideo() {
        Intent intent = new Intent();
        //only search for videos on phone
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
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
        // Integer value stored in class field.
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
        this.pathways.put(frame_counter-1, emptyArr);

        // Change view to next frame
        onItemClick(canvasView, frame_counter-1);

        frame_counter++;
        adjust_timeline();

        Log.i("Add Frame Button", "frames: " + (frames));
        Log.i("Add Frame Button", "Pathways: " + pathways);

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

}
