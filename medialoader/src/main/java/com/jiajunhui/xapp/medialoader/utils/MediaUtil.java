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

    public static String getPageSelection(MediaConfig config) {
        String durationCondition = getDurationCondition(config);
        String sizeCondition = getFileSizeCondition(config);
        String queryMimeCondition = getQueryMimeCondition(config);
        if (config.getMediaType() == MediaType.ALL) {
            return getPageSelectionArgsForAllMediaCondition(queryMimeCondition, durationCondition, sizeCondition);
        } else if (config.getMediaType() == MediaType.PHOTO) {
            return getPageSelectionArgsForImageMediaCondition(queryMimeCondition, sizeCondition);
        } else if (config.getMediaType() == MediaType.VIDEO || config.getMediaType() == MediaType.AUDIO) {
            return getPageSelectionArgsForVideoOrAudioMediaCondition(queryMimeCondition, durationCondition, sizeCondition);
        } else {
            return null;
        }
    }

    private static String getPageSelectionArgsForAllMediaCondition(String queryMimeCondition, String durationCondition, String sizeCondition) {
        return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE +
                "=?" + queryMimeCondition + " OR " + MediaStore.Files.FileColumns.MEDIA_TYPE + "=? AND " + durationCondition + ") AND " +
                sizeCondition;
    }

    private static String getPageSelectionArgsForImageMediaCondition(String queryMimeCondition, String sizeCondition) {
        return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" +
                queryMimeCondition + ") AND " + sizeCondition;
    }

    private static String getPageSelectionArgsForVideoOrAudioMediaCondition(String queryMimeCondition, String durationCondition, String sizeCondition) {
        return "(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?" + queryMimeCondition + " AND " + durationCondition + ") AND " +
                sizeCondition;
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
        if (mediaType == MediaType.ALL) {
            return new String[]{
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE),
                    String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO),
            };
        } else if (mediaType == MediaType.PHOTO) {
            return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)};
        } else if (mediaType == MediaType.VIDEO) {
            return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)};
        } else if (mediaType == MediaType.AUDIO) {
            return new String[]{String.valueOf(MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO)};
        } else {
            return null;
        }
    }

}
