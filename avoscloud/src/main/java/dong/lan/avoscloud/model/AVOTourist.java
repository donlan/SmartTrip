package dong.lan.avoscloud.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:03.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
@AVClassName("Tourist")
public class AVOTourist extends AVObject {


    public AVOUser getOwner() {
        return super.getAVUser("owner", AVOUser.class);
    }

    public void setOwner(AVOUser user) {
        super.put("owner", user);
    }


    public AVOTravel getTravel() {
        try {
            return getAVObject("travel", AVOTravel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setTravel(AVOTravel travel) {
        super.put("travel", true);
    }

    public int getRole() {
        return getInt("role");
    }

    public void setRole(int role) {
        put("role", role);
    }

    public int getStatus() {
        return getInt("status");
    }

    public void setStatus(int status) {
        put("status", status);
    }


}
