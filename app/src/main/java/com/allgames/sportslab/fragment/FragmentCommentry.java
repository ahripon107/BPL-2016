package com.allgames.sportslab.fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.Commentry;
import com.allgames.sportslab.model.match.Match;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FragmentCommentry extends RoboFragment {
    private RecyclerView commentryList;
    private TextView noCommentry;
    private int fileNo;

    @Inject
    private ArrayList<Commentry> commentry;
    private Match match;

    @Inject
    private NetworkService networkService;

    @InjectView(R.id.refresh)
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_summary, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.commentryList = view.findViewById(R.id.commentry_list);
        this.noCommentry = view.findViewById(R.id.no_commentry);
        match = (Match) getArguments().getSerializable("summary");

        commentryList.setLayoutManager(new LinearLayoutManager(getContext()));
        commentryList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        noCommentry.setVisibility(View.GONE);

        //Log.d(Constants.TAG, liveMatchID);
        setCommentry(commentry);
        fileNo = 1;
        loadCommentry(match.getMatchId(), fileNo);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                commentry.clear();
                swipeRefreshLayout.setRefreshing(true);
                fileNo = 1;
                loadCommentry(match.getMatchId(), fileNo);
            }
        });
    }


    private void loadCommentry(String matchID, final int fileno) {

        networkService.loadCommentry(matchID, fileno, new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                swipeRefreshLayout.setRefreshing(false);
                String string1 = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string1);


                    JSONArray jsonArray = response.getJSONArray("commlines");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject commentryObj = jsonArray.getJSONObject(i);
                        String type, event, over, details;
                        over = commentryObj.getString("ballno");
                        if (over.equals("")) {
                            type = "nonball";
                        } else {
                            type = "ball";
                        }
                        details = commentryObj.getString("commtxt");
                        switch (commentryObj.getString("event")) {
                            case "wicket":
                                event = "W";
                                break;
                            case "four":
                                event = "4";
                                break;
                            case "six":
                                event = "6";
                                break;
                            default:
                                event = "";
                                break;
                        }
                        commentry.add(new Commentry(type, event, over, details));
                    }

                    if (fileno == 1) {
                        setCommentry(commentry);
                    } else {
                        commentryList.getAdapter().notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCommentry(final ArrayList<Commentry> commentries) {
        if (isAdded()) {
            commentryList.setAdapter(new BasicListAdapter<Commentry, CommentryViewHolder>(commentries) {
                @Override
                public CommentryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_commentary, parent, false);
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
                            holder.event.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
                        } else if (commentry.getEvent().equals("W")) {
                            holder.event.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_red_dark));
                        }
                        holder.event.setText(commentry.getEvent());
                        //holder.event.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.round));
                    }

                    if (position == commentries.size()-1) {
                        fileNo++;
                        loadCommentry(match.getMatchId(), fileNo);
                    }
                }

                @Override
                public int getItemViewType(int position) {
                    return position;
                }
            });

        }
    }

    private void setNoCommentry() {
        noCommentry.setVisibility(View.VISIBLE);
    }

    private static class CommentryViewHolder extends RecyclerView.ViewHolder {

        TextView item;
        LinearLayout linearLayout;
        TextView overno;
        TextView event;

        CommentryViewHolder(View itemView) {
            super(itemView);
            this.item = itemView.findViewById(R.id.live_commentary);
            this.linearLayout = itemView.findViewById(R.id.ball_layout);
            this.overno = itemView.findViewById(R.id.tv_overno);
            this.event = itemView.findViewById(R.id.tv_event);
        }
    }
}