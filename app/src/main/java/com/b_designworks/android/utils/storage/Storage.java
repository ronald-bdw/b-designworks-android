package com.b_designworks.android.utils.storage;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ilya Eremin on 4/26/16.
 */
public class Storage implements IStorage {

    private static volatile Storage instance;

    public static Storage getInstance(SharedPreferences sp, Gson gson) {
        Storage localInstance = instance;
        if (localInstance == null) {
            synchronized (Storage.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Storage(sp, gson);
                }
            }
        }
        return localInstance;
    }

    private final SharedPreferences sp;
    private final Gson              gson;

    protected Storage(SharedPreferences sp, Gson gson) {
        this.sp = sp;
        this.gson = gson;
    }

    @Override public void put(@NonNull String key, @NonNull Object items) {
        sp.edit().putString(key, gson.toJson(items)).apply();
    }

    @Override @SuppressWarnings("StringEquality")
    @Nullable public <T> T get(@NonNull String key, @NonNull Type clazz) {
        String json = sp.getString(key, null);
        return json == null ? null : gson.fromJson(json, clazz);
    }

    @Override public boolean contains(@NonNull String key) {
        return sp.contains(key);
    }

    @Override public void putLong(@NonNull String key, long number) {
        sp.edit().putLong(key, number).apply();
    }

    @Override public long getLong(@NonNull String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    @Override public void putInt(@NonNull String key, int number) {
        sp.edit().putInt(key, number).apply();
    }

    @Override public int getInt(@NonNull String key, int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    @Override public void putBoolean(@NonNull String key, boolean value) {
        sp.edit().putBoolean(key, value).apply();
    }

    @Override public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    @Override public void putString(@NonNull String key, @NonNull String str) {
        sp.edit().putString(key, str).apply();
    }

    @Nullable @Override public String getString(@NonNull String key) {
        return sp.getString(key, null);
    }

    @NonNull @Override
    public <T> List<T> putItemInCollection(
        @NonNull String key, @NonNull T item, @NonNull Type type) {
        List<T> objects = get(key, type);
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.add(item);
        put(key, objects);
        return objects;
    }

    @Override public <T> void putItemsInCollection(@NonNull String key,
                                                   @NonNull T[] items,
                                                   @NonNull Type type) {
        List<T> objects = get(key, type);
        if (objects == null) {
            objects = new ArrayList<>();
        }
        Collections.addAll(objects, items);
        put(key, objects);
    }

    @Override
    public <T> void removeFromCollection(@NonNull String key, @NonNull T item, @NonNull Type type) {
        List<Object> collection = getCollection(key, type);
        if (collection != null) {
            collection.remove(item);
        }
        put(key, collection);
    }

    @Override public <T> List<T> getCollection(@NonNull String key, @NonNull Type type) {
        return get(key, type);
    }

    @Override public void remove(@NonNull String key) {
        sp.edit().remove(key).apply();
    }

    @Override public void clear() {
        sp.edit().clear().apply();
    }

}