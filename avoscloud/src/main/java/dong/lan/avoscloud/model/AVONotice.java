package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午1:51.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
@AVClassName("Notice")
public class AVONotice extends AVObject {

    public void setTravel(AVOTravel travel) {
        put("travel", travel);
    }

    public AVOTravel getTravel() {
        try {
            return getAVObject("travel", AVOTravel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public AVOUser getCreator() {
        return super.getAVUser("creator",AVOUser.class);
    }

    public void setCreator(AVUser user) {
        super.put("creator", user);
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


    public long getTime() {
        return getLong("time");
    }

    public void setTime(long time) {
        put("time", time);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

}
