package dong.lan.avoscloud;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 梁桂栋 on 17-2-22 ： 下午12:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Secure {
    public static String MD5(String origin){
        if (origin == null)
            return "";
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] ori = origin.getBytes();
            digest.update(ori);
            byte[] enc = digest.digest();
            return Base64.encodeToString(enc, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
