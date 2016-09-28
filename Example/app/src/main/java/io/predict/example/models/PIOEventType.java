package io.predict.example.models;

public enum PIOEventType {
    DEPARTED,
    DEPARTURE_CANCELED,
    TRANSPORT_MODE,
    ARRIVAL_SUSPECTED,
    ARRIVED,
    SEARCHING;

    public String getName() {
        switch (this) {
            default:
                return "Unknown";
            case DEPARTED:
                return "Departed";
            case DEPARTURE_CANCELED:
                return "Departure Cancel";
            case TRANSPORT_MODE:
                return "Transport: ";
            case ARRIVAL_SUSPECTED:
                return "Arrival Suspected";
            case ARRIVED:
                return "Arrived";
            case SEARCHING:
                return "Searching";
        }
    }
}