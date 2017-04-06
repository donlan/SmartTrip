package dong.lan.model.bean.notice;

import android.content.Context;

import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.IGatherReply;
import io.realm.RealmObject;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午10:34.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GatherReply extends RealmObject implements IGatherReply{

    public String objId;

    public Gather gather;
    public Tourist tourist;
    public long replyTime;
    public int status;
    public double latitude;
    public double longitude;


    public String getObjId() {
        return objId;
    }

    public Gather getGather() {
        return gather;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String faceUrl() {
        return tourist.getUser().getAvatarUrl();
    }

    @Override
    public String displayName() {
        return tourist.displayName();
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
