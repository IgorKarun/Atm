package com.ikar.atm.components;

import com.ikar.atm.algorithms.ICash;
import com.ikar.atm.modules.CashRecursiveAlgorithmModule;

import dagger.Component;

/**
 * Created by igorkarun on 2/26/17.
 */

@Component(modules={CashRecursiveAlgorithmModule.class})
public interface ICashComponent {
    ICash getCash();
}
