package com.arizona.jiachengzhang.imagesync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Client extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        Button fetch_request = findViewById(R.id.fetch_request);
        fetch_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread client = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UDP_Client.run();
                    }
                });
                client.start();
            }
        });

    }
}
