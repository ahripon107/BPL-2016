package com.sfuronlabs.bpl2016.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sfuronlabs.bpl2016.R;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class CommentaryAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> commentaries;
    LayoutInflater layoutInflater;

    public CommentaryAdapter(Context context, ArrayList<String> commentaries) {
        this.context = context;
        this.commentaries = commentaries;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return commentaries.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.singlecommentary, parent, false);
        }
        TextView c = (TextView) view.findViewById(R.id.live_commentary);
        c.setText(commentaries.get(position));
        return view;
    }
}
