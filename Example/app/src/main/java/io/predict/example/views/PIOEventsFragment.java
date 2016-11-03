package io.predict.example.views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.predict.example.R;
import io.predict.example.common.PIOBroadcastUtils;
import io.predict.example.common.PIOPreferences;
import io.predict.example.models.PIOEvent;

public class PIOEventsFragment extends Fragment {
    private List<PIOEvent> mEventsList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private PIOEventsAdapter mEventsAdapter;
    private boolean isListenerEvents;
    public BroadcastReceiver mEventUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                boolean isListenerEvent = intent.getBooleanExtra(PIOBroadcastUtils.EXTRA_EVENT_UPDATE_IS_LISTENER, true);
                if(isListenerEvent == isListenerEvents) {
                    populateEventsData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public PIOEventsFragment() {
        this.isListenerEvents = false;
    }

    public void setListenerEvents(boolean listenerEvents) {
        isListenerEvents = listenerEvents;
    }

    public static PIOEventsFragment newInstance(boolean isListenerEvents) {
        PIOEventsFragment fragment = new PIOEventsFragment();
        fragment.setListenerEvents(isListenerEvents);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pio_events, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view == null) {
            return;
        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mEventsAdapter = new PIOEventsAdapter(mEventsList);

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mEventsAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), mRecyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                PIOEvent event = mEventsList.get(position);
                Intent intent = new Intent(getContext(), MapsActivity.class);
                intent.putExtra(MapsActivity.EXTRA_EVENT, new Gson().toJson(event));
                getActivity().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        populateEventsData();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            IntentFilter intentFilter = new IntentFilter(PIOBroadcastUtils.ACTION_EVENT_UPDATE);
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mEventUpdateReceiver, intentFilter);
            populateEventsData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mEventUpdateReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateEventsData() {
        if (isListenerEvents) {
            mEventsList.clear();
            ArrayList<PIOEvent> updatedEvents = PIOPreferences.getInstance(getContext()).getListenerEvents();
            if(updatedEvents != null) {
                mEventsList.addAll(updatedEvents);
            }
        } else {
            mEventsList.clear();
            ArrayList<PIOEvent> updatedEvents = PIOPreferences.getInstance(getContext()).getBroadcastEvents();
            if(updatedEvents != null) {
                mEventsList.addAll(updatedEvents);
            }
        }
        mEventsAdapter.notifyDataSetChanged();
    }
}