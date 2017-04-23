package com.banglacricket.bdcricketteam.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.inject.Inject;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.activity.FullCommentryActivity;
import com.banglacricket.bdcricketteam.adapter.BasicListAdapter;
import com.banglacricket.bdcricketteam.model.Commentry;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.DefaultMessageHandler;
import com.banglacricket.bdcricketteam.util.DividerItemDecoration;
import com.banglacricket.bdcricketteam.util.NetworkService;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FragmentMatchSummary extends RoboFragment {
    private RecyclerView commentryList;
    private TextView noCommentry;
    private Button fullCommentry;
    private String yahooID = "not set";

    @Inject
    private ArrayList<Commentry> commentry;
    private String liveMatchID;

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
        this.commentryList = (RecyclerView) view.findViewById(R.id.commentry_list);
        this.noCommentry = (TextView) view.findViewById(R.id.no_commentry);
        this.fullCommentry = (Button) view.findViewById(R.id.btn_full_commentry);
        liveMatchID = getArguments().getString("liveMatchID");

        Log.d(Constants.TAG, liveMatchID);

        fullCommentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FullCommentryActivity.class);
                intent.putExtra("numberofinnings", FragmentScoreBoard.numberOfInnings);
                intent.putExtra("id", yahooID);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                fetch();
            }
        });

        fetch();

        String idMatcherURL = "http://apisea.xyz/Cricket/apis/v1/fetchIDMatcher.php";
        Log.d(Constants.TAG, idMatcherURL);
    }

    private void fetch() {
        networkService.fetchMatchIdMatcher(liveMatchID, new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                String string = (String) msg.obj;
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject response = new JSONObject(string);

                    if (response.getString("msg").equals("Successful")) {
                        String yahooID = response.getJSONArray("content").getJSONObject(0).getString("yahooID");
                        setMatchID(yahooID);
                        loadCommentry(yahooID);
                    } else {
                        setNoCommentry();
                    }

                    Log.d(Constants.TAG, response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadCommentry(String yahooID) {

        networkService.loadCommentryFromYahoo(yahooID, new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                commentry.clear();

                String string1 = (String) msg.obj;

                try {
                    JSONObject response = new JSONObject(string1);

                    Object object = response.getJSONObject("query").getJSONObject("results").get("Over");
                    if (object instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) object;
                        for (int p = 0; p < jsonArray.length(); p++) {
                            object = jsonArray.getJSONObject(p).get("Ball");
                            if (object instanceof JSONArray) {
                                JSONArray jsonArray1 = (JSONArray) object;
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    JSONObject obj = jsonArray1.getJSONObject(i);
                                    if (obj.getString("type").equals("ball")) {
                                        String string = "";
                                        String ov = (Integer.parseInt(obj.getString("ov")) - 1) + "." + obj.getString("n") + " ";
                                        string += obj.getString("shc") + " - ";
                                        string += obj.getString("r") + " run; ";
                                        string += obj.getString("c");
                                        if (obj.has("dmsl")) {
                                            commentry.add(new Commentry("ball", "W", ov, string));
                                        } else {
                                            commentry.add(new Commentry("ball", obj.getString("r"), ov, string));
                                        }

                                    } else {
                                        commentry.add(new Commentry("nonball", "", "", obj.getString("c")));
                                    }
                                }
                            } else if (object instanceof JSONObject) {
                                JSONObject obj = (JSONObject) object;
                                if (obj.getString("type").equals("ball")) {
                                    String string = "";
                                    String ov = (Integer.parseInt(obj.getString("ov")) - 1) + "." + obj.getString("n") + " ";
                                    string += obj.getString("shc") + " - ";
                                    string += obj.getString("r") + " run; ";
                                    string += obj.getString("c");
                                    if (obj.has("dmsl")) {
                                        commentry.add(new Commentry("ball", "W", ov, string));
                                    } else {
                                        commentry.add(new Commentry("ball", obj.getString("r"), ov, string));
                                    }
                                } else {
                                    commentry.add(new Commentry("nonball", "", "", obj.getString("c")));
                                }
                            }
                            //commentry.add("-------------------------------------------");
                        }
                    } else if (object instanceof JSONObject) {
                        JSONObject objt = (JSONObject) object;
                        JSONArray jsonArray1 = objt.getJSONArray("Ball");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject obj = jsonArray1.getJSONObject(i);
                            if (obj.getString("type").equals("ball")) {
                                String string = "";
                                String ov = (Integer.parseInt(obj.getString("ov")) - 1) + "." + obj.getString("n") + " ";
                                string += obj.getString("shc") + " - ";
                                string += obj.getString("r") + " run; ";
                                string += obj.getString("c");
                                if (obj.has("dmsl")) {
                                    commentry.add(new Commentry("ball", "W", ov, string));
                                } else {
                                    commentry.add(new Commentry("ball", obj.getString("r"), ov, string));
                                }
                            } else {
                                commentry.add(new Commentry("nonball", "", "", obj.getString("c")));
                            }
                        }

                    }

                    setCommentry(commentry);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCommentry(final ArrayList<Commentry> commentries) {
        if (isAdded()) {
            commentryList.setAdapter(new BasicListAdapter<Commentry, CommentryViewHolder>(commentries) {
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
                            holder.event.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_green_dark));
                        } else if (commentry.getEvent().equals("W")) {
                            holder.event.setTextColor(ContextCompat.getColor(getContext(), R.color.cpb_red_dark));
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
            commentryList.setLayoutManager(new LinearLayoutManager(getContext()));
            commentryList.addItemDecoration(new DividerItemDecoration(getContext()));
            noCommentry.setVisibility(View.GONE);
            fullCommentry.setVisibility(View.VISIBLE);

        }
    }

    public void setNoCommentry() {
        noCommentry.setVisibility(View.VISIBLE);
        fullCommentry.setVisibility(View.GONE);
    }

    public void setMatchID(String id) {
        this.yahooID = id;
    }

    private static class CommentryViewHolder extends RecyclerView.ViewHolder {

        protected TextView item;
        protected LinearLayout linearLayout;
        protected TextView overno, event;

        public CommentryViewHolder(View itemView) {
            super(itemView);
            item = ViewHolder.get(itemView, R.id.live_commentary);
            this.linearLayout = (LinearLayout) itemView.findViewById(R.id.ball_layout);
            this.overno = (TextView) itemView.findViewById(R.id.tv_overno);
            this.event = (TextView) itemView.findViewById(R.id.tv_event);
        }
    }
}