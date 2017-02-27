package com.ikar.grossumtesttask.algorithms;

import com.ikar.grossumtesttask.data.CashDeskItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by igorkarun on 2/26/17.
 */

public class CashRecursiveAlgorithm implements ICash {

    private Map<Integer, Integer> amountOfMoney = new HashMap<>();

    @Override
    public Map<Integer, Integer> getAmount(List<CashDeskItem> cashDeskItems, int noteItemNumber, int currentAmount) {
        if(noteItemNumber < 0) return null;
        if(currentAmount > 0) {
            final int defaultAmount = currentAmount;
            int betItem = cashDeskItems.get(noteItemNumber).getDenomination();
            int maxBetsPerAmount = (int) Math.floor(currentAmount / betItem);
            int maxBetsCount = cashDeskItems.get(noteItemNumber).getInventory();
            int minIterations = maxBetsPerAmount <= maxBetsCount ? maxBetsPerAmount : maxBetsCount;
            for(int i = minIterations; i >= 0; i--) {
                currentAmount = defaultAmount;
                amountOfMoney.put(betItem, maxBetsCount - i);
                currentAmount = currentAmount - i * betItem;
                if(currentAmount == 0) {
                    return amountOfMoney;
                } else {
                    Map<Integer, Integer> tmp = getAmount(cashDeskItems, noteItemNumber - 1, currentAmount);
                    if(tmp != null) {
                        amountOfMoney.putAll(tmp);
                        return amountOfMoney;
                    }
                }
            }
        }

        return null;
    }
}
