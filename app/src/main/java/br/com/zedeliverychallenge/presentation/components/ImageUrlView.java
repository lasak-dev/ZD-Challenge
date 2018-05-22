package br.com.zedeliverychallenge.presentation.components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import br.com.zedeliverychallenge.R;

public class ImageUrlView extends AppCompatImageView {

    public ImageUrlView(Context context) {
        this(context, null);
    }

    public ImageUrlView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void load(String url) {
        load(url, R.drawable.ic_image_load, R.drawable.ic_error);
    }

    public void load(String url, @DrawableRes int placeHolder, @DrawableRes int error) {
        Drawable placeHolderDrawable = null;
        Drawable errorDrawable = null;
        if (placeHolder != 0) {
            placeHolderDrawable = AppCompatResources.getDrawable(getContext(), placeHolder);
        }
        if (error != 0) {
            errorDrawable = AppCompatResources.getDrawable(getContext(), error);
        }
        RequestOptions options = new RequestOptions()
                .placeholder(placeHolderDrawable)
                .error(errorDrawable)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(getContext())
                .load(url)
                .apply(options)
                .into(this);
    }
}
