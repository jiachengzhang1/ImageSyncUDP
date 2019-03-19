package com.arizona.jiachengzhang.imagesync;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.Arrays;

public class UDP_Client {
    private static final int MAX_TIMEOUT = 4000;

    public static void run() {

        try {
            int port = 2214;
            int count = 0;
            for (int i = 0; i<10000; i++) {
                InetAddress group = InetAddress.getByName("239.22.22.114");
                MulticastSocket multicastSocket = new MulticastSocket(port);
                multicastSocket.joinGroup(group);

                String str = "hello";
                DatagramPacket request = new DatagramPacket(str.getBytes(),
                        str.getBytes().length, group, port);
                multicastSocket.send(request);

                try {
                    multicastSocket.setSoTimeout(MAX_TIMEOUT);
                    byte[] buf = new byte[32768];
                    DatagramPacket response = new DatagramPacket(buf, buf.length);
                    multicastSocket.receive(response);
                    count ++;
                    System.out.println("Package number : " +count);
                    response.getData();
                } catch (SocketTimeoutException e) {
                    System.out.println("Package Time out");
                }

                // System.out.println(response.getData().length);
            }

        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
