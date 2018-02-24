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
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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

import static ca326.com.activities.Sign_In_Screen.user_id;


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
    private Bitmap image;
    private Map<Integer, Bitmap> canvas_bitmaps = new HashMap<Integer, Bitmap>();            // Initialise bitmap cache memory


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
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        // END

        // Assign pathways (IMPORTANT)
        pathways.put(pos, this.canvasView.newPaths);

        // Start at first frame
        for(int i = 0; i < pathways.size(); i++) {
            this.canvasView.newPaths = pathways.get(i);
            Bitmap bitmap = loadBitmapFromView(canvasView);
            canvas_bitmaps.put(i, bitmap);
        }

        // restore canvasView.newPath before the loop
        this.canvasView.newPaths = pathways.get(pos);

        // Array is now full of all bitmap images, now encode them into a video:
        SeekableByteChannel out = null;
        try {
            // Verify Storage Permissions
            verifyStoragePermissions(this);
            // If /Animation_Doodle_Images doesn't exist yet, generate it.
            File file = new File(Environment.getExternalStorageDirectory(),"Animation_Doodle_Images");
            if(!file.exists()){
                file.mkdirs();
            }
            out = NIOUtils.writableFileChannel(Environment.getExternalStorageDirectory() + "/Animation_Doodle_Images/outputx.mp4");
            // for Android use: AndroidSequenceEncoder
            encoder = new AndroidSequenceEncoder(out, Rational.R(frame_rate_value, 1));

            // ASYNC TASK HERE
            for(int i = 0; i < canvas_bitmaps.size(); i++)
            {
                // START (Adjust code here)
                image = canvas_bitmaps.get(i);
                encoder.encodeImage(image); // --- This line takes an extreme amount of time to process (ASYNC needed)
                Log.i("save_animation", "Encoder: " + "Image (" + i + ") encoded! (50 seconds encoding rate per frame)");
            }
            encoder.finish();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("==============\nError in (save_animation()) function\n==============");
        }
        finally {
            v.setDrawingCacheEnabled(false);
            NIOUtils.closeQuietly(out);
            Toast.makeText(getApplication(), "Animation successfully Downloaded (/sdcard/Animation_Doodle_Images)", Toast.LENGTH_SHORT).show();

            // Testing...
            System.out.println("Current CanvasView paths: " + this.canvasView.newPaths);
            System.out.println("pathways: " + pathways);
            System.out.println("pathways Length: " + pathways.size());

        }
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(canvasView.getWidth(), canvasView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        c.drawColor(Color.WHITE);   // Essential
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
        File file = new File(Environment.getExternalStorageDirectory(), "/Animation_Doodle_Images/tmp.jpg");
        boolean deleted = file.delete();
        System.out.println("tmp deleted: " + deleted);
        // Print out to log
        System.out.println("File Saved");
    }

    private void save(String store_name) {
        verifyStoragePermissions(this);

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
            FileOutputStream outputStream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outputStream);
            outputStream.close();
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
            Activity instance;

            UploadVideo(Activity instance) {
                this.instance = instance;

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //set message for the user to see the pr
                uploading = ProgressDialog.show(Start_Drawing_Screen.this, "uploading file to the database", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String result) {
                //super.onPostExecute(result);
                //gets rid of progress dialog
                StringBuilder sb = new StringBuilder();
                sb.append(result + "\n");
                String jsonStr = sb.toString();
                Log.i("response",jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String query_result = jsonObj.getString("query_result");
                        if (query_result.equals("SUCCESS")) {

                        }
                     else if (query_result.equals("FAILURE")) {
                        Toast.makeText(instance, "Data could not be inserted.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(instance, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(instance, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(instance, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
            }

                uploading.dismiss();
                textViewResponse.setText(Html.fromHtml("File uploaded!"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                try{

                    //video and image object upload

                    FileUpload upload = new FileUpload();
                    System.out.println("file is " + imagePath);
                    String msg = upload.uploadFile(imagePath);
                    String msg2 = upload.uploadFile(videoPath);
                    //delete file from phone as its now on the server
                    //not sure if this is working or not, need to check later
                    boolean deleted = newfile.delete();

                    //you need to strip imagePath and videoPath to only their name + file extension
                    ///storage/emulated/0/Animation_Doodle_Images/testing.mp4 becomes
                    /// http://animationdoodle2017.com/videos/uploads/testing.mp4


                    //insert the location of the files into the database
                    String imageLink = "http://animationdoodle2017.com/videos/uploads/";
                    String videoLink = "http://animationdoodle2017.com/videos/uploads/";
                    String newImagePath = imagePath.substring(44);
                    newImagePath = imageLink += newImagePath;
                    String newVideoPath = videoPath.substring(44);
                    newVideoPath = videoLink += newVideoPath;
                    System.out.println("strings are " + newVideoPath + " " + newImagePath);
                    String link = "http://animationdoodle2017.com/uploadLinks.php";
                    URL url = new URL(link);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream out=con.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    String post_data= URLEncoder.encode("imageFile","UTF-8")+"="+URLEncoder.encode(newImagePath,"UTF-8")+"&"+
                        URLEncoder.encode("videoFile","UTF-8")+"="+URLEncoder.encode(newVideoPath,"UTF-8")+"&"+
                            URLEncoder.encode("id","UTF-8")+"="+user_id;
                    writer.write(post_data);
                    writer.flush();
                    writer.close();
                    out.close();
                    InputStream in=con.getInputStream();
                    BufferedReader br=new BufferedReader(new InputStreamReader(in,"iso-8859-1"));
                    String line="";
                    String result="";
                    while((line=br.readLine())!=null)
                {
                        result+=line;
                }
                    return result;

            }   catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
            }


            }
        }
        UploadVideo upload = new UploadVideo(this);
        upload.execute();
    }

}
