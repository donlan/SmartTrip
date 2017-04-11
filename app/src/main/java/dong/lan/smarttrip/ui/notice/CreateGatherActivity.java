package dong.lan.smarttrip.ui.notice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
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
import dong.lan.model.bean.notice.Gather;
import dong.lan.model.base.BaseJsonData;
import dong.lan.smarttrip.model.im.CustomMessage;
import dong.lan.smarttrip.ui.PickLocationActivity;
import com.tencent.qcloud.ui.base.BaseBarActivity;
import dong.lan.smarttrip.ui.customview.DateTimePicker;
import dong.lan.smarttrip.ui.customview.TagCloudView;
import dong.lan.model.utils.TimeUtil;
import io.realm.Realm;

public class CreateGatherActivity extends BaseBarActivity {

    @BindView(R.id.new_gather_tag_cloud)
    TagCloudView gatherTagsView;
    @BindView(R.id.new_gather_location)
    EditText gatherLocInput;
    @BindView(R.id.new_gather_time)
    TextView gatherTimeTv;
    @BindView(R.id.new_gather_content)
    EditText gatherInfoInput;
    @BindView(R.id.bar_right)
    ImageButton barRight;

    @OnClick(R.id.bar_right)
    void publishGather() {
        if (latitude == 0 || longitude == 0) {
            dialog("无法获取位置信息");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            dialog("无位置描述信息");
            return;
        }
        if (gatherTimeMilli < System.currentTimeMillis()) {
            dialog("集合时间小于当前时间");
            return;
        }
        final Gather gather = new Gather();
        gather.id = String.valueOf(System.currentTimeMillis());
        gather.travelId = travelId;
        gather.address = address;
        gather.content = gatherInfoInput.getText().toString();
        gather.latitude = latitude;
        gather.longitude = longitude;
        gather.time = gatherTimeMilli;
        gather.creatorId = UserManager.instance().identifier();
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, travelId);
        CustomMessage customMessage = null;
        try {
            customMessage = new CustomMessage(new BaseJsonData<Gather>(CustomMessage.Action.ACTION_GATHER, gather).toJson());
        } catch (JSONException e) {
            e.printStackTrace();
            toast("解析集合信息异常");
        }
        if (customMessage == null)
            return;
        conversation.sendMessage(customMessage.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                dialog("发送集合信息失败,错误码:"+i);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                toast("发送集合成功");
                MessageEvent.getInstance().onNewMessage(null);
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(gather);
                    }
                });
            }
        });
    }

    @OnClick(R.id.new_gather_time)
    void pickGatherTime() {
        if (dateTimePicker == null) {
            dateTimePicker = new DateTimePicker(this, callBack);
        }
        dateTimePicker.reset(System.currentTimeMillis());
        dateTimePicker.show();
    }

    @OnClick(R.id.new_gather_pick_location)
    void pickLocation() {
        startActivityForResult(new Intent(this, PickLocationActivity.class), Config.REQUEST_CODE_PICK_LOCATION);
    }

    private DateTimePicker.CallBack callBack = new DateTimePicker.CallBack() {
        @Override
        public void onClose(long time) {
            gatherTimeMilli = time;
            gatherTimeTv.setText(TimeUtil.getTime(time, "yyyy.MM.dd HH:mm"));
        }
    };

    private TagCloudView.OnTagClickListener tagClickListener = new TagCloudView.OnTagClickListener() {
        @Override
        public void onTagClick(int postion) {
            gatherInfoInput.setText(gatherTagsView.getData(postion));
        }
    };
    private DateTimePicker dateTimePicker;
    private long gatherTimeMilli;
    private String travelId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gather);
        bindView("发起集合");
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        barRight.setImageResource(R.drawable.upload);
        List<String> gatherInfos = new ArrayList<>();
        gatherInfos.add("机场集合");
        gatherInfos.add("门口集合");
        gatherInfos.add("十分钟后集合");
        gatherInfos.add("三十分钟后集合");
        gatherInfos.add("景点入口集合");
        gatherTagsView.setData(gatherInfos);
        gatherTagsView.setOnTagClickListener(tagClickListener);
    }


    private double latitude;
    private double longitude;
    private String address;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Config.RESULT_LOCATION) {
            latitude = data.getDoubleExtra(Config.LATITUDE, 0);
            longitude = data.getDoubleExtra(Config.LONGITUDE, 0);
            address = data.getStringExtra(Config.LOC_ADDRESS);
            gatherLocInput.setText(address);
        }
    }
}
