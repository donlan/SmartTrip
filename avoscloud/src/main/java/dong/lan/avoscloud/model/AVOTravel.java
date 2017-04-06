package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-3-31 ： 下午11:49.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 后台保存的旅行数据
 */
@AVClassName("Travel")
public class AVOTravel extends AVObject {

    public AVUser getCreator() {
        return super.getAVUser("creator");
    }

    public void setCreator(AVUser user) {
        super.put("creator", user);
    }

    public AVFile getRawImage() {
        return super.getAVFile("imageFile");
    }

    public void setRawImage(AVFile file) {
        super.put("imageFile", file);
    }

    public String getTag() {
        return getString("tag");
    }

    public void setTag(String tag) {
        put("tag", tag);
    }

    public String getId() {
        return getString("id");
    }

    public void setId(String id) {
        put("id", id);
    }

    public long getCreateTime() {
        return getLong("createTime");
    }

    public void setCreateTime(long createTime) {
        put("createTime", createTime);
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public void setImageUrl(String imageUrl) {
        put("imageUrl", imageUrl);
    }

    public long getStartTime() {
        return getLong("startTime");
    }

    public void setStartTime(long startTime) {
        put("startTime", startTime);
    }

    public long getEndTime() {
        return getLong("endTime");
    }

    public void setEndTime(long endTime) {
        put("endTime", endTime);
    }

    public String getIntroduce() {
        return getString("introduce");
    }

    public void setIntroduce(String introduce) {
        put("introduce", introduce);
    }

    public int getUnread() {
        return getInt("unread");
    }

    public void setUnread(int unread) {
        put("unread", unread);
    }

    public String getDestinations() {
        return getString("destinations");
    }

    public void setDestinations(String destinations) {
        put("destinations", destinations);
    }


    public AVRelation getTourists() {
        return getRelation("tourists");
    }

    public void removeTourist(AVOTourist tourist) {
        getTourists().remove(tourist);
        this.saveInBackground();
    }

    public void addTourist(AVOTourist tourist) {
        getTourists().add(tourist);
        this.saveInBackground();
    }

}
