package com.allgames.sportslab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.PlayerListAdapter;
import com.allgames.sportslab.model.Player;
import com.allgames.sportslab.util.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class PlayersFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Player> pl;
    private PlayerListAdapter playerListAdapter;

    public PlayersFragment() {

    }

    public static PlayersFragment newInstanceOfPlayersFragment(String text) {
        PlayersFragment myFragment = new PlayersFragment();
        Bundle arguments = new Bundle();
        arguments.putString("details", text);
        myFragment.setArguments(arguments);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.playersfragment, container, false);
        recyclerView = v.findViewById(R.id.recycler_view);
        pl = new ArrayList<>();
        playerListAdapter = new PlayerListAdapter(getContext(), pl);
        recyclerView.setAdapter(playerListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            pl.clear();
            String string = getArguments().getString("details");
            try {
                JSONObject jsonObject = new JSONObject(string);

                jsonObject = jsonObject.getJSONObject("query");
                jsonObject = jsonObject.getJSONObject("results");
                jsonObject = jsonObject.getJSONObject("TeamProfile");
                jsonObject = jsonObject.getJSONObject("Players");
                JSONArray jsonArray = jsonObject.getJSONArray("Player");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String personid = obj.getString("personid");
                    String name = obj.getString("FirstName") + " " + obj.getString("LastName");
                    Player player = new Player(name, null, personid);
                    pl.add(player);

                }
                playerListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }
    }
}
