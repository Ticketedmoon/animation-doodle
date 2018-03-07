package ca326.com.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Switch;

public class Settings_Screen extends AppCompatActivity{

    public static boolean isOnionSwitched = true;
    private Switch onionSkinSwitch;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.exit_to_left);
        setContentView(R.layout.settings_screen);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);


        Window window = this.getWindow();

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_grey));

        // Find button
        onionSkinSwitch = (Switch) findViewById(R.id.switch1);
        onionSkinSwitch.setChecked(isOnionSwitched);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, Main_Menu_Screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void change_onion_skin_mode(View v) {
        CanvasView.settings_onion_skin = !CanvasView.settings_onion_skin;
        Settings_Screen.isOnionSwitched = !Settings_Screen.isOnionSwitched;
    }
}
