package com.ikar.grossumtesttask.modules;

import com.ikar.grossumtesttask.algorithms.CashRecursiveAlgorithm;
import com.ikar.grossumtesttask.algorithms.ICash;

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
