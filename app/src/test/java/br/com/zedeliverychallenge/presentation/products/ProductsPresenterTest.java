package br.com.zedeliverychallenge.presentation.products;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import br.com.zedeliverychallenge.domain.interactor.ProductsInteractor;
import br.com.zedeliverychallenge.domain.model.Product;
import io.reactivex.disposables.Disposable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductsPresenterTest {

    @Mock ProductsInteractor interactor;
    @Mock ProductsMvpView view;
    @Mock Disposable disposable;
    @Mock List<Product> products;

    private ProductsPresenter presenter;

    @Before
    public void setUp() {
        initMocks(this);
        presenter = new ProductsPresenter(interactor);
        presenter.view = view;
    }

    @Test
    public void onViewBindShouldLoadProductsWithPocId() {
        when(interactor.getProducts(any(), any(), any())).thenReturn(disposable);

        presenter.bind(view, "123");

        verify(view).showProgress();
        verify(interactor).getProducts(eq("123"), any(), any());
    }

    @Test
    public void onReceiveProductsShouldUpdateView() {
        presenter.onReceiveProducts(products);

        verify(view).hideProgress();
        verify(view).showProducts(products);
        verify(view).showSearch();
    }

    @Test
    public void onErrorShouldUpdateView() {
        presenter.onError();

        verify(view).hideProgress();
        verify(view).showError();
    }

    @Test
    public void onSearchProductsShouldUpdateView() {
        when(products.isEmpty()).thenReturn(false);

        presenter.onSearchProducts(products);

        verify(view).hideEmpty();
        verify(view).showProducts(products);
    }

    @Test
    public void onSearchProductsWithNoResultsShouldUpdateView() {
        when(products.isEmpty()).thenReturn(true);

        presenter.onSearchProducts(products);

        verify(view).hideProducts();
        verify(view).showEmpty();
    }

    @Test
    public void onViewDestroyShouldDisposeInteractorRequest() {
        presenter.disposable = disposable;

        presenter.onDestroy();

        verify(disposable).dispose();
    }
}