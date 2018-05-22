package br.com.zedeliverychallenge.presentation.location;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

class LocationSearchHelper {

    private final Geocoder geocoder;
    private final PublishSubject<String> searchSubject;

    public static Observable<List<Address>> from(Geocoder geocoder, AppCompatEditText inputSearch, ProgressBar progress) {
        return new LocationSearchHelper(geocoder, inputSearch, progress).observable();
    }

    private LocationSearchHelper(Geocoder geocoder, AppCompatEditText inputSearch, ProgressBar progress) {
        this.geocoder = geocoder;
        searchSubject = PublishSubject.create();
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                progress.setVisibility(View.VISIBLE);
                searchSubject.onNext(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private Observable<List<Address>> observable() {
        return searchSubject.debounce(700, TimeUnit.MILLISECONDS)
                .switchMap((Function<String, ObservableSource<List<Address>>>)
                        query -> Observable.fromArray(geocoder.getFromLocationName(query, 3)));
    }
}
