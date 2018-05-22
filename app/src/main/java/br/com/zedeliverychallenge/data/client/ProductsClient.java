package br.com.zedeliverychallenge.data.client;

import com.apollographql.apollo.ApolloQueryCall;

import br.com.zedeliverychallenge.ProductsQuery;

public class ProductsClient extends BaseClient {

    public ApolloQueryCall<ProductsQuery.Data> getProducts(String pocId, long categoryId) {
        return apolloClient.query(ProductsQuery.builder()
                .id(pocId)
                .categoryId(categoryId)
                .search("")
                .build());
    }
}
