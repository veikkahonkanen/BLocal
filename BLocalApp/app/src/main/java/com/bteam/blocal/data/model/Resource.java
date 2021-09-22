package com.bteam.blocal.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// https://developer.android.com/jetpack/guide#addendum
public class Resource<T> {
    @NonNull public final Status status;
    @Nullable
    public final T data;
    @Nullable public final Throwable error;

    private Resource(@NonNull Status status, @Nullable T data,
                     @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(Throwable msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }


    public enum Status { SUCCESS, ERROR, LOADING }
}

