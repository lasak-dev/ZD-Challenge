package br.com.zedeliverychallenge.presentation.di;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.zedeliverychallenge.data.client.PocClient;
import br.com.zedeliverychallenge.data.client.ProductsClient;
import br.com.zedeliverychallenge.data.repository.PocRepository;
import br.com.zedeliverychallenge.data.repository.ProductsRepository;
import br.com.zedeliverychallenge.domain.interactor.LocationInteractor;
import br.com.zedeliverychallenge.domain.interactor.ProductsInteractor;
import br.com.zedeliverychallenge.data.mapper.PocMapper;
import br.com.zedeliverychallenge.data.mapper.ProductMapper;
import br.com.zedeliverychallenge.presentation.location.LocationPresenter;
import br.com.zedeliverychallenge.presentation.products.ProductsPresenter;

public class PresenterFactory {

    public static LocationPresenter locationPresenter() {
        PocClient pocClient = new PocClient();
        PocMapper pocMapper = new PocMapper();
        PocRepository pocRepository = new PocRepository(pocClient, pocMapper);
        LocationInteractor interactor = new LocationInteractor(pocRepository);
        return new LocationPresenter(interactor);
    }

    public static ProductsPresenter productsPresenter() {
        ProductsClient productsClient = new ProductsClient();
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        ProductMapper productMapper = new ProductMapper(format);
        ProductsRepository productsRepository = new ProductsRepository(productsClient, productMapper);
        ProductsInteractor interactor = new ProductsInteractor(productsRepository);
        return new ProductsPresenter(interactor);
    }
}
