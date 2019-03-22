package com.arizona.jiachengzhang.imagesync;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;

public class UDP_Client {


    public static void run() {

        try {
            int port = 2222;

            // set up the group and multicast socket
            InetAddress group = InetAddress.getByName("224.0.0.0");
            MulticastSocket multicastSocket = new MulticastSocket(port);
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(group);

            // send a request for images
            String str = "hello";
            DatagramPacket request = new DatagramPacket(str.getBytes(),
                    str.getBytes().length, group, port);
            multicastSocket.send(request);

            ArrayList<ImagePacket> imageBytes = Image_Receiver.receive_image(multicastSocket, group, port);
            saveImage(imageBytes, "test1.jpg");

            ArrayList<ImagePacket> imageBytes_image2 = Image_Receiver.receive_image(multicastSocket, group, port);
            saveImage(imageBytes, "test2.jpg");

            //multicastSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveImage (ArrayList<ImagePacket> imageBytes, String fileName) {
        byte[] image = imageBytes.get(0).getImageSegment();

        for (int i = 1; i<imageBytes.size(); i++) {
            byte[] temp = imageBytes.get(i).getImageSegment();
            byte[] combined = new byte[image.length + temp.length];

            int count = 0;
            for (int j = 0; j<image.length; j++) {
                combined[count] = image[j];
                count ++;
            }
            for (int j = 0; j<temp.length; j++) {
                combined[count] = temp[j];
                count ++;
            }
            image = new byte[combined.length];
            for (int j = 0; j<combined.length; j++) {
                image[j] = combined[j];
            }
        }

        System.out.println(image.length);
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        if (bitmap == null)  System.out.println("Invalid image data.");
        else {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + fileName);
            try {

                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
