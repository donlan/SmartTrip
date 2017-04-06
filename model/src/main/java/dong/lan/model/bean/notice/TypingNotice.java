package dong.lan.model.bean.notice;

import dong.lan.model.base.Data;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午12:17.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TypingNotice implements Data{
    String desc = "对方正在输入中...";

    @Override
    public String toJson() {
        return desc;
    }
}
