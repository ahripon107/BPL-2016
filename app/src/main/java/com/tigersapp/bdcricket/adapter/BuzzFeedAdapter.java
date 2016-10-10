package com.tigersapp.bdcricket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.model.BuzzFeed;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ripon
 */

public class BuzzFeedAdapter extends BaseAdapter {

    private Context context;

    private LayoutInflater layoutInflater;

    private List<BuzzFeed> buzzFeedList;

    public BuzzFeedAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return buzzFeedList == null ? 0 : buzzFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return buzzFeedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.buzz_feed_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        BuzzFeed buzzFeed = (BuzzFeed) getItem(position);
        viewHolder.labelMessageSender.setText(buzzFeed.getSender());
        viewHolder.labelMessageTime.setText(buzzFeed.getTime());
        viewHolder.labelMessageText.setText(buzzFeed.getFeed());

        return convertView;
    }

    public void setBuzzFeedList(List<BuzzFeed> buzzFeedList) {
        this.buzzFeedList = buzzFeedList;
    }

    public void addBuzzFeed(BuzzFeed buzzFeed) {
        if(buzzFeedList == null) buzzFeedList = new ArrayList<>();
        buzzFeedList.add(buzzFeed);
        notifyDataSetChanged();
    }

    public void clearAll() {
        if(buzzFeedList != null) buzzFeedList.clear();
    }

    public class ViewHolder {
        TextView labelMessageSender;
        TextView labelMessageTime;
        TextView labelMessageText;

        public ViewHolder(View view) {
            labelMessageSender = (TextView) view.findViewById(R.id.labelMessageSender);
            labelMessageTime = (TextView) view.findViewById(R.id.labelMessageTime);
            labelMessageText = (TextView) view.findViewById(R.id.labelMessageText);
        }
    }
}
