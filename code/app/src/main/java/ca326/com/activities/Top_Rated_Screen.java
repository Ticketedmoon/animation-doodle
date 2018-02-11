package ca326.com.activities;

import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// __Author__ = Shane Creedon (Shane.creedon3@mail.dcu.ie)
// __Author__ = James Collins


public class Top_Rated_Screen extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{


    public static Integer pos = 0;
    private List<Integer> images = new ArrayList<Integer>();
    private List<String> videoDescription = new ArrayList<String>();

    private List<Integer> images2 = new ArrayList<Integer>();
    private List<String> videoDescription2 = new ArrayList<String>();

   private MyRecyclerViewAdapter adapter;
    private MyRecyclerViewAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_top_rated__screen);



        // set up the RecyclerView
        this.setUpTimeline();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.frames);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(Top_Rated_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new MyRecyclerViewAdapter(this, images, videoDescription);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        this.setUpTimeline();
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.frames2);
        LinearLayoutManager horizontalLayoutManager2
                = new LinearLayoutManager(Top_Rated_Screen.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(horizontalLayoutManager2);
        adapter2 = new MyRecyclerViewAdapter(this, images, videoDescription);
        adapter2.setClickListener(this);
        recyclerView2.setAdapter(adapter2);



    }

    @Override
    public void onItemClick(View view, int position) {

        // Integrate here with canvasView class
        // Make sure when user swaps the frame in the timeline
        // it updates to the correct canvas.


        //canvasView.newPaths.addAll(pathways.get(position));
        //canvasView.invalidate();
        if (this.pos != position) {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup__screen, menu);
        return true;
    }
    private void setUpTimeline() {
        // data to populate the RecyclerView with
        Integer tmp = R.drawable.play;

        images.add(tmp);
        images.add(tmp);
        images.add(tmp);
        images.add(tmp);
        images.add(tmp);
        images.add(tmp);

        videoDescription.add("stick man");
        videoDescription.add("funny");
        videoDescription.add("tmp");
        videoDescription.add("tmp");
        videoDescription.add("tmp");
        videoDescription.add("tmp");



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
