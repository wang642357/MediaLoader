package com.jiajunhui.xapp.medialoader.callback;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.loader.content.Loader;

import com.jiajunhui.xapp.medialoader.bean.MediaFolder;
import com.jiajunhui.xapp.medialoader.utils.MediaUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public abstract class OnMediaFolderLoaderCallBack extends BaseLoaderCallBack<List<MediaFolder>> {

    private static final String COLUMN_COUNT = "count";
    private static final String[] PROJECTION_29 = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE};

    private static final String[] PROJECTION = {
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.DATA,
            COLUMN_BUCKET_ID,
            COLUMN_BUCKET_DISPLAY_NAME,
            MediaStore.MediaColumns.DISPLAY_NAME,
            MediaStore.MediaColumns.MIME_TYPE,
            "COUNT(*) AS " + COLUMN_COUNT};

    @Override
    public String[] getSelectProjection() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? PROJECTION_29 : PROJECTION;
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
        return MediaStore.Files.FileColumns._ID + " DESC";
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Files.getContentUri("external");
    }

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() <= 0) {
            onResult(new ArrayList<>());
            return;
        }
        List<MediaFolder> folders = new ArrayList<>();
        long totalCount = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Map<Long, Long> countMap = new HashMap<>();
            while (data.moveToNext()) {
                long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                Long newCount = countMap.get(bucketId);
                if (newCount == null) {
                    newCount = 1L;
                } else {
                    newCount++;
                }
                countMap.put(bucketId, newCount);
            }

            if (data.moveToFirst()) {
                Set<Long> hashSet = new HashSet<>();
                do {
                    long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                    if (hashSet.contains(bucketId)) {
                        continue;
                    }
                    MediaFolder mediaFolder = new MediaFolder();
                    mediaFolder.setFolderId(bucketId);
                    String bucketDisplayName = data.getString(
                            data.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME));
                    String mimeType = data.getString(data.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                    long size = countMap.get(bucketId);
                    long id = data.getLong(data.getColumnIndex(MediaStore.Files.FileColumns._ID));
                    mediaFolder.setFolderName(bucketDisplayName);
                    mediaFolder.setFolderCount(size);
                    mediaFolder.setFolderCover(MediaUtil.getRealPathAndroid_Q(id));
                    mediaFolder.setCoverMineType(mimeType);
                    folders.add(mediaFolder);
                    hashSet.add(bucketId);
                    totalCount += size;
                } while (data.moveToNext());
            }
        } else {
            data.moveToFirst();
            do {
                MediaFolder mediaFolder = new MediaFolder();
                long bucketId = data.getLong(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_ID));
                String bucketDisplayName = data.getString(data.getColumnIndex(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME));
                String mimeType = data.getString(data.getColumnIndex(MediaStore.MediaColumns.MIME_TYPE));
                int size = data.getInt(data.getColumnIndex(COLUMN_COUNT));
                mediaFolder.setFolderId(bucketId);
                String url = data.getString(data.getColumnIndex(MediaStore.MediaColumns.DATA));
                mediaFolder.setFolderCover(url);
                mediaFolder.setFolderName(bucketDisplayName);
                mediaFolder.setCoverMineType(mimeType);
                mediaFolder.setFolderCount(size);
                folders.add(mediaFolder);
                totalCount += size;
            } while (data.moveToNext());
        }

        // 相机胶卷
        MediaFolder allMediaFolder = new MediaFolder();
        allMediaFolder.setFolderCount(totalCount);
        allMediaFolder.setFolderId(-1);
        if (data.moveToFirst()) {
            allMediaFolder.setFolderCover(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ? MediaUtil.getFirstUri(data) : MediaUtil.getFirstUrl(data));
            allMediaFolder.setCoverMineType(MediaUtil.getFirstCoverMimeType(data));
        }
        String bucketDisplayName = "相机胶卷";
        allMediaFolder.setFolderName(bucketDisplayName);
        folders.add(0, allMediaFolder);
        onResult(folders);
    }


}
