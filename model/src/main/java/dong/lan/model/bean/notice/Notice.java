package dong.lan.model.bean.notice;


import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import dong.lan.model.Config;
import dong.lan.model.base.Data;
import dong.lan.model.base.GsonHelper;
import dong.lan.model.features.INotice;
import dong.lan.model.utils.TimeUtil;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午1:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description:
 */

public class Notice extends RealmObject implements Data, NoticeShow ,INotice{

    public static final int SCOPE_ALL = 0;      //集合范围时所有人
    public static final int SCOPE_ABNORMAL = 1; //仅集合状态异常的用户
    public static final int SCOPE_WARNING = 2;  //仅集合处于预警状态的用户
    public static final int SCOPE_NEAR = 3;     //仅集合附近的用户


    @PrimaryKey
    public String id;           //通知的id
    public String objId;        //后台Notice表的objectId
    public long createTime;     //创建时间
    public long time;           //通知生效时间(好像没用到)
    public String creatorId;    //创建用户的id
    public String travelId;     //旅行的id,可以知道时哪一个旅行的通知
    public String content;      //通知内容


    @Override
    public String getObjId() {
        return objId;
    }

    @Override
    public String getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getTravelId() {
        return travelId;
    }

    public void setTravelId(String travelId) {
        this.travelId = travelId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String getShowTittle() {
        return "通知";
    }

    public String getShowContent() {
        return content;
    }

    @Override
    public void jump(Context context) {
        ARouter.getInstance().build("/notice/noticeInfo")
                .withString("id",id)
                .withString(Config.TRAVEL_ID,travelId)
                .withString(Config.IDENTIFIER,creatorId)
                .navigation();
    }

    @Override
    public String showExtras() {
        return "通知时间: " + TimeUtil.getTime(createTime, "MM.dd HH:mm");
    }

    @Override
    public int type() {
        return NoticeShow.TYPE_NOTICE;
    }

    @Override
    public void delete() {
        if (isManaged())
            deleteFromRealm();
        else {
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Notice.class)
                            .equalTo("id", id)
                            .findAll().deleteAllFromRealm();

                }
            });
        }
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toJson() {
        return GsonHelper.getInstance().toJson(this);
    }
}
