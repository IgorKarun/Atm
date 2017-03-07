package com.ikar.atm.common.components;

import com.ikar.atm.common.algorithms.ICash;
import com.ikar.atm.common.modules.CashRecursiveAlgorithmModule;

import dagger.Component;

/**
 * Created by igorkarun on 2/26/17.
 * Easy to replace CashRecursiveAlgorithm to any another
 */

@Component(modules={CashRecursiveAlgorithmModule.class})
public interface ICashComponent {
    ICash getCash();
}
