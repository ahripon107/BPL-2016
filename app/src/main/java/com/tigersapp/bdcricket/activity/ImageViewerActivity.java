package com.tigersapp.bdcricket.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.inject.Inject;
import com.tigersapp.bdcricket.model.ImageHolder;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Ripon
 */

public class ImageViewerActivity extends AppCompatActivity {

    @Inject
    private ImageHolder imageHolder;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(createImageView());
    }

    private ImageView createImageView() {
        ImageView imageView = new ImageView(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;

        imageView.setLayoutParams(params);
        imageView.setImageBitmap(imageHolder.getImage());

        new PhotoViewAttacher(imageView);

        return imageView;
    }

    @Override
    public void finish() {
        imageHolder.setImage(null);
        super.finish();
    }
}
