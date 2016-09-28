package io.predict.example.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import io.predict.example.models.PIOEvent;

public class PIOPreferences {
    private static final String PREFERENCES_NAME = "io.predict.example.common.PIOPreferences";
    private static final String LISTENER_EVENTS = "LISTENER_EVENTS";
    private static final String BROADCAST_EVENTS = "BROADCAST_EVENTS";
    private static PIOPreferences mInstance;
    private SharedPreferences prefs;

    public synchronized static PIOPreferences getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PIOPreferences(context);
        }
        return mInstance;
    }

    private PIOPreferences(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void clearPreferences() {
        prefs.edit().clear().apply();
    }

    private void saveString(String preferenceName, String data) {
        prefs.edit().putString(preferenceName, data).apply();
    }

    public String getString(String preferenceName) {
        return prefs.getString(preferenceName, "");
    }

    public ArrayList<PIOEvent> getListenerEvents() {
        String json = getString(LISTENER_EVENTS);
        Type listType = new TypeToken<ArrayList<PIOEvent>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public void persistListenerEvent(PIOEvent event) {
        ArrayList<PIOEvent> events = getListenerEvents();
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(0, event);
        saveString(LISTENER_EVENTS, new Gson().toJson(events));
    }

    public ArrayList<PIOEvent> getBroadcastEvents() {
        String json = getString(BROADCAST_EVENTS);
        Type listType = new TypeToken<ArrayList<PIOEvent>>(){}.getType();
        return new Gson().fromJson(json, listType);
    }

    public void persistBroadcastEvent(PIOEvent event) {
        ArrayList<PIOEvent> events = getBroadcastEvents();
        if (events == null) {
            events = new ArrayList<>();
        }
        events.add(0, event);
        saveString(BROADCAST_EVENTS, new Gson().toJson(events));
    }
}