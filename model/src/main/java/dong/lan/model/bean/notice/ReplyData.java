package dong.lan.model.bean.notice;

import com.alibaba.fastjson.JSON;

import dong.lan.model.base.Data;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午10:56.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class ReplyData implements Data {
    public long time;
    public String identifier;
    public String travelId;


    public ReplyData() {
        this.time = System.currentTimeMillis();
    }

    public ReplyData(String identifier, String travelId) {
        this.time = System.currentTimeMillis();
        this.identifier = identifier;
        this.travelId = travelId;
    }

    @Override
    public String toJson() {
        return JSON.toJSONString(this);
    }
}
