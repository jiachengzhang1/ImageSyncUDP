package com.arizona.jiachengzhang.imagesync;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons for choosing between run as client or as server
        Button client = findViewById(R.id.main_client);
        Button server = findViewById(R.id.main_server);

        // click the client button, then start the app as client
        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent client_intent = new Intent(MainActivity.this, Client.class);
                startActivity(client_intent);
            }
        });

        // click the server button, then start the app as server
        server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent server_intent = new Intent(MainActivity.this, Server.class);
                startActivity(server_intent);
            }
        });

    }
}
