package dong.lan.smarttrip.presentation.presenter;

import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.tencent.qcloud.ui.Dialog;

import java.util.List;

import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.features.ITravel;
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

public class EditTravelPresenter implements ITravelAction {

    private static final String TAG = EditTravelPresenter.class.getSimpleName();
    private AddTravelView view;
    private boolean isUpload;
    private Travel travel = null;
    private Realm realm;
    private String imgUrl;

    public EditTravelPresenter(AddTravelView view, String travelId) {
        this.view = view;
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        travel = realm.where(Travel.class).equalTo("id", travelId).findFirst();
        realm.commitTransaction();
        if (travel == null) {
            view.toast("无效旅行Id");
            view.activity().finish();
            realm.close();
            realm = null;
        }
    }

    private void uploadAndSaveTravel(final String travelName, final List<String> dest, final String travelIntro, final long travelStartTime, final long travelEndTime, final String imgPath) {

        final String fileId = FileUtils.createFileId(imgPath);
        realm.beginTransaction();
        if (!travel.getName().equals(travelName))
            travel.setName(travelName);
        String dst = "";
        for (int i = 0, s = dest.size(); i < s; i++) {
            dst += dest.get(i) + " ";
        }
        if (!travel.getDestinations().equals(dst))
            travel.setDestinations(dst);
        if (!travel.getIntroduce().equals(travelIntro))
            travel.setIntroduce(travelIntro);
        if (travelEndTime != 0 && travel.getEndTime() != travelEndTime)
            travel.setEndTime(travelEndTime);
        if (travelStartTime != 0 && travel.getStartTime() != travelStartTime)
            travel.setStartTime(travelStartTime);
        realm.commitTransaction();
        final AVOTravel avoTravel = BeanConvert.toAvoTravel(travel);
        if (!TextUtils.isEmpty(fileId) && !fileId.contains(travel.getImageUrl())) {
            view.alert("开始更新图片....");
            if (avoTravel.getRawImage() != null) {
                avoTravel.getRawImage().deleteEventually();
            }
            final AVFile file = new AVFile(FileUtils.PathToFileName(imgPath), FileUtil.File2byte(imgPath));
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    view.dismiss();
                    if(e == null){
                        if(!realm.isClosed()) {
                            realm.beginTransaction();
                            travel.setImageUrl(file.getUrl());
                            realm.commitTransaction();
                        }
                        avoTravel.setImageUrl(file.getUrl());
                        avoTravel.setRawImage(file);
                        avoTravel.saveEventually();
                    }else{
                        view.dialog("更新旅行图片失败,错误码:"+e.getCode());
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    view.alert("开始更新图片: "+integer);
                }
            });
        }else{
            avoTravel.saveEventually();
        }
    }


    @Override
    public void actionSave(String travelName, List<String> dest, String travelIntro, long travelStartTime, long travelEndTime, String imgPath) {
        if (0 < travel.getPermission(new Tourist(UserManager.instance().currentUser()))) {
            new Dialog(view.activity())
                    .setMessageText("你不是旅行的创建者,无权修改此旅行的信息")
                    .setLeftText("")
                    .setRightText("关闭")
                    .show();
        } else {
            if (!isUpload) {
                isUpload = true;
                view.alert("修改中....");
                uploadAndSaveTravel(travelName, dest, travelIntro, travelStartTime, travelEndTime, imgPath);
            }
        }
    }

    @Override
    public ITravel getTravel() {
        return travel;
    }
}
