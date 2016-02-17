package com.mtest.util;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/*
 * Copyright 2015 (C) Mavin Apps 
 * Author   : Bala M on 8/26/2015
 * Comments :
 */
public class ApplicationBus {

    private static final Bus BUS = new Bus(ThreadEnforcer.ANY);

    private ApplicationBus() {
    }

    public static Bus getInstance() {
        return BUS;
    }
}
