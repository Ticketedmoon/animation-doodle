package ca326.com.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class Start_Drawing_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    // Class Fields
    private CanvasView canvasView;
    private Paint mDefaultPaint;
    private Button colour_picker;
    private ImageView timeline;

    private MyRecyclerViewAdapter adapter;
    private List<Integer> frames = new ArrayList();
    private List<String> frameNums = new ArrayList();
    private static final String TAG = "MainActivity";
    private static String value;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This function below creates the nice fade in / out transition between activities.
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_start__drawing__screen);

        // Drawing Functionality
        this.canvasView = (CanvasView) findViewById(R.id.canvas);

        // Colour Picker Stuff
        this.mDefaultPaint = canvasView.mPaint;
        this.colour_picker = (Button) findViewById(R.id.change_colour);
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
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();
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
                f = new File(file.getAbsolutePath()+file.separator + store_name +".jpg");
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
        frameNums.add("Frame 1");
        frameNums.add("Frame 2");
        frameNums.add("Frame 1");
        frameNums.add("Frame 2");

    }

    // Left Arrow & Right Arrow pushed
    // goLeft brings the user to the sign-in / profile screen for viewing
    // If already signed in, bring to profile... (We can make an IF statement check later to see if they are signed in)
    // Sync both activities
    // Animations

    public void goLeft(View view){
        Intent intent = new Intent (Start_Drawing_Screen.this, Sign_In_Screen.class);
        startActivity(intent);
    }

    // Sync both activities
    // goRight brings the user to the top-rated animations page
    // Only signed in users can upload, rate etc...
    public void goRight(View view){
        Intent intent = new Intent (Start_Drawing_Screen.this, Top_Rated_Screen.class);
        startActivity(intent);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
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
}