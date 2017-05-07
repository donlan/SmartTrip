package dong.lan.smarttrip.presentation.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.blankj.ALog;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import dong.lan.avoscloud.model.AVOTourist;
import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.ITourist;
import dong.lan.model.features.ITravel;
import dong.lan.model.permission.Permission;
import dong.lan.model.utils.FileUtils;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.presentation.presenter.features.ITravelAction;
import dong.lan.smarttrip.presentation.viewfeatures.AddTravelView;
import dong.lan.smarttrip.utils.FileUtil;
import io.realm.Realm;


/**
 * Created by 梁桂栋 on 17-2-2 ： 下午10:44.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class AddTravelPresenter implements ITravelAction {

    private static final String TAG = AddTravelPresenter.class.getSimpleName();
    private AddTravelView view;
    private boolean isUpload;
    private String groupId;
    private Travel travel;

    public AddTravelPresenter(AddTravelView view) {
        this.view = view;
    }


    private void createGroupWithTravel(final String travelName, final List<String> dest, final String travelIntro, final long travelStartTime, final long travelEndTime, final String imgPath) {
        List<TIMGroupMemberInfo> memberinfos = new ArrayList<>();
        TIMGroupManager.CreateGroupParam groupGroupParam = TIMGroupManager.getInstance().new CreateGroupParam();
        groupGroupParam.setGroupName(travelName);
        groupGroupParam.setMembers(memberinfos);
        groupGroupParam.setGroupType("Private");
        groupGroupParam.setIntroduction(travelName);

        TIMGroupManager.getInstance().createGroup(groupGroupParam, new TIMValueCallBack<String>() {
            @Override
            public void onError(int i, String s) {
                view.dismiss();
                isUpload = false;
                ALog.d(TAG, "onSuccess: " + s);
                view.dialog("保存失败,错误码:" + i);
            }

            @Override
            public void onSuccess(String id) {
                ALog.d(TAG, "onSuccess: " + id);
                groupId = id;
                uploadAndSaveTravel(groupId, travelName, dest, travelIntro, travelStartTime, travelEndTime, imgPath);
            }
        });
    }

    private void uploadAndSaveTravel(final String id, final String travelName, final List<String> dest, final String travelIntro, final long travelStartTime, final long travelEndTime, final String imgPath) {
        if (isUpload) {
            view.toast("正在保存中...");
        }
        isUpload = true;
        final AVFile avFile = new AVFile(FileUtils.PathToFileName(imgPath),
                FileUtil.File2byte(imgPath));
        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    String dst = "";
                    for (int i = 0, s = dest.size(); i < s; i++) {
                        dst += dest.get(i) + " ";
                    }
                    final AVOTravel avoTravel = new AVOTravel();
                    avoTravel.setCreator(AVUser.getCurrentUser());
                    avoTravel.setRawImage(avFile);
                    avoTravel.setName(travelName);
                    avoTravel.setTag(UserManager.instance().identifier());
                    avoTravel.setCreateTime(System.currentTimeMillis());
                    avoTravel.setImageUrl(avFile.getUrl());
                    avoTravel.setDestinations(dst);
                    avoTravel.setIntroduce(travelIntro);
                    avoTravel.setEndTime(travelEndTime);
                    avoTravel.setStartTime(travelStartTime);
                    avoTravel.setId(id);
                    avoTravel.addTourist(AVOUser.getCurrentUser(AVOUser.class));
                    avoTravel.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            isUpload = false;
                            view.dismiss();
                            if (e == null || e.getCode() == -1) {
                                Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        travel = BeanConvert.toTravel(avoTravel);
                                        realm.copyToRealmOrUpdate(travel);
                                        final AVOTourist tourist = new AVOTourist();
                                        tourist.setTravel(avoTravel);
                                        tourist.setOwner(AVOUser.getCurrentUser(AVOUser.class));
                                        tourist.setRole(Permission.LEVEL_GUIDER);
                                        tourist.setStatus(ITourist.STATUS_NORMAL);
                                        tourist.saveEventually(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                                        @Override
                                                        public void execute(Realm realm) {
                                                            Tourist t = new Tourist();
                                                            t.setObjId(tourist.getObjectId());
                                                            t.setRole(tourist.getRole());
                                                            t.setStatus(tourist.getStatus());
                                                            t.setTravel(travel);
                                                            t.setUser(UserManager.instance().currentUser());
                                                            realm.copyToRealmOrUpdate(t);
                                                        }
                                                    });
                                                } else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                });
                                view.toast("保存成功");
                            } else {
                                e.printStackTrace();
                                view.dialog("保存旅行失败,错误码:" + e.getCode());
                            }
                        }
                    });
                } else {
                    isUpload = false;
                    view.dialog("保存旅行图片失败,错误码:" + e.getCode());
                }
            }
        });
    }


    @Override
    public void actionSave(String travelName, List<String> dest, String travelIntro, long travelStartTime, long travelEndTime, String imgPath) {
        if (!isUpload) {
            isUpload = true;
            view.alert("保存中....");
            if (TextUtils.isEmpty(groupId))
                createGroupWithTravel(travelName, dest, travelIntro, travelStartTime, travelEndTime, imgPath);
            else
                uploadAndSaveTravel(groupId, travelName, dest, travelIntro, travelStartTime, travelEndTime, imgPath);
        }
    }

    @Override
    public ITravel getTravel() {
        return travel;
    }
}
