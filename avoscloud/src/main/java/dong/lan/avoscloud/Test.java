package dong.lan.avoscloud;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.SaveCallback;

import java.util.Collections;

import dong.lan.avoscloud.model.AVOGather;
import dong.lan.avoscloud.model.AVOGatherReply;
import dong.lan.avoscloud.model.AVONode;
import dong.lan.avoscloud.model.AVONotice;
import dong.lan.avoscloud.model.AVONoticeReply;
import dong.lan.avoscloud.model.AVOTourist;
import dong.lan.avoscloud.model.AVOTransportation;
import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.avoscloud.model.AVOUser;

/**
 * Created by 梁桂栋 on 17-5-5 ： 下午11:20.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Test {

    public void run(){

        final AVOTourist avoTourist = new AVOTourist();
        avoTourist.setRole(1);
        avoTourist.setStatus(1);
        final AVOTravel travel = new AVOTravel();
        travel.setObjectId("58e4ff9ab1acfc0056ba2ba4");
        final AVOUser user = new AVOUser();
        user.setObjectId("590c0d66315c1e005635a206");
        avoTourist.setOwner(user);
        avoTourist.setTravel(travel);
        avoTourist.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                travel.addTourist(user);
            }
        });

        final AVOTransportation transportation = new AVOTransportation();
        transportation.setType(1);
        transportation.setStartTime(System.currentTimeMillis());
        transportation.setEndTime(System.currentTimeMillis());
        transportation.setStartCity("a");
        transportation.setEndCity("b");
        transportation.setCode("123");
        transportation.setInfo("fsadf");
        transportation.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVONode node = new AVONode();
                node.setLocation(new AVGeoPoint(38.4234,112.4234));
                node.setTravel(travel);
                node.setTransportations(Collections.singletonList(transportation));
                node.setCity("a");
                node.setAddress("fasd");
                node.setArrivedTime(System.currentTimeMillis());
                node.setCountry("v");
                node.setCreateTime(System.currentTimeMillis());
                node.setDescription("b");
                node.setNo(1);
                node.setArrivedTime(System.currentTimeMillis());
                node.setRemainTime(21322);
                node.setStayTime(434455);
                node.setRequired(true);
                node.setTag("432423");
                node.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {

                    }
                });
            }
        });

        final AVONotice avoNotice = new AVONotice();
        avoNotice.setCreator(user);
        avoNotice.setId("13213");
        avoNotice.setTravel(travel);
        avoNotice.setContent("fsafda");
        avoNotice.setCreateTime(System.currentTimeMillis());
        avoNotice.setTime(324324234);
        avoNotice.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVONoticeReply avoNoticeReply = new AVONoticeReply();
                avoNoticeReply.setNotice(avoNotice);
                avoNoticeReply.setReplyTime(System.currentTimeMillis());
                avoNoticeReply.setStatus(1);
                avoNoticeReply.setTourist(avoTourist);
                avoNoticeReply.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {

                    }
                });
            }
        });



        final AVOGather avoGather = new AVOGather();
        avoGather.setCreator(user);
        avoGather.setId("13213");
        avoGather.setTravel(travel);
        avoGather.setContent("fsafda");
        avoGather.setCreateTime(System.currentTimeMillis());
        avoGather.setTime(324324234);
        avoGather.setAddress("fsdafad");
        avoGather.setLocation(new AVGeoPoint(38.4324,112.43));
        avoGather.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVOGatherReply avoGatherReply = new AVOGatherReply();
                avoGatherReply.setGather(avoGather);
                avoGatherReply.setLocation(new AVGeoPoint(38.4324,112.43));
                avoGatherReply.setStatus(1);
                avoGatherReply.setReplyTime(System.currentTimeMillis());
                avoGatherReply.setStatus(1);
                avoGatherReply.setTourist(avoTourist);
                avoGatherReply.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {

                    }
                });
            }
        });


    }
}
