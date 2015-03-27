// Eddy Davila
// MDF-3
// Week-4
// Full Sail University

package com.edavila.mapping.photos.android.fullsail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FormActivity extends ActionBarActivity implements View.OnClickListener, LocationListener {

    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    private EditText editTextName;
    private EditText editTextDetail;
    private ImageView myImageView;
    private Uri myImageUri;
    private Button buttonSave;

    TextView myLatitude;
    TextView myLongitude;

    LocationManager myLocationManager;

    Context context;

    boolean isEnableGPS = true;

    MyMarker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_form);

        initialViews();

        //show back button in action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        context = this;

        //if edit functionality triggers
        Intent myIntent = getIntent();
        myMarker = (MyMarker) myIntent.getSerializableExtra("marker");
        if (myMarker != null) {
            editTextName.setText(myMarker.getName());
            editTextDetail.setText(myMarker.getDetail());
            String photo_Path = myMarker.getPhotoPath();
            if (photo_Path != null && !photo_Path.isEmpty() && photo_Path.length() > 0) {
                myImageView.setImageBitmap(BitmapFactory.decodeFile(photo_Path));
            }
            myLatitude.setText(myMarker.getLatitude());
            myLongitude.setText(myMarker.getLongitude());
        }

        //get and set latitude and longitude by long click action.
        Double currentLatitude = myIntent.getDoubleExtra("Latitude:", 0);
        Double currentLongitude = myIntent.getDoubleExtra("Longitude:", 0);
        if (currentLatitude != null && currentLatitude != 0 && currentLongitude != null && currentLongitude != 0) {
            isEnableGPS = false;
            myLatitude.setText(String.valueOf(currentLatitude));
            myLongitude.setText(String.valueOf(currentLongitude));
        }

    }

    private void initialViews()
    {
        editTextName = (EditText) findViewById(R.id.et_Name);
        editTextDetail = (EditText) findViewById(R.id.et_Detail);
        myImageView = (ImageView) findViewById(R.id.image);
        myImageView.setOnClickListener(this);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(this);
        myLatitude = (TextView) findViewById(R.id.latitude);
        myLongitude = (TextView) findViewById(R.id.longitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_form, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.image)
            takeNewPhoto();
        if(v.getId() == R.id.buttonSave)
            saveCurrentInfo();
    }

    private void enableGps() {
        if (isEnableGPS) {
            if (myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

                Location myLocation = myLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (myLocation != null) {
                    myLatitude.setText("" + myLocation.getLatitude());
                    myLongitude.setText("" + myLocation.getLongitude());
                }

            } else {
                new AlertDialog.Builder(this)
                        .setTitle("GPS Unavailable")
                        .setMessage("Please make sure GPS is enabled in the system settings.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                            }

                        })
                        .show();
            }

        }
    }

    private void saveCurrentInfo() {
        if (myMarker != null) {
            myMarker.setName(editTextName.getText().toString());
            myMarker.setDetail(editTextDetail.getText().toString());
            if (myImageUri != null) {
                myMarker.setPhotoPath(myImageUri.getPath());
            }
            FileReadAndWrite fileReadAndWrite = new FileReadAndWrite();
            fileReadAndWrite.update_marker_from_file(myMarker);

        } else {
            String name = editTextName.getText().toString();
            String detail = editTextDetail.getText().toString();
            String lat = myLatitude.getText().toString();
            String lng = myLongitude.getText().toString();
            String photo_path = myImageUri.getPath();

            if (name != null && !name.isEmpty() &&
                    detail != null && !detail.isEmpty() &&
                    lat != null && !lat.isEmpty() &&
                    lng != null && !lng.isEmpty() &&
                    photo_path != null && !photo_path.isEmpty()) {

                //create new CMarker object "marker"
                MyMarker marker = new MyMarker();
                marker.setName(name);
                marker.setDetail(detail);
                marker.setPhotoPath(photo_path);
                marker.setLatitude(lat);
                marker.setLongitude(lng);

                //save the info in file
                FileReadAndWrite fileReadAndWrite = new FileReadAndWrite();
                fileReadAndWrite.add_marker_to_file(marker);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Fill all the info.")
                        .setTitle("Error")
                        .setPositiveButton("OK", null);
                // Now create the AlertDialog object and then display it to user
                builder.create();
                builder.show();
            }
        }

        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        else if(item.getItemId() == R.id.menu_take_picture){
            takeNewPhoto();
            return true;
        }
        else if(item.getItemId() == R.id.menu_save){
            saveCurrentInfo();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);

    }

    private void takeNewPhoto() {
        Intent myIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        myImageUri = getImageUri();
        if (myImageUri != null) {
            myIntent.putExtra(MediaStore.EXTRA_OUTPUT, myImageUri);
        }
        startActivityForResult(myIntent, REQUEST_TAKE_PICTURE);
    }

    private Uri getImageUri() {

        String myImageName = new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date(System.currentTimeMillis()));

        File myImageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File appFileDirectory = new File(myImageDirectory, "CameraExample");
        appFileDirectory.mkdirs();

        File myImage = new File(appFileDirectory, myImageName + ".jpg");
        try {
            myImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(myImage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PICTURE && resultCode != RESULT_CANCELED) {
            if (myImageUri != null) {
                myImageView.setImageBitmap(BitmapFactory.decodeFile(myImageUri.getPath()));
                addImageToGallery(myImageUri);
            } else {
                myImageView.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
            }
        }
        enableGps();
    }

    private void addImageToGallery(Uri imageUri) {
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScannerIntent.setData(imageUri);
        sendBroadcast(mediaScannerIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        enableGps();
    }

    @Override
    protected void onPause() {
        super.onPause();

        myLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        myLatitude.setText("" + location.getLatitude());
        myLongitude.setText("" + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}