package com.allgames.sportslab.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.activity.InsertOpinionActivity;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.util.Constants;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.google.inject.Inject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.fragment.RoboFragment;

/**
 * @author Ripon
 */

public class OpinionFragment extends RoboFragment {

    private RecyclerView recyclerView;
    private ArrayList<String> data;
    private ArrayList<String> ids;
    private Typeface typeface;

    @Inject
    private NetworkService networkService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recycler_view);
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

        networkService.fetch(Constants.OPINION_QUES_URL, new DefaultMessageHandler(getContext(), false) {
            @Override
            public void onSuccess(Message msg) {
                try {
                    JSONObject response = new JSONObject((String) msg.obj);
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
            }
        });
    }

    private static class OpinionViewHolder extends RecyclerView.ViewHolder {
        TextView ques;
        LinearLayout linearLayout;

        OpinionViewHolder(View itemView) {
            super(itemView);
            this.ques = itemView.findViewById(R.id.tv_opinion_ques);
            this.linearLayout = itemView.findViewById(R.id.opinion_layout);
        }
    }
}
