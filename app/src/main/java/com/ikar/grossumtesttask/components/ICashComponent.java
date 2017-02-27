package com.ikar.grossumtesttask.components;

import com.ikar.grossumtesttask.algorithms.ICash;
import com.ikar.grossumtesttask.modules.CashRecursiveAlgorithmModule;

import dagger.Component;

/**
 * Created by igorkarun on 2/26/17.
 */

@Component(modules={CashRecursiveAlgorithmModule.class})
public interface ICashComponent {
    ICash getCash();
}
