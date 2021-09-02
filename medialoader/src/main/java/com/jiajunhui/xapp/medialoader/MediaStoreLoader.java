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

import com.jiajunhui.xapp.medialoader.callback.OnAudioLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnMediaFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnPhotoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;
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

    public void loadPhotos(@NonNull Fragment fragment, OnPhotoLoaderCallBack onPhotoLoaderCallBack) {
        load(fragment, onPhotoLoaderCallBack);
    }

    public void loadMediaFile(@NonNull Fragment fragment, OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        load(fragment, onMediaFileLoaderCallBack);
    }

    public void loadVideos(@NonNull Fragment fragment, OnVideoLoaderCallBack onVideoLoaderCallBack) {
        load(fragment, onVideoLoaderCallBack);
    }

    public void loadAudios(@NonNull Fragment fragment, OnAudioLoaderCallBack onAudioLoaderCallBack) {
        load(fragment, onAudioLoaderCallBack);
    }

    public void loadFiles(@NonNull Fragment fragment, OnFileLoaderCallBack onFileLoaderCallBack) {
        load(fragment, onFileLoaderCallBack);
    }

    public void loadPhotos(@NonNull FragmentActivity activity, OnPhotoLoaderCallBack onPhotoLoaderCallBack) {
        load(activity, onPhotoLoaderCallBack);
    }

    public void loadMediaFile(@NonNull FragmentActivity activity, OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        load(activity, onMediaFileLoaderCallBack);
    }

    public void loadVideos(@NonNull FragmentActivity activity, OnVideoLoaderCallBack onVideoLoaderCallBack) {
        load(activity, onVideoLoaderCallBack);
    }

    public void loadAudios(@NonNull FragmentActivity activity, OnAudioLoaderCallBack onAudioLoaderCallBack) {
        load(activity, onAudioLoaderCallBack);
    }

    public void loadFiles(@NonNull FragmentActivity activity, OnFileLoaderCallBack onFileLoaderCallBack) {
        load(activity, onFileLoaderCallBack);
    }

}
