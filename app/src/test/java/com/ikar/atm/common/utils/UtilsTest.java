package com.ikar.atm.common.utils;

import com.ikar.atm.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by igorkarun on 3/7/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest=Config.NONE)
public class UtilsTest {

    @Before
    public void setup() {

    }

    @Test
    public void testparseAmountFromText() {

        assertNull(Utils.parseAmountFromText(null));
        assertNull(Utils.parseAmountFromText(""));
        assertNull(Utils.parseAmountFromText(" "));

        assertNotNull(Utils.parseAmountFromText("0"));
        assertNotNull(Utils.parseAmountFromText("999"));

        assertEquals(Utils.parseAmountFromText("999"), Integer.valueOf(999));

    }

}
