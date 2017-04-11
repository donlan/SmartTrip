package dong.lan.filecloud;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.CreateDirRequest;
import com.tencent.cos.model.CreateDirResult;
import com.tencent.cos.task.listener.ICmdTaskListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 梁桂栋 on 2016/10/13 ： 下午11:02.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public class CreateDirSamples extends AsyncTask<BizServer,Void,String>{
    private static final String TAG = CreateDirSamples.class.getSimpleName();
    private COSCallBack cosCallBack;
    public CreateDirSamples(COSCallBack callBack){
        cosCallBack = callBack;
    }
    @Override
    protected String doInBackground(BizServer... bizServers) {
        BizServer bizServer = bizServers[0];
        String cgi = "http://onjudge.applinzi.com/SmartTrip/Configration.php?" + "bucket=" + bizServer.bucket + "&path="+bizServer.getPath();
        try {
            URL url = new URL(cgi);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            if (line == null) return null;
            JSONObject json = new JSONObject(line);
            Log.d(TAG, "doInBackground: "+line);
            if (json.has("code")) {
                int code = json.getInt("code");
                if(code == 0)
                    cosCallBack.callback(COSCallBack.STATUS_DONE,null);
                else
                    cosCallBack.callback(COSCallBack.STATUS_FAILED,null);
                return json.getString("msg");
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            cosCallBack.callback(COSCallBack.STATUS_FAILED,null);
        }
        cosCallBack.callback(COSCallBack.STATUS_FAILED,null);
        return "";
    }

}
