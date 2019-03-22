package com.arizona.jiachengzhang.imagesync;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class Image_Sender {

    public static Boolean send_image (InetAddress group,
                                   int port,
                                   String imageName) {
        MulticastSocket multicastSocket;
        try {
            final int byteLength = 4096;

            multicastSocket = new MulticastSocket(port);
            multicastSocket.setLoopbackMode(true);
            multicastSocket.joinGroup(group);

            // convert the images into byte array
            Bitmap bitmap_image1 = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/Pictures/" + imageName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap_image1.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();

            System.out.println(imageBytes.length);


            // dividing the image's byte array into multiple segments
            byte[] buf = new byte[byteLength];
            int k = 0;
            int count = 0;
            ArrayList<ImagePacket> segmentList = new ArrayList<>();
            for (int j = 0; j < imageBytes.length; j++) {
                buf[k] = imageBytes[j];
                k++;
                if (j != 0 && j % (byteLength - 1) == 0) {
                    segmentList.add(new ImagePacket(count, buf));
                    buf = new byte[byteLength];
                    k = 0;
                    count++;
                }
            }

            // add the imagePacket object into the segment list
            segmentList.add(new ImagePacket(count, buf));

            int orderOfLastSegment = segmentList.size() - 1;
            while (true) {

                // receive order number
                byte[] orderInfo = new byte[64];
                DatagramPacket orderInfoPacket = new DatagramPacket(orderInfo, orderInfo.length, group, port);
                multicastSocket.receive(orderInfoPacket);

                byte[] orderNumBytes = orderInfoPacket.getData();
                ByteBuffer wrapped = ByteBuffer.wrap(orderNumBytes);
                int orderNum = wrapped.getInt();

                // if the client asks for the order the server doesn't have
                while (orderNum > orderOfLastSegment) {
                    // send done signal
                    String signal = "done";
                    DatagramPacket signalPacket = new DatagramPacket(signal.getBytes(), signal.getBytes().length,
                            group, port);
                    multicastSocket.send(signalPacket);

                    multicastSocket.setSoTimeout(4000);

                    // receive feedback from the client
                    DatagramPacket check = new DatagramPacket(new byte[64], 64, group, port);

                    try {
                        multicastSocket.receive(check); // if time out, re-send done signal
                        String checkStr = new String(check.getData(), 0, check.getData().length);
                        if (checkStr.equals("received")) { // if get "received" message from the client, then done
                            return true;
                        }
                        else { // otherwise, it may be a order number, it means the client receives the done signal too
                               // then done
                            orderNumBytes = check.getData();
                            wrapped = ByteBuffer.wrap(orderNumBytes);
                            orderNum = wrapped.getInt();
                            System.out.println(orderNum);
                            if (orderNum == 0)
                                return true;
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("Confirmation is not arrived.");
                    }
                }

                // prepare and send segment packet with corresponding order number
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos);
                out.writeObject(segmentList.get(orderNum));
                out.flush();
                byte[] segmentBytes = bos.toByteArray();
                DatagramPacket segmentPacket = new DatagramPacket(segmentBytes, segmentBytes.length, group, port);
                multicastSocket.send(segmentPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
