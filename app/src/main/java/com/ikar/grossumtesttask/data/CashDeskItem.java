package com.ikar.grossumtesttask.data;

/**
 * Created by iKar on 11/5/15.
 */
public class CashDeskItem {

    private int denomination;
    private int inventory;

    public CashDeskItem() {

    }

    public CashDeskItem(int denomination, int inventory) {
        this.denomination = denomination;
        this.inventory = inventory;
    }

    public int getDenomination() {
        return denomination;
    }

    public void setDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }
}
