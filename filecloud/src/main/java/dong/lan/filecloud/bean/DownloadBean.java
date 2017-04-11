package dong.lan.filecloud.bean;

import dong.lan.filecloud.utils.FileUtils;

/**
 * Created by 梁桂栋 on 17-3-3 ： 下午1:44.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class DownloadBean implements BeanPath {

    private String url;
    private String filePath;
    private String fileId;
    private String path;

    public DownloadBean(COSType type, String visitUrl, String savePath) {
        String filename = FileUtils.PathToFileName(visitUrl);
        fileId = FileUtils.createFileId(filename);
        switch (type){
            case TRAVELS:
                path = BASE_PATH;
                url =BASE_PATH+fileId;
                break;
            case AVATARS:
                path = AVATARS_PATH;
                url =AVATARS_PATH+fileId;
                break;
            case DOCUMENTS:
                path = DOCUMENTS_PATH;
                url =DOCUMENTS_PATH+fileId;
                break;
        }
        filePath = savePath;
    }

    @Override
    public String urlPath() {
        return url;
    }

    @Override
    public String filePath() {
        return filePath;
    }

    @Override
    public String fileId() {
        return url;
    }

    @Override
    public String path() {
        return path;
    }
}