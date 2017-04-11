package dong.lan.model.bean.user;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import dong.lan.model.Config;
import dong.lan.model.features.IUser;
import dong.lan.model.features.IUserInfo;
import dong.lan.model.features.ItemTextDisplay;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 16-10-9 ： 下午3:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 一个用户对应一个User
 */
@RealmClass
public class User extends RealmObject implements IUserInfo ,ItemTextDisplay {

    public static final int TYPE_FRIEND = 1;
    public static final int TYPE_GROUP_MEMBER = 2;
    public static final int TYPE_GROUP_FRIEND = 4;
    public static final int TYPE_SELF = 8;

    @PrimaryKey
    private String identifier;          //对应云信SDK的用户id
    private String objId;
    private String username;            //用户名
    private String phone;               //手机号码
    private int sex;                    //性别
    private String avatarUrl;           //头像
    private String nickname;            //昵称
    private String remark;              //备注
    private int role;                   //角色,用于后期做权限处理
    private int type;                   //用户类型,只在本地保存(只是方便我开发而已)


    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User() {
        type = TYPE_FRIEND;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    @Override
    public String displayName() {
        if (TextUtils.isEmpty(remark)) {
            if (TextUtils.isEmpty(nickname)) {
                if (TextUtils.isEmpty(username)) {
                    return identifier;
                }
                return username;
            }
            return nickname;
        }
        return remark;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }


    public String getPinYin() {

        return Pinyin.toPinyin(displayName(), "");
    }


    @Override
    public int compareTo(@NonNull IUserInfo o) {
        if (o == this)
            return 0;
        String cur = Pinyin.toPinyin(displayName(), "");
        String other = Pinyin.toPinyin(o.displayName(), "");

        return cur.compareTo(other);
    }

    @Override
    public String display() {
        String n = Pinyin.toPinyin(displayName(), "");
        return TextUtils.isEmpty(n) ? "#" : n.substring(0, 1);
    }


    @Override
    public IUser getUser() {
        return this;
    }

    @Override
    public int getRole() {
        return role;
    }

    @Override
    public int getStatus() {
        return Config.STATUS_NORMAL;
    }


    @Override
    public void call(Context context) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.putExtra("tel", getPhone());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(intent);
    }

    @Override
    public void sms(Context context) {

    }
}
