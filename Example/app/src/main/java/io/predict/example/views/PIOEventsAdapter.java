package io.predict.example.views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.predict.example.R;
import io.predict.example.common.MainUtils;
import io.predict.example.models.PIOEvent;

public class PIOEventsAdapter  extends RecyclerView.Adapter<PIOEventsAdapter.PIOEventsViewHolder> {
    private List<PIOEvent> mList;

    public PIOEventsAdapter(List<PIOEvent> moviesList) {
        this.mList = moviesList;
    }

    @Override
    public PIOEventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pio_events_list_item, parent, false);

        return new PIOEventsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PIOEventsViewHolder holder, int position) {
        PIOEvent event = mList.get(position);
        try {
            holder.mTvLog.setText(event.type.getName(event));
            holder.mTvTime.setText(MainUtils.DATE_TIME_FORMAT.format(event.timeStamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class PIOEventsViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvLog, mTvTime;

        public PIOEventsViewHolder(View view) {
            super(view);
            mTvLog = (TextView) view.findViewById(R.id.tv_log);
            mTvTime = (TextView) view.findViewById(R.id.tv_time);
        }
    }
}