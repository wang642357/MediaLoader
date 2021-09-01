package com.jiajunhui.xapp.medialoader.bean;

import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public class MediaResult extends BaseResult {
    private List<MediaFolder> folders;
    private List<MediaItem> items;

    public MediaResult() {
    }

    public MediaResult(List<MediaFolder> folders, List<MediaItem> items) {
        this.folders = folders;
        this.items = items;
    }

    public MediaResult(List<MediaFolder> folders, List<MediaItem> items, long totalSize) {
        super(totalSize);
        this.folders = folders;
        this.items = items;
    }

    public List<MediaFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<MediaFolder> folders) {
        this.folders = folders;
    }

    public List<MediaItem> getItems() {
        return items;
    }

    public void setItems(List<MediaItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "MediaResult{" +
                "folders=" + folders +
                ", items=" + items +
                '}';
    }
}
