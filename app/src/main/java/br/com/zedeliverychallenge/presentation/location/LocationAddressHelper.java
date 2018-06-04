package br.com.zedeliverychallenge.presentation.location;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import io.reactivex.Single;

class LocationAddressHelper {

    private final Geocoder geocoder;
    private final Location location;

    public static Single<Address> from(Geocoder geocoder, Location location) {
        return new LocationAddressHelper(geocoder, location).single();
    }

    private LocationAddressHelper(Geocoder geocoder, Location location) {
        this.geocoder = geocoder;
        this.location = location;
    }

    private Single<Address> single() {
        return Single.fromCallable(() -> geocoder.getFromLocation(
                location.getLatitude(), location.getLongitude(), 1)
                .get(0));
    }
}
