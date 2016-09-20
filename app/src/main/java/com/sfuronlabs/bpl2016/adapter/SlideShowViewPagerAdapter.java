package com.sfuronlabs.bpl2016.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sfuronlabs.bpl2016.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Ripon
 */
public class SlideShowViewPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> imageUrls,texts;

    public SlideShowViewPagerAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> texts) {
        this.imageUrls = imageUrls;
        this.texts = texts;
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.list_item_image_slide_show, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgSlideShow);
        TextView tvText = (TextView) itemView.findViewById(R.id.tvText);
        Picasso.with(mContext)
                .load(imageUrls.get(position))
                .placeholder(R.drawable.default_image)
                .into(imageView);
        container.addView(itemView);
        tvText.setText(texts.get(position));
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}