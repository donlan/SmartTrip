package dong.lan.smarttrip.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.TIMFriendshipManager;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.binder.InviteUserBinder;
import dong.lan.model.Config;
import dong.lan.smarttrip.model.factory.UserFactory;
import dong.lan.model.features.IUserInfo;
import dong.lan.smarttrip.model.wilddog.WDTourist;
import com.tencent.qcloud.ui.base.BaseActivity;
import dong.lan.smarttrip.ui.customview.IndexRecycleView;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午10:32.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class SendTravelInviteActivity extends BaseActivity implements TIMValueCallBack<List<TIMUserProfile>>{


    private static final String TAG = SendTravelInviteActivity.class.getSimpleName();
    @BindView(R.id.sms_invite_users_list)
    IndexRecycleView indexRecycleView;
    private InviteUserBinder binder;
    private List<IUserInfo> selectUsers;

    @OnClick(R.id.bar_left)
    void back() {
        finish();
    }

    @BindView(R.id.bar_center)
    TextView tittle;
    @BindView(R.id.bar_right)
    ImageButton send;

    @OnClick(R.id.bar_right)
    void sendSmsInvite() {
        selectUsers = binder.getSelectedUsers();
        if (selectUsers.isEmpty()) {
            show("没有选择用户");
        } else {
            final List<String> ids = new ArrayList<>();
            for(IUserInfo user:selectUsers){
                ids.add(user.getIdentifier());
            }
            TIMGroupManager.getInstance().inviteGroupMember(travelId, ids, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                @Override
                public void onError(int i, String s) {
                    dialog("发送邀请失败,错误码:"+i+","+s);
                }

                @Override
                public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                    for(IUserInfo user:selectUsers){
                        new WDTourist(user,travelId).save(new SyncReference.CompletionListener() {
                            @Override
                            public void onComplete(SyncError syncError, SyncReference syncReference) {
                                Log.d(TAG, "onComplete: "+syncError+","+syncReference);
                            }
                        });
                    }
                    toast("发送邀请成功");
                }
            });
        }
    }


    private String travelId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traven_send_sms_invite);
        bindView();
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        initView();

        loadAllUsers();
    }

    private void loadAllUsers() {
        TIMFriendshipManager.getInstance().getFriendList(this);
    }



    private void initView() {
        tittle.setText("发送旅行邀请");
        send.setImageResource(R.drawable.send_arrow);
        indexRecycleView.innerRecycleView().setLayoutManager(new GridLayoutManager(this,1));

    }

    @Override
    public void onError(int i, String s) {
        toast("获取好友失败:" + s);
    }

    @Override
    public void onSuccess(List<TIMUserProfile> userProfiles) {
        if (userProfiles == null || userProfiles.isEmpty()) {
            toast("你还没有任何联系人");
        } else {
            List<IUserInfo> users = UserFactory.createUser(userProfiles);
            Collections.sort(users);
            indexRecycleView.setItemTittle(users);

            binder = new InviteUserBinder();
            binder.init(users);
            indexRecycleView.innerRecycleView().setAdapter(binder.build());
        }
    }

}
