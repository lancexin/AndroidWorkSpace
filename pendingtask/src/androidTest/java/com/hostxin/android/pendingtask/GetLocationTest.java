package com.hostxin.android.pendingtask;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GetLocationTest {

    private Context appContext;

    private PendingTaskManager mPendingTaskManager;

    private static final String GET_LOCATION_ID = "GET_LOCATION_ID";

    @Before
    public void init(){
        appContext = InstrumentationRegistry.getTargetContext();
        mPendingTaskManager = new PendingTaskManager();
        mPendingTaskManager.init(appContext);
    }

    @Test
    public void testGetLocation() throws Exception {
        GetLocationResult result = mPendingTaskManager.execute(new GetLocationRequest(mPendingTaskManager,GET_LOCATION_ID)).await();
        assertEquals(true, result.getStatus().isSuccess());
        assertEquals("100", result.getLocation().la);
        assertEquals("101", result.getLocation().lo);
    }
}
