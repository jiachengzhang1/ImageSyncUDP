package com.arizona.jiachengzhang.imagesync;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Arrays;

public class UDP_Client extends AsyncTask<Object, Object, Boolean> {
    private static final int MAX_TIMEOUT = 4000;


    private Boolean run() {

        try {
            int port = 2214;
            InetAddress group = InetAddress.getByName("239.22.22.114");
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);

            String str = "hello";
            DatagramPacket request = new DatagramPacket(str.getBytes(),
                    str.getBytes().length, group, port);
            multicastSocket.send(request);

            multicastSocket.setSoTimeout(MAX_TIMEOUT);
            byte[] buf = new byte[256];
            DatagramPacket response = new DatagramPacket(buf, buf.length);
            multicastSocket.receive(response);

            System.out.println(Arrays.toString(response.getData()));

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
