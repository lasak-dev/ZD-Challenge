package br.com.zedeliverychallenge.domain.interactor;

import java.util.List;

import br.com.zedeliverychallenge.data.repository.ProductsRepository;
import br.com.zedeliverychallenge.domain.model.Product;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProductsInteractor {

    private final ProductsRepository productsRepository;

    public ProductsInteractor(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    public Disposable getProducts(String pocId, Consumer<List<Product>> onSuccess,
                                  Consumer<Throwable> onError) {
        return productsRepository.products(pocId, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
    }
}
