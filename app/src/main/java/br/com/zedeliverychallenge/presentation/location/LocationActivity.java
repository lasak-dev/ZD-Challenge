package br.com.zedeliverychallenge.presentation.location;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.TransitionManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.com.zedeliverychallenge.R;
import br.com.zedeliverychallenge.presentation.components.KeyboardHelper;
import br.com.zedeliverychallenge.presentation.components.PermissionHelper;
import br.com.zedeliverychallenge.presentation.di.PresenterFactory;
import br.com.zedeliverychallenge.presentation.products.ProductsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LocationActivity extends AppCompatActivity implements LocationMvpView, OnMapReadyCallback {

    @BindView(R.id.content) View content;
    @BindView(R.id.text_title) AppCompatTextView textTitle;
    @BindView(R.id.text_map) AppCompatTextView textMap;
    @BindView(R.id.divider) View divider;
    @BindView(R.id.input_search) AppCompatEditText inputSearch;
    @BindView(R.id.progress_search) ProgressBar progressSearch;
    @BindView(R.id.recycler) RecyclerView recycler;
    @BindView(R.id.container_map) ViewGroup containerMap;
    @BindView(R.id.container_fragment) FrameLayout containerFragment;
    @BindView(R.id.progress_map) ProgressBar progressMap;
    @BindView(R.id.text_map_location) AppCompatTextView textMapLocation;
    @BindView(R.id.button_confirm_location) AppCompatButton buttonConfirmLocation;

    private LocationPresenter presenter;
    private CompositeDisposable compositeDisposable;
    private Geocoder geocoder;
    private AddressesAdapter addressesAdapter;
    private PermissionHelper permissionHelper;
    private GoogleMap googleMap;
    private Address mapAddress;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        geocoder = new Geocoder(this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        presenter = PresenterFactory.locationPresenter();
        presenter.bind(this);
        setupSearch();
    }

    private void setupSearch() {
        inputSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                KeyboardHelper.hideKeyboard(inputSearch);
            }
            return false;
        });
        compositeDisposable.add(LocationSearchHelper.from(geocoder, inputSearch, progressSearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceiveAddresses, throwable -> onReceiveAddresses(null)));
    }

    void onReceiveAddresses(@Nullable List<Address> addresses) {
        progressSearch.setVisibility(View.INVISIBLE);
        if (addressesAdapter == null) {
            addressesAdapter = new AddressesAdapter(this, addresses, presenter::onAddressSelected);
            recycler.setAdapter(addressesAdapter);
        } else {
            addressesAdapter.update(addresses);
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this,
                getString(R.string.location_progress_title),
                getString(R.string.location_progress_message));
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void openProducts(String pocId, String location) {
        startActivity(ProductsActivity.newIntent(this, pocId, location));
        finish();
    }

    @Override
    public void showFindPocError() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.location_progress_error)
                .setPositiveButton(R.string.general_accept, null)
                .show();
    }

    @OnClick(R.id.text_map)
    public void onTextMapClicked() {
        permissionHelper = new PermissionHelper(this, this::showMap);
        permissionHelper.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,
                getString(R.string.location_deny), getString(R.string.location_block));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.onRequestPermissionsResult(permissions, grantResults);
    }

    private void showMap() {
        TransitionManager.beginDelayedTransition(findViewById(android.R.id.content));
        KeyboardHelper.hideKeyboard(inputSearch);
        content.setVisibility(View.GONE);
        containerMap.setVisibility(View.VISIBLE);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng initialLocation = new LatLng(-23.632919, -46.699453);
        this.googleMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(this::onReceiveMapLocation);
    }

    void onReceiveMapLocation(Location location) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 15));
        compositeDisposable.add(LocationAddressHelper.from(geocoder, location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onReceiveMapAddress, throwable -> onMapAddressError()));
    }

    void onReceiveMapAddress(Address address) {
        mapAddress = address;
        textMapLocation.setText(AddressFormat.full(address));
        TransitionManager.beginDelayedTransition(containerMap);
        progressMap.setVisibility(View.GONE);
        buttonConfirmLocation.setEnabled(true);
    }

    void onMapAddressError() {
        textMapLocation.setText(R.string.location_address_error);
    }

    @OnClick(R.id.button_confirm_location)
    public void onConfirmLocationClicked() {
        presenter.onAddressSelected(mapAddress);
    }
}
