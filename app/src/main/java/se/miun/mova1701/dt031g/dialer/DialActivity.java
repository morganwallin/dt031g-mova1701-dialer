package se.miun.mova1701.dt031g.dialer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
                SoundPlayer.getInstance(this);
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
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    View layout = findViewById(R.id.dialpad);
                    Snackbar mySnackbar = Snackbar.make(layout, R.string.externalStorageNoPermissionError, 3000);
                    mySnackbar.show();
                    useSound = false;
                    return;
                }
            }
        }
        if(isExternalStorageMounted()) {
            useSound = true;
            SoundPlayer.getInstance(this);
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
        if (id == R.id.general_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startSettingsActivity.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
            startSettingsActivity.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SoundPlayer.getInstance(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dial);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        shouldWeUseSound();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SoundPlayer.getInstance(this).destroy();
    }




}
