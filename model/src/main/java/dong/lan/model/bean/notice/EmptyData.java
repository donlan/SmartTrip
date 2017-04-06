package dong.lan.model.bean.notice;

import dong.lan.model.base.Data;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午12:19.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class EmptyData implements Data {
    @Override
    public String toJson() {
        return "";
    }
}
