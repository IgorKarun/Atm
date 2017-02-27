package com.ikar.grossumtesttask.modules;

import com.ikar.grossumtesttask.algorithms.CashIteratorAlgorithm;
import com.ikar.grossumtesttask.algorithms.ICash;

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
