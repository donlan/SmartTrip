package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午2:12.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

@AVClassName("GatherReply")
public class AVOGatherReply extends AVObject {
    public void setGather(AVOGather notice) {
        put("gather", notice);
    }

    public AVOGather getGather() {
        try {
            return getAVObject("gather", AVOGather.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTourist(AVOTourist tourist) {
        put("tourist", tourist);
    }

    public AVOTourist getTourist() {
        try {
            return getAVObject("tourist", AVOTourist.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getStatus() {
        return getInt("status");
    }

    public void setStatus(int status) {
        put("status", status);
    }


    public long getReplyTime(){
        return getLong("replyTime");
    }

    public void setReplyTime(long replyTime){
        put("replyTime",replyTime);
    }

    public AVGeoPoint getLocation() {
        return getAVGeoPoint("location");
    }

    public void setLocation(AVGeoPoint point) {
        put("location", point);
    }

    public void setLocation(double lat, double lng) {
        put("location", new AVGeoPoint(lat, lng));
    }

}
