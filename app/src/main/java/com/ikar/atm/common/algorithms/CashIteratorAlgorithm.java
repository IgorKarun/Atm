package com.ikar.atm.common.algorithms;

import com.ikar.atm.common.models.CashDeskItem;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by igorkarun on 2/26/17.
 */

public class CashIteratorAlgorithm implements ICash {

    @Override
    public Observable<Map<Integer, Integer>> getAmount(List<CashDeskItem> cashDeskItems, int currentAmount) {
        return null;
    }

    @Override
    public Map<Integer, Integer> calculate(List<CashDeskItem> cashDeskItems, int noteItemNumber, int currentAmount) {
        return null;
    }
}
