package ca326.com.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
        // Bitmap bitmap = Bitmap.createBitmap(this.canvasView.getDrawingCache());

        Integer tmp = R.drawable.frame;
        frames.add(tmp);
        frames.add(tmp);
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
        frameNums.add("Frame 7");
        frameNums.add("Frame 8");

    }

    // Left Arrow & Right Arrow pushed
    // goLeft brings the user to the sign-in / profile screen for viewing
    // If already signed in, bring to profile... (We can make an IF statement check later to see if they are signed in)
    // Sync both activities
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
}