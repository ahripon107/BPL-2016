package com.tigersapp.bdcricket.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;
import com.tigersapp.bdcricket.R;
import com.tigersapp.bdcricket.model.CricketNews;
import com.tigersapp.bdcricket.util.Constants;
import com.tigersapp.bdcricket.util.Dialogs;
import com.tigersapp.bdcricket.util.FetchFromWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.tigersapp.bdcricket.activity.NewsDetailsActivity.EXTRA_NEWS_OBJECT;

/**
 * @author Ripon
 */

public class NewsDetailsFragment extends Fragment {

    Button detailsNews;
    ImageView imageView;
    TextView headline;
    TextView date;
    TextView author;
    TextView details;
    CricketNews cricketNews;

    Typeface typeface;
    Dialogs dialogs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailsNews = (Button) view.findViewById(R.id.btn_details_news);
        imageView = (ImageView) view.findViewById(R.id.image);
        headline = (TextView) view.findViewById(R.id.text_view_headline);
        date = (TextView) view.findViewById(R.id.text_view_date);
        author = (TextView) view.findViewById(R.id.text_view_author);
        details = (TextView) view.findViewById(R.id.text_view_details);

        dialogs = new Dialogs(getContext());

        cricketNews = (CricketNews) getActivity().getIntent().getSerializableExtra(EXTRA_NEWS_OBJECT);

        typeface = Typeface.createFromAsset(getActivity().getAssets(), Constants.SOLAIMAN_LIPI_FONT);

        headline.setTypeface(typeface);
        author.setTypeface(typeface);
        details.setTypeface(typeface);
        headline.setText(cricketNews.getTitle());

        date.setText(cricketNews.getDate());
        author.setText(cricketNews.getSourceBangla());
        details.setText(cricketNews.getTitle());

        Picasso.with(getContext())
                .load(cricketNews.getImageUrl())
                .placeholder(R.drawable.default_image)
                .into(imageView);
        detailsNews.setVisibility(View.GONE);

        dialogs.showDialog();
        FetchFromWeb.get(cricketNews.getDetailNewsUrl(), null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                dialogs.dismissDialog();
                try {

                    JSONObject jsonObject = response.getJSONObject(0);
                    if (jsonObject.has("ContentDetails")) {
                        details.setText(Html.fromHtml(jsonObject.getString("ContentDetails")));
                    } else {
                        details.setText(Html.fromHtml(jsonObject.getString("NewsDetails")));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(Constants.TAG, response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                dialogs.dismissDialog();
                try {
                    if (response.has("getnewsby_id")) {
                        JSONObject jsonObject = response.getJSONObject("getnewsby_id");
                        details.setText(Html.fromHtml(jsonObject.getString("summery")));
                    } else if (response.has("contents")) {
                        response = response.getJSONArray("contents").getJSONObject(0);
                        details.setText(Html.fromHtml(response.getString("news_details")));
                    } else {
                        details.setText(Html.fromHtml(response.getString("ContentDetails")));
                    }


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

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                dialogs.dismissDialog();
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
