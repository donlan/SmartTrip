package dong.lan.model;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by 梁桂栋 on 17-1-10 ： 下午9:59.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class Config {

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_ABNORMAL = 1;
    public static final int STATUS_OFFLINE = 2;


    public static final int REQUEST_CODE_PICK_IMAGE = 10001;
    public static final int REQUEST_CODE_PICK_DOCUMENT = 10002;
    public static final int REQUEST_CODE_NEW_NODE = 10003;
    public static final int REQUEST_CODE_ZXING = 10004;
    public static final int RESULT_LOCATION = 10005;
    public static final int REQUEST_CODE_PICK_LOCATION = 10006;
    public static final String BAIDU_API_KEY = "c723501528e9a20b6076377ac0606c38";
    public static final String SP_IS_FIRST = "isFirst";
    public static final String SP_KEY_USER = "user";
    public static final String DEFAULT_SP = "smartTrip";
    public static final String TRAVEL_ID = "travelId";
    public static final String TRAVEL_START_TIME = "travelStartTime";
    public static final String TRAVEL_END_TIME = "travelEndTime";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String LOC_ADDRESS = "locAddress";
    public static final String APP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/lhyf";
    public static final String APP_NODES_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/lhyf/nodes";
    public static final String APP_DOCUMENTS_DIR = Environment.getExternalStorageDirectory().getAbsolutePath()+"/lhyf/documents";
    public static final String BASE_URL = "http://onjudge.applinzi.com";
    public static final String USER_QRCODE_URL = "http://onjudge.applinzi.com/user/";
    public static final String SHARE_BASE_URL = "http://onjudge.applinzi.com.SmartTrip/Travel/share.php";
    public static final String APP_DOWNLOAD_DIR = Environment.getExternalStorageDirectory() + File.separator + "lhyf" + File.separator + "documents" + File.separator;
    public static final String NODE_DAY_TIME = "nodeDayTime";
    public static final String BAR_TITTLE = "barTittle";
    public static final String QRCODE_CONTENT = "qrcodeContent";
    public static final String CITY_NAME = "cityname";
    public static final int REGION_RESULT_CODE = 100007;
    public static final int REQUEST_CODE_PICK_LEAVED_CITY = 10008;
    public static final int REQUEST_CODE_PICK_ARRIVED_CITY = 10009;
    public static final String GUIDE_LOCATION_FLAG = "guideLocationFlag";
    public static final String IDENTIFIER = "identifier";
    public static final String NOTICE_TYPE = "noticeType";

    private Config() {
    }


    public static String checkId2Key(String id) {
        if (TextUtils.isEmpty(id))
            return "";
        StringBuilder sb = new StringBuilder();
        char[] chars = id.toCharArray();
        for (int i = 0, s = chars.length; i < s; i++) {
            char c = chars[i];
            if (c == '$' || c == '#' || c == '[' || c == ']' || c == '/') {
                sb.append("_");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }


    public static class URLFactory{
        public static final String BASE_URL = "http://onjudge.applinzi.com";

        public static  String userLink(String userIdentifier){
            return BASE_URL+"/user/index.php?id="+userIdentifier;
        }

        public static  String travelLink(String travelId){
            return BASE_URL+"/travel/index.php?id="+travelId;
        }




    }
}
