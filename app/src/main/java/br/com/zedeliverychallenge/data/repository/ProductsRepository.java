package br.com.zedeliverychallenge.data.repository;

import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import java.util.ArrayList;
import java.util.List;

import br.com.zedeliverychallenge.ProductsQuery;
import br.com.zedeliverychallenge.data.client.ProductsClient;
import br.com.zedeliverychallenge.data.mapper.ProductMapper;
import br.com.zedeliverychallenge.domain.model.Product;
import io.reactivex.Single;

public class ProductsRepository {

    private final ProductsClient productsClient;
    private final ProductMapper productMapper;

    public ProductsRepository(ProductsClient productsClient, ProductMapper productMapper) {
        this.productsClient = productsClient;
        this.productMapper = productMapper;
    }

    public Single<List<Product>> products(String pocId, long categoryId) {
        ApolloQueryCall<ProductsQuery.Data> query = productsClient.getProducts(pocId, categoryId);
        return Single.fromObservable(Rx2Apollo.from(query)
                .map(dataResponse -> {
                    if (hasProducts(dataResponse)) {
                        return dataResponse.data().poc().products();
                    }
                    return new ArrayList<ProductsQuery.Product>();
                }))
                .map(productsResult -> {
                    List<Product> products = new ArrayList<>();
                    for (ProductsQuery.Product product : productsResult) {
                        if (product.productVariants() != null && !product.productVariants().isEmpty()) {
                            products.add(productMapper.parse(product.productVariants().get(0)));
                        }
                    }
                    return products;
                });
    }

    boolean hasProducts(Response<ProductsQuery.Data> dataResponse) {
        return dataResponse.data() != null
                && dataResponse.data().poc() != null
                && dataResponse.data().poc().products() != null;
    }
}
