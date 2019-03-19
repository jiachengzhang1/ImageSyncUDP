package com.arizona.jiachengzhang.imagesync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.util.ArrayList;
import java.util.Arrays;

public class UDP_Server  {

    public static void run() {

        try {
            int port = 2214;
            MulticastSocket multicastSocket = new MulticastSocket(port);
            InetAddress group = InetAddress.getByName("239.22.22.114");
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[100];

            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/image1.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageBytes = baos.toByteArray();

            DatagramPacket request = new DatagramPacket(new byte[256], 256);

            System.out.println("Waiting for Request.");

            multicastSocket.receive(request);

            System.out.println("Received Request.");

            System.out.println(imageBytes.length);

            byte[] buf = new byte[32768];
            int k = 0;
            int count = 1;
            //ArrayList<byte[]> bufList = ArrayList<byte[]>();
            for (int j = 0; j < imageBytes.length; j++) {
                // System.out.println(count);
                buf[k] = imageBytes[j];
                k++;
                if (j % 32768 == 0) {
                    //System.out.println(buf[k-1]);
                    DatagramPacket imagePacket = new DatagramPacket(buf, buf.length, group, port);
                    multicastSocket.send(imagePacket);
                    System.out.println("Package number " + count);
                    count++;
                    buf = new byte[32768];
                    k = 0;
                    //Thread.sleep(1000);
                }
            }

            DatagramPacket imagePacket = new DatagramPacket(buf, buf.length, group, port);
            multicastSocket.send(imagePacket);
            System.out.println("Package number " + count);

            System.out.println("Reply sent.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
