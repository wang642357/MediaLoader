package com.jiajunhui.xapp.medialoaderdemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.jiajunhui.xapp.medialoader.MediaLoader;
import com.jiajunhui.xapp.medialoader.MediaStoreLoader;
import com.jiajunhui.xapp.medialoader.MediaType;
import com.jiajunhui.xapp.medialoader.bean.FileResult;
import com.jiajunhui.xapp.medialoader.bean.FileType;
import com.jiajunhui.xapp.medialoader.bean.MediaFolder;
import com.jiajunhui.xapp.medialoader.bean.MediaItem;
import com.jiajunhui.xapp.medialoader.bean.MediaResult;
import com.jiajunhui.xapp.medialoader.callback.OnFileLoaderCallBack;
import com.jiajunhui.xapp.medialoader.callback.OnMediaFileLoaderCallBack;

import java.util.ArrayList;
import java.util.List;

public class TestFragment extends Fragment {

    private final String TAG = "TestFragment";

    private TextView tv_photo_info;
    private TextView tv_video_info;
    private TextView tv_audio_info;
    private TextView tv_file_info;
    private TextView tv_traversal_info;

    private long start;

    private View mRootView;

    public static TestFragment createInstance(FragmentManager fragmentManager){
        return new TestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_test, null);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_photo_info = (TextView) mRootView.findViewById(R.id.tv_photo_info);
        tv_video_info = (TextView) mRootView.findViewById(R.id.tv_video_info);
        tv_audio_info = (TextView) mRootView.findViewById(R.id.tv_audio_info);
        tv_file_info = (TextView) mRootView.findViewById(R.id.tv_file_info);
        tv_traversal_info = (TextView) mRootView.findViewById(R.id.tv_traversal_info);

        start = System.currentTimeMillis();
        startLoad();
    }

    private void startLoad() {
        loadPhotos();
        loadAudios();
        loadVideos();
        final StringBuilder mInfos = new StringBuilder();
        MediaStoreLoader.getLoader().loadFiles(this, new OnFileLoaderCallBack(FileType.DOC) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("doc file : " + result.getItems().size()).append("\n");
            }
        });

        MediaStoreLoader.getLoader().loadFiles(this, new OnFileLoaderCallBack(FileType.ZIP) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("zip file : " + result.getItems().size()).append("\n");
            }
        });

        MediaStoreLoader.getLoader().loadFiles(this, new OnFileLoaderCallBack(FileType.APK) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("apk file : " + result.getItems().size()).append("\n");
                mInfos.append("consume time : " + (System.currentTimeMillis() - start)).append("ms");
                tv_file_info.setText(mInfos.toString());
            }
        });
    }

    private void loadPhotos() {
        MediaLoader.with(this).setOnMediaFileLoaderCallBack(new OnMediaFileLoaderCallBack() {
            @Override
            public void onResult(MediaResult result) {
                List<MediaItem> list = new ArrayList<>();
                for (MediaFolder folder : result.getFolders()) {
                    list.addAll(folder.getItems());
                }
                tv_photo_info.setText("图片: " + list.size() + " 张");
            }
        }).setPageIndex(0).setPageSize(30).setVideoMaxSecond(10000)
                .setMediaType(MediaType.PHOTO).load();
    }

    private void loadAudios() {
        MediaLoader.with(this).setOnMediaFileLoaderCallBack(new OnMediaFileLoaderCallBack() {
            @Override
            public void onResult(MediaResult result) {
                List<MediaItem> list = new ArrayList<>();
                for (MediaFolder folder : result.getFolders()) {
                    list.addAll(folder.getItems());
                }
                tv_audio_info.setText("音乐: " + list.size() + " 个");
            }
        }).setPageIndex(0).setPageSize(30).setVideoMaxSecond(10000)
                .setMediaType(MediaType.AUDIO).load();
    }

    private void loadVideos() {
        MediaLoader.with(this).setOnMediaFileLoaderCallBack(new OnMediaFileLoaderCallBack() {
            @Override
            public void onResult(MediaResult result) {
                List<MediaItem> list = new ArrayList<>();
                for (MediaFolder folder : result.getFolders()) {
                    list.addAll(folder.getItems());
                }
                tv_photo_info.setText("视频: " + list.size() + " 张");
            }
        }).setPageIndex(0).setPageSize(30).setVideoMaxSecond(10000)
                .setMediaType(MediaType.VIDEO).load();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
