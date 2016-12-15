package com.pairup.android.utils.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ilya Eremin on 6/10/16.
 */
public class RuntimeStorage implements IStorage {

    private Map<String, Object> map = new HashMap<>();

    @Override public void put(@NonNull String key, @NonNull Object item) {
        map.put(key, item);
    }

    @SuppressWarnings("unchecked") @Nullable @Override
    public <T> T get(@NonNull String key, @NonNull Type clazz) {
        return (T) map.get(key);
    }

    @Override public boolean contains(@NonNull String key) {
        return map.containsKey(key);
    }

    @Override public void putLong(@NonNull String key, long number) {
        map.put(key, Long.valueOf(number));
    }

    @Override public long getLong(@NonNull String key, long defaultValue) {
        Object o = map.get(key);
        if (o != null) {
            return ((Long) o);
        } else {
            return defaultValue;
        }
    }

    @Override public void putInt(@NonNull String key, int number) {
        map.put(key, Integer.valueOf(number));
    }

    @Override public int getInt(@NonNull String key, int defaultValue) {
        Object o = map.get(key);
        if (o != null) {
            return ((Integer) o);
        } else {
            return defaultValue;
        }
    }

    @Override public void putBoolean(@NonNull String key, boolean value) {
        map.put(key, Boolean.valueOf(value));
    }

    @Override public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        Object o = map.get(key);
        if (o != null) {
            return ((Boolean) o);
        } else {
            return defaultValue;
        }
    }

    @Override public void putString(@NonNull String key, @NonNull String str) {
        map.put(key, str);
    }

    @Override public String getString(@NonNull String key) {
        Object o = map.get(key);
        return (o != null) ? (String) o : null;
    }

    @NonNull @Override public <T> List<T> putItemInCollection(
        @NonNull String key, @NonNull T item, @NonNull Type type) {
        Object o = map.get(key);
        ArrayList<T> list;
        if (o != null) {
            list = ((ArrayList<T>) o);
        } else {
            list = new ArrayList<>();
        }
        list.add(item);
        map.put(key, list);
        return list;
    }

    @Override public <T> void putItemsInCollection(
        @NonNull String key, @NonNull T[] items, @NonNull Type type) {
        Object o = map.get(key);
        ArrayList<T> list;
        if (o != null) {
            list = ((ArrayList<T>) o);
        } else {
            list = new ArrayList<>();
        }
        Collections.addAll(list, items);
        map.put(key, list);
    }

    @Override
    public <T> void removeFromCollection(@NonNull String key, @NonNull T item, @NonNull Type type) {
        Object o = map.get(key);
        if (o != null) {
            ((ArrayList) o).remove(item);
        }
    }

    @Override public <T> List<T> getCollection(@NonNull String key, @NonNull Type type) {
        return (List<T>) map.get(key);
    }

    @Override public void remove(@NonNull String key) {
        map.remove(key);
    }

    @Override public void clear() {
        map.clear();
    }
}