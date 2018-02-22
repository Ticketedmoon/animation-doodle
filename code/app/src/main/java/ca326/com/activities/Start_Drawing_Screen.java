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
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import yuku.ambilwarna.AmbilWarnaDialog;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class Start_Drawing_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    // Views
    private static CanvasView canvasView;
    private RelativeLayout menu;
    private RecyclerView timeline_frames;

    // Object creations
    private Paint mDefaultPaint;
    private ImageButton colour_picker;
    private MyRecyclerViewAdapter adapter;
    private AndroidSequenceEncoder encoder;

    // Animation & Timeline Logic
    public static Integer pos = 0;
    private Integer frame_counter = 1;
    public static List<Integer> frames = new ArrayList<Integer>();
    private List<String> frameNums = new ArrayList<String>();

    // Input from user stored in these variables
    private static String value;
    private static Integer frame_rate_value = 2;

    // IMPORTANT
    public static Map<Integer, List <Pair<Path, Paint>>> pathways = new HashMap<Integer, List<Pair <Path, Paint>>>();

    // Login Credentials
    private SharedPreferences mSharedPreferences;
    public static final String PREFERENCE= "preference";
    public static final String PREF_EMAIL = "email";
    public static final String PREF_PASSWORD = "password";

    // Image Buttons / Buttons
    private ImageButton onionButton;
    private ImageButton play;
    private ImageButton ham_menu;
    private ImageButton profile;
    private ImageButton top_rated;

    //Upload feature
    private TextView textView;
    private TextView textViewResponse;

    //just used to check if its a video being uploaded for onStartActivity
    private static final int video_code = 1;
    private Bitmap tmp;
    private String videoPath;
    private String imagePath;
    private File newfile = null;


    // Other Fields
    public static boolean onionSkinning = true;
    private boolean is_menu_open = false;

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
        this.onionButton = (ImageButton) findViewById(R.id.onionSkinningButton);
        this.play = (ImageButton) findViewById((R.id.play_button));
        // END

        // Image button / Button onClick Listeners (For Style effects)
        this.ham_menu = (ImageButton) findViewById(R.id.menu);
        this.ham_menu.setBackgroundColor(Color.TRANSPARENT);

        this.profile = (ImageButton) findViewById(R.id.profile_menu);
        this.profile.setBackgroundColor(Color.TRANSPARENT);
        this.profile.setImageResource(R.drawable.profile_background_colour);

        this.top_rated = (ImageButton) findViewById(R.id.top_rated);
        this.top_rated.setBackgroundColor(Color.TRANSPARENT);
        this.top_rated.setImageResource(R.drawable.top_rated_background_colour);

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

        // Onion Button
        onionButton.setImageResource(R.drawable.onion);
        onionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //add onion layers
                if (onionSkinning) {
                    for (int i = 0; i < pathways.size(); i++) {
                        if (pos != 0) {
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
            this.pathways.put(this.pos, this.canvasView.newPaths);
            this.canvasView.newPaths = this.pathways.get(position);

            this.pos = position;
            this.canvasView.invalidate();
        }
    }

    // Save Animation Function, takes all frames in canvas
    // Converts them to bitmaps and encodes them to mp4.
    // *DYSFUNCTIONAL*
    public void save_animation(View v) {
        System.out.println("Beginning Encoded / Decoding (Saving Animation /sdcard/Animation_Doodle_Images");

        // Store bitmaps in an array
        Map<Integer, Bitmap> canvas_bitmaps = new HashMap<Integer, Bitmap>();            // Initialise bitmap cache memory
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        // END

        // Assign pathways
        pathways.put(pos, this.canvasView.newPaths);

        // Start at first frame
        for(int i = 0; i < pathways.size(); i++) {
            this.canvasView.newPaths = pathways.get(i);
            Bitmap bitmap = loadBitmapFromView(v);
            canvas_bitmaps.put(i, bitmap);

        }

        // restore canvasView.newPath before the loop
        System.out.println(canvas_bitmaps);
        this.canvasView.newPaths = pathways.get(pos);

        // Array is now full of all bitmap images, encode them into a video:
        SeekableByteChannel out = null;
        try {
            out = NIOUtils.writableFileChannel("/sdcard/Animation_Doodle_Images/outputx.mp4");
            // for Android use: AndroidSequenceEncoder
            encoder = new AndroidSequenceEncoder(out, Rational.R(1, 4));
            for (int i = 0; i < canvas_bitmaps.size(); i++) {
                // Generate the image, for Android use Bitmap

                // START (Adjust code here)
                Bitmap image = canvas_bitmaps.get(i);
                encoder.encodeImage(image); // Huge Delays (Fix this particular part)
                // END (Finished)

                System.out.println("Encoded Frame (" + i + ")");
            }
            // Finalize the encoding, i.e. clear the buffers, write the header, etc.
            encoder.finish();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("==============\nError in (save_animation()) function\n==============");
        }
        finally {
            v.setDrawingCacheEnabled(false);
            NIOUtils.closeQuietly(out);
            System.out.println("Animation Successfully Saved...");

            // Testing...
            System.out.println("\nTesting");
            System.out.println("Current CanvasView paths: " + this.canvasView.newPaths);
            System.out.println("pathways: " + pathways);
            System.out.println("pathways Length: " + pathways.size());

        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap( 64, 64, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        canvasView.layout(canvasView.getLeft(), canvasView.getTop(), canvasView.getRight(), canvasView.getBottom());
        canvasView.draw(c);
        return b;
    }

    private void adjust_timeline() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.frames);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(Start_Drawing_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        adapter = new MyRecyclerViewAdapter(this, frames, frameNums);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public static CanvasView getCView() {
        return canvasView;
    }

    // Disable back button on this screen

    public void save_external(View v) {
        System.out.println("Pushed Save Button");
        get_file_input_save(this.canvasView);
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

    public void get_file_input_save(View v) {
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
                canvasView.setUpPaint(color, mDefaultPaint);
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
            intent = new Intent(Start_Drawing_Screen.this, Register_Screen.class);
        }

        startActivity(intent);
    }

    public void goToTopRatedAnimations(View view){
        Intent intent = new Intent (Start_Drawing_Screen.this, Top_Rated_Screen.class);
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
        uploadVideo();
    }

    public void choose(View v){

        // the thumbnail of the video will be the frame halfway through the animation
        Integer middle = (this.pathways.size() / 2)-1;
        this.canvasView.newPaths = this.pathways.get(middle);

        try {

            this.canvasView.setDrawingCacheEnabled(true);
            tmp = this.canvasView.getDrawingCache();
            this.canvasView.setDrawingCacheEnabled(true);
            Canvas canvas = new Canvas(tmp);
            canvas.drawColor(Color.WHITE);
            this.canvasView.draw(canvas);

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = new File(Environment.getExternalStorageDirectory(),"Animation_Doodle_Images");
                if(!file.exists()){
                    file.mkdirs();
                }

                /////   Very important
                ///     here is where you will substitute this path into the method to upload

                ///     f is now going to be the same as path

                //// make the file name the same as the video file name + .jpg to differentiate
                //// will change this later when save feature is done
                newfile = new File(file.getAbsolutePath()+ file.separator + "video_namedddd" +".jpg");
                System.out.println("file path is " + newfile);
            }
            FileOutputStream ostream = new FileOutputStream(newfile);
            tmp.compress(Bitmap.CompressFormat.JPEG, 15, ostream);
            ostream.close();
        } catch(Exception e){
            e.printStackTrace();
        }



        chooseVideo();
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
                textView.setText(imagePath);
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

        // Add to pathways current frame -- onItemClick() will handle the rest
        this.pathways.put(this.pos, this.canvasView.newPaths);

        // Store frame user is on
        pos = this.frames.size()-1;
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
        this.pathways.put(frame_counter, emptyArr);

        frame_counter++;
        adjust_timeline();

        // Change view to next frame
        System.out.println(frames);
        System.out.println("Pathways:" + pathways);
    }

    public void previous_frame(View v) {
        Integer currentIndex = this.pos;
        List<Pair<Path, Paint>> mixed_frame = new ArrayList<>();

        if (currentIndex > 0) {

            List<Pair<Path, Paint>> prev_frame = pathways.get(currentIndex-1); // -1 for previous version

            //example of transparent paint , obviously not working properly
            // #80000000 == 50 % transparent
            // #33000000 == 20% transparent
            // https://gist.github.com/lopspower/03fb1cc0ac9f32ef38f4 -- link to colours

            // this.canvasView.setUpPaint(Color.parseColor("#3B000000"),mDefaultPaint);
            // Combine Both the previous frame with the current frame
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

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //set message for the user to see the pr
                uploading = ProgressDialog.show(Start_Drawing_Screen.this, "uploading file to the database", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //gets rid of progress dialog
                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("File uploaded!"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                FileUpload upload = new FileUpload();
                System.out.println("file is " + imagePath);
                String msg = upload.uploadFile(imagePath);
                String msg2 = upload.uploadFile(videoPath);
                return msg;

            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

}
