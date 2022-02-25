package com.jiajunhui.xapp.medialoader.callback;

import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.jiajunhui.xapp.medialoader.MediaConfig;

/**
 * Created by Taurus on 2017/5/23.
 */

public abstract class BaseLoaderCallBack<T> extends OnLoaderCallBack {

    public static final String COLUMN_BUCKET_ID = "bucket_id";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_BUCKET_DISPLAY_NAME = "bucket_display_name";

    protected MediaConfig mMediaConfig;

    public void setMediaConfig(MediaConfig mediaConfig) {
        mMediaConfig = mediaConfig;
    }

    public abstract void onResult(@NonNull T result);

    @Override
    public String getSelections() {
        return MediaStore.MediaColumns.SIZE + " > ?";
    }

    @Override
    public String[] getSelectionsArgs() {
        return new String[]{"0"};
    }

    @Override
    public String getSortOrderSql() {
        return MediaStore.MediaColumns.DATE_MODIFIED + " DESC";
    }

    @Override
    public int getLimit() {
        return -1;
    }

    @Override
    public int getOffset() {
        return -1;
    }
}
