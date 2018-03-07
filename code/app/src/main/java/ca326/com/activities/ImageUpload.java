package ca326.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import static ca326.com.activities.ItemTwoFragment.user_id;
import static ca326.com.activities.Sign_In_Screen.PREF_ID;
import static ca326.com.activities.ItemTwoFragment.mSharedPreference;

public class ImageUpload  extends AsyncTask<String, Void, String> {

    ProgressDialog uploading;
    Activity instance;


    ImageUpload(Activity instance) {
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
        Toast toast = Toast.makeText(instance, "File uploaded!", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String imagePath = params[0];
        try {

            //video and image object upload

            FileUpload upload = new FileUpload();

            String msg2 = upload.uploadFile(imagePath);
            //delete file from phone as its now on the server
            //not sure if this is working or not, need to check later

            //you need to strip imagePath and videoPath to only their name + file extension
            /// http://animationdoodle2017.com/videos/uploads/testing.mp4


            //insert the location of the files into the database
            String imageLink = "http://animationdoodle2017.com/videos/uploads/";
            String videoLink = "http://animationdoodle2017.com/videos/uploads/";
            int index = imagePath.lastIndexOf("/");
            user_id = mSharedPreference.getInt(PREF_ID,0);
            String newImagePath = imagePath.substring(index+1);
            newImagePath = imageLink += newImagePath;
            Log.i("file path","is: "+ user_id);
            String link = "http://animationdoodle2017.com/uploadImage.php";
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String post_data = URLEncoder.encode("imageFile", "UTF-8") + "=" + URLEncoder.encode(newImagePath, "UTF-8") + "&" +
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
