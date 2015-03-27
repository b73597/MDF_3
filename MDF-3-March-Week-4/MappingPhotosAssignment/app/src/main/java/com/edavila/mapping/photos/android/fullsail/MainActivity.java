// Eddy Davila
// MDF-3
// Week-4
// Full Sail University

package com.edavila.mapping.photos.android.fullsail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_main);
        try {
            MyMapFragment myFragment = new MyMapFragment();
            getFragmentManager().beginTransaction().add(R.id.map, myFragment).commit();
        } catch(Exception ex) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_menu){
            startAddActivity();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private void startAddActivity() {
        Intent myIntent = new Intent(this, FormActivity.class);
        startActivity(myIntent);
    }
}
