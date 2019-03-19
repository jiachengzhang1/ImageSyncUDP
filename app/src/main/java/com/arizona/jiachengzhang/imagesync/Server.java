package com.arizona.jiachengzhang.imagesync;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Server extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Thread server = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("dsafdsdfa");
                    UDP_Server.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        server.start();
    }
}
