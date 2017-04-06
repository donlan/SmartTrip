package dong.lan.model.features;

/**
 * Created by 梁桂栋 on 17-3-31 ： 下午7:53.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface FileDataAction {

    void addOrUpdate(); //上传文件到后台,存在则覆盖

    void pullOrCovered(); //从后台拉取到本地,存在则覆盖

    void delete(); //删除本地以及后台记录
}
