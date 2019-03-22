package com.arizona.jiachengzhang.imagesync;

import java.io.Serializable;

public class ImagePacket implements Serializable {

    private int order;
    private byte[] imageSegment;

    public ImagePacket (int order, byte[] imageSegment) {

        this.order = order;
        this.imageSegment = imageSegment;

    }


    public int getOrder () {
        return this.order;
    }

    public byte[] getImageSegment() {
        return imageSegment;
    }
}
