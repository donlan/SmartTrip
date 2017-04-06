package dong.lan.model.bean.travel;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import dong.lan.model.Config;
import dong.lan.model.features.ITravel;
import dong.lan.model.features.IUserInfo;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.permission.Permission;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;


/**
 * Created by 梁桂栋 on 16-10-9 ： 下午3:33.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 一次旅行的数据封装
 */

@RealmClass
public class Travel extends RealmObject implements ITravel {

    @PrimaryKey
    String id;          //旅行id(实际是云通信的群id)
    String objId;       //后台Travel表的objectId
    String tag;         //目前保存的是创建者的id
    long createTime;    //旅行创建的时间
    String name;        //旅行名称
    String imageUrl;    //旅行封面的图片URL
    long startTime;     //旅行开始时间(只算年月日)
    long endTime;       //旅行结束时间(只算年月日)
    String introduce;   //旅行简介
    int unread;         //旅行未读消息数
    String destinations;//描述旅行经过的主要地点(后台存储的是一个字符串,每个地点用空格隔开)

    @Ignore
    RealmResults<Node> nodes;

    public Travel() {

    }

    @Override
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getUnread() {
        return unread;
    }

    public void setUnread(int unread) {
        this.unread = unread;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDestinations() {
        return destinations;
    }

    public void setDestinations(String destinations) {
        this.destinations = destinations;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    @Override
    public int getPermission(IUserInfo userInfo) {
        if (userInfo == null || userInfo.getUser() == null)
            return Permission.LEVEL_OTHER;
        if (tag.equals(userInfo.getUser().getIdentifier()))
            return 0;
        return userInfo.getRole();
    }

    @Override
    public List<Node> getNodes() {
        if (nodes == null)
            nodes = Realm.getDefaultInstance().where(Node.class).equalTo("travel.id", id).findAllAsync();
        return nodes;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }


    @Override
    public String imageUrl() {
        return imageUrl;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void jumpToDetail(Context context) {
        Intent travelingIntent = new Intent();
        travelingIntent.setClassName("dong.lan.smarttrip", "TravelingActivity");
        travelingIntent.setAction("dong.lan.smarttrip.app.TravelingActivity");
        travelingIntent.putExtra(Config.TRAVEL_ID, id);
        context.startActivity(travelingIntent);
    }

    @Override
    public void delete() {
        if (isManaged()) {
            deleteFromRealm();
        } else {
            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.where(Travel.class).equalTo("id", id).findAll().deleteAllFromRealm();
                }
            });
        }
    }

    @Override
    public int unread() {
        return 0;
    }

}
