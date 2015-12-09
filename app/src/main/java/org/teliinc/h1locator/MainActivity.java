package org.teliinc.h1locator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // Mock location
    MockLocationProvider mock;
    String s_latitude="21.44";
    String s_longitude="-157.78";

    @Bind(R.id.longitude) EditText etLongitude;
    @Bind(R.id.latitude) EditText etLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        etLongitude.setText("-117.42");
        etLatitude.setText("33.33");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        mock.shutdown();
        super.onDestroy();
    }

    // Method to start the service
    public void startService(View view) {
        MockLocationProvider.lat = Double.parseDouble(etLatitude.getText().toString());
        MockLocationProvider.lon = Double.parseDouble(etLongitude.getText().toString());

        // If service is running stop it
        stopService(new Intent(getBaseContext(), MockLocationProvider.class));

        Intent intent = new Intent(getBaseContext(), MockLocationProvider.class);
        startService(intent);
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), MockLocationProvider.class));
    }
}
