package com.pairup.android.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public final class Bus {

    public static final boolean STICKY = true;
    private static boolean sDisableForTest;

    /**
     * to speed up eventBus use code generation.
     *
     * @see <a href="http://greenrobot.org/eventbus/documentation/subscriber-index/">
     * GreenRobot Eventbus docs</a>
     */
    private static EventBus sBus;

    private Bus() {
    }

    public static void subscribe(Object object) {
        initialize();
        sBus.register(object);
    }

    private static void initialize() {
        if (sBus == null) {
            sBus = EventBus.builder().build();
        }
    }

    public static void unsubscribe(Object object) {
        initialize();
        sBus.unregister(object);
    }

    public static void event(Object object, boolean... sticky) {
        if (sDisableForTest) return;
        initialize();
        if (sticky != null && sticky.length > 0) {
            sBus.postSticky(object);
        } else {
            sBus.post(object);
        }
    }

    public static void removeSticky(Class clazz) {
        sBus.removeStickyEvent(clazz);
    }

    public static boolean hasSubscribersForType(Class clazz) {
        return sBus.hasSubscriberForEvent(clazz);
    }

    public static void disableForTest() {
        sDisableForTest = true;
    }
}