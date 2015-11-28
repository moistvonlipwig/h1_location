package org.teliinc.h1locator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by chris.teli on 11/9/2015.
 */
public class MockLocationProvider extends Service {
    String providerName;
    Context ctx;
    LocationManager mLocationManager;

    /**
     * indicates how to behave if the service is killed
     */
    int mStartMode;

    /**
     * interface for clients that bind
     */
    IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    boolean mAllowRebind;

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        Log.i("LocalService", "Received start id ");
    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.providerName = LocationManager.GPS_PROVIDER;
        this.ctx = this;

        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        mLocationManager = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);

        double lat = 35.05;
        double lon = -117.01;
        int counter=0;
        while(counter < 1000) {
            setMockLocation(lat,lon);
            lat = lat + 0.2;
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            counter++;
        }
        return mStartMode;

    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        shutdown();
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
        /*
        Method method;
        try {
            method = Location.class.getMethod("makeComplete", new Class[0]);
            if (method != null) {
                try {
                    Log.d("Mock GPS", "Invoking makeComplete");
                    method.invoke(newLocation, new Object[0]);
                } catch (Exception exception) {
                    Log.d("Mock GPS", "Could not invoke message");
                }
            }
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       */
        mLocationManager.setTestProviderLocation
                (
                        LocationManager.GPS_PROVIDER,
                        newLocation
                );

        try {
            Thread.sleep(1000);
        } catch (Exception e) {

        }

    }

    public void pushLocation(double lat, double lon) {
        setMockLocation(lat, lon);
    }

    public void shutdown() {
        if (mLocationManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
            mLocationManager.removeTestProvider(LocationManager.GPS_PROVIDER);
        }
    }
}