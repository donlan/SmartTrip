package dong.lan.model.bean.notice;


import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

import dong.lan.model.Config;
import dong.lan.model.base.Data;
import dong.lan.model.base.GsonHelper;
import dong.lan.model.features.IGather;
import dong.lan.model.utils.TimeUtil;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午1:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 一个集合信息
 */

public class Gather extends RealmObject implements Data, NoticeShow,IGather {

    public static final int SCOPE_ALL = 0;      //集合范围时所有人
    public static final int SCOPE_ABNORMAL = 1; //仅集合状态异常的用户
    public static final int SCOPE_WARNING = 2;  //仅集合处于预警状态的用户
    public static final int SCOPE_NEAR = 3;     //仅集合附近的用户

    @PrimaryKey
    public String id;           //集合id
    public String objId;        //对应后台Gather表的id
    public long createTime;     //创建时间
    public String creatorId;    //创建者的id
    public String travelId;     //旅行id
    public String address;      //集合地点
    public double latitude;     //集合地点的维度
    public double longitude;    //集合地点的精度
    public long time;           //集合时间
    public String content;      //集合内容


    public String getObjId() {
        return objId;
    }

    public String getId() {
        return id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getTravelId() {
        return travelId;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }





    public Gather() {
        createTime = System.currentTimeMillis();
    }

    @Override
    public String toJson() {
        return GsonHelper.getInstance().toJson(this);
    }

    @Override
    public String getShowTittle() {
        return "集合  " + TimeUtil.getTime(time, "MM.dd HH:mm");
    }

    @Override
    public String getShowContent() {
        return "集合地点:" + address + "\n\n" + content;
    }

    @Override
    public void jump(Context context) {
        ARouter.getInstance().build("/notice/gatherInfo")
                .withString("id",id)
                .withString(Config.TRAVEL_ID,travelId)
                .withString(Config.IDENTIFIER,creatorId)
                .navigation();
    }

    @Override
    public String showExtras() {
        return "集合时间: " + TimeUtil.getTime(time, "MM.dd HH:mm");
    }

    @Override
    public int type() {
        return NoticeShow.TYPE_GATHER;
    }

    @Override
    public void delete() {
        if(isManaged())
            deleteFromRealm();
        else{
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Gather.class)
                            .equalTo("id",id)
                            .findAll().deleteAllFromRealm();

                }
            });
        }
    }
}
