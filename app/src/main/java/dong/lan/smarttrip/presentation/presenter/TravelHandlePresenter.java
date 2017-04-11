package dong.lan.smarttrip.presentation.presenter;

import android.content.Intent;
import android.text.TextUtils;

import dong.lan.smarttrip.presentation.viewfeatures.AddTravelView;

import java.util.List;

import dong.lan.model.Config;
import dong.lan.model.features.ITravel;
import dong.lan.smarttrip.presentation.presenter.features.ITravelAction;
import dong.lan.smarttrip.presentation.presenter.features.ITravelHandlePresenter;
import dong.lan.smarttrip.ui.SendTravelInviteActivity;
import dong.lan.smarttrip.ui.travel.TravelDocActivity;
import dong.lan.smarttrip.ui.travel.AddTravelLineActivity;

/**
 * Created by 梁桂栋 on 17-2-26 ： 下午11:17.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelHandlePresenter implements ITravelHandlePresenter {

    private AddTravelView view;
    private ITravelAction action;
    private ITravel travel;
    private String travelId;

    public TravelHandlePresenter(AddTravelView view, String  travelId) {
        this.view = view;
        this.travelId = travelId;
        if (TextUtils.isEmpty(travelId)) {
            action = new AddTravelPresenter(view);
            view.initView(null);
        } else  {
            action = new EditTravelPresenter(view,travelId);
            view.initView(action.getTravel());
        }
    }


    @Override
    public void actionSave(String travelName, List<String> dest, String travelIntro, long travelStartTime, long travelEndTime, String imgPath) {

        if (TextUtils.isEmpty(travelName)) {
            view.toast("旅行名称不能为空！");
            return;
        }
        if (dest == null || dest.isEmpty()) {
            view.toast("旅行目的地不能为空！");
            return;
        }
        if (TextUtils.isEmpty(travelIntro)) {
            view.toast("旅行简介不能为空！");
            return;
        }

        if (imgPath == null && TextUtils.isEmpty(travelId)) {
            view.toast("没有旅行图片！");
            return;
        }
        action.actionSave(travelName, dest, travelIntro, travelStartTime, travelEndTime, imgPath);
    }

    @Override
    public void toTravelMembersAc() {
        if (verifyTravel()) {
            Intent smsInviteIntent = new Intent(view.activity(), SendTravelInviteActivity.class);
            smsInviteIntent.putExtra(Config.TRAVEL_ID, travel.getId());
            view.activity().startActivity(smsInviteIntent);
        }
    }

    @Override
    public void toTravelDocAc() {
        if (verifyTravel()) {
            Intent pickDocIntent = new Intent(view.activity(), TravelDocActivity.class);
            pickDocIntent.putExtra(Config.TRAVEL_ID, travel.getId());
            view.activity().startActivityForResult(pickDocIntent, Config.REQUEST_CODE_PICK_DOCUMENT);
        }
    }

    @Override
    public void toTravelLineAc() {
        if (verifyTravel()) {
            Intent addLineIntent = new Intent(view.activity(), AddTravelLineActivity.class);
            addLineIntent.putExtra(Config.TRAVEL_START_TIME, travel.getStartTime());
            addLineIntent.putExtra(Config.TRAVEL_END_TIME, travel.getEndTime());
            addLineIntent.putExtra(Config.TRAVEL_ID, travel.getId());
            view.activity().startActivity(addLineIntent);
        }
    }

    @Override
    public void toTravelOtherSetAc() {

    }


    private boolean verifyTravel() {
        travel = action.getTravel();
        if (travel == null) {
            view.toast("请先保存旅行再进行此操作");
            return false;
        }
        return true;
    }
}
