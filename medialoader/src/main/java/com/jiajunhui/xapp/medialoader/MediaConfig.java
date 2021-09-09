package com.jiajunhui.xapp.medialoader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.jiajunhui.xapp.medialoader.callback.OnMediaFileLoaderCallBack;

import java.util.Arrays;
import java.util.HashSet;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public class MediaConfig {

    private long mVideoMaxSecond;
    private long mVideoMinSecond;
    private long mFilterMaxFileSize;
    private long mFilterMinFileSize;
    private MediaType mMediaType;
    private FragmentActivity mActivity;
    public HashSet<String> mQueryMimeTypeHashSet;
    private OnMediaFileLoaderCallBack mOnMediaFileLoaderCallBack;
    private int mPageIndex;
    private int mPageSize;
    private long folderId = -1;
    private Fragment mFragment;

    public MediaConfig(@NonNull FragmentActivity activity) {
        this.mActivity = activity;
        initDefaultValue();
    }

    public MediaConfig(@NonNull Fragment fragment) {
        mFragment = fragment;
        initDefaultValue();
    }

    public void initDefaultValue() {
        mMediaType = MediaType.PHOTO;
        mVideoMaxSecond = 0;
        mVideoMinSecond = 0;
        mFilterMaxFileSize = 0;
        mFilterMinFileSize = 0;
        mQueryMimeTypeHashSet = null;
        mPageIndex = -1;
        mPageSize = 0;
    }

    public MediaConfig setVideoMaxSecond(long videoMaxSecond) {
        mVideoMaxSecond = videoMaxSecond;
        return this;
    }

    public MediaConfig setVideoMinSecond(long videoMinSecond) {
        mVideoMinSecond = videoMinSecond;
        return this;
    }

    public MediaConfig setMediaType(MediaType mediaType) {
        mMediaType = mediaType;
        return this;
    }

    public MediaConfig setOnMediaFileLoaderCallBack(@NonNull OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        mOnMediaFileLoaderCallBack = onMediaFileLoaderCallBack;
        return this;
    }

    public long getFilterMaxFileSize() {
        return mFilterMaxFileSize;
    }

    public MediaConfig setFilterMaxFileSize(long filterMaxFileSize) {
        mFilterMaxFileSize = filterMaxFileSize;
        return this;
    }

    public long getFilterMinFileSize() {
        return mFilterMinFileSize;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public MediaConfig setFilterMinFileSize(long filterMinFileSize) {
        mFilterMinFileSize = filterMinFileSize;
        return this;
    }

    public long getVideoMaxSecond() {
        return mVideoMaxSecond;
    }

    public long getVideoMinSecond() {
        return mVideoMinSecond;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public MediaConfig setPageIndex(int pageIndex) {
        mPageIndex = pageIndex;
        return this;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public MediaConfig setPageSize(int pageSize) {
        mPageSize = pageSize;
        return this;
    }

    public long getFolderId() {
        return folderId;
    }

    public MediaConfig setFolderId(long folderId) {
        this.folderId = folderId;
        return this;
    }

    public MediaConfig queryMimeTypeConditions(String... mimeTypes) {
        if (mimeTypes != null && mimeTypes.length > 0) {
            mQueryMimeTypeHashSet = new HashSet<>(Arrays.asList(mimeTypes));
        } else {
            mQueryMimeTypeHashSet = null;
        }
        return this;
    }

    public void load() {
        if (mOnMediaFileLoaderCallBack != null) {
            mOnMediaFileLoaderCallBack.setMediaConfig(this);
            if (mActivity != null) {
                MediaStoreLoader.getLoader().loadMediaFile(mActivity, mOnMediaFileLoaderCallBack);
            } else if (mFragment != null) {
                MediaStoreLoader.getLoader().loadMediaFile(mFragment, mOnMediaFileLoaderCallBack);
            }
        }
    }
}
