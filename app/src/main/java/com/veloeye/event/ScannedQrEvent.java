package com.veloeye.event;

/**
 * Created by Florin Mihalache on 17.09.2015.
 */
public class ScannedQrEvent {
    public String getQrcode() {
        return qrcode;
    }

    public String qrcode;

    public ScannedQrEvent(String qrcode) {
        this.qrcode = qrcode;
    }


}
