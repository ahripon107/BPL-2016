package com.allgames.sportslab.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.allgames.sportslab.R;
import com.allgames.sportslab.activity.InsertOpinionActivity;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.FetchFromWeb;
import com.allgames.sportslab.util.ViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * @author Ripon
 */

public class OpinionFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> data, ids;
    Typeface typeface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        data = new ArrayList<>();
        ids = new ArrayList<>();

        typeface = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);
        recyclerView.setAdapter(new BasicListAdapter<String, OpinionViewHolder>(data) {
            @Override
            public OpinionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_opinion_ques, parent, false);
                return new OpinionViewHolder(view1);
            }

            @Override
            public void onBindViewHolder(OpinionViewHolder holder, final int position) {
                holder.ques.setTypeface(typeface);
                holder.ques.setText(data.get(position));
                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), InsertOpinionActivity.class);
                        intent.putExtra("opinionid", ids.get(position));
                        intent.putExtra("question", data.get(position));
                        getActivity().startActivity(intent);
                    }
                });
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        String url = Constants.OPINION_QUES_URL;
        Log.d(Constants.TAG, url);
        RequestParams params = new RequestParams();
        params.add("key", "bl905577");

        FetchFromWeb.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("content");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        data.add(obj.getString("question"));
                        ids.add(obj.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private static class OpinionViewHolder extends RecyclerView.ViewHolder {
        protected TextView ques;
        protected LinearLayout linearLayout;

        public OpinionViewHolder(View itemView) {
            super(itemView);
            ques = ViewHolder.get(itemView, R.id.tv_opinion_ques);
            linearLayout = ViewHolder.get(itemView, R.id.opinion_layout);
        }
    }
}
