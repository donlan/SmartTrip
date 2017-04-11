package dong.lan.filecloud;

import android.content.Context;

import com.tencent.cos.COSClientConfig;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import dong.lan.filecloud.bean.BeanPath;

/**
 * Created by 梁桂栋 on 17-2-3 ： 下午9:06.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface COSApi {

    String BASE_URL = "http://lhyf-1251826459.cosgz.myqcloud.com";

    void init(Context context);

    void upload(BeanPath path, COSCallBack uploadTaskListenerCallBack);

    void delete(BeanPath path, COSCallBack callBack);

    void download(BeanPath path, COSCallBack callBack);

    void createDir(BeanPath path,COSCallBack callBack);

}
