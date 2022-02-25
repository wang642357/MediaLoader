package com.jiajunhui.xapp.medialoader.callback;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.MIME_TYPE;
import static android.provider.MediaStore.MediaColumns.SIZE;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.MediaFolder;
import com.jiajunhui.xapp.medialoader.bean.MediaItem;
import com.jiajunhui.xapp.medialoader.bean.MediaResult;
import com.jiajunhui.xapp.medialoader.utils.MediaUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public abstract class OnMediaFileLoaderCallBack extends BaseLoaderCallBack<MediaResult> {

    @Override
    public String[] getSelectProjection() {
        return new String[]{
                MediaStore.Files.FileColumns._ID,
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.MIME_TYPE,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT,
                COLUMN_DURATION,
                MediaStore.MediaColumns.SIZE,
                COLUMN_BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DISPLAY_NAME,
                COLUMN_BUCKET_ID,
                MediaStore.MediaColumns.DATE_ADDED,
                MediaStore.MediaColumns.DATE_MODIFIED,
        };
    }

    @Override
    public String[] getSelectionsArgs() {
        return MediaUtil.getPageSelectionArgs(mMediaConfig);
    }

    @Override
    public String getSelections() {
        return MediaUtil.getPageSelection(mMediaConfig);
    }

    @Override
    public String getSortOrderSql() {
        int pageIndex = mMediaConfig.getPageIndex();
        int pageSize = mMediaConfig.getPageSize();
        return pageIndex == -1 ? MediaStore.MediaColumns.DATE_MODIFIED + " DESC" : MediaStore.MediaColumns.DATE_MODIFIED + " DESC limit " + pageSize + " offset " + pageSize * pageIndex;
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Files.getContentUri("external");
    }

    @Override
    public int getLimit() {
        return mMediaConfig.getPageSize();
    }

    @Override
    public int getOffset() {
        return mMediaConfig.getPageIndex() * mMediaConfig.getPageSize();
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<MediaFolder> folders = new ArrayList<>();
        if (data == null) {
            onResult(new MediaResult(folders));
            return;
        }
        MediaFolder folder;
        MediaItem item;
        long sum_size = 0;
        while (data.moveToNext()) {
            long folderId = data.getLong(data.getColumnIndexOrThrow(COLUMN_BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(COLUMN_BUCKET_DISPLAY_NAME));
            int imageId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            long duration = data.getLong(data.getColumnIndexOrThrow(COLUMN_DURATION));
            String mineType = data.getString(data.getColumnIndexOrThrow(MIME_TYPE));
            folder = new MediaFolder();
            folder.setFolderId(folderId);
            folder.setFolderName(folderName);
            item = new MediaItem(imageId, name, path, size, modified, duration, mineType);
            if (folders.contains(folder)) {
                folders.get(folders.indexOf(folder)).addItem(item);
            } else {
                folder.setFolderCover(path);
                folder.addItem(item);
                folders.add(folder);
            }
            sum_size += size;
        }
        onResult(new MediaResult(folders, sum_size));
    }
}
