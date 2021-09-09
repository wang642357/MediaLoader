package com.jiajunhui.xapp.medialoader.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/9/1
 */
public class MediaFolder implements Serializable {
    private String folderCover;
    private String coverMineType;
    private long id;
    private String folderName;
    private String folderPath;
    private long folderCount;
    private List<MediaItem> items = new ArrayList<>();

    public List<MediaItem> getItems() {
        return items;
    }

    public MediaFolder() {
    }

    public MediaFolder(Long id, String name) {
        this.id = id;
        this.folderName = name;
    }

    public void setItems(List<MediaItem> items) {
        this.items = items;
    }

    public void addItem(MediaItem item) {
        items.add(item);
    }

    public String getFolderCover() {
        return folderCover;
    }

    public void setFolderCover(String cover) {
        this.folderCover = cover;
    }

    public long getFolderId() {
        return id;
    }

    public void setFolderId(long id) {
        this.id = id;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String name) {
        this.folderName = name;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getCoverMineType() {
        return coverMineType;
    }

    public void setCoverMineType(String coverMineType) {
        this.coverMineType = coverMineType;
    }

    public long getFolderCount() {
        return folderCount;
    }

    public void setFolderCount(long folderCount) {
        this.folderCount = folderCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaFolder that = (MediaFolder) o;
        return id == that.id &&
                folderName.equals(that.folderName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, folderName);
    }

    @Override
    public String toString() {
        return "MediaFolder{" +
                "cover='" + folderCover + '\'' +
                ", id=" + id +
                ", name='" + folderName + '\'' +
                ", items=" + items +
                '}';
    }
}
