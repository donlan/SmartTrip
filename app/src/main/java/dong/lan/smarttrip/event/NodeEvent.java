package dong.lan.smarttrip.event;

import dong.lan.model.bean.travel.Node;

/**
 * Created by 梁桂栋 on 17-3-9 ： 下午8:04.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NodeEvent {
    private Node node;

    public NodeEvent(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
