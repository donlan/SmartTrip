package dong.lan.smarttrip.notice;

import com.alibaba.fastjson.JSON;

import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeShow;
import dong.lan.smarttrip.model.im.CustomMessage;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午2:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeFactory {

    public static NoticeShow parseNotice(int code, String json){
        switch (code){
            case CustomMessage.Action.ACTION_GATHER:
                return JSON.parseObject(json,Gather.class);
            case CustomMessage.Action.ACTION_NOTICE:
                return JSON.parseObject(json, Notice.class);
            default:
                return null;
        }
    }
}
