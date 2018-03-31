package com.soussidev.notificationfirebasechannel.notification;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Soussi on 30/03/2018.
 */

public class NotificationID {

    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getID() {
        return c.incrementAndGet();
    }
}
