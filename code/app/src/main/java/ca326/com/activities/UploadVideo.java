package ca326.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static ca326.com.activities.Sign_In_Screen.user_id;
import static ca326.com.activities.Start_Drawing_Screen.imagePath;
import static ca326.com.activities.Start_Drawing_Screen.textViewResponse;
import static ca326.com.activities.Start_Drawing_Screen.videoPath;

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
                textViewResponse.setText(Html.fromHtml("File uploaded!"));
                textViewResponse.setMovementMethod(LinkMovementMethod.getInstance());
            }

            @Override
            protected String doInBackground(Void... params) {
                try {

                    //video and image object upload

                    FileUpload upload = new FileUpload();
                    System.out.println("file is " + imagePath);
                    String msg = upload.uploadFile(imagePath);
                    String msg2 = upload.uploadFile(videoPath);
                    //delete file from phone as its now on the server
                    //not sure if this is working or not, need to check later

                    //you need to strip imagePath and videoPath to only their name + file extension
                    ///storage/emulated/0/Animation_Doodle_Images/testing.mp4 becomes
                    /// http://animationdoodle2017.com/videos/uploads/testing.mp4


                    //insert the location of the files into the database
                    String imageLink = "http://animationdoodle2017.com/videos/uploads/";
                    String videoLink = "http://animationdoodle2017.com/videos/uploads/";
                    String newImagePath = imagePath.substring(36);
                    newImagePath = imageLink += newImagePath;
                    String newVideoPath = videoPath.substring(36);
                    newVideoPath = videoLink += newVideoPath;
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
                            URLEncoder.encode("id", "UTF-8") + "=" + user_id;
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