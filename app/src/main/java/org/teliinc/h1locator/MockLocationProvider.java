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
    static double lon = 0.0, lat = 0.0;
    String providerName;
    Context ctx;
    LocationManagerHandler mLocationManager;
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

        Log.i("MockLocationProvider", "Received start id " + startId + ": " + intent);
        Log.i("MockLocationProvider", "Lat " + lat+ ": lon " + lon);

        mLocationManager = new LocationManagerHandler(ctx,lat,lon);


        // Run LocaltionManagerHandler
        mLocationManager.run();
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

    public void shutdown() {
        mLocationManager.shutdown();
    }
}