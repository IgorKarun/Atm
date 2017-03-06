package com.ikar.atm.common.modules;

import com.ikar.atm.common.algorithms.CashRecursiveAlgorithm;
import com.ikar.atm.common.algorithms.ICash;

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
