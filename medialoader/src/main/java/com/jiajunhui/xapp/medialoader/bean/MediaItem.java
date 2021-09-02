/*
 * Copyright 2016 jiajunhui
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jiajunhui.xapp.medialoader.bean;


/**
 * Created by Taurus on 16/8/28.
 */
public class MediaItem extends BaseItem {

    private long duration;
    private String mineType;
    private boolean checked;

    public MediaItem() {
    }

    public MediaItem(int id, String displayName, String path, long size, long modified, long duration, String mineType) {
        super(id, displayName, path, size, modified);
        this.duration = duration;
        this.mineType = mineType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    @Override
    public String toString() {
        return "BaseItem{" +
                "id=" + getId() +
                ", duration=" + duration +
                ", checked=" + checked +
                ", displayName='" + getDisplayName() + '\'' +
                ", path='" + getPath() + '\'' +
                ", size=" + getSize() +
                ", modified=" + getModified() +
                ", mineType=" + getMineType() +
                '}';
    }
}
