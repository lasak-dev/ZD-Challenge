package br.com.zedeliverychallenge.presentation.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.zedeliverychallenge.R;
import br.com.zedeliverychallenge.presentation.components.ImageUrlView;
import br.com.zedeliverychallenge.domain.model.Product;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDetailActivity extends AppCompatActivity {

    private static final String PARAM_PRODUCT = "product";

    public static Intent newIntent(Context context, Product product) {
        Intent intent = new Intent(context, ProductDetailActivity.class);
        intent.putExtra(PARAM_PRODUCT, product);
        return intent;
    }

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.image) ImageUrlView image;
    @BindView(R.id.text_name) AppCompatTextView textName;
    @BindView(R.id.text_price) AppCompatTextView textPrice;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.label_description) AppCompatTextView labelDescription;
    @BindView(R.id.text_description) AppCompatTextView textDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(v -> finish());
        bind((Product) getIntent().getSerializableExtra(PARAM_PRODUCT));
    }

    private void bind(Product product) {
        image.load(product.getImageUrl());
        textName.setText(product.getName());
        if (product.getDisplayPrice() == null) {
            textPrice.setVisibility(View.GONE);
        } else {
            textPrice.setText(product.getDisplayPrice());
        }
        if (product.getDescription() == null) {
            divider.setVisibility(View.GONE);
            labelDescription.setVisibility(View.GONE);
            textDescription.setVisibility(View.GONE);
        } else {
            textDescription.setText(product.getDescription());
        }
    }
}
