package dong.lan.smarttrip;

import java.util.ArrayList;
import java.util.List;

import dong.lan.model.bean.travel.Travel;
import dong.lan.model.utils.TimeUtil;

/**
 * Created by 梁桂栋 on 16-11-19 ： 下午7:58.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class Test {

//    public static final String TAG = "TEST";
//
//    private Context context;
//    private User user;
//    private Tourist tourist;
//    private Travel travel;
//
//    public Test(Context context) {
//        this.context = context;
//        travel = new Travel();
//        travel.setObjectId("b13542c26a");
//        user = new User();
//        user.setObjectId("8c8ed3c475");
////        BmobQuery<User> userBmobQuery = new BmobQuery<>();
////        userBmobQuery.addWhereEqualTo("tid", UserInfo.getInstance().getIdentifier());
////        userBmobQuery.findObjects(new FindListener<User>() {
////            @Override
////            public void done(List<User> list, BmobException e) {
////                Log.d(TAG, "getUser done: ");
////                if(e==null && !list.isEmpty()) {
////                    newTourist(list.get(0));
////                }else{
////                    Log.d(TAG, "newTravel done: "+e.getMessage());
////                }
////            }
////        });
//
//        //createHotel();
//        createJourney();
//    }
//
//
//    private void createJourney() {
//        final Journey journey = new Journey();
//        journey.setAuthor(user);
//        journey.setTittle("春风十里，不如有你");
//        journey.setFollower(new BmobRelation(user));
//        journey.setLikes(new BmobRelation(user));
//        journey.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    journey.setObjectId(s);
//                    createFootprint(journey);
//                    createComment(journey);
//                } else {
//                    Log.d(TAG, "createJourney done: " + s + " " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    private void createComment(final Journey journey){
//        final JourneyComment comment = new JourneyComment();
//        comment.setAuthor(user);
//        comment.setJourney(journey);
//        comment.setContent("太棒啦");
//        comment.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if(e==null){
//                    comment.setObjectId(s);
//                    createReply(comment);
//                }else{
//                    Log.d(TAG, "createComment done: "+e.getMessage());
//                }
//            }
//        });
//    }
//
//    private void createReply(JourneyComment comment) {
//        JourneyReply reply = new JourneyReply();
//        reply.setContent("谢谢");
//        reply.setAuthor(user);
//        reply.setComment(comment);
//        reply.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "createReply done: "+s+" "+(s==null?"":e.getMessage()));
//            }
//        });
//    }
//
//    private void createFootprint(Journey journey) {
//        Footprint fp = new Footprint();
//        fp.setOwner(journey);
//        fp.setTravelTime(new BmobDate(new Date()));
//        fp.setInfo("北京八达岭长城");
//        fp.setPhoto(null);
//        fp.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "createFootprint done: " + s + " " + (e == null ? "" : e.getMessage()));
//            }
//        });
//    }
//
//    public void createHotel() {
//
//        final Hotel hotel = new Hotel();
//        hotel.setTravel(travel);
//        hotel.setName("北京美杜莎大酒店");
//        hotel.setCheckInTime(new BmobDate(new Date()));
//        hotel.setCheckOutTime(new BmobDate(new Date()));
//        hotel.setRemarks("12点退房");
//        hotel.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    hotel.setObjectId(s);
//                    createRoom(hotel, user);
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//
//    private void createRoom(Hotel hotel, User user) {
//        for (int i = 1; i <= 10; i++) {
//            Room room = new Room();
//            room.setRemarks("没有独立卫生间");
//            room.setHotel(hotel);
//            room.setRoomNo("301" + i);
//            room.setHasRoomCard(i % 3 == 0);
//            room.setCheckInUsers(i % 3);
//            room.setHomeOwner(new BmobRelation(user));
//            room.save(new SaveListener<String>() {
//                @Override
//                public void done(String s, BmobException e) {
//                    Log.d(TAG, "createRoom done: " + s + "  " + (e == null ? "" : e.getMessage()));
//                }
//            });
//        }
//    }
//
//    public void createUser() {
//        final User u = new User();
//
//        u.setIdentifier(UserInfo.getInstance().getIdentifier());
//        u.setUsername(UserInfo.getInstance().getIdentifier());
//        u.setPhone(UserInfo.getInstance().getIdentifier());
//        u.setBirthday(new BmobDate(new Date()));
//        u.setSex(true);
//        u.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newTravel done: " + s);
//                if (e == null) {
//                    u.setObjectId(s);
//                    newTourist(u);
//                } else {
//                    Log.d(TAG, "newTravel done: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    public void newTourist(final User user) {
//        tourist = new Tourist();
//        tourist.setUser(user);
//        tourist.setRole(Tourist.ROLE_TEAM_LEADER);
//        tourist.setStatus(Tourist.STATUS_NORMAL);
//        tourist.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newTourist done: " + s);
//                if (e == null) {
//                    tourist.setObjectId(s);
//                    newTravel(user, tourist);
//                } else {
//                    Log.d(TAG, "newTourist done: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//
//    public void newUserRemark(Travel travel, User user) {
//        UserRemark r = new UserRemark();
//        r.setTravel(travel);
//        r.setUser(user);
//        r.setInfo("小王，本次带队负责人");
//        r.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newUserRemark done: " + s + (e == null ? "" : e.getMessage()));
//            }
//        });
//    }
//
//    public void newTravel(final User user, final Tourist tourist) {
//        final Travel travel = new Travel();
//        travel.setAuthor(user);
//        travel.setDays(3);
//        travel.setMembers(12);
//        travel.setDetail("北京三日游");
//        travel.setTittle("北京三日游");
//        travel.setEndTime(new BmobDate(new Date()));
//        travel.setUsers(new BmobRelation(tourist));
//        travel.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newTravel done: " + s);
//                if (e == null) {
//                    travel.setObjectId(s);
//                    newNode(travel, tourist);
//                    newUserRemark(travel, user);
//                } else {
//                    Log.d(TAG, "newTravel done: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    public void newNode(final Travel travel, final Tourist tourist) {
//        final Node node = new Node();
//        node.setTravel(travel);
//        node.setArrivalTime(new BmobDate(new Date()));
//        node.setCity("北京");
//        node.setCountry("中国");
//        node.setNo(1);
//        node.setPosition(new BmobGeoPoint(122.324234, 38.010202));
//        node.setRemark("北京长城半日游，所有人早上10点集合出发");
//        node.setRequired(true);
//        node.setDescription("北京八达岭长城半日游");
//        node.setReservedTime(10 * 60 * 60 * 1000);
//        node.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newNode done: " + s);
//                if (e == null) {
//                    node.setObjectId(s);
//                    newTransportation(travel, node);
//                    newGather(travel, node, tourist);
//                    newNotice(travel, node, tourist);
//                } else {
//                    Log.d(TAG, "newNode done: " + e.getMessage());
//                }
//            }
//        });
//    }
//
//    public void newTransportation(Travel travel, Node node) {
//        Transportation tran = new Transportation();
//        tran.setNode(node);
//        tran.setTravel(travel);
//        tran.setType(Transportation.TYPE_CAR);
//        tran.setInfo("东直门打车");
//        tran.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newTransportation done: " + s + (e == null ? "" : e.getMessage()));
//            }
//        });
//    }
//
//    public void newGather(Travel travel, Node node, Tourist tourist) {
//        Gather gather = new Gather();
//        gather.setTravel(travel);
//        gather.setNode(node);
//        gather.setInfo("八达岭售票口集合");
//        gather.setScope(Gather.SCOPE_ALL);
//        gather.setTime(new BmobDate(new Date()));
//        gather.setStayTime(20 * 60 * 60 * 1000);
//        gather.setNoResponse(new BmobRelation(tourist));
//        gather.setNormalResponse(new BmobRelation(tourist));
//        gather.setOffline(new BmobRelation(tourist));
//        gather.setPostpones(new BmobRelation(tourist));
//        gather.setRejectResponse(new BmobRelation(tourist));
//        gather.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newGather done: " + s + (e == null ? "" : e.getMessage()));
//            }
//        });
//    }
//
//    public void newNotice(Travel travel, Node node, Tourist tourist) {
//        Notice notice = new Notice();
//        notice.setInfo("所有人注意，下午有雨，请带好雨具");
//        notice.setTime(new BmobDate(new Date()));
//        notice.setScope(Notice.SCOPE_ALL);
//        notice.setNode(node);
//        notice.setTravel(travel);
//        notice.setNoResponse(new BmobRelation(tourist));
//        notice.setNormalResponse(new BmobRelation(tourist));
//        notice.setOffline(new BmobRelation(tourist));
//        notice.setPostpones(new BmobRelation(tourist));
//        notice.setRejectResponse(new BmobRelation(tourist));
//        notice.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                Log.d(TAG, "newNotice done: " + s + (e == null ? "" : e.getMessage()));
//            }
//        });
//    }


    public List<Travel> testTravels(){
        List<Travel> travels = new ArrayList<>();
        for(int i = 0; i < 15;i++){
            Travel travel = new Travel();
            travel.setId("id_"+i);
            travel.setName("xxx的旅行 "+i);
            travel.setStartTime(System.currentTimeMillis());
            travel.setEndTime(System.currentTimeMillis()+ ((int)(Math.random()*10)) * TimeUtil.DAY_IN_MILI);
            travels.add(travel);
        }
        return travels;
    }

}
