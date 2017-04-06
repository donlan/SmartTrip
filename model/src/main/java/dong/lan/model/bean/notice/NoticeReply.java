package dong.lan.model.bean.notice;

import android.content.Context;

import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.INoticeReply;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午10:34.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeReply extends RealmObject implements INoticeReply{


    @PrimaryKey
    public String objId;
    public Notice notice;
    public Tourist tourist;
    public long replyTime;
    public int status;

    @Override
    public String getObjId() {
        return objId;
    }

    public Notice getNotice() {
        return notice;
    }

    public Tourist getTourist() {
        return tourist;
    }

    public long getReplyTime() {
        return replyTime;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String faceUrl() {
        return getTourist().getAvatarUrl();
    }

    @Override
    public String displayName() {
        return getTourist().displayName();
    }

    @Override
    public String tips() {
        return "";
    }

    @Override
    public void call(Context context) {

    }

    @Override
    public void sms(Context context) {

    }
}
