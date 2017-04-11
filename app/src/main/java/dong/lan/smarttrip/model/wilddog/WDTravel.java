package dong.lan.smarttrip.model.wilddog;

import com.wilddog.client.SyncReference;

import java.util.List;

import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.common.WildDog;
import dong.lan.model.bean.travel.Document;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.ITravel;
import io.realm.RealmList;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午4:04.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class WDTravel implements IBaseWildDog {

    private static final String TAG = WDTravel.class.getSimpleName();
    String tag;
    String id;
    String name;
    long createTime;
    long startTime;
    long endTime;
    String imageUrl;
    String introduce;
    String destinations;
    int unread;
    List<Node> nodes;
    List<Document> documents;
    List<Tourist> tourists;


//    public WDTravel(HashMap<String, Object> map) {
//        this.tag = map.get("tag").toString();
//        this.id = map.get("id").toString();
//        this.createTime = ((Number) map.get("createTime")).longValue();
//        this.name = map.get("name").toString();
//        this.startTime = ((Number) map.get("startTime")).longValue();
//        this.endTime = ((Number) map.get("endTime")).longValue();
//        this.imageUrl = map.get("imageUrl").toString();
//        this.introduce = map.get("introduce").toString();
//        this.destinations = map.get("destinations").toString();
//        this.unread = ((Number) map.get("unread")).intValue();
//
//        RealmList<Node> nodes = new RealmList<>();
//        RealmList<Document> documents = new RealmList<>();
//        RealmList<Tourist> tourists = new RealmList<>();
//        Object nodeObj = map.get("nodes");
//        if (nodeObj != null) {
//            Map<String, Object> nodeMap = (Map<String, Object>) nodeObj;
//            for (Object key : nodeMap.keySet()) {
//                Log.d(TAG, "nodes: " + nodeMap.get(key));
//                nodes.add(new Node(new WDNode((Map<String, Object>) nodeMap.get(key))));
//            }
//        }
//
//        Collections.sort(nodes, new Comparator<Node>() {
//            @Override
//            public int compare(Node o1, Node o2) {
//                if (o1 == o2)
//                    return 0;
//                if (o1.getArrivedTime() < o2.getArrivedTime())
//                    return -1;
//                if (o1.getArrivedTime() > o2.getArrivedTime())
//                    return 1;
//                return 0;
//            }
//        });
//
//        Object docObj = map.get("documents");
//        if (docObj != null) {
//
//            Map<String, Object> docMap = (Map<String, Object>) docObj;
//            for (Object key : docMap.keySet()) {
//                Log.d(TAG, "documents: " + docMap.get(key));
//                documents.add(new Document((Map<String, Object>) docMap.get(key)));
//            }
//        }
//        this.nodes = nodes;
//        this.documents = documents;
//        this.tourists = tourists;
//
//    }

    public WDTravel(ITravel travel) {
        this.tag = travel.getTag();
        this.id = travel.getId();
        this.createTime = travel.getCreateTime();
        this.name = travel.getName();
        this.startTime = travel.getStartTime();
        this.endTime = travel.getEndTime();
        this.imageUrl = travel.getImageUrl();
        this.introduce = travel.getIntroduce();
        this.destinations = travel.getDestinations();
        this.unread = travel.getUnread();
        this.nodes = new RealmList<>();
        this.documents = new RealmList<>();
        this.tourists = new RealmList<>();
    }


    public String getTag() {
        return tag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getDestinations() {
        return destinations;
    }

    public int getUnread() {
        return unread;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Tourist> getTourists() {
        return tourists;
    }

    @Override
    public void save(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier()
                + "/travels/" + Config.checkId2Key(id)).setValue(this, callback);
    }

    @Override
    public void delete(SyncReference.CompletionListener callback) {
        WildDog.instance().reference("users/" + UserManager.instance().identifier()
                + "/travels/" + Config.checkId2Key(id)).removeValue(callback);
    }

    @Override
    public String toString() {
        return "WDTravel{" +
                "tag='" + tag + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", imageUrl='" + imageUrl + '\'' +
                ", introduce='" + introduce + '\'' +
                ", destinations='" + destinations + '\'' +
                ", unread=" + unread +
                ", nodes=" + nodes +
                ", documents=" + documents +
                ", tourists=" + tourists +
                '}';
    }
}
