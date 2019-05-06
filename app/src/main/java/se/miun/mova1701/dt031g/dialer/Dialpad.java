package se.miun.mova1701.dt031g.dialer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Dialpad extends ConstraintLayout {
    private static final int REQUEST_PHONE_CALL = 1;
    TextView phoneNumberEditText = null;
    Button callButton = null;
    Button deleteButton = null;
    SharedPreferences sharedPref = null;

    Dialpad(Context context) {
        super(context);
    }

    public Dialpad(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.dialpad, this);
        init();

    }

    public Dialpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.dialpad, this);
        init();
    }

    public boolean checkPermissionForCall() {
        return ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

    private void setOnClickListeners() {
        callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                sharedPref.edit().putString("phoneNumber", phoneNumber).apply();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", phoneNumber, null));

                if(sharedPref.getBoolean("saveSwitch", false)) {
                    String listViewString = sharedPref.getString("listView", "No numbers are stored!");
                    if(listViewString.contains("No numbers are stored!")) {
                        listViewString = phoneNumber;
                    }
                    else {
                        listViewString = listViewString + "," + phoneNumber;
                    }


                    sharedPref.edit().putString("listView", listViewString).apply();
                    String[] listArray = listViewString.split(",");
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, listArray);
                    ListView lv = CallListActivity.getList();
                    if(lv == null) {
                        lv = new ListView(getContext());
                    }
                    lv.setAdapter(adapter);

                }
                if(!checkPermissionForCall()) {
                    ActivityCompat.requestPermissions((Activity) getContext(), new String[]{Manifest.permission.CALL_PHONE},
                            REQUEST_PHONE_CALL);
                    View layout = findViewById(R.id.dialpad);
                    Snackbar mySnackbar = Snackbar.make(layout, R.string.noCallPermission, 3000);
                    mySnackbar.show();
                }
                else {
                    getContext().startActivity(callIntent);
                }
            }
        });

        deleteButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                String phoneNumberString = phoneNumberEditText.getText().toString();
                if(phoneNumberString.length() != 0) {
                    phoneNumberString = phoneNumberString.substring(0, phoneNumberString.length()-1);
                    phoneNumberEditText.setText(phoneNumberString);
                }
            }
        });
        deleteButton.setOnLongClickListener(new View.OnLongClickListener(){
            public boolean onLongClick(View v){
                phoneNumberEditText.setText("");
                return true;
            }
        });
    }

    void init() {
        View parent = getRootView();
        phoneNumberEditText = (TextView) parent.findViewById(R.id.phoneNumberEditText);
        callButton = (Button) parent.findViewById(R.id.callButton);
        deleteButton = (Button) parent.findViewById(R.id.deleteButton);
        sharedPref = getContext().getSharedPreferences(
                getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        setOnClickListeners();

    }


}
