package dong.lan.filecloud.bean;

/**
 * Created by 梁桂栋 on 17-3-22 ： 下午11:00.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class DirBean implements BeanPath {

    String path;

    public DirBean(String path) {
        this.path = path;
    }

    @Override
    public String urlPath() {
        return null;
    }

    @Override
    public String filePath() {
        return null;
    }

    @Override
    public String fileId() {
        return null;
    }

    @Override
    public String path() {
        return path;
    }
}
