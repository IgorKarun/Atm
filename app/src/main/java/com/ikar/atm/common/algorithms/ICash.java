package com.ikar.atm.common.algorithms;

import com.ikar.atm.common.models.CashDeskItem;

import java.util.List;
import java.util.Map;

/**
 * Created by igorkarun on 2/26/17.
 */
public interface ICash {
    Map<Integer, Integer> getAmount(List<CashDeskItem> cashDeskItems,
                                    int noteItemNumber, int currentAmount);
}
