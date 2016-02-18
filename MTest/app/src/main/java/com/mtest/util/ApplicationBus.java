package com.mtest.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by udaya on 2/15/16.
 */
public class ApplicationBus {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    private ApplicationBus() {
    }

    public static Bus getInstance() {
        return BUS;
    }
}
