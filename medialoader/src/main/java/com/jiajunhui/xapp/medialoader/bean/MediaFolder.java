package com.jiajunhui.xapp.medialoader.bean;

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
    public String toString() {
        return "MediaFolder{" +
                "id='" + getId() + '\'' +
                "cover='" + cover + '\'' +
                ", items=" + items +
                '}';
    }

}
