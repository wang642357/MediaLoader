package com.jiajunhui.xapp.medialoader;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

/**
 * 作者：wangjianxiong
 * 创建时间：2021/9/1
 */
public class MediaLoader {

    public static MediaConfig with(@NonNull FragmentActivity activity) {
        return new MediaConfig(activity);
    }

    public static MediaConfig with(@NonNull Fragment fragment) {
        return new MediaConfig(fragment);
    }

}
