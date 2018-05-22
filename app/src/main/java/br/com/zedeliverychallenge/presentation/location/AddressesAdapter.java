package br.com.zedeliverychallenge.presentation.location;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.zedeliverychallenge.R;
import butterknife.BindView;
import butterknife.ButterKnife;

class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressHolder> {

    private LayoutInflater inflater;
    private List<Address> addresses;
    private AddressClickListener listener;

    AddressesAdapter(Context context, @Nullable List<Address> addresses, @NonNull AddressClickListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.addresses = addresses;
        this.listener = listener;
    }

    void update(@Nullable List<Address> addresses) {
        this.addresses = addresses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AddressHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AddressHolder(inflater.inflate(R.layout.item_address, parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressHolder holder, int position) {
        holder.bind(addresses.get(position));
    }

    @Override
    public int getItemCount() {
        return addresses == null ? 0 : addresses.size();
    }

    static class AddressHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text) AppCompatTextView textView;

        private AddressClickListener listener;
        private Address address;

        AddressHolder(View itemView, AddressClickListener listener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        void bind(Address address) {
            this.address = address;
            textView.setText(AddressFormat.full(address));
        }

        @Override
        public void onClick(View v) {
            listener.onAddressClicked(address);
        }
    }

    interface AddressClickListener {
        void onAddressClicked(Address address);
    }
}
