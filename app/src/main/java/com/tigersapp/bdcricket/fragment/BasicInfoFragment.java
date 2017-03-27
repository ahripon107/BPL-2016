package com.tigersapp.bdcricket.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.activity.TeamDetailsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Ripon
 */
public class BasicInfoFragment extends Fragment {

    public BasicInfoFragment() {
    }

    public static BasicInfoFragment newInstanceOfDescriptionFragment(String text) {
        BasicInfoFragment myFragment = new BasicInfoFragment();
        Bundle arguments = new Bundle();
        arguments.putString("details", text);
        myFragment.setArguments(arguments);
        return myFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.basicinfofragment, container, false);
        TextView testRank = (TextView) v.findViewById(R.id.tvTestRank);
        TextView odiRank = (TextView) v.findViewById(R.id.tvODIRank);
        TextView t20Rank = (TextView) v.findViewById(R.id.tvT20Rank);
        TextView testCaptain = (TextView) v.findViewById(R.id.tvTestCaptain);
        TextView odiCaptain = (TextView) v.findViewById(R.id.tvODICaptain);
        TextView t20Captain = (TextView) v.findViewById(R.id.tvT20Captain);
        TextView coach = (TextView) v.findViewById(R.id.tvCoach);
        TextView description = (TextView) v.findViewById(R.id.tvDescription);
        String string = getArguments().getString("details");


        try {
            JSONObject jsonObject = new JSONObject(string);

            jsonObject = jsonObject.getJSONObject("query");
            jsonObject = jsonObject.getJSONObject("results");
            jsonObject = jsonObject.getJSONObject("TeamProfile");

            String a = jsonObject.getString("TeamName");
            ((TeamDetailsActivity)getActivity()).getSupportActionBar().setTitle(a);

            JSONObject object;


            JSONArray jsonArray;
            if (jsonObject.has("Ranking")) {
                jsonArray = jsonObject.getJSONArray("Ranking");
                if (jsonArray.length() == 3) {
                    object = jsonArray.getJSONObject(0);
                    testRank.setText("Test: " + object.getString("content"));

                    object = jsonArray.getJSONObject(1);
                    odiRank.setText("ODI: " + object.getString("content"));

                    object = jsonArray.getJSONObject(2);
                    t20Rank.setText("T20I: " + object.getString("content"));
                } else if (jsonArray.length() == 2) {

                    testRank.setText("Test: N/A");

                    object = jsonArray.getJSONObject(0);
                    odiRank.setText("ODI: " + object.getString("content"));

                    object = jsonArray.getJSONObject(1);
                    t20Rank.setText("T20I: " + object.getString("content"));
                } else {
                    testRank.setText("Test: N/A");
                    odiRank.setText("ODI: N/A");
                    t20Rank.setText("T20I: N/A");
                }

            } else {
                testRank.setText("Test: N/A");
                odiRank.setText("ODI: N/A");
                t20Rank.setText("T20I: N/A");
            }


            String s;

            if (jsonObject.has("Captain")) {
                jsonArray = jsonObject.getJSONArray("Captain");
                if (jsonArray.length() == 3) {
                    object = jsonArray.getJSONObject(0);
                    s = "Test: " + object.getString("FirstName") + " " + object.getString("LastName");
                    testCaptain.setText(s);

                    object = jsonArray.getJSONObject(1);
                    s = "ODI: " + object.getString("FirstName") + " " + object.getString("LastName");
                    odiCaptain.setText(s);

                    object = jsonArray.getJSONObject(2);
                    s = "T20: " + object.getString("FirstName") + " " + object.getString("LastName");
                    t20Captain.setText(s);
                } else if (jsonArray.length() == 2) {

                    s = "Test: N/A";
                    testCaptain.setText(s);

                    object = jsonArray.getJSONObject(0);
                    s = "ODI: " + object.getString("FirstName") + " " + object.getString("LastName");
                    odiCaptain.setText(s);

                    object = jsonArray.getJSONObject(1);
                    s = "T20: " + object.getString("FirstName") + " " + object.getString("LastName");
                    t20Captain.setText(s);
                } else {
                    testCaptain.setText("Test: N/A");
                    odiCaptain.setText("ODI: N/A");
                    t20Captain.setText("T20I: N/A");
                }

            } else {
                testCaptain.setText("Test: N/A");
                odiCaptain.setText("ODI: N/A");
                t20Captain.setText("T20I: N/A");
            }


            object = jsonObject.getJSONObject("Coach");
            s = "Coach: " + object.getString("FirstName") + " " + object.getString("LastName");
            coach.setText(s);

            //object = jsonObject.getJSONObject("Castrol");
            //description.setText(jsonObject.getString("Description"));
            description.setText(Html.fromHtml(jsonObject.getString("Description")));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return v;
    }
}
