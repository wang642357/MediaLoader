package com.jiajunhui.xapp.medialoader;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.jiajunhui.xapp.medialoader.callback.OnAudioLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnMediaFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnPhotoLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnVideoLoaderCallBack;

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
    private OnPhotoLoaderCallBack mOnPhotoLoaderCallBack;
    private OnVideoLoaderCallBack mOnVideoLoaderCallBack;
    private OnAudioLoaderCallBack mOnAudioLoaderCallBack;
    private OnMediaFileLoaderCallBack mOnMediaFileLoaderCallBack;
    private int mPageIndex;
    private int mPageSize;
    private Fragment mFragment;

    public MediaConfig(FragmentActivity activity) {
        this.mActivity = activity;
        initDefaultValue();
    }

    public MediaConfig(Fragment fragment) {
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

    public MediaConfig setOnPhotoLoaderCallBack(OnPhotoLoaderCallBack onPhotoLoaderCallBack) {
        mOnPhotoLoaderCallBack = onPhotoLoaderCallBack;
        return this;
    }

    public MediaConfig setOnVideoLoaderCallBack(OnVideoLoaderCallBack onVideoLoaderCallBack) {
        mOnVideoLoaderCallBack = onVideoLoaderCallBack;
        return this;
    }

    public MediaConfig setOnAudioLoaderCallBack(OnAudioLoaderCallBack onAudioLoaderCallBack) {
        mOnAudioLoaderCallBack = onAudioLoaderCallBack;
        return this;
    }

    public MediaConfig setOnMediaFileLoaderCallBack(OnMediaFileLoaderCallBack onMediaFileLoaderCallBack) {
        mOnMediaFileLoaderCallBack = onMediaFileLoaderCallBack;
        return this;
    }

    public long getFilterMaxFileSize() {
        return mFilterMaxFileSize;
    }

    public void setFilterMaxFileSize(long filterMaxFileSize) {
        mFilterMaxFileSize = filterMaxFileSize;
    }

    public long getFilterMinFileSize() {
        return mFilterMinFileSize;
    }

    public MediaType getMediaType() {
        return mMediaType;
    }

    public void setFilterMinFileSize(long filterMinFileSize) {
        mFilterMinFileSize = filterMinFileSize;
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

    public MediaConfig queryMimeTypeConditions(String... mimeTypes) {
        if (mimeTypes != null && mimeTypes.length > 0) {
            mQueryMimeTypeHashSet = new HashSet<>(Arrays.asList(mimeTypes));
        } else {
            mQueryMimeTypeHashSet = null;
        }
        return this;
    }

    public void load() {
        switch (mMediaType) {
            case PHOTO:
                mOnPhotoLoaderCallBack.setMediaConfig(this);
                if (mActivity != null) {
                    MediaStoreLoader.getLoader().loadPhotos(mActivity, mOnPhotoLoaderCallBack);
                } else if (mFragment != null) {
                    MediaStoreLoader.getLoader().loadPhotos(mFragment, mOnPhotoLoaderCallBack);
                }
                break;
            case VIDEO:
                mOnVideoLoaderCallBack.setMediaConfig(this);
                if (mActivity != null) {
                    MediaStoreLoader.getLoader().loadVideos(mActivity, mOnVideoLoaderCallBack);
                } else if (mFragment != null) {
                    MediaStoreLoader.getLoader().loadVideos(mFragment, mOnVideoLoaderCallBack);
                }
                break;
            case AUDIO:
                mOnAudioLoaderCallBack.setMediaConfig(this);
                if (mActivity != null) {
                    MediaStoreLoader.getLoader().loadAudios(mActivity, mOnAudioLoaderCallBack);
                } else if (mFragment != null) {
                    MediaStoreLoader.getLoader().loadAudios(mFragment, mOnAudioLoaderCallBack);
                }
                break;
            case ALL:
                mOnMediaFileLoaderCallBack.setMediaConfig(this);
                if (mActivity != null) {
                    MediaStoreLoader.getLoader().loadMediaFile(mActivity, mOnMediaFileLoaderCallBack);
                } else if (mFragment != null) {
                    MediaStoreLoader.getLoader().loadMediaFile(mFragment, mOnMediaFileLoaderCallBack);
                }
                break;
        }
    }
}
