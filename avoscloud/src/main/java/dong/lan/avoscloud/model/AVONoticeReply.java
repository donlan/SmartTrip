package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午2:12.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

@AVClassName("NoticeReply")
public class AVONoticeReply extends AVObject {
    public void setNotice(AVONotice notice) {
        put("notice", notice);
    }

    public AVONotice getNotice() {
        try {
            return getAVObject("notice", AVONotice.class);
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

    public int getStatus(){
        return getInt("status");
    }

    public void setStatus(int status){
        put("status",status);
    }


    public long getReplyTime(){
        return getLong("replyTime");
    }

    public void setReplyTime(long replyTime){
        put("replyTime",replyTime);
    }
}
