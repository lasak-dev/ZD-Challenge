package br.com.zedeliverychallenge.data.client;

import com.apollographql.apollo.ApolloQueryCall;

import java.util.Date;

import br.com.zedeliverychallenge.PocQuery;

public class PocClient extends BaseClient {

    public ApolloQueryCall<PocQuery.Data> searchPocs(String latitude, String longitude) {
        return apolloClient.query(PocQuery.builder()
                .algorithm("NEAREST")
                .lat(latitude)
                .lng(longitude)
                .now(new Date())
                .build());
    }
}
