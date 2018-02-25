package ca326.com.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static ca326.com.activities.Profile_Screen.deciding_string;

public class Top_Rated_Screen extends AppCompatActivity implements MyCardAdapter.ItemClickListener {

    //list of videos
    public static List<Video> listVideos;
    public static Integer position;

    //Creating Views
    private RecyclerView recyclerView;
    private MyCardAdapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //This will be used to get the page number, so a number
    // are loaded and then when you scroll more get loaded
    private int pageCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_rated__screen);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        //list 2 videos side by side in a grid
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing video list
        listVideos = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //method to retrieve data from database
        getData();

        //initializing our adapter with list of videos
        adapter = new MyCardAdapter(listVideos, this);

        adapter.setClickListener(this);
        //Add adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }


    private JsonArrayRequest getVideoFromDB(int pageCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //set up jsonArrayRequest as the data retrieved using PHP script will be in a list format
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://animationdoodle2017.com/video_db.php?page=" + String.valueOf(pageCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        parseData(response);
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs means no more videos on db to load
                        Toast.makeText(Top_Rated_Screen.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }


    private void getData() {
        //Adding the method to the queue by calling the method getVideoFromDB
        requestQueue.add(getVideoFromDB(pageCount));
        //Incrementing the page count
        pageCount++;
    }

    //used to parse the json data returned by the php script
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the video object
            Video video = new Video();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the video object
                String rating =(json.getString("rating"));
                Float number = Float.parseFloat(rating);
                video.setRating(number);
                video.setImageUrl(json.getString("image"));
                video.setVideoUrl(json.getString("video"));
                video.setName(json.getString("name"));
                video.setDescription(json.getString("video description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Add to the list
            listVideos.add(video);
        }

        //add to adapter
        adapter.notifyDataSetChanged();
    }
    public void rating(View view){
        Toast.makeText(getApplicationContext(),
                String.valueOf(MyCardAdapter.ratingBar.getRating()), Toast.LENGTH_LONG).show();
    }


    public void onItemClick(View view, int position) {
        this.position = position;
        deciding_string = "topRated";
        Intent intent = new Intent (Top_Rated_Screen.this, Test_VideoPlayer.class);
        startActivity(intent);

    }

}