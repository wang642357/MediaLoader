package com.jiajunhui.xapp.medialoader.utils;

import android.provider.MediaStore;
import android.text.TextUtils;

import com.jiajunhui.xapp.medialoader.MediaConfig;
import com.jiajunhui.xapp.medialoader.MediaMineType;
import com.jiajunhui.xapp.medialoader.MediaType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public class MediaUtil {
    private static final String NOT_GIF_UNKNOWN = "!='image/*'";
    private static final String NOT_GIF = " AND (" + MediaStore.MediaColumns.MIME_TYPE + "!='image/gif' AND " + MediaStore.MediaColumns.MIME_TYPE + NOT_GIF_UNKNOWN + ")";
    private static final String COLUMN_BUCKET_ID = "bucket_id";

    public static String getPageSelection(MediaConfig config) {
        String durationCondition = getDurationCondition(config);
        String sizeCondition = getFileSizeCondition(config);
        String queryMimeCondition = getQueryMimeCondition(config);
        if (config.getMediaType() == MediaType.ALL) {
            return getPageSelectionArgsForAllMediaCondition(config.getFolderId(), queryMimeCondition, durationCondition, sizeCondition);
        } else if (config.getMediaType() == MediaType.PHOTO) {
            return getPageSelectionArgsForImageMediaCondition(config.getFolderId(), queryMimeCondition, sizeCondition);
        } else if (config.getMediaType() == MediaType.VIDEO || config.getMediaType() == MediaType.AUDIO) {
            return getPageSelectionArgsForVideoOrAudioMediaCondition(config.getFolderId(), queryMimeCondition, durationCondition, sizeCondition);
        } else {
            return null;
        }
    }

    private static String getPageSelectionArgsForAllMediaCondition(long bucketId, String queryMimeCondition, String durationCondition, String sizeCondition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(MediaStore.Files.FileColumns.MEDIA_TYPE)
                .append("=?").append(queryMimeCondition).append(" OR ").append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=? AND ").append(durationCondition).append(") AND ");
        if (bucketId == -1) {
            return stringBuilder.append(sizeCondition).toString();
        } else {
            return stringBuilder.append(COLUMN_BUCKET_ID).append("=? AND ").append(sizeCondition).toString();
        }
    }

    private static String getPageSelectionArgsForImageMediaCondition(long bucketId, String queryMimeCondition, String sizeCondition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=?");
        if (bucketId == -1) {
            return stringBuilder.append(queryMimeCondition).append(") AND ").append(sizeCondition).toString();
        } else {
            return stringBuilder.append(queryMimeCondition).append(") AND ").append(COLUMN_BUCKET_ID).append("=? AND ").append(sizeCondition).toString();
        }
    }

    private static String getPageSelectionArgsForVideoOrAudioMediaCondition(long bucketId, String queryMimeCondition, String durationCondition, String sizeCondition) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(").append(MediaStore.Files.FileColumns.MEDIA_TYPE).append("=?").append(queryMimeCondition).append(" AND ").append(durationCondition).append(") AND ");
        if (bucketId == -1) {
            return stringBuilder.append(sizeCondition).toString();
        } else {
            return stringBuilder.append(COLUMN_BUCKET_ID).append("=? AND ").append(sizeCondition).toString();
        }
    }

    public static String getDurationCondition(MediaConfig config) {
        long maxS = config.getVideoMaxSecond() == 0 ? Long.MAX_VALUE : config.getVideoMaxSecond();
        return String.format(Locale.CHINA, "%d <%s " + MediaStore.MediaColumns.DURATION + " and " + MediaStore.MediaColumns.DURATION + " <= %d",
                Math.max(0, config.getVideoMinSecond()),
                Math.max(0, config.getVideoMinSecond()) == 0 ? "" : "=",
                maxS);
    }

    private static String getFileSizeCondition(MediaConfig config) {
        long maxS = config.getFilterMaxFileSize() == 0 ? Long.MAX_VALUE : config.getFilterMaxFileSize();
        return String.format(Locale.CHINA, "%d <%s " + MediaStore.MediaColumns.SIZE + " and " + MediaStore.MediaColumns.SIZE + " <= %d",
                Math.max(0, config.getFilterMinFileSize()),
                Math.max(0, config.getFilterMinFileSize()) == 0 ? "" : "=",
                maxS);
    }

    private static String getQueryMimeCondition(MediaConfig config) {
        HashSet<String> stringHashSet = config.mQueryMimeTypeHashSet;
        if (stringHashSet == null) {
            stringHashSet = new HashSet<>();
        }
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator = stringHashSet.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            String value = iterator.next();
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            if (config.getMediaType() == MediaType.VIDEO) {
                if (value.startsWith(MediaMineType.MIME_TYPE_PREFIX_IMAGE) || value.startsWith(MediaMineType.MIME_TYPE_PREFIX_AUDIO)) {
                    continue;
                }
            } else if (config.getMediaType() == MediaType.PHOTO) {
                if (value.startsWith(MediaMineType.MIME_TYPE_PREFIX_AUDIO) || value.startsWith(MediaMineType.MIME_TYPE_PREFIX_VIDEO)) {
                    continue;
                }
            } else if (config.getMediaType() == MediaType.AUDIO) {
                if (value.startsWith(MediaMineType.MIME_TYPE_PREFIX_VIDEO) || value.startsWith(MediaMineType.MIME_TYPE_PREFIX_IMAGE)) {
                    continue;
                }
            }
            index++;
            stringBuilder.append(index == 0 ? " AND " : " OR ").append(MediaStore.MediaColumns.MIME_TYPE).append("='").append(value).append("'");
        }
        if (config.getMediaType() != MediaType.VIDEO) {
            if (!stringHashSet.contains(MediaMineType.ofGIF())) {
                stringBuilder.append(NOT_GIF);
            }
        }
        return stringBuilder.toString();
    }

    public static String[] getPageSelectionArgs(MediaConfig config) {
        MediaType mediaType = config.getMediaType();
        long bucketId = config.getFolderId();
        if (mediaType == MediaType.ALL) {
            if (bucketId == -1) {
                // ofAll
                return new String[]{
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                        String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                };
            }
            return new String[]{
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
                    String.valueOf(bucketId)
            };
        } else if (mediaType == MediaType.PHOTO) {
            return bucketId == -1 ? new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)} : new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE), String.valueOf(bucketId)};
        } else if (mediaType == MediaType.VIDEO) {
            return bucketId == -1 ? new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)} : new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO), String.valueOf(bucketId)};
        } else if (mediaType == MediaType.AUDIO) {
            return bucketId == -1 ? new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)} : new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO), String.valueOf(bucketId)};
        } else {
            return null;
        }
    }

}
