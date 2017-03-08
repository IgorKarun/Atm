package com.ikar.atm.common.algorithms;

import com.ikar.atm.common.models.CashDeskItem;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by igorkarun on 2/26/17.
 */
public interface ICash {
    Observable<Map<Integer, Integer>> getAmount(List<CashDeskItem> cashDeskItems, int currentAmount);

    Map<Integer, Integer> calculate(List<CashDeskItem> cashDeskItems,
                                     int noteItemNumber, int currentAmount);
}
