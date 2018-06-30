package com.allgames.sportslab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.allgames.sportslab.R;
import com.allgames.sportslab.util.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * @author Ripon
 */

public class PlayerBasicInfoFragment extends RoboFragment {

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

    @InjectView(R.id.tv_birth_place)
    private TextView birthPlace;

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

            response = response.getJSONObject("info");
            if (response.has("name")) playerName.setText(response.getString("name"));
            else playerName.setText("");


            if (response.has("DoB")) born.setText(response.getString("DoB"));
            else born.setText("");

            if (response.has("birth_place")) birthPlace.setText(response.getString("birth_place"));
            else birthPlace.setText("");

            if (response.has("teams")) majorTeams.setText(response.getJSONArray("teams").toString());
            else majorTeams.setText("");

            if (response.has("role")) playingRole.setText(response.getString("role"));
            else playingRole.setText("");

            if (response.has("style"))
                battingStyle.setText(response.getJSONObject("style").getString("bat"));
            else battingStyle.setText("");

            if (response.has("style"))
                bowlingStyle.setText(response.getJSONObject("style").getString("bowl"));
            else bowlingStyle.setText("");

            if (response.has("name")) fullName.setText(response.getString("name"));
            else fullName.setText("");

            if (response.has("nationality")) playerCountry.setText(response.getString("nationality"));
            else playerCountry.setText("");

            if (response.has("image")) {
                Picasso.with(getActivity())
                        .load("http://i.cricketcb.com/stats/img/"+response.getString("image"))
                        .placeholder(R.drawable.default_image)
                        .into(playerImage);
            }

            response = new JSONObject(data);
            if (response.has("bio")) profile.setText(Html.fromHtml(response.getString("bio")));
            else profile.setText("");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
