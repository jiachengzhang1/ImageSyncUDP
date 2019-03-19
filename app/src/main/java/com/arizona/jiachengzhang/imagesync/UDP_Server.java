package com.arizona.jiachengzhang.imagesync;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class UDP_Server extends AsyncTask<Object, Object, Boolean> {

    private boolean run() {

        try {
            int port = 2214;
            MulticastSocket multicastSocket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName("239.22.22.114");
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[100];

            DatagramPacket request = new DatagramPacket(new byte[256], 256);

            System.out.println("Waiting for Request.");

            multicastSocket.receive(request);

            System.out.println("Received Request.");

            byte[] buf = request.getData();
            DatagramPacket reply = new DatagramPacket(buf, buf.length, group, port);

            System.out.println("Reply sent.");

            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    protected Boolean doInBackground(Object... objects) {
        return run();
    }

    protected void onPostExecute(Boolean success) {
        if (success) System.out.println("Done.");
        else System.out.println("Some thing is wrong.");
    }
}
