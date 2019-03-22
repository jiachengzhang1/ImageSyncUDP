package com.arizona.jiachengzhang.imagesync;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Client extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        final Activity thisActivity = this;

        Button fetch_request = findViewById(R.id.fetch_request);
        fetch_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread client = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        // ask for the external written and read permission
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(thisActivity, permissions, 1);

                        UDP_Client.run();
                    }
                });
                client.start();
            }
        });

    }
}