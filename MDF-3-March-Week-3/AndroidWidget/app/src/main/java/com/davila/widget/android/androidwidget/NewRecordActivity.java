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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.davila.widget.android.androidwidget.storage.FileReadAndWrite;
import com.davila.widget.android.androidwidget.storage.Record;


public class NewRecordActivity extends ActionBarActivity implements View.OnClickListener {

    final int REQUEST_CODE_NEW_RECORD = 2;

    private EditText et_title;
    private EditText et_author;
    private EditText et_date;
    private Button btn_back;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record);
        et_title = (EditText) findViewById(R.id.et_title);
        et_author = (EditText) findViewById(R.id.et_author);
        et_date = (EditText) findViewById(R.id.et_date);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        //use to add back button in action bars
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception ex) {

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //handles the action bar back button by calling onbackpressed function
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_add_save:
                save_data();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_save:
                save_data();
                break;
        }
    }

    private void save_data() {
        Record record = new Record();
        FileReadAndWrite frw = new FileReadAndWrite();
        if (et_title.getText().toString() != null && !et_title.getText().toString().equalsIgnoreCase("") &&
                et_author.getText().toString() != null && !et_author.getText().toString().equalsIgnoreCase("") &&
                et_date.getText().toString() != null && !et_date.getText().toString().equalsIgnoreCase("")) {

            record.setTitle(et_title.getText().toString());
            record.setAuthor(et_author.getText().toString());
            record.setDater(et_date.getText().toString());

            frw.add_record_to_file(record);
            Intent intent = new Intent();
            setResult(REQUEST_CODE_NEW_RECORD, intent);
            finish();//finishing activity
        } else {
            Toast.makeText(getApplicationContext(), "Fill all field to save", Toast.LENGTH_LONG).show();
        }
    }
}
