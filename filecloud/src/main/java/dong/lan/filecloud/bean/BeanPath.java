package dong.lan.filecloud.bean;

/**
 * Created by 梁桂栋 on 17-3-3 ： 下午1:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface BeanPath {


    String BASE_PATH ="/travels/";
    String DOCUMENTS_PATH ="/documents/";
    String AVATARS_PATH ="/avatars/";

    /**
     *
     * @return 用于上传.删除,下载的后台云存储地址
     */
    String urlPath();

    /**
     *
     * @return 上传时对应的本地文件地址
     */
    String filePath();

    String fileId();

    String path();
}
