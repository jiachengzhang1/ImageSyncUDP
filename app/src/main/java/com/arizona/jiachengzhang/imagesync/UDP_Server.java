package com.arizona.jiachengzhang.imagesync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.ContactsContract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.util.ArrayList;
import java.util.Arrays;

public class UDP_Server  {

    private static int port = 2222; // the port is 2222

    private static MulticastSocket multicastSocket;
    private static InetAddress group;

    // receive the request from the client.
    public static Boolean run_server_receiver() {
        try {

            multicastSocket = new MulticastSocket(port);
            group = InetAddress.getByName("224.0.0.0");
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(group);

            // receiving the request from the client
            DatagramPacket request = new DatagramPacket(new byte[256], 256);
            System.out.println("Waiting for Request.");
            multicastSocket.receive(request);
            System.out.println("Received Request.");

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // send the images back to the client if the Server asks to do so.
    public static void run_server_sender () {
        try {
            multicastSocket = new MulticastSocket(port);
            group = InetAddress.getByName("224.0.0.0");
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(group);

            // once the request is received, then start sending images
            if (Image_Sender.send_image(multicastSocket, group, port, "image1.jpg") &&
                    Image_Sender.send_image(multicastSocket, group, port, "image2.jpg"))
                System.out.println("All set!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
