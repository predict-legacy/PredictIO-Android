package io.predict.example.models;

import io.predict.TransportationMode;

public class PIOEvent {
    public double latitude;
    public double longitude;
    public double accuracy;
    public long timeStamp;
    public PIOEventType type;
    public TransportationMode transportationMode;

    public PIOEvent(double latitude, double longitude, double accuracy, long timeStamp, PIOEventType type, TransportationMode transportationMode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.timeStamp = timeStamp;
        this.type = type;
        this.transportationMode = transportationMode;
    }
}