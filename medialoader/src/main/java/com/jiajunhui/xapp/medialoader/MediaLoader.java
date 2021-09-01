package com.jiajunhui.xapp.medialoader;

import androidx.fragment.app.FragmentActivity;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/9/1
 */
public class MediaLoader {

    public static MediaConfig with(FragmentActivity activity) {
        return new MediaConfig(activity);
    }

}
