package com.tigersapp.bdcricket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.ActivityMatchDetails;
import com.tigersapp.bdcricket.activity.FullCommentryActivity;
import com.tigersapp.bdcricket.adapter.BasicListAdapter;
import com.tigersapp.bdcricket.model.Commentry;
import com.tigersapp.bdcricket.util.DividerItemDecoration;
import com.tigersapp.bdcricket.util.ViewHolder;

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

    public void setCommentry(final ArrayList<Commentry> commentries) {
        if (isAdded()) {
            commentry.setAdapter(new BasicListAdapter<Commentry, CommentryViewHolder>(commentries) {
                @Override
                public CommentryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlecommentary, parent, false);
                    return new CommentryViewHolder(view);
                }

                @Override
                public void onBindViewHolder(CommentryViewHolder holder, int position) {
                    Commentry commentry = commentries.get(position);
                    holder.item.setText(Html.fromHtml(commentry.getCommentr()));
                    if (commentry.getType().equals("nonball")) {
                        holder.linearLayout.setVisibility(View.GONE);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        params.setMargins(15, 22, 2, 22);
                        holder.item.setLayoutParams(params);
                    } else {
                        holder.overno.setText(commentry.getOver());
                        if (commentry.getEvent().equals("4") || commentry.getEvent().equals("6")) {
                            holder.event.setTextColor(ContextCompat.getColor(getContext(),R.color.cpb_green_dark));
                        } else if (commentry.getEvent().equals("W")) {
                            holder.event.setTextColor(ContextCompat.getColor(getContext(),R.color.cpb_red_dark));
                        }
                        holder.event.setText(commentry.getEvent());
                        //holder.event.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.round));
                    }
                }

                @Override
                public int getItemViewType(int position) {
                    return position;
                }
            });
            commentry.setLayoutManager(new LinearLayoutManager(getContext()));
            commentry.addItemDecoration(new DividerItemDecoration(getContext()));
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
        protected LinearLayout linearLayout;
        protected TextView overno,event;

        public CommentryViewHolder(View itemView) {
            super(itemView);
            item = ViewHolder.get(itemView, R.id.live_commentary);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.ball_layout);
            this.overno = (TextView) itemView.findViewById(R.id.tv_overno);
            this.event = (TextView) itemView.findViewById(R.id.tv_event);
        }
    }
}