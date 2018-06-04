package br.com.zedeliverychallenge.data.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.zedeliverychallenge.ProductsQuery;
import br.com.zedeliverychallenge.domain.model.Product;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProductMapperTest {

    @Mock ProductsQuery.ProductVariant productVariant;
    private ProductMapper productMapper;

    @Before
    public void setUp() {
        initMocks(this);
        productMapper = new ProductMapper(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")));
    }

    @Test
    public void parseProductVariant() {
        when(productVariant.title()).thenReturn("title");
        when(productVariant.description()).thenReturn("description");
        when(productVariant.price()).thenReturn(20d);
        when(productVariant.imageUrl()).thenReturn("image url");

        Product product = productMapper.parse(productVariant);

        assertEquals("title", product.getName());
        assertEquals("description", product.getDescription());
        assertEquals("R$ 20,00", product.getDisplayPrice());
        assertEquals("image url", product.getImageUrl());
    }
}