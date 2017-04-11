package dong.lan.smarttrip.model.wilddog;

import com.wilddog.client.SyncReference;

import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.common.WildDog;
import dong.lan.model.features.IUser;

/**
 * Created by 梁桂栋 on 17-3-8 ： 下午4:17.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WDTourist implements IBaseWildDog {

    String tag;
    String id;
    String name;
    String faceUrl;
    double latitude = 0;
    double longitude = 0;
    int status = 0;


    public WDTourist(IUser user,String tag) {
        this.id = user.getIdentifier();
        this.name = user.getUsername();
        this.faceUrl = user.getAvatarUrl();
        this.tag = tag;
    }


    @Override
    public void save(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier() +
                "/travels/" + Config.checkId2Key(tag) + "/tourist/"
                + id).setValue(this,callback);
    }

    @Override
    public void delete(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier() +
                "/travels/" + Config.checkId2Key(tag) + "/tourist/"
                + id).removeValue(callback);
    }
}
