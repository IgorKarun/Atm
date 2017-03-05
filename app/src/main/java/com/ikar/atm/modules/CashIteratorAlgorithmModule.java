package com.ikar.atm.modules;

import com.ikar.atm.algorithms.CashIteratorAlgorithm;
import com.ikar.atm.algorithms.ICash;

import dagger.Module;
import dagger.Provides;

/**
 * Created by igorkarun on 2/26/17.
 */
@Module
public class CashIteratorAlgorithmModule {

    @Provides
    ICash provideCashIteratorAlgorithm() {
        return new CashIteratorAlgorithm();
    }
}
