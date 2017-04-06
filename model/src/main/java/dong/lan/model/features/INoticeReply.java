package dong.lan.model.features;

import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.user.Tourist;

/**
 * Created by 梁桂栋 on 17-4-6 ： 下午3:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface INoticeReply extends IBaseReply{

    String getObjId();

    INotice getNotice();

    IUserInfo getTourist();

    long getReplyTime();

    int getStatus();
}
