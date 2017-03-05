package com.ikar.atm.modules;

import com.ikar.atm.algorithms.CashRecursiveAlgorithm;
import com.ikar.atm.algorithms.ICash;

import dagger.Module;
import dagger.Provides;

/**
 * Created by igorkarun on 2/26/17.
 */
@Module
public class CashRecursiveAlgorithmModule {

    @Provides
    ICash provideCashRecursiveAlgorithm() {
        return new CashRecursiveAlgorithm();
    }
}
