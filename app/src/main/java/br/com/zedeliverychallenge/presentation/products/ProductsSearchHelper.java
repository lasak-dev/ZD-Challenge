package br.com.zedeliverychallenge.presentation.products;

import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.zedeliverychallenge.domain.model.Product;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

class ProductsSearchHelper {

    private final List<Product> products;
    private final PublishSubject<String> searchSubject;

    public static Observable<List<Product>> from(List<Product> products, AppCompatEditText inputSearch) {
        return new ProductsSearchHelper(products, inputSearch).observable();
    }

    private ProductsSearchHelper(List<Product> products, AppCompatEditText inputSearch) {
        this.products = products;
        searchSubject = PublishSubject.create();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Observable<List<Product>> observable() {
        return searchSubject.debounce(300, TimeUnit.MILLISECONDS)
                .switchMap((Function<String, ObservableSource<List<Product>>>)
                        query -> Observable.fromIterable(products)
                                .filter(product -> product.getName().toLowerCase().contains(query.toLowerCase()))
                                .toList()
                                .toObservable());
    }
}
