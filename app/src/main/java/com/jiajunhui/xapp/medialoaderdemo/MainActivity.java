package com.jiajunhui.xapp.medialoaderdemo;

import android.Manifest;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.jiajunhui.xapp.medialoader.filter.PhotoFilter;
import com.jiajunhui.xapp.medialoader.inter.OnRecursionListener;
import com.jiajunhui.xapp.medialoader.utils.TraversalSearchLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity {

    private TextView tv_photo_info;
    private TextView tv_video_info;
    private TextView tv_audio_info;
    private TextView tv_file_info;
    private TextView tv_traversal_info;

    private long start;
    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_photo_info = (TextView) findViewById(R.id.tv_photo_info);
        tv_video_info = (TextView) findViewById(R.id.tv_video_info);
        tv_audio_info = (TextView) findViewById(R.id.tv_audio_info);
        tv_file_info = (TextView) findViewById(R.id.tv_file_info);
        tv_traversal_info = (TextView) findViewById(R.id.tv_traversal_info);

        findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(intent);
            }
        });

        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .request();

    }

    private void recursionLoad() {
        TraversalSearchLoader.LoadParams params = new TraversalSearchLoader.LoadParams();
        //需要遍历的根目录
        params.root = Environment.getExternalStorageDirectory();
        //过滤器
        params.fileFilter = new PhotoFilter();
        mTask = TraversalSearchLoader.loadAsync(params, new OnRecursionListener() {
            @Override
            public void onStart() {
                System.out.println("load_log : start---->");
            }

            @Override
            public void onItemAdd(File file, int counter) {
                System.out.println("load_log : onItemAdd : " + file.getAbsolutePath());
                tv_traversal_info.setText("number : " + counter + " : " + file.getAbsolutePath());
            }

            @Override
            public void onFinish(List<File> files) {
                System.out.println("load_log : finish ***** size = " + files.size());
                tv_traversal_info.setText("number : " + files.size());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void onPermissionSuccess(){
        start = System.currentTimeMillis();
        recursionLoad();
        startLoad();
    }

    private void startLoad() {
        loadPhotos();
        loadAudios();
        loadVideos();
        final StringBuilder mInfos = new StringBuilder();
        MediaStoreLoader.getLoader().loadFiles(MainActivity.this, new OnFileLoaderCallBack(FileType.DOC) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("doc file : " + result.getItems().size()).append("\n");
            }
        });

        MediaStoreLoader.getLoader().loadFiles(MainActivity.this, new OnFileLoaderCallBack(FileType.ZIP) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("zip file : " + result.getItems().size()).append("\n");
            }
        });

        MediaStoreLoader.getLoader().loadFiles(MainActivity.this, new OnFileLoaderCallBack(FileType.APK) {
            @Override
            public void onResult(FileResult result) {
                mInfos.append("apk file : " + result.getItems().size()).append("\n");
                mInfos.append("consume time : " + (System.currentTimeMillis() - start)).append("ms").append("\n");
                tv_file_info.setText(mInfos.toString());
            }
        });
        MediaLoader.with(this).setOnMediaFileLoaderCallBack(new OnMediaFileLoaderCallBack() {
            @Override
            public void onResult(MediaResult result) {
                for (MediaFolder folder : result.getFolders()) {
                    Log.e("----", folder + "");
                }
                mInfos.append("media file : " + result.getTotalSize()).append("\n");
                mInfos.append("consume time : " + (System.currentTimeMillis() - start)).append("ms").append("\n");
                tv_file_info.setText(mInfos.toString());
            }
        }).setPageIndex(0).setPageSize(30).setVideoMaxSecond(10000)
                .setMediaType(MediaType.ALL).load();
    }

    @PermissionFail(requestCode = 100)
    public void onPermissionFail(){
        Toast.makeText(this, "permission deny", Toast.LENGTH_SHORT).show();
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
                tv_video_info.setText("视频: " + list.size() + " 张");
            }
        }).setPageIndex(0).setPageSize(30).setVideoMaxSecond(10000)
                .setMediaType(MediaType.VIDEO).load();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTask!=null){
            mTask.cancel(true);
        }
    }
}
