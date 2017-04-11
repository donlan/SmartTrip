package dong.lan.filecloud.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.cos.task.storage.StorageHelper;

/**
 * Created by 梁桂栋 on 17-2-9 ： 下午6:18.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class FileUtils {
    private FileUtils() {
    }

    public static String createFileId(String filename){
        if (TextUtils.isEmpty(filename))
            return null;
        int i = filename.lastIndexOf(".");
        if (i == -1)
            return null;
        return filename;
    }

    public static String PathToFileName(String path){
        if(TextUtils.isEmpty(path))
            return "";
        int i = path.lastIndexOf("/");
        if(i == -1 )
            return path;
        return path.substring(i+1);
    }

    public static String getFileType(String path){
        if(TextUtils.isEmpty(path))
            return "";
        int j = path.lastIndexOf(".");
        if( j == -1)
            return path;
        return path.substring(j+1);
    }

    /**
     * 根据Uri获取文件绝对路径
     * @param context 上下文
     * @param uri 带获取路径的Uri
     * @return 文件的绝对路径
     */
    public static String UriToPath(Context context, Uri uri) {
        if(uri == null)
            return null;

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.MediaColumns.DATA};
            String colum_name = "_data";
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int colum_index = cursor.getColumnIndex(colum_name);
                    String path = cursor.getString(colum_index);
                    return path.replaceFirst("^/sto[a-z]*/sdcard[0-9]*",root);
                }
            } catch (Exception e) {
                Log.w("FileUtils", e.getMessage(), e);
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath().replaceFirst("^/sto[a-z]*/sdcard[0-9]*",root);
        } else {
            Toast.makeText(context, "选择文件路径为空", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
