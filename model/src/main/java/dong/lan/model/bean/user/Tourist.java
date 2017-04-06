package dong.lan.model.bean.user;


import android.support.annotation.NonNull;

import dong.lan.model.features.IUserInfo;
import dong.lan.model.features.ItemTextDisplay;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午8:06.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 游客:一个用户可以有多个游客身份
 */
@RealmClass
public class Tourist extends RealmObject implements IUserInfo,ItemTextDisplay {


    private String objId;   //后台表id(保存本地,方便操作)
    private User user;      //游客对用的用户信息
    private int status;     //用户当前状态
    private int role;       //游客在旅行中的角色


    public Tourist() {
    }

    public Tourist(User user) {
        this.user = user;
        status = user.getStatus();
        role = user.getRole();
    }


    @Override
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public String getIdentifier() {
        return user.getIdentifier();
    }

    @Override
    public String getAvatarUrl() {
        return user.getAvatarUrl();
    }

    @Override
    public String getPinYin() {
        return user.getPinYin();
    }

    @Override
    public String display() {
        return user.display();
    }


    @Override
    public int getSex() {
        return getUser().getSex();
    }

    @Override
    public String displayName() {
        return getUser().displayName();
    }

    @Override
    public int compareTo(@NonNull IUserInfo o) {
        return displayName().compareTo(o.displayName());
    }
}
