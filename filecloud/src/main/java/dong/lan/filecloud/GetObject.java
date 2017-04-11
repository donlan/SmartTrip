package dong.lan.filecloud;

import android.os.AsyncTask;

import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.task.listener.IDownloadTaskListener;

/**
 * Created by 梁桂栋 on 2016/10/13 ： 下午4:38.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public class GetObject extends AsyncTask<BizServer, Long, String> {

    protected String resultStr;
    private CallBack<COSResult> callBack;

    public GetObject(CallBack<COSResult> callBack) {
        this.callBack = callBack;
    }

    @Override
    protected String doInBackground(BizServer... params) {
        /** GetObjectRequest 请求对象 */
        GetObjectRequest getObjectRequest = new GetObjectRequest(params[0].getDownloadUrl(),
                params[0].getSavePath());
        //若是设置了防盗链则需要签名；否则，不需要
        /** 设置listener: 结果回调 */
        getObjectRequest.setListener(new IDownloadTaskListener() {
            @Override
            public void onProgress(COSRequest cosRequest, long currentSize, long totalSize) {
                long progress = (long) ((100.00 * currentSize) / totalSize);
                publishProgress(progress);
            }

            @Override
            public void onCancel(COSRequest cosRequest, COSResult cosResult) {
                callBack.callback(COSCallBack.STATUS_CANCEL, cosResult);
            }

            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                callBack.callback(COSCallBack.STATUS_DONE, cosResult);
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                callBack.callback(COSCallBack.STATUS_FAILED, cosResult);
            }
        });
        /** 发送请求：执行 */
        params[0].getCOSClient().getObject(getObjectRequest);
        return resultStr;
    }

    @Override
    protected void onPostExecute(String s) {
    }
}
