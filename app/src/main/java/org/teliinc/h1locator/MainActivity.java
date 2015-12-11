package org.teliinc.h1locator;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int SERVERPORT = 38301;
    // Mock location
    MockLocationProvider mock;
    // UI Binding
    @Bind(R.id.longitude)
    EditText etLongitude;
    @Bind(R.id.latitude)
    EditText etLatitude;
    Handler updatePositionInformation;

    Thread serverThread = null;
    // Socket Initialization
    private ServerSocket serverSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bind with butterknife
        ButterKnife.bind(this);

        // Initialize UI
        etLongitude.setText("-117.42");
        etLatitude.setText("33.33");

        // Initialize Threading requirements
        updatePositionInformation = new Handler();
        this.serverThread = new Thread(new ServerThread());

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

    public void connectToHost(View view) {
        this.serverThread.start();
    }

    class ServerThread implements Runnable {

        public void run() {
            Socket socket = null;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    socket = serverSocket.accept();
                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;
        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    String read = input.readLine();
                    Log.i("Client Read", read);
                    updatePositionInformation.post(new updateUIPosotion(read));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class updateUIPosotion implements Runnable {
        private String msg;

        public updateUIPosotion(String str) {
            this.msg = str;
        }

        @Override
        public void run() {
            etLongitude.setText(msg);
        }
    }

}
