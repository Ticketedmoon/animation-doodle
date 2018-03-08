package ca326.com.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ca326.com.activities.ItemTwoFragment.user_id;

// __Author__ = Shane Creedon (Shane.creedon3@mail.dcu.ie)
// __Author__ = James Collins


public class Profile_Screen extends AppCompatActivity implements  ProfileCardAdapter.ItemClickListener {

    public static String deciding_string;
    private Bitmap bitmap;

    private RelativeLayout drop_down_option_menu;

    //list of videos
    public static List<Video> listVideos;
    public static Integer position2 = 0;

    //Creating Views
    private RecyclerView recyclerView;
    private ProfileCardAdapter adapter;
    public static TextView textViewAbout;
    public static TextView textView;
    public static TextView picture;

    private NetworkImageView profilePicture;


    private boolean menu_button = false;

    private EditText simpleEditText;
    private EditText simpleEditText2;
    private String editTextValue;
    private String editTextValue2;
    public static boolean check;

    private static int video_code = 1;


    private RequestQueue requestQueue;
    private RequestQueue requestQueue2;

    private Button aboutDoneButton;
    private Button ideasDoneButton;

    //This will be used to get the page number, so a number
    // are loaded and then when you scroll more get loaded
    private int pageCount = 1;
    public static int i = 0;

    private Context context;
    private ImageLoader loadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_profile__screen);

        aboutDoneButton = (Button) findViewById(R.id.about);

        ideasDoneButton = (Button) findViewById(R.id.ideas);



        this.drop_down_option_menu = (RelativeLayout) findViewById(R.id.menu_layout);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        profilePicture = (NetworkImageView) findViewById(R.id.user_profile_photo);
        picture = (TextView) findViewById(R.id.changePicture);
        picture.setVisibility(View.VISIBLE);

        //this sets up the users text data
        textViewAbout = (TextView) findViewById(R.id.textViewAbout);
        textView = (TextView) findViewById(R.id.textView);


        //list 2 videos side by side in a grid
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing video list
        listVideos = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        requestQueue2 = Volley.newRequestQueue(this);

        //method to retrieve profile picture from database
        fetchImage();

        //method to retrieve profile data from database
        get_profile_data();
        getData();


        //initializing our adapter with list of videos
        adapter = new ProfileCardAdapter(listVideos, this);

        adapter.setClickListener(this);
        //Add adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    // method for user to upload image from phone
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            String imagePath = getImagePath(imageUri);
            setImage(imagePath);

        }

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

    public Canvas setImage(String imagePath){
        File file2 = new File(imagePath);
        BitmapFactory.Options bit = new BitmapFactory.Options();
        //Log.i("bitmap","bitmap: " +bit);
        Bitmap background2 = BitmapFactory.decodeFile(file2.getAbsolutePath(),bit);

        Canvas canvas = new Canvas(background2.copy(Bitmap.Config.ARGB_8888, true));

        Drawable newDrawable = new BitmapDrawable(getResources(), background2);
        profilePicture.setBackground(newDrawable);

        //update database
        ImageUpload upload = new ImageUpload(this);
        //Log.i("file","is " + imagePath);
        upload.execute(imagePath);
        picture.setVisibility(View.INVISIBLE);
        return canvas;

    }

    public void get_image(View v){

        // Get the users selected image from phone
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath());
        intent.setDataAndType(uri, "image/*");
        //intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select an image"), video_code);
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

    public void about(View v){
        simpleEditText = (EditText) findViewById(R.id.textViewAbout);
        simpleEditText.setFocusableInTouchMode(true);
        editTextValue = simpleEditText.getText().toString();
        aboutDoneButton.setVisibility(View.VISIBLE);
        //when user presses aboutDoneButton button set this to false
        //simpleEditText.setFocusableInTouchMode(false);
    }

    public void doneButton1(View v){
        //when user presses aboutDoneButton button set this to false
        simpleEditText.setFocusableInTouchMode(false);
        aboutDoneButton.setVisibility(View.INVISIBLE);
        editTextValue = simpleEditText.getText().toString();
        //Used to differentiate between which text box has been updated
        check = true;

        //now submit the user data to database
        ProfileUpload upload = new ProfileUpload(this);
        Log.i("result ","text is "+editTextValue);
        upload.execute(editTextValue);
    }

    public void doneButton2(View v){
        //when user presses aboutDoneButton button set this to false
        simpleEditText2.setFocusableInTouchMode(false);
        ideasDoneButton.setVisibility(View.INVISIBLE);
        editTextValue2 = simpleEditText2.getText().toString();

        check = false;
        Log.i("result ","text is "+editTextValue2);
        ProfileUpload upload = new ProfileUpload(this);
        upload.execute(editTextValue2);


        //then submit the user data to database
    }


    public void appIdeas(View v){
        simpleEditText2 = (EditText) findViewById(R.id.textView);
        simpleEditText2.setFocusableInTouchMode(true);
        editTextValue2 = simpleEditText2.getText().toString();
        Log.i("result ","text is "+editTextValue2);
        ideasDoneButton.setVisibility(View.VISIBLE);

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
        Log.i("Logging", "Logged out user: " + user_id);
        SharedPreferences.Editor editor = getSharedPreferences("preference",getApplicationContext().MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
        // Leave the Intent here as Main_Menu_Screen, if you make it Sign_In_Screen, causes a bug
        Intent intent = new Intent(Profile_Screen.this, Main_Menu_Screen.class);
        startActivity(intent);
    }

    public void onItemClick(View view, int position) {
        this.position2 = position;
        System.out.println("its clicked");
        deciding_string = "profile";
        Intent intent = new Intent (Profile_Screen.this, Test_VideoPlayer.class);
        startActivity(intent);

    }


    private void fetchImage() {
        //Adding the method to the queue by calling the method getVideoFromDB
        pageCount=1;
        requestQueue.add(getImageFromDB(pageCount));
        //Incrementing the page count
        pageCount++;
    }

    private JsonArrayRequest getImageFromDB(int pageCount) {

        System.out.println("user_id is " + user_id);
        // System.out.println("shared  " + mSharedPreference.getAll());


        //set up jsonArrayRequest as the data retrieved using PHP script will be in a list format
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://animationdoodle2017.com/fetchImage.php?id=" + String.valueOf(user_id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        parseImageData(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //If an error occurs means no more videos on db to load
                        Toast.makeText(Profile_Screen.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }



    //used to parse the json data returned by the php script
    private void parseImageData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject json = null;
            Log.i("drawable","is "+ json);
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the video object
                String image = (json.getString("image"));
                if (image.equals("null")){
                    return;
                }
                Log.i("drawable","bbb is "+ image);
                new imageLoad(this).execute(image);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class imageLoad extends AsyncTask<String, Void, Bitmap> {

        Activity instance;
        public imageLoad(Activity instance) {
            this.instance = instance;
        }
        @Override
        protected Bitmap doInBackground(String... src) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(src[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);

                Bitmap roundedBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas canvas = new Canvas(roundedBitmap);

                final int color = 0xff424242;
                final Paint paint = new Paint();
                final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                final RectF rectF = new RectF(rect);
                final float roundPx = 100;

                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                paint.setColor(color);
                canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                canvas.drawBitmap(bitmap, rect, rect, paint);




                return roundedBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            try {
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                Log.i("draw","is " + drawable);
                    profilePicture.setBackground(drawable);
                    picture.setVisibility(View.INVISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
