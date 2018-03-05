package ca326.com.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static ca326.com.activities.Profile_Screen.textView;
import static ca326.com.activities.Profile_Screen.textViewAbout;
import static ca326.com.activities.ItemTwoFragment.user_id;

public class ProfileData extends AppCompatActivity {

    public JsonArrayRequest getData(Integer id) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://animationdoodle2017.com/profile.php?id=" + String.valueOf(id),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileData.this, "Error getting data. Make sure your internet connection is working", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {

            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);
                String text = json.getString("text");
                if (text.equals("null")){
                    textViewAbout.setText("");
                }
                else {
                    textViewAbout.setText(json.getString("text"));
                }
                String ideasText = json.getString("text2");
                if (ideasText.equals("null")){
                    textView.setText("");
                }
                else {
                    textView.setText((json.getString("text2")));
                }
                Log.i("textviewabout","text is " + textViewAbout);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }
}
