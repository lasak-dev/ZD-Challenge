package br.com.zedeliverychallenge.presentation.products;

import android.support.annotation.VisibleForTesting;

import java.util.List;

import br.com.zedeliverychallenge.domain.interactor.ProductsInteractor;
import br.com.zedeliverychallenge.domain.model.Product;
import io.reactivex.disposables.Disposable;

public class ProductsPresenter {

    @VisibleForTesting ProductsInteractor interactor;
    @VisibleForTesting Disposable disposable;
    @VisibleForTesting ProductsMvpView view;
    @VisibleForTesting String pocId;

    public ProductsPresenter(ProductsInteractor interactor) {
        this.interactor = interactor;
    }

    public void bind(ProductsMvpView view, String pocId) {
        this.view = view;
        this.pocId = pocId;
        loadProducts();
    }

    private void loadProducts() {
        view.showProgress();
        disposable = interactor.getProducts(pocId, this::onReceiveProducts,
                throwable -> this.onError());
    }

    void onReceiveProducts(List<Product> products) {
        view.hideProgress();
        view.showProducts(products);
        view.showSearch();
    }

    void onError() {
        view.hideProgress();
        view.showError();
    }

    public void onRetryClicked() {
        loadProducts();
    }

    public void onSearchProducts(List<Product> products) {
        if (products.isEmpty()) {
            view.hideProducts();
            view.showEmpty();
        } else {
            view.hideEmpty();
            view.showProducts(products);
        }
    }

    public void onDestroy() {
        disposable.dispose();
    }
}
