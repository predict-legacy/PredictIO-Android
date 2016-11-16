package io.predict.example.models;

import io.predict.PIOZone;
import io.predict.TransportationMode;

public class PIOEvent {
    public double latitude;
    public double longitude;
    public double accuracy;
    public long timeStamp;
    public PIOEventType type;
    public TransportationMode transportationMode;
    public boolean stationaryAfterArrival;
    public PIOZone zone;

    public PIOEvent(double latitude, double longitude, double accuracy, long timeStamp, PIOEventType type,
                    TransportationMode transportationMode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.timeStamp = timeStamp;
        this.type = type;
        this.transportationMode = transportationMode;
        stationaryAfterArrival = false;
    }

    public PIOEvent(double latitude, double longitude, double accuracy, long timeStamp, PIOEventType type,
                    TransportationMode transportationMode, boolean stationaryAfterArrival) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.timeStamp = timeStamp;
        this.type = type;
        this.transportationMode = transportationMode;
        this.stationaryAfterArrival = stationaryAfterArrival;
    }

    public PIOEvent(double latitude, double longitude, double accuracy, long timeStamp, PIOEventType type,
                    TransportationMode transportationMode, boolean stationaryAfterArrival, PIOZone zone) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.timeStamp = timeStamp;
        this.type = type;
        this.transportationMode = transportationMode;
        this.stationaryAfterArrival = stationaryAfterArrival;
        this.zone = zone;
    }

    public String getStationaryEventDetails() {
        return stationaryAfterArrival ? "Stationary" : "Not stationary";
    }
}