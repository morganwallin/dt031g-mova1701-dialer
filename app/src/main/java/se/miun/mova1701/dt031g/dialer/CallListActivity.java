package se.miun.mova1701.dt031g.dialer;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class CallListActivity extends ListActivity {
    public static ListView CallListListView;
    DatabaseHandler dbHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        CallListListView = (ListView) findViewById(android.R.id.list);
        dbHandler = new DatabaseHandler(this);
        if(dbHandler.numberOfRows() == 0) {
            CallListListView.setEmptyView(findViewById(R.id.empty));
        }
        ArrayList<HashMap<String, String>> callsList = dbHandler.getCalls();

        ArrayAdapter<HashMap<String, String>> adapter = new ArrayAdapter<>(this, R.layout.activity_listview, callsList);
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
