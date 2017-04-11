package dong.lan.filecloud;

import android.os.AsyncTask;
import android.util.Log;

import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tencent.cos.utils.FileUtils;

import java.io.File;
import java.util.List;

/**
 * Created by 梁桂栋 on 2016/10/13 ： 下午12:55.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public class COSPutObject extends AsyncTask<BizServer,Long,String>{
    private static final String TAG = COSPutObject.class.getSimpleName();
    private IUploadTaskListener iUploadListenerHandler;
    private String resultStr;
    private CallBack<COSResult> callBack;
    private PUT_TYPE put_type;
    public  enum PUT_TYPE{
        SAMPLE(1,"简单文件上传模式"),
        SLICE(2,"分片文件上传模式"),
        LIST(3,"一键文件上传模式");
        private int id;
        private String desc;
        PUT_TYPE(int id, String desc){
            this.id = id;
            this.desc = desc;
        }
        public String getDesc(){
            return desc;
        }
    }
    public COSPutObject(PUT_TYPE put_type ,CallBack<COSResult> callBack){
        iUploadListenerHandler = new IUploadListenerHandler();
        this.callBack = callBack;
        this.put_type = put_type;
    }
    @Override
    protected String doInBackground(BizServer... bizServers) {
        switch (put_type){
            case SAMPLE:
                putObjcetForSmallFile(bizServers[0]);
                break;
            case SLICE:
                putObjcetForLargeFile(bizServers[0]);
                break;
            case LIST:
                putObjcetForListFile(bizServers[0]);
                break;
        }
        return resultStr;
    }

    @Override
    protected void onPostExecute(String s) {
    }

    @Override
    protected void onProgressUpdate(Long... values) {
    }


    /**
     * 简单文件上传
     */
    protected void putObjcetForSmallFile(BizServer bizServer){
        /** PutObjectRequest 请求对象 */
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        /** 设置Bucket */
        putObjectRequest.setBucket(bizServer.getBucket());
        /** 设置cosPath :远程路径*/
        putObjectRequest.setCosPath(bizServer.getFileId());
        /** 设置srcPath: 本地文件的路径 */
        putObjectRequest.setSrcPath(bizServer.getSrcPath());
        /** 设置 insertOnly: 是否上传覆盖同名文件*/
        putObjectRequest.setInsertOnly("1");
        /** 设置sign: 签名，此处使用多次签名 */
        putObjectRequest.setSign(bizServer.getOnceSign());

        /** 设置sha: 是否上传文件时带上sha，一般不需要带*/
        if(bizServer.getCheckSha()){
            putObjectRequest.checkSha();
            putObjectRequest.setSha(putObjectRequest.getsha());
        }
        /** 设置listener: 结果回调 */
        putObjectRequest.setListener(iUploadListenerHandler);
        /** 发送请求：执行 */
        bizServer.getCOSClient().putObject(putObjectRequest);
    }
    /**
     * 分片上传 一般文件》20M,使用分片上传;
     */
    protected void putObjcetForLargeFile(BizServer bizServer){
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(bizServer.getBucket());
        putObjectRequest.setCosPath(bizServer.getFileId());

        putObjectRequest.setSrcPath(bizServer.getSrcPath());
        /** 设置sliceFlag: 是否开启分片上传 */
        putObjectRequest.setSliceFlag(true);
        /** 设置slice_size: 若使用分片上传，设置分片的大小 */
        putObjectRequest.setSlice_size(1024*1024);

        putObjectRequest.setSign(bizServer.getSign());

        putObjectRequest.setInsertOnly("1");

        if(bizServer.getCheckSha()){
            putObjectRequest.checkSha();
            putObjectRequest.setSha(putObjectRequest.getsha());
        }

        putObjectRequest.setListener(iUploadListenerHandler);

        bizServer.getCOSClient().putObject(putObjectRequest);

    }
    /**
     * 一键上传
     */
    protected void putObjcetForListFile(BizServer bizServer){
        List<String> listPath = bizServer.getListPath();
        PutObjectRequest putObjectRequest = null;
        for(int i = 0;i < listPath.size(); i++){
            putObjectRequest = new PutObjectRequest();
            putObjectRequest.setBucket(bizServer.getBucket());
            putObjectRequest.setCosPath(bizServer.getFileId()+ File.separator + FileUtils.getFileName(listPath.get(i)));
            putObjectRequest.setSign(bizServer.getSign());
            putObjectRequest.setListener(iUploadListenerHandler);

            putObjectRequest.setSrcPath(listPath.get(i));
            bizServer.getCOSClient().putObject(putObjectRequest);
        }
    }


    protected class IUploadListenerHandler implements IUploadTaskListener{

        @Override
        public void onProgress(COSRequest cosRequest, long currentSize, long totalSize) {
            long progress = ((long) ((100.00 * currentSize) / totalSize));
            Log.d(TAG, "onProgress: "+progress);
            publishProgress(progress);
        }

        @Override
        public void onCancel(COSRequest cosRequest, COSResult cosResult) {
            callBack.callback(COSCallBack.STATUS_CANCEL,cosResult);
        }

        @Override
        public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
            PutObjectResult result = (PutObjectResult) cosResult;
            resultStr = (" 上传结果： ret=" + result.code + "; msg =" + result.msg + "\n") +
                    result.access_url + "\n" +
                    result.resource_path + "\n" +
                    result.url
                    +":"+cosRequest.getDownloadUrl()+","+cosRequest;
            Log.d(TAG, "onSuccess: "+resultStr);
            callBack.callback(COSCallBack.STATUS_DONE,cosResult);

        }

        @Override
        public void onFailed(COSRequest cosRequest, COSResult cosResult) {

            resultStr = "上传出错： ret =" +cosResult.code + "; msg =" + cosResult.msg
                    +":"+cosRequest.getDownloadUrl()+","+cosRequest;
            Log.d(TAG, "onFailed: "+resultStr);
            callBack.callback(COSCallBack.STATUS_FAILED,cosResult);
        }
    }
}
