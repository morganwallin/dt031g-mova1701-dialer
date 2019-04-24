package se.miun.mova1701.dt031g.dialer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Dialpad extends ConstraintLayout {


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

    void init() {
        View parent = getRootView();
        TextView phoneNumberEditText = (TextView) parent.findViewById(R.id.phoneNumberEditText);
        Button callButton = (Button) parent.findViewById(R.id.callButton);
        Button deleteButton = (Button) parent.findViewById(R.id.deleteButton);


        callButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberEditText.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                SharedPreferences sharedPref = getContext().getSharedPreferences(
                        getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                boolean saveOrNot = sharedPref.getBoolean("saveSwitch", false);
                if(saveOrNot) {
                    String tmpString = sharedPref.getString("listView", "No numbers are stored!");
                    if(tmpString.contains("No numbers are stored!")) {
                        tmpString = phoneNumber;
                    }
                    else {
                        tmpString = tmpString + "," + phoneNumber;
                    }
                    sharedPref.edit().putString("listView", tmpString).apply();
                    String[] listArray = tmpString.split(",");
                    ArrayAdapter<String> adapter;
                    adapter = new ArrayAdapter<>(getContext(), R.layout.activity_listview, listArray);
                    ListView lv = CallListActivity.getList();
                    lv.setAdapter(adapter);
                }
                getContext().startActivity(callIntent);
            }
        });

        deleteButton.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                String phoneNumberString = phoneNumberEditText.getText().toString();
                int phoneNumberLength = phoneNumberString.length();
                if(phoneNumberLength != 0) {
                    phoneNumberString = phoneNumberString.substring(0, phoneNumberLength-1);
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


}
