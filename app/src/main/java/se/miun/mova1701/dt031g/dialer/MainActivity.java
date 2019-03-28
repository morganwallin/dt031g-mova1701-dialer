package se.miun.mova1701.dt031g.dialer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

//MainActivity Class
public class MainActivity extends AppCompatActivity {
    protected boolean dialogShowing = false;

    //Create 'About'-dialog window
    protected void createAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        //Set messages to what is specified in strings.xml
        builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);
        //Block back-button
        builder.setCancelable(false)
                //Add OK-button for the user to be able to close the dialog
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogShowing = false;
                    }
                });
        //Create alert and show it
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Save dialogShowing boolean on orientation change to know if we should recreate it
    //when the phone changes orientation
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("dialogShowing", dialogShowing);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if it's a new instance or a saved one
        if(savedInstanceState != null) {
            //Retrieve the boolean dialogShowing to know if we should recreate the dialog
            dialogShowing = savedInstanceState.getBoolean("dialogShowing");
        } else {dialogShowing = false;}
        setContentView(R.layout.activity_main);

        Button dialButton = (Button) findViewById(R.id.dialButton);
        dialButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, DialActivity.class));
            }
        });

        Button callListButton = (Button) findViewById(R.id.callListButton);
        callListButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, CallListActivity.class));
            }
        });

        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        Button mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

        //Checks if we should create the dialog or not
        if(dialogShowing) {
            createAboutDialog();
        }

        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                dialogShowing = true;
                createAboutDialog();
            }
        });
    }
}
