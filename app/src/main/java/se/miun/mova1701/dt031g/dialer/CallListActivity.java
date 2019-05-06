package se.miun.mova1701.dt031g.dialer;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CallListActivity extends ListActivity {
    public static ListView CallListListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        CallListListView = (ListView) findViewById(android.R.id.list);
        SharedPreferences sharedPref = this.getBaseContext().getSharedPreferences(
                getResources().getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String tmpString = sharedPref.getString("listView", "No numbers are stored!");
        String[] listArray = tmpString.split(",");
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<>(this, R.layout.activity_listview, listArray);

        CallListListView.setAdapter(adapter);
    }
    public static ListView getList() {
        return CallListListView;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("listView", CallListListView.getAdapter().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString("listView");

    }
}
