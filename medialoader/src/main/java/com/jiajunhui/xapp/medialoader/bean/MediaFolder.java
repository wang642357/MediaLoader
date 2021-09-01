package com.jiajunhui.xapp.medialoader.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/9/1
 */
public class MediaFolder extends BaseFolder {
    private String cover;

    private List<MediaItem> items = new ArrayList<>();

    public List<MediaItem> getItems() {
        return items;
    }

    public void setItems(List<MediaItem> items) {
        this.items = items;
    }

    public void addItem(MediaItem item) {
        items.add(item);
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaFolder)) return false;

        MediaFolder directory = (MediaFolder) o;

        boolean hasId = !TextUtils.isEmpty(getId());
        boolean otherHasId = !TextUtils.isEmpty(directory.getId());

        if (hasId && otherHasId) {
            if (!TextUtils.equals(getId(), directory.getId())) {
                return false;
            }

            return TextUtils.equals(getName(), directory.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(getId())) {
            if (TextUtils.isEmpty(getName())) {
                return 0;
            }

            return getName().hashCode();
        }

        int result = getId().hashCode();

        if (TextUtils.isEmpty(getName())) {
            return result;
        }

        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "MediaFolder{" +
                "cover='" + cover + '\'' +
                ", items=" + items +
                '}';
    }
}
