package com.bteam.blocal.utility;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;

// Inspired by https://stackoverflow.com/a/63136873
public class NavigationResult {
    public final static String DEFAULT_KEY = "result";
    public static <T> void setNavigationResult(Fragment f, @Nullable String key, T value){
        NavBackStackEntry backStackEntry = NavHostFragment
                .findNavController(f).getPreviousBackStackEntry();
        if(backStackEntry != null){
            SavedStateHandle handle = backStackEntry.getSavedStateHandle();
            handle.set(key == null ? DEFAULT_KEY : key, value);
        }
    }

    public static <T> LiveData<T> getNavigationResult(Fragment f, @Nullable String key){
        NavBackStackEntry backStackEntry = NavHostFragment
                .findNavController(f).getCurrentBackStackEntry();
        if(backStackEntry != null){
            SavedStateHandle handle = backStackEntry.getSavedStateHandle();
            return handle.getLiveData(key == null ? DEFAULT_KEY : key);
        }
        return null;
    }

    /**
     * @param id ID of your destination
     */
    public static <T> void getDialogNavigationResult(Fragment f, @IdRes Integer id,
                                                     @Nullable String key,
                                                     IOnResultCallback<T> onResult){
        NavBackStackEntry backStackEntry = NavHostFragment.findNavController(f)
                .getBackStackEntry(id);
        LifecycleEventObserver observer = (source, event) -> {
            if(event == Lifecycle.Event.ON_RESUME && backStackEntry.getSavedStateHandle()
                    .contains(key)){
                SavedStateHandle handle = backStackEntry.getSavedStateHandle();
                T result = handle.<T>get(key == null ? DEFAULT_KEY : key);
                onResult.onResult(result);
                handle.<T>remove(key);
            }
        };

        backStackEntry.getLifecycle().addObserver(observer);

        f.getViewLifecycleOwner().getLifecycle().addObserver(
                (LifecycleEventObserver) (source, event) -> {
            if(event == Lifecycle.Event.ON_DESTROY){
                backStackEntry.getLifecycle().removeObserver(observer);
            }
        });
    }

    @FunctionalInterface
    public interface  IOnResultCallback<T>{
        void onResult(T value);
    }
}
