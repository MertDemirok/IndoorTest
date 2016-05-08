package com.example.mert.rssilistener.datatype_Class;

/**
 * Created by mertdemirok on 18/04/16.
 */
public class AccessPointData {
    public String loc_Name_post;
    public String RSSI_post;
    public String SSID_post;
    public String MAC_post;

    public String getLoc_Name_post() {
        return loc_Name_post;
    }

    public String getRSSI_post() {
        return RSSI_post;
    }

    public String getSSID_post() {
        return SSID_post;
    }

    public String getMAC_post() {
        return MAC_post;
    }

    public void setLoc_Name_post(String loc_Name_post) {
        this.loc_Name_post = loc_Name_post;
    }

    public void setMAC_post(String MAC_post) {
        this.MAC_post = MAC_post;
    }

    public void setSSID_post(String SSID_post) {
        this.SSID_post = SSID_post;
    }

    public void setRSSI_post(String RSSI_post) {
        this.RSSI_post = RSSI_post;
    }

    @Override
    public String toString() {
        return "AccessPointData[log_Name="+loc_Name_post+", RSSI_post="+RSSI_post+", SSID_post="+SSID_post+", MAC_post="+MAC_post+"]";
    }
}
