package br.com.zedeliverychallenge.presentation.location;

import android.location.Address;
import android.support.annotation.VisibleForTesting;

import br.com.zedeliverychallenge.domain.interactor.LocationInteractor;
import br.com.zedeliverychallenge.domain.model.Poc;
import io.reactivex.disposables.Disposable;

public class LocationPresenter {

    @VisibleForTesting LocationInteractor interactor;
    @VisibleForTesting LocationMvpView view;
    @VisibleForTesting Disposable disposable;

    public LocationPresenter(LocationInteractor interactor) {
        this.interactor = interactor;
    }

    public void bind(LocationMvpView view) {
        this.view = view;
    }

    public void onAddressSelected(Address address) {
        view.showProgress();
        double latitude = -23.632919; // address.getLatitude();
        double longitude = -46.699453; // address.getLongitude();
        disposable = interactor.findNearestPoc(latitude, longitude,
                poc -> onReceivePoc(poc, address), throwable -> this.onError());
    }

    void onReceivePoc(Poc poc, Address address) {
        view.hideProgress();
        view.openProducts(poc.getId(), AddressFormat.simple(address));
    }

    void onError() {
        view.hideProgress();
        view.showFindPocError();
    }

    public void onDestroy() {
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
