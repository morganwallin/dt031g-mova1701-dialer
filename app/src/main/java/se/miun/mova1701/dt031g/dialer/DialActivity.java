package se.miun.mova1701.dt031g.dialer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;



public class DialActivity extends AppCompatActivity {
    Dialpad dp;
    private static final int REQUEST_PHONE_CALL = 1;
    static final Integer READ_STORAGE_PERMISSION_REQUEST_CODE=0x3;
    private static boolean useSound = false;
    public static boolean getUseSound() {
        return useSound;
    }

    private boolean isExternalStorageMounted() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public void shouldWeUseSound() {
        if (checkPermissionForReadExternalStorage()) {
            if(isExternalStorageMounted()) {
                useSound = true;
                SoundPlayer.getInstance(this).loadSound(this);
            }
        }
        else {
            requestPermissionForReadExternalStorage();
        }
    }


    public boolean checkPermissionForReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PHONE_CALL) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    View layout = findViewById(R.id.dialpad);
                    Snackbar mySnackbar = Snackbar.make(layout, R.string.noCallPermission, 3000);
                    mySnackbar.show();
                }
                else if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences sharedPref = this.getSharedPreferences(
                            getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", sharedPref.getString("phoneNumber", ""), null)));
                }
            }
        }
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    View layout = findViewById(R.id.dialpad);
                    Snackbar mySnackbar = Snackbar.make(layout, R.string.externalStorageNoPermissionError, 3000);
                    mySnackbar.show();
                    useSound = false;
                }
                else if(grantResult == PackageManager.PERMISSION_GRANTED) {
                    useSound = true;
                }
            }
        }
        if(isExternalStorageMounted() && useSound) {
            SoundPlayer.getInstance(this).loadSound(getBaseContext());
        }
        else {
            View layout = findViewById(R.id.dialpad);
            Snackbar mySnackbar = Snackbar.make(layout, R.string.externalStorageNoSDCardError, 3000);
            mySnackbar.show();
            useSound = false;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.general_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startSettingsActivity.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
                startSettingsActivity.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                startActivity(startSettingsActivity);
                return true;
            case R.id.action_bar_call_list:
                Intent startCallListActivity = new Intent(this, CallListActivity.class);
                startActivity(startCallListActivity);
                return true;
            case R.id.action_bar_download:
                Intent startDownloadActivity = new Intent(this, DownloadActivity.class);
                startDownloadActivity.putExtra("url", "http://dt031g.programvaruteknik.nu/dialpad/sounds/");
                startActivity(startDownloadActivity);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SoundPlayer.getInstance(this);
        super.onCreate(savedInstanceState);
        dp = new Dialpad(this);
        setContentView(R.layout.activity_dial);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


    }

    @Override
    public void onResume(){
        super.onResume();
        shouldWeUseSound();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SoundPlayer.getInstance(this).destroy();
    }




}
