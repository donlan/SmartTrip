package dong.lan.model;

import com.avos.avoscloud.AVGeoPoint;

import java.util.ArrayList;
import java.util.List;

import dong.lan.avoscloud.model.AVODocument;
import dong.lan.avoscloud.model.AVOGather;
import dong.lan.avoscloud.model.AVOGatherReply;
import dong.lan.avoscloud.model.AVONode;
import dong.lan.avoscloud.model.AVONotice;
import dong.lan.avoscloud.model.AVONoticeReply;
import dong.lan.avoscloud.model.AVOTourist;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.avoscloud.model.AVOTransportation;
import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.notice.GatherReply;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.bean.notice.NoticeReply;
import dong.lan.model.bean.travel.Document;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Transportation;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.bean.user.User;
import dong.lan.model.features.IDocument;
import dong.lan.model.features.INode;
import dong.lan.model.features.ITourist;
import dong.lan.model.features.ITransportation;
import dong.lan.model.features.ITravel;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * Created by 梁桂栋 on 17-4-2 ： 下午2:51.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public final class BeanConvert {

    private BeanConvert() {
        //no instance
    }

    public static AVOTransportation toAvoTransportation(ITransportation transportation) {
        AVOTransportation trans = new AVOTransportation();
        trans.setObjectId(transportation.getObjId());
        trans.setCode(transportation.getCode());
        trans.setEndCity(transportation.getEndCity());
        trans.setEndTime(transportation.getEndTime());
        trans.setInfo(transportation.getInfo());
        trans.setStartCity(transportation.getStartCity());
        trans.setStartTime(transportation.getStartTime());
        trans.setType(transportation.getType());
        return trans;
    }

    public static AVODocument toAvoDocument(IDocument document) {
        AVODocument avoDocument = new AVODocument();
        avoDocument.setObjectId(document.getObjId());
        avoDocument.setCreateTime(document.getCreateTime());
        avoDocument.setId(document.getId());
        avoDocument.setLocalPath(document.getLocalPath());
        avoDocument.setName(document.getName());
        avoDocument.setUrl(document.getUrl());
        avoDocument.setRole(document.getRole());
        return avoDocument;
    }


    public static AVONode toAvoNode(INode node) {
        AVONode avoNode = new AVONode();
        avoNode.setObjectId(node.getObjId());
        avoNode.setCreateTime(node.getCreateTime());
        avoNode.setAddress(node.getAddress());
        avoNode.setArrivedTime(node.getArrivedTime());
        avoNode.setCity(node.getCity());
        avoNode.setCountry(node.getCountry());
        avoNode.setDescription(node.getDescription());
        avoNode.setLocation(node.getLatitude(),node.getLongitude());
        avoNode.setNo(node.getNo());
        avoNode.setRequired(node.isRequired());
        avoNode.setTag(node.getTag());
        avoNode.setTeamType(node.getTeamType());
        avoNode.setStayTime(node.getStayTime());
        List<AVOTransportation> transportations = new ArrayList<>();
        if (node.getTransportations() != null)
            for (Transportation transportation : node.getTransportations())
                transportations.add(toAvoTransportation(transportation));
        avoNode.setTransportations(transportations);
        return avoNode;
    }

    public static AVOTravel toAvoTravel(ITravel travel) {

        AVOTravel avoTravel = new AVOTravel();
        avoTravel.setObjectId(travel.getObjId());
        avoTravel.setId(travel.getId());
        avoTravel.setTag(travel.getTag());
        avoTravel.setCreateTime(travel.getCreateTime());
        avoTravel.setDestinations(travel.getDestinations());
        avoTravel.setEndTime(travel.getEndTime());
        avoTravel.setImageUrl(travel.getImageUrl());
        avoTravel.setName(travel.getName());
        avoTravel.setIntroduce(travel.getIntroduce());
        avoTravel.setUnread(travel.getUnread());
        avoTravel.setStartTime(travel.getStartTime());
        return avoTravel;
    }

    public static AVOUser toAvoUser(ITourist tourist) {
        AVOUser avoUser = new AVOUser();
        avoUser.setIdentifier(tourist.getUser().getIdentifier());
        return avoUser;
    }

    public static Transportation toTransportation(AVOTransportation transportation) {
        Transportation trans = new Transportation();
        trans.setObjId(transportation.getObjectId());
        trans.setCode(transportation.getCode());
        trans.setEndCity(transportation.getEndCity());
        trans.setEndTime(transportation.getEndTime());
        trans.setInfo(transportation.getInfo());
        trans.setStartCity(transportation.getStartCity());
        trans.setStartTime(transportation.getStartTime());
        trans.setType(transportation.getType());
        return trans;
    }

    public static Document toDocument(AVODocument document) {
        Document doc = new Document();
        doc.setObjId(document.getObjectId());
        doc.setCreateTime(document.getCreateTime());
        doc.setId(document.getId());
        doc.setLocalPath(document.getLocalPath());
        doc.setName(document.getName());
        doc.setUrl(document.getRawDocument() != null ? document.getRawDocument().getUrl() : document.getUrl());
        doc.setRole(document.getRole());
        return doc;
    }


    public static Node toNode(AVONode node) {
        Node avoNode = new Node();
        avoNode.setObjId(node.getObjectId());
        avoNode.setCreateTime(node.getCreateTime());
        avoNode.setAddress(node.getAddress());
        avoNode.setArrivedTime(node.getArrivedTime());
        avoNode.setCity(node.getCity());
        avoNode.setCountry(node.getCountry());
        avoNode.setDescription(node.getDescription());
        avoNode.setLatitude(node.getLocation().getLatitude());
        avoNode.setLongitude(node.getLocation().getLongitude());
        avoNode.setNo(node.getNo());
        avoNode.setRequired(node.isRequired());
        avoNode.setTag(node.getTag());
        avoNode.setTeamType(node.getTeamType());
        avoNode.setStayTime(node.getStayTime());
        RealmList<Transportation> transportations = new RealmList<>();
        if (node.getTransportations() != null)
            for (AVOTransportation transportation : node.getTransportations())
                transportations.add(toTransportation(transportation));
        avoNode.setTransportations(transportations);
        return avoNode;
    }

    public static Travel toTravel(AVOTravel travel) {
        Travel avoTravel = new Travel();
        avoTravel.setObjId(travel.getObjectId());
        avoTravel.setTag(travel.getTag());
        avoTravel.setCreateTime(travel.getCreateTime());
        avoTravel.setDestinations(travel.getDestinations());
        avoTravel.setEndTime(travel.getEndTime());
        avoTravel.setImageUrl(travel.getRawImage() != null ? travel.getRawImage().getUrl() : travel.getImageUrl());
        avoTravel.setName(travel.getName());
        avoTravel.setId(travel.getId());
        avoTravel.setIntroduce(travel.getIntroduce());
        avoTravel.setUnread(travel.getUnread());
        avoTravel.setStartTime(travel.getStartTime());
        RealmList<Node> nodes = new RealmList<>();
        return avoTravel;
    }

    public static User toUser(AVOUser avoUser) {
        User user = new User();
        user.setUsername(avoUser.getUsername());
        user.setAvatarUrl(avoUser.getFaceUrl());
        user.setNickname(avoUser.getNickName());
        user.setPhone(avoUser.getMobile());
        user.setIdentifier(avoUser.getIdentifier());
        user.setObjId(avoUser.getObjectId());
        return user;
    }

    public static AVOUser toAvoUser(User user) {
        AVOUser avoUser = new AVOUser();
        avoUser.setIdentifier(user.getIdentifier());
        avoUser.setObjectId(user.getObjId());
        avoUser.setFaceUrl(user.getAvatarUrl());
        avoUser.setMobile(user.getPhone());
        avoUser.setNickName(user.getNickname());
        return avoUser;
    }


    public static Tourist toTourist(AVOTourist avoTourist) {
        Tourist tourist = new Tourist();
        tourist.setObjId(avoTourist.getObjectId());
        tourist.setRole(avoTourist.getRole());
        tourist.setStatus(avoTourist.getStatus());
        tourist.setUser(toUser(avoTourist.getOwner()));
        tourist.setTravel(toTravel(avoTourist.getTravel()));
        return tourist;
    }


    public static AVOTourist toAvoTourist(Tourist tourist) {
        AVOTourist avoTourist = new AVOTourist();
        avoTourist.setObjectId(tourist.getObjId());
        avoTourist.setOwner(toAvoUser(tourist.getUser()));
        avoTourist.setRole(tourist.getRole());
        avoTourist.setStatus(tourist.getStatus());
        avoTourist.setTravel(toAvoTravel(tourist.getTravel()));
        return avoTourist;
    }

    public static Gather toGather(AVOGather avoGather) {
        Gather gather = new Gather();
        gather.objId = avoGather.getObjectId();
        gather.address = avoGather.getAddress();
        gather.content = avoGather.getContent();
        gather.createTime = avoGather.getCreateTime();
        gather.creatorId = avoGather.getCreator().getIdentifier();
        gather.travelId = avoGather.getTravel().getId();
        gather.time = avoGather.getTime();
        gather.latitude = avoGather.getLocation().getLatitude();
        gather.longitude = avoGather.getLocation().getLongitude();
        return gather;
    }

    public static AVOGather toAvoGather(Gather gather) {
        Realm realm = Realm.getDefaultInstance();
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        User user = realm.where(User.class).equalTo("identifier", gather.getCreatorId()).findFirst();
        Travel travel = realm.where(Travel.class).equalTo("id", gather.getTravelId()).findFirst();
        realm.commitTransaction();
        AVOGather avoGather = new AVOGather();
        avoGather.setObjectId(gather.getObjId());
        avoGather.setAddress(gather.getAddress());
        avoGather.setContent(gather.getContent());
        avoGather.setCreateTime(gather.getCreateTime());
        avoGather.setCreator(toAvoUser(user));
        avoGather.setTravel(toAvoTravel(travel));
        avoGather.setTime(gather.getTime());
        avoGather.setLocation(new AVGeoPoint(gather.getLatitude(), gather.getLongitude()));
        return avoGather;
    }


    public static Notice toNotice(AVONotice avoNotice) {
        Notice notice = new Notice();
        notice.objId = avoNotice.getObjectId();
        notice.content = avoNotice.getContent();
        notice.createTime = avoNotice.getCreateTime();
        notice.creatorId = avoNotice.getCreator().getIdentifier();
        notice.travelId = avoNotice.getTravel().getId();
        notice.time = avoNotice.getTime();
        return notice;
    }


    public static AVONotice toAvoNotice(Notice notice) {
        Realm realm = Realm.getDefaultInstance();
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        User user = realm.where(User.class).equalTo("identifier", notice.getCreatorId()).findFirst();
        Travel travel = realm.where(Travel.class).equalTo("id", notice.getTravelId()).findFirst();
        realm.commitTransaction();
        AVONotice avoNotice = new AVONotice();
        avoNotice.setCreateTime(notice.getCreateTime());
        avoNotice.setContent(notice.getContent());
        avoNotice.setObjectId(notice.getObjId());
        avoNotice.setId(notice.getId());
        avoNotice.setCreator(toAvoUser(user));
        avoNotice.setTravel(toAvoTravel(travel));
        return avoNotice;
    }

    public static NoticeReply toNoticeReply(AVONoticeReply avoNoticeReply) {

        NoticeReply noticeReply = new NoticeReply();
        noticeReply.objId = avoNoticeReply.getObjectId();
        noticeReply.replyTime = avoNoticeReply.getReplyTime();
        noticeReply.status = avoNoticeReply.getStatus();
        noticeReply.notice = toNotice(avoNoticeReply.getNotice());
        noticeReply.tourist = toTourist(avoNoticeReply.getTourist());

        return noticeReply;
    }


    public static AVONoticeReply toAvoNoticeReply(NoticeReply noticeReply) {

        AVONoticeReply avoNoticeReply = new AVONoticeReply();
        avoNoticeReply.setObjectId(noticeReply.getObjId());
        avoNoticeReply.setReplyTime(noticeReply.getReplyTime());
        avoNoticeReply.setStatus(noticeReply.getStatus());
        avoNoticeReply.setTourist(toAvoTourist(noticeReply.getTourist()));
        avoNoticeReply.setNotice(toAvoNotice(noticeReply.getNotice()));
        return avoNoticeReply;
    }

    public static AVOGatherReply toAvoGatherReply(GatherReply gatherReply) {
        AVOGatherReply avoGatherReply = new AVOGatherReply();
        avoGatherReply.setObjectId(gatherReply.getObjId());
        avoGatherReply.setStatus(gatherReply.getStatus());
        avoGatherReply.setReplyTime(gatherReply.getReplyTime());
        avoGatherReply.setLocation(new AVGeoPoint(gatherReply.getLatitude(), gatherReply.getLongitude()));
        avoGatherReply.setGather(toAvoGather(gatherReply.getGather()));
        avoGatherReply.setTourist(toAvoTourist(gatherReply.getTourist()));
        return avoGatherReply;
    }

    public static GatherReply toGatherReply(AVOGatherReply avoGatherReply) {
        GatherReply gatherReply = new GatherReply();
        gatherReply.objId = avoGatherReply.getObjectId();
        gatherReply.gather = toGather(avoGatherReply.getGather());
        gatherReply.status = avoGatherReply.getStatus();
        gatherReply.latitude = avoGatherReply.getLocation().getLatitude();
        gatherReply.longitude = avoGatherReply.getLocation().getLongitude();
        gatherReply.replyTime = avoGatherReply.getReplyTime();
        return gatherReply;
    }


}
