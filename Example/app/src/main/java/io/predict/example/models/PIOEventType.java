package io.predict.example.models;

public enum PIOEventType {
    DEPARTED,
    DEPARTURE_CANCELED,
    TRANSPORT_MODE,
    ARRIVAL_SUSPECTED,
    ARRIVED,
    SEARCHING,
    BEING_STATIONARY_AFTER_ARRIVAL,
    TRAVELED_BY_AIRPLANE;

    public String getName(PIOEvent event) {
        switch (this) {
            default:
                return "Unknown";
            case DEPARTED:
                return "Departed";
            case DEPARTURE_CANCELED:
                return "Departure cancel";
            case TRANSPORT_MODE:
                return "Transportation Mode: " + event.transportationMode.name();
            case ARRIVAL_SUSPECTED:
                return "Arrival suspected";
            case ARRIVED:
                return "Arrived";
            case SEARCHING:
                return "Searching";
            case BEING_STATIONARY_AFTER_ARRIVAL:
                return event.stationaryAfterArrival ? "Stationary: Yes" : "Stationary: No";
            case TRAVELED_BY_AIRPLANE:
                return "Traveled by airplane";
        }
    }
}