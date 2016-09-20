package com.sfuronlabs.bpl2016.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.activity.ActivityMatchDetails;
import com.sfuronlabs.bpl2016.activity.FullCommentryActivity;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.util.ViewHolder;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class FragmentMatchSummary extends Fragment {
    private RecyclerView commentry;
    private TextView noCommentry;
    private Button fullCommentry;
    private String yahooID = "0";

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_summary, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.commentry = (RecyclerView) view.findViewById(R.id.commentry_list);
        this.noCommentry = (TextView) view.findViewById(R.id.no_commentry);
        this.fullCommentry = (Button) view.findViewById(R.id.btn_full_commentry);

        fullCommentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FullCommentryActivity.class);
                intent.putExtra("numberofinnings",((ActivityMatchDetails)getActivity()).numberOfInnings);
                intent.putExtra("id",yahooID);
                startActivity(intent);
            }
        });


    }

    public void setCommentry(final ArrayList<String> commentries) {
        if (isAdded()) {
            commentry.setAdapter(new BasicListAdapter<String, CommentryViewHolder>(commentries) {
                @Override
                public CommentryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecommentary, parent, false);
                    return new CommentryViewHolder(view);
                }

                @Override
                public void onBindViewHolder(CommentryViewHolder holder, int position) {
                    holder.item.setText(Html.fromHtml(commentries.get(position)));
                }
            });
            commentry.setLayoutManager(new LinearLayoutManager(getContext()));
            noCommentry.setVisibility(View.GONE);
            fullCommentry.setVisibility(View.VISIBLE);

        }
    }

    public void setNoCommentry() {
        noCommentry.setVisibility(View.VISIBLE);
        fullCommentry.setVisibility(View.GONE);
    }

    public void setMatchID (String id) {
        this.yahooID = id;
    }

    private static class CommentryViewHolder extends RecyclerView.ViewHolder {

        protected TextView item;

        public CommentryViewHolder(View itemView) {
            super(itemView);
            item = ViewHolder.get(itemView, R.id.live_commentary);
        }
    }
}