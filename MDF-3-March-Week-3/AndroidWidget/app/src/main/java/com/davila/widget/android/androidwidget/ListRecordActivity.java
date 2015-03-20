// Eddy Davila
// MDF 3
// Week 3
// Full Sail University

package com.davila.widget.android.androidwidget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.davila.widget.android.androidwidget.adapter.CustomListAdapter;
import com.davila.widget.android.androidwidget.storage.FileReadAndWrite;
import com.davila.widget.android.androidwidget.storage.Record;

import java.util.ArrayList;

public class ListRecordActivity extends ActionBarActivity implements View.OnClickListener, ListView.OnItemClickListener {

    final int REQUEST_CODE_NEW_RECORD = 2;

    //    Button btn_add_new;
    ListView list;

    ArrayList<Record> records;
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_record);
//        btn_add_new = (Button) findViewById(R.id.btn_add_new);
//        btn_add_new.setOnClickListener(this);
        list = (ListView) findViewById(R.id.listView1);
        records = new ArrayList<Record>();
        PopulateRecords();
        adapter = new CustomListAdapter(ListRecordActivity.this, records);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

    }

    private void PopulateRecords() {
        if (records != null) {
            if (records.size() > 0) {
                records.clear();
            }
            FileReadAndWrite frw = new FileReadAndWrite();
            for (Record record : frw.get_all_records_from_file()) {
                records.add(record);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_new:
                Intent intent = new Intent(ListRecordActivity.this, NewRecordActivity.class);
                startActivityForResult(intent, REQUEST_CODE_NEW_RECORD);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_NEW_RECORD) {
            PopulateRecords();
            adapter.notifyDataSetChanged();
            Intent intent = new Intent(getApplicationContext(), RecordsWidgetProvider.class);
            intent.setAction("com.davila.widget.android.ACTION_ADDED_NEW");
            sendBroadcast(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.btn_add_new:
//                Intent intent = new Intent(ListRecordActivity.this, NewRecordActivity.class);
//                startActivityForResult(intent, Constant.REQUEST_CODE_NEW_RECORD);
//                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Record record = adapter.getItem(position);
        Intent intent = new Intent(ListRecordActivity.this, ViewRecordActivity.class);
        intent.putExtra("view_record", record);
        startActivity(intent);
    }
}
