package com.allgames.sportslab.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allgames.sportslab.R;
import com.allgames.sportslab.util.CircleImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ripon
 */

public class PlayerBasicInfoFragment extends Fragment {

    private CircleImageView playerImage;
    private TextView playerName;
    private TextView playerCountry;
    private TextView fullName;
    private TextView born;
    private TextView currentAge;
    private TextView playingRole;
    private TextView battingStyle;
    private TextView bowlingStyle;
    private TextView majorTeams;
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

        initialize(view);
        data = getArguments().getString("data");

        try {
            response = new JSONObject(data);

            if (response.has("name")) playerName.setText(response.getString("name"));
            else playerName.setText("");


            if (response.has("born")) born.setText(response.getString("born"));
            else born.setText("");

            if (response.has("currentAge")) currentAge.setText(response.getString("currentAge"));
            else currentAge.setText("");

            if (response.has("majorTeams")) majorTeams.setText(response.getString("majorTeams"));
            else majorTeams.setText("");

            if (response.has("playingRole")) playingRole.setText(response.getString("playingRole"));
            else playingRole.setText("");

            if (response.has("battingStyle"))
                battingStyle.setText(response.getString("battingStyle"));
            else battingStyle.setText("");

            if (response.has("bowlingStyle"))
                bowlingStyle.setText(response.getString("bowlingStyle"));
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

    private void initialize(View view) {
        playerImage = (CircleImageView) view.findViewById(R.id.player_image);
        playerName = (TextView) view.findViewById(R.id.tv_player_name);
        playerCountry = (TextView) view.findViewById(R.id.tv_player_country);
        fullName = (TextView) view.findViewById(R.id.tv_full_name);
        born = (TextView) view.findViewById(R.id.tv_born);
        currentAge = (TextView) view.findViewById(R.id.tv_current_age);
        playingRole = (TextView) view.findViewById(R.id.tv_playing_role);
        battingStyle = (TextView) view.findViewById(R.id.tv_batting_style);
        bowlingStyle = (TextView) view.findViewById(R.id.tv_bowling_style);
        majorTeams = (TextView) view.findViewById(R.id.tv_major_teams);
        profile = (TextView) view.findViewById(R.id.tv_profile);
    }
}
