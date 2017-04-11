package dong.lan.smarttrip.model;


import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Travel;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午1:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 旅行节点的备注（限定条件 旅行+节点）
 */

public class NodeRemark {
    private Travel travel;      //所属于的旅行
    private Node node;          //所对应的节点
    private String info;        //备注详情

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
