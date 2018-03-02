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

import static ca326.com.activities.Sign_In_Screen.user_id;
import static ca326.com.activities.Start_Drawing_Screen.imagePath;
import static ca326.com.activities.Start_Drawing_Screen.videoPath;
import static ca326.com.activities.Start_Drawing_Screen.video_description;

class SetLimit {

    public void SetLimit(){

        try {
            String link = "http://animationdoodle2017.com/updateLimit.php";
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            String post_data = URLEncoder.encode("limit", "UTF-8") + "=" + limitCounter + "&" +
                    URLEncoder.encode("userId", "UTF-8") + "=" + user_id;
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

        } catch (Exception e) {

        }


    }



}
