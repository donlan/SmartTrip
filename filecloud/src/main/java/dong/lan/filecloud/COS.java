package dong.lan.filecloud;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.cos.model.COSResult;
import com.tencent.cos.network.HttpResponse;

import java.io.File;

import dong.lan.filecloud.bean.BeanPath;
import dong.lan.filecloud.utils.FileUtils;
import tencent.tls.tools.MD5;


/**
 * Created by 梁桂栋 on 17-2-3 ： 下午9:07.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class COS implements COSApi {


    private static final String TAG = COS.class.getSimpleName();

    private COS() {
    }

    private static COS instance;
    private BizServer bizServer;

    public static COS instance() {
        if (instance == null)
            instance = new COS();
        return instance;
    }

    @Override
    public void init(Context context) {
        bizServer = BizServer.getInstance(context);
    }


    @Override
    public void upload(BeanPath path, COSCallBack iUploadTaskListenerCallBack) {
        File file = new File(path.filePath());
        Log.d(TAG, "upload: " + file);
        if (file.exists()) {
            long size = file.length() / 1024;
            Log.d(TAG, "upload: " + size);
            if (size > 100000) {
                iUploadTaskListenerCallBack.callback(COSCallBack.STATUS_CANCEL, new COSResult() {
                    {
                        msg = "文件已经大于100MB";
                    }

                    @Override
                    public void getResponse(HttpResponse httpResponse) {
                    }
                });
                return;
            }
            bizServer.setFileId(path.fileId());
            bizServer.setSrcPath(path.filePath());
            bizServer.setPath(path.path());
            COSPutObject cos;
            if (size >= 20000) { //分片上传
                cos = new COSPutObject(COSPutObject.PUT_TYPE.SLICE, iUploadTaskListenerCallBack);
            } else { //直接上传
                cos = new COSPutObject(COSPutObject.PUT_TYPE.SAMPLE, iUploadTaskListenerCallBack);
            }
            cos.execute(bizServer);
        } else {
            iUploadTaskListenerCallBack.callback(COSCallBack.STATUS_CANCEL, new COSResult() {
                {
                    msg = "文件不存在";
                }

                @Override
                public void getResponse(HttpResponse httpResponse) {
                }
            });
        }
    }

    @Override
    public void delete(BeanPath path, COSCallBack callBack) {
        if (!TextUtils.isEmpty(path.fileId())) {
            bizServer.setFileId(path.fileId());
            new DeleteObject(callBack).execute(bizServer);
        }
    }

    @Override
    public void download(BeanPath path, COSCallBack callBack) {
        if (!TextUtils.isEmpty(path.urlPath()) && !TextUtils.isEmpty(path.filePath())) {
            bizServer.setDownloadUrl(path.urlPath());
            bizServer.setSavePath(path.filePath());
            GetObject g = new GetObject(callBack);
            g.execute(bizServer);
        }
    }

    @Override
    public void createDir(BeanPath path, COSCallBack callBack) {
        if (path != null) {
            bizServer.setFileId(path.path());
            bizServer.setPath(path.path());
            new CreateDirSamples(callBack).execute(bizServer);
        }
    }

}
