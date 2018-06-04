package br.com.zedeliverychallenge.presentation.location;

public interface LocationMvpView {

    void showProgress();

    void hideProgress();

    void openProducts(String pocId, String format);

    void showFindPocError();
}
