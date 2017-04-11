package dong.lan.smarttrip.presentation.presenter;

import android.util.Log;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMGroupMemberRoleFilter;
import com.tencent.TIMGroupMemberSuccV2;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dong.lan.smarttrip.model.factory.UserFactory;
import dong.lan.model.features.IUserInfo;
import dong.lan.smarttrip.presentation.presenter.features.ITravelMemberPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.ITravelMembersView;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午5:57.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelMembersPresenter implements ITravelMemberPresenter {
    private static final String TAG = TravelMembersPresenter.class.getSimpleName();
    ITravelMembersView view;
    private boolean isFirstQuery = true;
    private List<IUserInfo> userInfos;
    private String travelId;

    public TravelMembersPresenter(ITravelMembersView view) {
        this.view = view;
        userInfos = new ArrayList<>();
    }

    @Override
    public void start(String travelId) {
        this.travelId = travelId;
        List<String> tag = new ArrayList<>();
        tag.add("nick");
        loopQuery(travelId, tag, 0);
    }

    private void loopQuery(final String id, final List<String> tag, long seq) {
        if (isFirstQuery || seq != 0) {
            isFirstQuery = false;
            TIMGroupManager.getInstance().getGroupMembersByFilter(id, 0, TIMGroupMemberRoleFilter.All, tag, seq, new TIMValueCallBack<TIMGroupMemberSuccV2>() {
                @Override
                public void onError(int i, String s) {
                    Log.d(TAG, "onError: " + i + "," + s);
                }

                @Override
                public void onSuccess(TIMGroupMemberSuccV2 timGroupMemberSuccV2) {
                    getMembers(timGroupMemberSuccV2.getMemberInfoList());
                    loopQuery(id, tag, timGroupMemberSuccV2.getNextSeq());
                }
            });
        } else {
            view.initMembersList(userInfos);
        }
    }


    private void getMembers(List<TIMGroupMemberInfo> groupMemberInfos) {
        for (TIMGroupMemberInfo info : groupMemberInfos) {
            IUserInfo userInfo = UserFactory.createUser(info);
            userInfos.add(userInfo);
        }
    }

    @Override
    public void delete(String userId, final int position) {
        TIMGroupManager.getInstance().deleteGroupMember(travelId, Collections.singletonList(userId), new TIMValueCallBack<List<TIMGroupMemberResult>>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + s);
                view.dialog("删除成员失败,错误码:" + s);
            }

            @Override
            public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                view.removeUser(position);
            }
        });
    }
}
