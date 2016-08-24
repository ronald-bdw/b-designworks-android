package com.b_designworks.android.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ilya Eremin on 18.08.2016.
 */
public class Bus {
    public static final boolean STICKY = true;

    /**
     * to speed up eventBus use code generation.
     *
     * @see <a href="http://greenrobot.org/eventbus/documentation/subscriber-index/">GreenRobot Eventbus docs</a>
     */
    private static final EventBus bus = EventBus.builder()
        .build();

    private Bus() {
    }

    public static void subscribe(Object object) {
        bus.register(object);
    }

    public static void unsubscribe(Object object) {
        bus.unregister(object);
    }

    public static void event(Object object, boolean... sticky) {
        if (sticky != null && sticky.length > 0) {
            bus.postSticky(object);
        } else {
            bus.post(object);
        }
    }

    public static void removeSticky(Class clazz) {
        bus.removeStickyEvent(clazz);
    }

    public static boolean hasSubscribersForType(Class clazz) {
        return bus.hasSubscriberForEvent(clazz);
    }
}
