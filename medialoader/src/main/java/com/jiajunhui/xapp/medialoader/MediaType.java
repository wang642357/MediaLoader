package com.jiajunhui.xapp.medialoader;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/8/31
 */
public enum MediaType {
    PHOTO(0), VIDEO(1), AUDIO(2), FILE(3),
    /**
     * 图片+视频
     */
    ALL(4);
    private int type;

    MediaType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
