package ca326.com.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import static ca326.com.activities.Profile_Screen.check;
import static ca326.com.activities.Sign_In_Screen.PREF_ID;
import static ca326.com.activities.Start_Drawing_Screen.mSharedPreferences;

class ProfileUpload extends AsyncTask<String, Void, String> {

    ProgressDialog uploading;
    Activity instance;
    private BufferedWriter writer;
    private String post_data;


    ProfileUpload(Activity instance) {
        this.instance = instance;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //set message for the user to see the pr
        uploading = ProgressDialog.show(this.instance, "updating data", "Please wait", false, false);
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(result);
        //gets rid of progress dialog
        StringBuilder sb = new StringBuilder();
        sb.append(result + "\n");
        String jsonStr = sb.toString();
        Log.i("response","string "+ jsonStr);
        Log.i("response", "is " + user_id);
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
    }

    @Override
    protected String doInBackground(String... params) {
        String text =params[0];
        String link;
        try {
            if (check) {
                link = "http://animationdoodle2017.com/profileUpload.php";
            }
            else {
                link = "http://animationdoodle2017.com/profileUpload2.php";
            }
            URL url = new URL(link);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStream out = con.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            post_data = URLEncoder.encode("text", "UTF-8") + "=" + URLEncoder.encode(text, "UTF-8") + "&" +
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
                Log.i("result","is " + result);
            }
            return result;

        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }


    }

}