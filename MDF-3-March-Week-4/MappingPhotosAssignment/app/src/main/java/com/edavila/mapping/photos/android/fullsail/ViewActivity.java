// Eddy Davila
// MDF-3
// Week-4
// Full Sail University

package com.edavila.mapping.photos.android.fullsail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewActivity extends ActionBarActivity implements View.OnClickListener {

    private MyMarker myMarker;
    private TextView textView_name;
    private TextView textView_detail;
    private ImageView myImageView;
    private Button buttonDelete;
    private Button buttonEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_view);

        //show back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent myIntent = getIntent();
        myMarker = (MyMarker) myIntent.getSerializableExtra("marker");

        if (myMarker != null) {
            textView_name = (TextView) findViewById(R.id.tv_name);
            textView_detail = (TextView) findViewById(R.id.tv_detail);
            myImageView = (ImageView) findViewById(R.id.image);
            buttonDelete = (Button) findViewById(R.id.buttonDelete);
            buttonDelete.setOnClickListener(this);
            buttonEdit = (Button) findViewById(R.id.buttonEdit);
            buttonEdit.setOnClickListener(this);

            textView_name.setText(myMarker.getName());
            textView_detail.setText(myMarker.getDetail());
            String path = myMarker.getPhotoPath();
            if (path != null && !path.isEmpty()) {
                /*File file = new File(path);
                if (file.exists()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                        if (bitmap != null) {
                            myImageView.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }*/
                myImageView.setImageBitmap(BitmapFactory.decodeFile(path));
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            removeMarker();
            return true;
        } else if (item.getItemId() == R.id.action_edit) {
            editMarker();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    private void removeMarker() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Do you want to remove the marker and detail?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (myMarker != null) {
                            FileReadAndWrite fileReadAndWrite = new FileReadAndWrite();
                            fileReadAndWrite.delete_marker_from_file(myMarker);
                            Intent myIntent = new Intent(ViewActivity.this, MainActivity.class);
                            startActivity(myIntent);
                            finish();
                        }
                    }
                })
                .show();
    }

    private void editMarker() {
        Intent myIntent = new Intent(ViewActivity.this, FormActivity.class);
        myIntent.putExtra("marker", myMarker);
        startActivity(myIntent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonDelete) {
            removeMarker();
        }
        if (v.getId() == R.id.buttonEdit) {
            editMarker();
        }
    }
}
