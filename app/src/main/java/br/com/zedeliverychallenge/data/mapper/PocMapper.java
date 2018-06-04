package br.com.zedeliverychallenge.data.mapper;

import br.com.zedeliverychallenge.PocQuery;
import br.com.zedeliverychallenge.domain.model.Poc;

public class PocMapper {

    public Poc parse(PocQuery.PocSearch pocSearch) {
        return new Poc(pocSearch.id());
    }
}
