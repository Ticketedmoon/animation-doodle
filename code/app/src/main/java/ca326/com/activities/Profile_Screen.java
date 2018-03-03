package ca326.com.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import static ca326.com.activities.ItemTwoFragment.user_id;

// __Author__ = Shane Creedon (Shane.creedon3@mail.dcu.ie)
// __Author__ = James Collins


public class Profile_Screen extends AppCompatActivity implements  ProfileCardAdapter.ItemClickListener {

    public static String deciding_string;

    private RelativeLayout drop_down_option_menu;

    //list of videos
    public static List<Video> listVideos;
    public static Integer position2 = 0;

    //Creating Views
    private RecyclerView recyclerView;
    private ProfileCardAdapter adapter;
    public static TextView textViewAbout;


    private boolean menu_button = false;

    private RequestQueue requestQueue;
    private RequestQueue requestQueue2;

    //This will be used to get the page number, so a number
    // are loaded and then when you scroll more get loaded
    private int pageCount = 1;
    public static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_profile__screen);

        this.drop_down_option_menu = (RelativeLayout) findViewById(R.id.menu_layout);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        textViewAbout = (TextView) findViewById(R.id.textViewAbout);

        //list 2 videos side by side in a grid
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing video list
        listVideos = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);

        //method to retrieve data from database
        getData();

        get_profile_data();

        //initializing our adapter with list of videos
        adapter = new ProfileCardAdapter(listVideos, this);

        adapter.setClickListener(this);
        //Add adapter to recyclerview
        recyclerView.setAdapter(adapter);

    }

    public void onBackPressed() {
        System.out.println("Back Button Pushed <Returning to Homescreen>");
        Intent startMain = new Intent(Profile_Screen.this, Main_Menu_Screen.class);
        startActivity(startMain);
    }

    private JsonArrayRequest getVideoFromDB(int pageCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);
        System.out.println("user_id is " + user_id);
       // System.out.println("shared  " + mSharedPreference.getAll());


        //set up jsonArrayRequest as the data retrieved using PHP script will be in a list format
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://animationdoodle2017.com/userVideos.php?id=" + String.valueOf(user_id),
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
                        Toast.makeText(Profile_Screen.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
            System.out.println(array.length());
            //Creating the video object
            Video video = new Video();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the video object
                video.setImageUrl(json.getString("image"));
                video.setVideoUrl(json.getString("video"));
                video.setName(json.getString("name"));
                video.setDescription(json.getString("video description"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Add to the list
            position2++;
            listVideos.add(video);
            System.out.println(video.getName());
            System.out.println(listVideos.size());
            System.out.println(video.getName());
        }

        //add to adapter
        adapter.notifyDataSetChanged();
    }

    public void get_profile_data(){
        ProfileData data = new ProfileData();
        requestQueue2.add(data.getData(user_id));

    }

    public void menu(View v) {
        System.out.println("clicked");
        if (!menu_button){
            this.drop_down_option_menu.setVisibility(View.VISIBLE);
            menu_button = true;
        }
        else{
            this.drop_down_option_menu.setVisibility(View.INVISIBLE);
            menu_button= false;
        }
    }

    public void logOut(View view) {
        //this.logout.setVisibility(View.VISIBLE);
        SharedPreferences.Editor editor = getSharedPreferences("preference",getApplicationContext().MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(Profile_Screen.this, Sign_In_Screen.class);
        startActivity(intent);
    }

    public void onItemClick(View view, int position) {
        this.position2 = position;
        System.out.println("its clicked");
        deciding_string = "profile";
        Intent intent = new Intent (Profile_Screen.this, Test_VideoPlayer.class);
        startActivity(intent);

    }


}
