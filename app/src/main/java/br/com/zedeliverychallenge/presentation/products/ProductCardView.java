package br.com.zedeliverychallenge.presentation.products;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import br.com.zedeliverychallenge.R;
import br.com.zedeliverychallenge.presentation.components.ImageUrlView;
import br.com.zedeliverychallenge.domain.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductCardView extends FrameLayout {

    @BindView(R.id.image) ImageUrlView image;
    @BindView(R.id.text_name) AppCompatTextView textName;
    @BindView(R.id.text_price) AppCompatTextView textPrice;

    public ProductCardView(@NonNull Context context) {
        this(context, null);
    }

    public ProductCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.item_product, this);
        ButterKnife.bind(this);
    }

    public void bind(Product product) {
        image.load(product.getImageUrl());
        textName.setText(product.getName());
        textPrice.setText(product.getDisplayPrice());
    }
}
