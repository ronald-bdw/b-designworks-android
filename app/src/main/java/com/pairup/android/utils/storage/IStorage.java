package com.pairup.android.utils.storage;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Ilya Eremin on 4/26/16.
 */
public interface IStorage {
    void put(String key, @NonNull Object item);
    @Nullable <T> T get(@NonNull String key, @NonNull Type clazz);
    boolean contains(@NonNull String key);
    void putLong(@NonNull String key, long number);
    long getLong(@NonNull String key, long defaultValue);
    void putInt(@NonNull String key, int number);
    int getInt(@NonNull String key, int defaultValue);
    void putBoolean(@NonNull String key, boolean value);
    boolean getBoolean(@NonNull String key, boolean defaultValue);
    void putString(@NonNull final String key, @NonNull String str);
    @Nullable String getString(@NonNull String key);
    @NonNull <T>  List<T> putItemInCollection(
    @NonNull String key, @NonNull T item, @NonNull Type type);
    <T> void putItemsInCollection(@NonNull String key, @NonNull T[] items, @NonNull Type type);
    <T> void removeFromCollection(@NonNull String key, @NonNull T item, @NonNull Type type);
    @Nullable <T> List<T> getCollection(@NonNull String key, @NonNull Type type);
    void remove(@NonNull String key);
    void clear();
}
