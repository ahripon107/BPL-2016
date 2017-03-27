package com.tigersapp.bdcricket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.util.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class PlayerBasicInfoFragment extends RoboFragment{

    @InjectView(R.id.player_image)
    private CircleImageView playerImage;

    @InjectView(R.id.tv_player_name)
    private TextView playerName;

    @InjectView(R.id.tv_player_country)
    private TextView playerCountry;

    @InjectView(R.id.tv_full_name)
    private TextView fullName;

    @InjectView(R.id.tv_born)
    private TextView born;

    @InjectView(R.id.tv_current_age)
    private TextView currentAge;

    @InjectView(R.id.tv_playing_role)
    private TextView playingRole;

    @InjectView(R.id.tv_batting_style)
    private TextView battingStyle;

    @InjectView(R.id.tv_bowling_style)
    private TextView bowlingStyle;

    @InjectView(R.id.tv_major_teams)
    private TextView majorTeams;

    @InjectView(R.id.tv_profile)
    private TextView profile;

    private String data;
    private JSONObject response;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_basic_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        data = getArguments().getString("data");

        try {
            response = new JSONObject(data);

            if (response.has("name"))  playerName.setText(response.getString("name"));
            else playerName.setText("");


            if (response.has("born")) born.setText(response.getString("born"));
            else born.setText("");

            if (response.has("currentAge"))currentAge.setText(response.getString("currentAge"));
            else currentAge.setText("");

            if (response.has("majorTeams")) majorTeams.setText(response.getString("majorTeams"));
            else majorTeams.setText("");

            if (response.has("playingRole")) playingRole.setText(response.getString("playingRole"));
            else playingRole.setText("");

            if (response.has("battingStyle")) battingStyle.setText(response.getString("battingStyle"));
            else battingStyle.setText("");

            if (response.has("bowlingStyle")) bowlingStyle.setText(response.getString("bowlingStyle"));
            else bowlingStyle.setText("");

            if (response.has("profile")) profile.setText(response.getString("profile"));
            else profile.setText("");

            if (response.has("fullName")) fullName.setText(response.getString("fullName"));
            else fullName.setText("");

            if (response.has("country")) playerCountry.setText(response.getString("country"));
            else playerCountry.setText("");

            if (response.has("imageURL")) {
                Picasso.with(getActivity())
                        .load(response.getString("imageURL"))
                        .placeholder(R.drawable.default_image)
                        .into(playerImage);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
