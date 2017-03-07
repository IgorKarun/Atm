package com.ikar.atm.common.algorithms;

import com.ikar.atm.BuildConfig;
import com.ikar.atm.common.models.CashDeskItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


/**
 * Created by igorkarun on 3/7/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class CashAlgorithmsTest {

    private ICash cashRecursiveAlgorithm;
    private ICash cashIteratorAlgorithm;
    private ICash cashGridyAlgorithm;

    private List<CashDeskItem> itemList;

    private static final int AMOUNT = 16; //Amount to be paid by the ATM, dollars
    @Before
    public void setup() {
         ShadowLog.stream = System.out;
        cashRecursiveAlgorithm = new CashRecursiveAlgorithm();
        cashIteratorAlgorithm = new CashIteratorAlgorithm();
        cashGridyAlgorithm = new CashGreedyAlgorithm();

        itemList = new ArrayList<>();
        itemList.add(new CashDeskItem(3, 10));
        itemList.add(new CashDeskItem(5, 10));
        itemList.add(new CashDeskItem(10, 10));

    }

    @Test
    public void testRecursiveAlgorithm() {
        Map<Integer, Integer> result = cashRecursiveAlgorithm
                .getAmount(itemList, itemList.size() - 1, AMOUNT);

        assertNotNull(result);

      //ShadowLog.e("CashAlgorithmsTest", "value: " + result.get(10));
        assertEquals(result.get(10).intValue(), 9);
        assertEquals(result.get(5).intValue(), 10);
        assertEquals(result.get(3).intValue(), 8);
    }

    /*
    Not implemented yet
     */
    @Test
    public void testIteratorAlgorithm() {
        Map<Integer, Integer> result = cashIteratorAlgorithm
                .getAmount(itemList, itemList.size() - 1, AMOUNT);

        assertNull(result);
    }

    /*
   Not implemented yet
    */
    @Test
    public void testGreedyAlgorithm() {
        Map<Integer, Integer> result = cashGridyAlgorithm
                .getAmount(itemList, itemList.size() - 1, AMOUNT);

        assertNull(result);
    }
}
