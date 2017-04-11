package dong.lan.smarttrip.model.wilddog;

import com.wilddog.client.SyncReference;

import dong.lan.model.bean.travel.Document;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午4:50.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WDDocument implements IBaseWildDog {


    String id;
    long createTime;
    String name;
    String url;
    int role;
    String localPath;

    public WDDocument() {
    }

    public WDDocument(String id, long createTime, String name, String url, int role, String localPath) {
        this.id = id;
        this.createTime = createTime;
        this.name = name;
        this.url = url;
        this.role = role;
        this.localPath = localPath;
    }

    public WDDocument(Document document){
        this.id = document.getId();
        this.createTime = document.getCreateTime();
        this.name = document.getName();
        this.url = document.getUrl();
        this.role = document.getRole();
        this.localPath = document.getLocalPath();
    }

    @Override
    public void save(SyncReference.CompletionListener callback) {

    }

    @Override
    public void delete(SyncReference.CompletionListener callback) {

    }
}
