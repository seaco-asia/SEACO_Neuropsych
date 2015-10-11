package com.seaco.seaconeuropsych;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class Intro extends ActionBarActivity {
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove title bar
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //set content view AFTER ABOVE sequence (to avoid crash)
        this.setContentView(R.layout.activity_intro);



    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new CalligraphyContextWrapper(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void endActivity(View view) {
        // when OK button is clicked, proceed
        Intent intent = new Intent(context, prospective_initial.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);

    }

    @Override
    public void onBackPressed() { // Disable hardware back button
    }

    public void onAboutClicked(View view) {
        int versionCode;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {
            versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            versionCode = 0;
        }
        builder.setMessage(getResources().getString(R.string.about_app, Integer.toString(versionCode)));
        builder.setPositiveButton(R.string.confirm_button, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
