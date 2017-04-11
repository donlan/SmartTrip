package dong.lan.filecloud;

import com.tencent.cos.model.COSResult;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午2:24.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public abstract class COSCallBack implements CallBack<COSResult> {

    public static final int STATUS_CANCEL = 0;
    public static final int STATUS_FAILED = 1;
    public static final int STATUS_DOING = 2;
    public static final int STATUS_DONE = 3;
    public abstract void callback(int status, COSResult listener);
}
