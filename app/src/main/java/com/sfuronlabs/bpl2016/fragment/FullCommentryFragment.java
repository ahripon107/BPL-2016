package com.sfuronlabs.bpl2016.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sfuronlabs.bpl2016.R;
import com.sfuronlabs.bpl2016.adapter.BasicListAdapter;
import com.sfuronlabs.bpl2016.util.Constants;
import com.sfuronlabs.bpl2016.util.FetchFromWeb;
import com.sfuronlabs.bpl2016.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import dmax.dialog.SpotsDialog;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */
public class FullCommentryFragment extends RoboFragment {

    @InjectView(R.id.recycler_view)
    RecyclerView recyclerView;

    ArrayList<String> commentries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentries = new ArrayList<>();
        recyclerView.setAdapter(new BasicListAdapter<String, CommentryViewHolder>(commentries) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchCommentries();
    }


    public void fetchCommentries() {

        String url = getArguments().getString("url");
        final AlertDialog progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.show();
        progressDialog.setCancelable(true);
        FetchFromWeb.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
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
                                        string += obj.getString("ov") + "." + obj.getString("n") + " ";
                                        string += obj.getString("shc") + " - ";
                                        string += obj.getString("r") + " run; ";
                                        string += obj.getString("c");
                                        commentries.add(string);
                                    } else {
                                        commentries.add(obj.getString("c"));
                                    }
                                }
                            } else if (object instanceof JSONObject) {
                                JSONObject obj = (JSONObject) object;
                                if (obj.getString("type").equals("ball")) {
                                    String string = "";
                                    string += obj.getString("ov") + "." + obj.getString("n") + " ";
                                    string += obj.getString("shc") + " - ";
                                    string += obj.getString("r") + " run; ";
                                    string += obj.getString("c");
                                    commentries.add(string);
                                } else {
                                    commentries.add(obj.getString("c"));
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
                                string += obj.getString("ov") + "." + obj.getString("n") + " ";
                                string += obj.getString("shc") + " - ";
                                string += obj.getString("r") + " run; ";
                                string += obj.getString("c");
                                commentries.add(string);
                            } else {
                                commentries.add(obj.getString("c"));
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
                progressDialog.dismiss();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });


    }

    private static class CommentryViewHolder extends RecyclerView.ViewHolder {

        protected TextView item;

        public CommentryViewHolder(View itemView) {
            super(itemView);
            item = ViewHolder.get(itemView, R.id.live_commentary);
        }
    }
}
