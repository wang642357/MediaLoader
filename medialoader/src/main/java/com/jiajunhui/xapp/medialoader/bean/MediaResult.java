package com.jiajunhui.xapp.medialoader.bean;

import java.util.List;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public class MediaResult extends BaseResult {
    private List<MediaFolder> folders;

    public MediaResult() {
    }

    public MediaResult(List<MediaFolder> folders) {
        this.folders = folders;
    }

    public MediaResult(List<MediaFolder> folders, long totalSize) {
        super(totalSize);
        this.folders = folders;
    }

    public List<MediaFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<MediaFolder> folders) {
        this.folders = folders;
    }

    @Override
    public String toString() {
        return "MediaResult{" +
                "folders=" + folders +
                '}';
    }
}
