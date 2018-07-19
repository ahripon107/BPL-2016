package com.allgames.sportslab.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.google.inject.Inject;
import com.allgames.sportslab.R;
import com.allgames.sportslab.adapter.BasicListAdapter;
import com.allgames.sportslab.model.CricketNews;
import com.allgames.sportslab.util.DefaultMessageHandler;
import com.allgames.sportslab.util.NetworkService;
import com.allgames.sportslab.util.RecyclerItemClickListener;
import com.allgames.sportslab.util.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

/**
 * @author ripon
 */
@ContentView(R.layout.activity_news)
public class CricketNewsListActivity extends CommonActivity {

    @InjectView(R.id.recycler_view)
    private RecyclerView recyclerView;

    @InjectView(R.id.adViewNews)
    private AdView adView;

    @Inject
    private ArrayList<CricketNews> cricketNewses;

    @Inject
    private NetworkService networkService;

    private int pageNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pageNo = 1;

        recyclerView.setAdapter(new BasicListAdapter<CricketNews, NewsViewHolder>(cricketNewses) {
            @Override
            public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_news, parent, false);
                return new NewsViewHolder(view);
            }

            @Override
            public void onBindViewHolder(NewsViewHolder holder, final int position) {

                Picasso.with(CricketNewsListActivity.this)
                        .load(cricketNewses.get(position).getImageUrl())
                        .placeholder(R.drawable.default_image)
                        .into(holder.imageView);

                holder.about.setText(cricketNewses.get(position).getAbout());
                holder.title.setText(cricketNewses.get(position).getTitle());

                if (position == cricketNewses.size()-1) {
                    pageNo++;
                    loadNews(pageNo);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(CricketNewsListActivity.this, NewsDetailsActivity.class);
                        intent.putExtra(NewsDetailsActivity.EXTRA_NEWS_OBJECT, cricketNewses.get(position));
                        CricketNewsListActivity.this.startActivity(intent);
                    }
                })
        );

        loadNews(pageNo);

    }

    private void loadNews(int pageNo) {
        networkService.fetchNews(pageNo, new DefaultMessageHandler(this, true) {
            @Override
            public void onSuccess(Message msg) {
                String result = (String) msg.obj;

                try {
                    JSONArray response = new JSONArray(result);
                    for (int i=0;i<response.length();i++) {
                        JSONObject singlenews = response.getJSONObject(i);
                        String id = singlenews.getString("object_id");
                        String imageUrl = "http://p.imgci.com";
                        if (singlenews.getJSONObject("i").has("p")) {
                            imageUrl += singlenews.getJSONObject("i").getString("p");
                        }
                        String title = singlenews.getString("h");
                        String about = singlenews.getString("s");

                        CricketNews cricketNews = new CricketNews(id, imageUrl, title, about);
                        cricketNewses.add(cricketNews);
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView about;
        TextView title;
        ImageView imageView;

        NewsViewHolder(View itemView) {
            super(itemView);

            about = ViewHolder.get(itemView, R.id.tv_about);
            title = ViewHolder.get(itemView, R.id.tv_headline);
            imageView = ViewHolder.get(itemView, R.id.civ_news_thumb);
        }
    }
}
