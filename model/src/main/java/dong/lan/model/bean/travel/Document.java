package dong.lan.model.bean.travel;


import java.util.Map;

import dong.lan.model.features.IDocument;
import dong.lan.model.features.ItemTextDisplay;
import dong.lan.model.permission.Permission;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午3:19.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 旅行文档的封装
 */

@RealmClass
public class Document extends RealmObject implements Comparable<Document>, ItemTextDisplay,IDocument {


    Travel travel;      //对应的旅行id
    String objId;       //对用后台Document表的objectId
    String id;          //文档id
    long createTime;    //创建时间
    String name;        //文档名称
    String url;         //访问链接
    int role;           //用于判断文档是否可以被用户查看
    String localPath;   //文档对于用户手机的本地访问路径(方便本地操作而已)

    public Document() {
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public Document(Map<String, Object> map) {
        id = map.get("id") ==null ? "":map.get("id").toString();
        createTime = (long) map.get("createTime");
        name = map.get("name") ==null ? "" :map.get("name").toString();
        url = map.get("url").toString();
        role = ((Number) map.get("role")).intValue();
        localPath =map.get("localPath") ==null ? "": map.get("localPath").toString();
    }

    @Override
    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public int compareTo(Document o) {
        if (o == this)
            return 0;
        if (this.role == o.role)
            return (int) (this.createTime - o.createTime);
        return this.role - o.role;
    }


    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", role=" + role +
                ", localPath='" + localPath + '\'' +
                '}';
    }

    @Override
    public String display() {
        switch (getRole()) {
            case Permission.LEVEL_GUIDER:
                return "导游";
            case Permission.LEVEL_LINGDUI:
                return "领队";
            case Permission.LEVEL_YOUKE:
                return "游客";
            case Permission.LEVEL_OTHER:
                return "其他";
        }
        return "";
    }
}
