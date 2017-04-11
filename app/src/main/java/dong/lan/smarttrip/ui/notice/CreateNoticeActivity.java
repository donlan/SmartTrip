package dong.lan.smarttrip.ui.notice;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMGroupSelfInfo;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.event.MessageEvent;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.model.base.BaseJsonData;
import dong.lan.smarttrip.model.im.CustomMessage;
import dong.lan.model.bean.notice.Notice;
import com.tencent.qcloud.ui.base.BaseBarActivity;
import dong.lan.smarttrip.ui.customview.DateTimePicker;
import dong.lan.smarttrip.ui.customview.TagCloudView;
import dong.lan.model.utils.TimeUtil;
import io.realm.Realm;

public class CreateNoticeActivity extends BaseBarActivity {

    private static final String TAG = CreateNoticeActivity.class.getSimpleName();
    @BindView(R.id.new_notify_tag_cloud)
    TagCloudView noticeTagsView;
    @BindView(R.id.new_notify_time)
    TextView noticeTimeTv;
    @BindView(R.id.new_notify_input)
    EditText noticeInfoInput;
    @BindView(R.id.bar_right)
    ImageButton barRight;

    @OnClick(R.id.bar_right)
    void publishNotice() {
        if(!hasPower){
            dialog("你不是群主不能发送群通知");
            return;
        }
        if (TextUtils.isEmpty(noticeInfoInput.getText().toString())) {
            dialog("无位通知内容");
            return;
        }
        if (noticeTimeMilli < System.currentTimeMillis()) {
            dialog("集合时间小于当前时间");
            return;
        }
        final Notice notice = new Notice();
        notice.content = noticeInfoInput.getText().toString();
        notice.createTime = System.currentTimeMillis();
        notice.creatorId = UserManager.instance().identifier();
        notice.time = noticeTimeMilli;
        notice.travelId = travelId;
        notice.id = String.valueOf(System.currentTimeMillis());

        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, travelId);
        CustomMessage customMessage = null;
        try {
            customMessage = new CustomMessage(new BaseJsonData<Notice>(CustomMessage.Action.ACTION_NOTICE, notice).toJson());
        } catch (JSONException e) {
            e.printStackTrace();
            toast("解析通知信息异常");
        }
        if (customMessage == null)
            return;
        conversation.sendMessage(customMessage.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                dialog("发送通知信息失败,错误码:"+i);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                toast("发送通知成功");
                MessageEvent.getInstance().onNewMessage(null);
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(notice);
                    }
                });
            }
        });
    }

    @OnClick(R.id.new_notify_time)
    void pickGatherTime() {
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(this, callBack);
        }
        dateTimePicker.reset(System.currentTimeMillis());
        dateTimePicker.show();
    }


    private DateTimePicker.CallBack callBack = new DateTimePicker.CallBack() {
        @Override
        public void onClose(long time) {
            noticeTimeMilli = time;
            noticeTimeTv.setText(TimeUtil.getTime(time, "yyyy.MM.dd HH:mm"));
        }
    };

    private TagCloudView.OnTagClickListener tagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int postion) {
            noticeInfoInput.setText(noticeTagsView.getData(postion));
        }
    };
    private DateTimePicker dateTimePicker;
    private long noticeTimeMilli;
    private String travelId;

    private boolean hasPower = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_notify);
        bindView("发起通知");
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        barRight.setImageResource(R.drawable.upload);
        List<String> gatherInfos = new ArrayList<>();
        gatherInfos.add("休息十分钟");
        gatherInfos.add("十分钟后出发");
        gatherInfos.add("检查证件是否齐全");
        gatherInfos.add("大家跟团走");
        gatherInfos.add("自由活动半小时");
        noticeTagsView.setData(gatherInfos);
        noticeTagsView.setOnTagClickListener(tagClickListener);

        TIMGroupManager.getInstance().getSelfInfo(travelId, new TIMValueCallBack<TIMGroupSelfInfo>() {
            @Override
            public void onError(int i, String s) {
                dialog("获取群信息失败,错误码:"+i);
                Log.d(TAG, "onError: "+s);
            }

            @Override
            public void onSuccess(TIMGroupSelfInfo timGroupSelfInfo) {
                if(timGroupSelfInfo.getRole() != TIMGroupMemberRoleType.Owner){
                    dialog("你不是群主不能发送群通知");
                    finish();
                }else{
                    hasPower = true;
                }
            }
        });
    }

}
