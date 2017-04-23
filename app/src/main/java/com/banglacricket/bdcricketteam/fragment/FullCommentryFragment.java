package com.banglacricket.bdcricketteam.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.banglacricket.bdcricketteam.R;
import com.banglacricket.bdcricketteam.adapter.BasicListAdapter;
import com.banglacricket.bdcricketteam.model.Commentry;
import com.banglacricket.bdcricketteam.util.Constants;
import com.banglacricket.bdcricketteam.util.Dialogs;
import com.banglacricket.bdcricketteam.util.DividerItemDecoration;
import com.banglacricket.bdcricketteam.util.FetchFromWeb;
import com.banglacricket.bdcricketteam.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FullCommentryFragment extends RoboFragment {

    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    @Inject
    private ArrayList<Commentry> commentries;

    Dialogs dialogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dialogs = new Dialogs(getContext());
        recyclerView.setAdapter(new BasicListAdapter<Commentry, CommentryViewHolder>(commentries) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        fetchCommentries();
    }


    public void fetchCommentries() {

        String url = getArguments().getString("url");
        dialogs.showDialog();
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                commentries.clear();

                try {
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
                                            commentries.add(new Commentry("ball", "W", ov, string));
                                        } else {
                                            commentries.add(new Commentry("ball", obj.getString("r"), ov, string));
                                        }
                                    } else {
                                        commentries.add(new Commentry("nonball", "", "", obj.getString("c")));
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
                                        commentries.add(new Commentry("ball", "W", ov, string));
                                    } else {
                                        commentries.add(new Commentry("ball", obj.getString("r"), ov, string));
                                    }
                                } else {
                                    commentries.add(new Commentry("nonball", "", "", obj.getString("c")));
                                }
                            }
                            //commentries.add("-------------------------------------------");
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
                                    commentries.add(new Commentry("ball", "W", ov, string));
                                } else {
                                    commentries.add(new Commentry("ball", obj.getString("r"), ov, string));
                                }
                            } else {
                                commentries.add(new Commentry("nonball", "", "", obj.getString("c")));
                            }
                        }
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(Constants.TAG, response.toString());
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                dialogs.dismissDialog();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


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
