package com.arizona.jiachengzhang.imagesync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Server extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        // the server receiver, receive the input signal
        Thread server_receiver = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UDP_Server.run_server_receiver();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        server_receiver.start();

        // shows the confirm button for the server phone, if the confirm is clicked, call
        // sender to send the images.
        final Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setVisibility(View.VISIBLE);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread server_sender = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UDP_Server.run_server_sender();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                server_sender.start();
                confirmButton.setVisibility(View.GONE);
            }
        });
    }
}
