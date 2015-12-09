package org.teliinc.h1locator;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by chris.teli on 12/7/2015.
 */
public class LocationManagerHandler {
    String providerName;
    Context ctx;
    double lat,lon;
    LocationManager mLocationManager;

    LocationManagerHandler(Context ctx, double lat, double lon) {
        this.ctx = ctx;
        this.lat = lat;
        this.lon = lon;
        mLocationManager = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
    }


    // Remove the test provider
    void shutdown() {
        if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }
    }

    // Run Asynchronus Task
    void run() {
        new MockLocationUpdate().execute("");
    }

    // Async Task to handle updates
    private class MockLocationUpdate extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            for (int i = 0; i < 5; i++) {
                try {
                    Log.i("MockLocationUpdate", "Lat " + lat + ": lon " + lon);
                    setMockLocation(lat, -lon);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        private void setMockLocation(double lat, double lon) {
            if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
                //mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
            }

            mLocationManager.addTestProvider
                    (
                            LocationManager.GPS_PROVIDER,
                            "requiresNetwork" == "",
                            "requiresSatellite" == "",
                            "requiresCell" == "",
                            "hasMonetaryCost" == "",
                            "supportsAltitude" == "",
                            "supportsSpeed" == "",
                            "supportsBearing" == "",

                            android.location.Criteria.POWER_LOW,
                            android.location.Criteria.ACCURACY_FINE
                    );

            Location newLocation = new Location(LocationManager.GPS_PROVIDER);


            newLocation.setLatitude(lat);
            newLocation.setLongitude(lon);

            newLocation.setAccuracy(500);
            newLocation.setTime(System.currentTimeMillis());

            mLocationManager.setTestProviderEnabled
                    (
                            LocationManager.GPS_PROVIDER,
                            true
                    );

            mLocationManager.setTestProviderStatus
                    (
                            LocationManager.GPS_PROVIDER,
                            LocationProvider.AVAILABLE,
                            null,
                            System.currentTimeMillis()
                    );


            newLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

            mLocationManager.setTestProviderLocation
                    (
                            LocationManager.GPS_PROVIDER,
                            newLocation
                    );
        }
    }
}
