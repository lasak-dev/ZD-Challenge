package br.com.zedeliverychallenge.data.repository;

import com.apollographql.apollo.ApolloQueryCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.rx2.Rx2Apollo;

import br.com.zedeliverychallenge.PocQuery;
import br.com.zedeliverychallenge.data.client.PocClient;
import br.com.zedeliverychallenge.data.mapper.PocMapper;
import br.com.zedeliverychallenge.domain.model.Poc;
import io.reactivex.Single;

public class PocRepository {

    private final PocClient pocClient;
    private final PocMapper pocMapper;

    public PocRepository(PocClient pocClient, PocMapper pocMapper) {
        this.pocClient = pocClient;
        this.pocMapper = pocMapper;
    }

    public Single<Poc> nearestPoc(double latitude, double longitude) {
        ApolloQueryCall<PocQuery.Data> query = pocClient.searchPocs(String.valueOf(latitude), String.valueOf(longitude));
        return Single.fromObservable(Rx2Apollo.from(query))
                .map(dataResponse -> {
                    if (hasNearPoc(dataResponse)) {
                        return getNearestPoc(dataResponse.data());
                    }
                    throw new RuntimeException("No poc found");
                })
                .map(pocMapper::parse);
    }

    boolean hasNearPoc(Response<PocQuery.Data> dataResponse) {
        return dataResponse.data() != null &&
                dataResponse.data().pocSearch() != null &&
                !dataResponse.data().pocSearch().isEmpty();
    }

    PocQuery.PocSearch getNearestPoc(PocQuery.Data data) {
        return data.pocSearch().get(0);
    }
}
