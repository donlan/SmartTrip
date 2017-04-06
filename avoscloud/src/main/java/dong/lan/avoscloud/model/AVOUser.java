package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVUser;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:03.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 后台保存的用户数据
 */

public class AVOUser extends AVUser {

    public String getIdentifier() {
        return getString("identifier");
    }

    public void setIdentifier(String identifier) {
        put("identifier", identifier);
    }

    public String getFaceUrl() {
        return getString("avatar");
    }

    public void setFaceUrl(String faceUrl) {
        put("avatar", faceUrl);
    }

    public String getNickName() {
        return getString("nickname");
    }

    public void setNickName(String nickName) {
        put("nickname", nickName);
    }

    public String getMobile() {
        return getString("mobile");
    }

    public void setMobile(String mobile) {
        put("mobile", mobile);
    }

}
