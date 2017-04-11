package dong.lan.filecloud;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.DeleteObjectRequest;
import com.tencent.cos.task.listener.ICmdTaskListener;

/**
 * Created by 梁桂栋 on 2016/10/13 ： 下午7:13.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public class DeleteObject extends AsyncTask<BizServer,Void,String>{
    protected String resultStr;
    private COSCallBack cosCallBack;
    public DeleteObject(COSCallBack  callBack){
        cosCallBack = callBack;
    }
    @Override
    protected String doInBackground(BizServer... params) {
        /** DeleteObjectRequest 请求对象 */
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
        /** 设置Bucket */
        deleteObjectRequest.setBucket(params[0].getBucket());
        /** 设置cosPath :远程路径*/
        deleteObjectRequest.setCosPath(params[0].getFileId());
        /** 设置sign: 签名，此处使用单次签名 */
        deleteObjectRequest.setSign(params[0].getOnceSign());
        /** 设置listener: 结果回调 */
        deleteObjectRequest.setListener(new ICmdTaskListener() {
            @Override
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                cosCallBack.callback(COSCallBack.STATUS_DONE,cosResult);
            }

            @Override
            public void onFailed(COSRequest cosRequest, COSResult cosResult) {
                cosCallBack.callback(COSCallBack.STATUS_FAILED,cosResult);
            }
        });
        /** 发送请求：执行 */
        params[0].getCOSClient().deleteObject(deleteObjectRequest);
        return resultStr;
    }
}
