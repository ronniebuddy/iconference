package ae.businessdna.iconference.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ae.businessdna.iconference.R;

/**
 * Project : iConference
 * Created by Rohith on 8/21/2017.
 */

public class MeetingInfoAdapter extends RecyclerView.Adapter<MeetingInfoAdapter.MyViewHolder> {
//    private static final String TAG = "MeetingInfoAdapter";
//    private Context context;
    private List<String> attendeesList = new ArrayList<>();
    private LayoutInflater inflater;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvAttendeeName;

        MyViewHolder(View view) {
            super(view);
            mTvAttendeeName = (TextView) view.findViewById(R.id.tv_attendees_name);
        }
    }

    public MeetingInfoAdapter(Context context, List<String> passedAttendeesList) {
//        this.context = context;
        inflater = LayoutInflater.from(context);
        this.attendeesList = passedAttendeesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row_text, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final String attendeeName = attendeesList.get(holder.getAdapterPosition());
        holder.mTvAttendeeName.setText(attendeeName);
    }

    @Override
    public int getItemCount() {
        return attendeesList == null ? 0 : attendeesList.size();
    }
}
