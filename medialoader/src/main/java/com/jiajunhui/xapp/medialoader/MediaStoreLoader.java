package com.jiajunhui.xapp.medialoader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnMediaFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnMediaFolderLoaderCallBack;
import com.jiajunhui.xapp.medialoader.loader.AbsLoaderCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Taurus on 2017/5/23.
 */
public class MediaStoreLoader {

    private static final int DEFAULT_START_ID = 1000;

    private final String TAG = "MediaLoader";
    private static final MediaStoreLoader loader = new MediaStoreLoader();

    private final Map<String, Integer> mIds = new HashMap<>();

    private MediaStoreLoader() {
    }

    public static MediaStoreLoader getLoader() {
        return loader;
    }

    private <T extends LifecycleOwner & ViewModelStoreOwner> int checkIds(T owner) {
        String name = owner.getClass().getName();
        int id;
        if (!mIds.containsKey(name)) {
            id = DEFAULT_START_ID;
            mIds.put(name, id);
        } else {
            int preId = mIds.get(name);
            preId++;
            mIds.put(name, preId);
            id = preId;
        }
        return id;
    }

    private <T extends LifecycleOwner & ViewModelStoreOwner> void load(T owner, OnLoaderCallBack onLoaderCallBack) {
        Context context;
        if (owner instanceof Activity) {
            context = ((Activity) owner).getApplicationContext();
        } else if (owner instanceof Fragment) {
            context = ((Fragment) owner).requireContext().getApplicationContext();
        } else {
            throw new IllegalArgumentException("参数异常");
        }
        int id = checkIds(owner);
        LoaderManager.getInstance(owner).initLoader(id, null, new AbsLoaderCallBack(context, onLoaderCallBack) {
            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                super.onLoaderReset(loader);
                mIds.clear();
                Log.d(TAG, "***onLoaderReset***");
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                LoaderManager.getInstance(owner).destroyLoader(id);
                Log.d(TAG, "***onLoaderFinished***");
            }
        });
    }

    public void loadMediaFile(@NonNull Fragment fragment, OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        load(fragment, onMediaFileLoaderCallBack);
    }

    public void loadMediaFile(@NonNull FragmentActivity activity, OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        load(activity, onMediaFileLoaderCallBack);
    }

    public void loadFiles(@NonNull Fragment fragment, OnFileLoaderCallBack onFileLoaderCallBack) {
        load(fragment, onFileLoaderCallBack);
    }

    public void loadFiles(@NonNull FragmentActivity activity, OnFileLoaderCallBack onFileLoaderCallBack) {
        load(activity, onFileLoaderCallBack);
    }

    public void loadFolders(@NonNull FragmentActivity activity, OnMediaFolderLoaderCallBack onFolderLoaderCallBack) {
        load(activity, onFolderLoaderCallBack);
    }

    public void loadFolders(@NonNull Fragment fragment, OnMediaFolderLoaderCallBack onFolderLoaderCallBack) {
        load(fragment, onFolderLoaderCallBack);
    }
}
