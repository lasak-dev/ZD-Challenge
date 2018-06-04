package br.com.zedeliverychallenge.presentation.products;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.zedeliverychallenge.domain.model.Product;

class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private List<Product> products;
    private ProductClickListener listener;

    ProductsAdapter(List<Product> products, @NonNull ProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    void update(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(new ProductCardView(parent.getContext()), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ProductClickListener listener;
        private Product product;

        ProductHolder(ProductCardView itemView, ProductClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        void bind(Product product) {
            this.product = product;
            ((ProductCardView) itemView).bind(product);
        }

        @Override
        public void onClick(View v) {
            listener.onProductClicked(product);
        }
    }

    interface ProductClickListener {
        void onProductClicked(Product product);
    }
}
