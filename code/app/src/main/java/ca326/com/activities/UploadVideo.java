package ca326.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static ca326.com.activities.Sign_In_Screen.PREF_ID;
import static ca326.com.activities.ItemTwoFragment.user_id;
import static ca326.com.activities.Start_Drawing_Screen.imagePath;
import static ca326.com.activities.Start_Drawing_Screen.limitCounter;
import static ca326.com.activities.Start_Drawing_Screen.mSharedPreferences;
import static ca326.com.activities.Start_Drawing_Screen.videoPath;
import static ca326.com.activities.Start_Drawing_Screen.video_description;

class UploadVideo extends AsyncTask<Void, Void, String> {

            ProgressDialog uploading;
            Activity instance;
            private File dir;


            UploadVideo(Activity instance) {
                this.instance = instance;
            }



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //set message for the user to see the pr
                uploading = ProgressDialog.show(this.instance, "uploading file to the database", "Please wait", false, false);
            }

            @Override
            protected void onPostExecute(String result) {
                //super.onPostExecute(result);
                //gets rid of progress dialog
                StringBuilder sb = new StringBuilder();
                sb.append(result + "\n");
                String jsonStr = sb.toString();
                Log.i("response", jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        String query_result = jsonObj.getString("query_result");
                        if (query_result.equals("SUCCESS")) {

                        } else if (query_result.equals("FAILURE")) {
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

                dir = new File(Environment.getExternalStorageDirectory() + "/AnimationDoodle/Temp");
                // Delete tmp folder
                if (dir.isDirectory())
                {
                    String[] children = dir.list();
                    for (int i = 0; i < children.length; i++)
                    {
                        new File(dir, children[i]).delete();
                    }
                }

                Toast toast = Toast.makeText(instance, "File uploaded!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                try {

                    //video and image object upload

                    FileUpload upload = new FileUpload();


                    System.out.println("file is " + imagePath);
                    String msg = upload.uploadFile(imagePath);
                    String msg2 = upload.uploadFile(videoPath);

                    //you need to strip imagePath and videoPath to only their name + file extension
                    /// http://animationdoodle2017.com/videos/uploads/testing.mp4


                    //insert the location of the files into the database
                    String imageLink = "http://animationdoodle2017.com/videos/uploads/";
                    String videoLink = "http://animationdoodle2017.com/videos/uploads/";

                    // strips the video name and image name from file paths
                    int index = imagePath.lastIndexOf("/");
                    int index2 = videoPath.lastIndexOf("/");
                    Log.i("file path","is: "+ index);

                    //gets the user id to insert video correctly into database with their id
                    user_id = mSharedPreferences.getInt(PREF_ID,0);

                    String newImagePath = imagePath.substring(index+1);
                    newImagePath = imageLink += newImagePath;
                    Log.i("file path","is: "+ newImagePath);
                    String newVideoPath = videoPath.substring(index2+1);
                    newVideoPath = videoLink += newVideoPath;
                    Log.i("file path","is: "+ newVideoPath);
                    System.out.println("strings are " + newVideoPath + " " + newImagePath);
                    String link = "http://animationdoodle2017.com/uploadLinks.php";
                    URL url = new URL(link);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStream out = con.getOutputStream();

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                    String post_data = URLEncoder.encode("imageFile", "UTF-8") + "=" + URLEncoder.encode(newImagePath, "UTF-8") + "&" +
                            URLEncoder.encode("videoFile", "UTF-8") + "=" + URLEncoder.encode(newVideoPath, "UTF-8") + "&" +
                            URLEncoder.encode("id", "UTF-8") + "=" + user_id + "&" +
                            URLEncoder.encode("videoDescription", "UTF-8") + "=" + video_description + "&" +
                            URLEncoder.encode("limitCounter", "UTF-8") + "=" + limitCounter;

                    Log.i("ids","are " + user_id + " " + limitCounter);
                    writer.write(post_data);
                    writer.flush();
                    writer.close();
                    out.close();
                    InputStream in = con.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in, "iso-8859-1"));
                    String line = "";
                    String result = "";
                    while ((line = br.readLine()) != null) {
                        result += line;
                    }
                    return result;

                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }


            }
        }