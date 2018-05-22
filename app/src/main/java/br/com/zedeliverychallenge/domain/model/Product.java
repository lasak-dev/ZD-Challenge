package br.com.zedeliverychallenge.domain.model;

import java.io.Serializable;

public class Product implements Serializable {
    private String name;
    private String imageUrl;
    private String description;
    private String displayPrice;

    public String getName() {
        return name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public String getDisplayPrice() {
        return displayPrice;
    }

    public Product displayPrice(String displayPrice) {
        this.displayPrice = displayPrice;
        return this;
    }
}
