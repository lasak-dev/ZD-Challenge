package br.com.zedeliverychallenge.presentation.products;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import br.com.zedeliverychallenge.R;
import br.com.zedeliverychallenge.domain.model.Product;
import br.com.zedeliverychallenge.presentation.detail.ProductDetailActivity;
import br.com.zedeliverychallenge.presentation.di.PresenterFactory;
import br.com.zedeliverychallenge.presentation.location.LocationActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import io.reactivex.schedulers.Schedulers;

public class ProductsActivity extends AppCompatActivity implements ProductsMvpView {

    private static final String PARAM_POC_ID = "pocId";
    private static final String PARAM_LOCATION = "location";

    public static Intent newIntent(Context context, String pocId, String location) {
        Intent intent = new Intent(context, ProductsActivity.class);
        intent.putExtra(PARAM_POC_ID, pocId);
        intent.putExtra(PARAM_LOCATION, location);
        return intent;
    }

    @BindView(R.id.content) ConstraintLayout content;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.text_location) AppCompatTextView textLocation;
    @BindView(R.id.input_search) AppCompatEditText inputSearch;
    @BindView(R.id.recycler) RecyclerView recyclerView;
    @BindView(R.id.text_empty) AppCompatTextView textEmpty;

    private ProductsPresenter presenter;
    private ProductsAdapter productsAdapter;
    private Disposable searchDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        textLocation.setText(getIntent().getStringExtra(PARAM_LOCATION));
        presenter = PresenterFactory.productsPresenter();
        presenter.bind(this, getIntent().getStringExtra(PARAM_POC_ID));
    }

    @Override
    protected void onDestroy() {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
        presenter.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.text_location)
    public void onLocationClicked() {
        startActivity(new Intent(this, LocationActivity.class));
        finish();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.location_progress_error)
                .setPositiveButton(R.string.general_retry, (dialog, which) -> presenter.onRetryClicked())
                .setNegativeButton(R.string.general_cancel, (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public void showSearch() {
        TransitionManager.beginDelayedTransition(content);
        inputSearch.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProducts(List<Product> products) {
        if (productsAdapter == null) {
            productsAdapter = new ProductsAdapter(products, this::showProductDetails);
            recyclerView.setAdapter(productsAdapter);
            searchDisposable = ProductsSearchHelper.from(products, inputSearch)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> presenter.onSearchProducts(result), Functions.emptyConsumer());
        } else {
            productsAdapter.update(products);
        }
        recyclerView.setVisibility(View.VISIBLE);
    }

    void showProductDetails(Product product) {
        startActivity(ProductDetailActivity.newIntent(this, product));
        overridePendingTransition(R.anim.anim_enter, R.anim.anim_empty);
    }

    @Override
    public void hideProducts() {
        recyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showEmpty() {
        textEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmpty() {
        textEmpty.setVisibility(View.GONE);
    }
}
