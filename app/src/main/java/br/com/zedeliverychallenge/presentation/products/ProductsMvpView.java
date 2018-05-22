package br.com.zedeliverychallenge.presentation.products;

import java.util.List;

import br.com.zedeliverychallenge.domain.model.Product;

public interface ProductsMvpView {
    void showProgress();

    void hideProgress();

    void showError();

    void showSearch();

    void showProducts(List<Product> products);

    void hideProducts();

    void showEmpty();

    void hideEmpty();
}
