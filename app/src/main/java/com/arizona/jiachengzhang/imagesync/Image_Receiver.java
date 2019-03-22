package com.arizona.jiachengzhang.imagesync;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Image_Receiver {

    private static final int MAX_TIMEOUT = 4000;

    // ths client receives the image from the givn socket
    public static ArrayList<ImagePacket> receive_image (MulticastSocket multicastSocket, InetAddress group, int port) {

        // receive the Image segment packets stored in an array list
        ArrayList<ImagePacket> imagePackets = receive(multicastSocket, group, port);

        // print out the segment orders
        for (int j = 0; j<imagePackets.size(); j++) {
            System.out.println("Segment: " + imagePackets.get(j).getOrder());
        }

        System.out.println(imagePackets.size());
        return imagePackets;
    }

    // ths client receives the image as segments stored in an array list from the given socket
    private static ArrayList<ImagePacket> receive (MulticastSocket multicastSocket, InetAddress group, int port) {
        int order = 0;
        ArrayList<ImagePacket> imagePackets = new ArrayList<>();
        while (true) {

            // current requesting order in bytes
            ByteBuffer orderBuf = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(order);
            byte[] orderBytes = orderBuf.array();

            try {
                multicastSocket.setSoTimeout(MAX_TIMEOUT); // set time out

                // request for a packet having the given order
                DatagramPacket requestByOrder = new DatagramPacket(orderBytes, orderBytes.length, group, port);
                multicastSocket.send(requestByOrder);

                byte[] buf = new byte[5000];
                DatagramPacket receivedPacket = new DatagramPacket(buf, buf.length, group, port);
                multicastSocket.receive(receivedPacket);

                String done = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                // if the signal says it's done, then get out the loop
                if (order != 0 && done.equals("done")) {
                    String str = "received";
                    DatagramPacket check = new DatagramPacket(str.getBytes(), str.getBytes().length, group, port);
                    multicastSocket.send(check);
                    order = 0;
                    break;
                }

                // while the signal says done but order is re-set (0), then send order number 0 to the server
                // wait till the server sends the order number 0's packet
                while (order == 0 && done.equals("done")) {
                    // request for a packet having the given order
                    requestByOrder = new DatagramPacket(orderBytes, orderBytes.length, group, port);
                    multicastSocket.send(requestByOrder);

                    buf = new byte[5000];
                    receivedPacket = new DatagramPacket(buf, buf.length, group, port);
                    multicastSocket.receive(receivedPacket);
                    done = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                }

                // add the received segment into the list
                ByteArrayInputStream bis = new ByteArrayInputStream(receivedPacket.getData());
                ObjectInput in = new ObjectInputStream(bis);
                ImagePacket imagePacket = (ImagePacket) in.readObject();
                imagePackets.add(imagePacket);
                System.out.println("Received packet order: " + imagePacket.getOrder());

                // next order
                order ++;
                //System.out.println(done);
            } catch (SocketTimeoutException e) {
                System.out.println("Time out for order " + order + ". Requesting new one");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Size is " + imagePackets.size());
        return imagePackets;
    }
}
