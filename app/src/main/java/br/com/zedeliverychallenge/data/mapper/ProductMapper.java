package br.com.zedeliverychallenge.data.mapper;

import java.text.NumberFormat;

import br.com.zedeliverychallenge.ProductsQuery;
import br.com.zedeliverychallenge.domain.model.Product;

public class ProductMapper {

    private NumberFormat currencyFormat;

    public ProductMapper(NumberFormat currencyFormat) {
        this.currencyFormat = currencyFormat;
    }

    public Product parse(ProductsQuery.ProductVariant productVariant) {
        return new Product()
                .name(productVariant.title())
                .description(productVariant.description())
                .displayPrice(parsePrice(productVariant.price()))
                .imageUrl(productVariant.imageUrl());
    }

    private String parsePrice(Double price) {
        try {
            return currencyFormat.format(price);
        } catch (Exception e) {
            return null;
        }
    }
}
