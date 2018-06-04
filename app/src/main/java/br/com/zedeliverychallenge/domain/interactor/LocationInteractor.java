package br.com.zedeliverychallenge.domain.interactor;

import br.com.zedeliverychallenge.data.repository.PocRepository;
import br.com.zedeliverychallenge.domain.model.Poc;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LocationInteractor {

    private final PocRepository pocRepository;

    public LocationInteractor(PocRepository pocRepository) {
        this.pocRepository = pocRepository;
    }

    public Disposable findNearestPoc(double latitude, double longitude, Consumer<Poc> onSuccess,
                                     Consumer<Throwable> onError) {
        return pocRepository.nearestPoc(latitude, longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError);
    }
}
