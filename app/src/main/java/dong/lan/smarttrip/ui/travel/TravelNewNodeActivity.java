package dong.lan.smarttrip.ui.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.event.NodeEvent;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Transportation;
import dong.lan.smarttrip.ui.PickLocationActivity;
import dong.lan.smarttrip.ui.PickRegionActivity;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.ui.customview.DateTimePicker;
import dong.lan.model.utils.TimeUtil;
import io.realm.RealmList;
import mobi.upod.timedurationpicker.TimeDurationPicker;
import mobi.upod.timedurationpicker.TimeDurationPickerDialog;

import static dong.lan.model.Config.RESULT_LOCATION;

/**
 * Created by 梁桂栋 on 17-2-13 ： 上午8:50.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelNewNodeActivity extends BaseBarActivity implements DateTimePicker.CallBack, TimeDurationPickerDialog.OnDurationSetListener {


    @BindView(R.id.new_node_pick_city)
    EditText city;
    @BindView(R.id.new_node_pick_location)
    EditText location;
    @BindView(R.id.new_node_pick_arrived_time)
    TextView arrivedTime;
    @BindView(R.id.new_node_trans_way_code)
    EditText transCode;
    @BindView(R.id.new_node_trans_desc)
    TextView transDesc;

    @BindView(R.id.new_node_trans_group)
    RadioGroup transGroup;

    @BindView(R.id.new_node_pick_leave_city)
    TextView levelCityTv;
    @BindView(R.id.new_node_pick_arrived_city)
    TextView arrivedCityTv;
    @BindView(R.id.new_node_verify_leave_time)
    TextView verifyLevelTimeTv;
    @BindView(R.id.new_node_pick_time_desc)
    TextView takesTimeDescTv;
    @BindView(R.id.new_node_verify_arrived_time)
    TextView verifyArrivedTimeTv;
    @BindView(R.id.new_node_set_stay_time)
    TextView stayTimeTv;
    @BindView(R.id.new_node_set_remain_time)
    TextView remainTimeTv;
    @BindView(R.id.new_node_intro_et)
    EditText nodeIntroEt;
    @BindView(R.id.new_node_trans_code_parent)
    LinearLayout transCodeParent;
    @BindView(R.id.new_node_trans_icon)
    ImageView transIcon;
    private long baseTime;
    private double latitude;
    private double longitude;
    private long arrivedTimeMilli;
    private long verifyArrivedTimeMilli;
    private long verifyLeavedTimeMilli;
    private long stayTimeMilli;
    private long remainTimeMilli;
    private int timePickTag = -1;
    private int tagTDP = -1;
    private String leavedCity;
    private String arrivedCity;
    private int transportationType = Transportation.TYPE_WALK;
    private String address;

    @OnClick(R.id.new_node_pick_leave_city)
    void toPickLeavedRegionAc(){
        Intent intent = new Intent(this, PickRegionActivity.class);
        startActivityForResult(intent,Config.REQUEST_CODE_PICK_LEAVED_CITY);
    }
    @OnClick(R.id.new_node_pick_arrived_city)
    void toPickArrivedRegionAc(){
        Intent intent = new Intent(this, PickRegionActivity.class);
        startActivityForResult(intent,Config.REQUEST_CODE_PICK_ARRIVED_CITY);
    }

    @OnClick(R.id.new_node_set_remain_time)
    void pickRemainTime(){
        tagTDP = 1;
        if(tdpDialog == null) {
            tdpDialog = new TimeDurationPickerDialog(this,this,0,TimeDurationPicker.HH_MM);
        }
        tdpDialog.setDuration(0);
        tdpDialog.show();
    }


    @OnClick(R.id.new_node_set_stay_time)
    void pickStayTime(){
        tagTDP = 0;
        if(tdpDialog == null) {
            tdpDialog = new TimeDurationPickerDialog(this,this,0,TimeDurationPicker.HH_MM);
        }
        tdpDialog.setDuration(0);
        tdpDialog.show();
    }

    @OnClick(R.id.new_node_pick_location)
    void pickLocation(){
        Intent locIntent = new Intent(this, PickLocationActivity.class);
        startActivityForResult(locIntent,Config.REQUEST_CODE_PICK_LOCATION);
    }

    @OnClick(R.id.new_node_verify_leave_time)
    void pickLeaveTime(){
        timePickTag = 1;
//        dateTimePicker.reset(baseTime);
        dateTimePicker.show(true);
    }

    @OnClick(R.id.new_node_verify_arrived_time)
    void pickVerifyArriveTime(){
        timePickTag = 2;
//        dateTimePicker.reset(baseTime);
        dateTimePicker.show(true);
    }
    @OnClick(R.id.new_node_pick_arrived_time)
    void pickArrivedTime(){
        timePickTag = 0;
//        dateTimePicker.reset(baseTime);
        dateTimePicker.show(true);
    }

    @OnClick(R.id.bar_right)
    void saveNode() {
        long curTimeMilli = System.currentTimeMillis();
        if(TextUtils.isEmpty(address)){
            toast("没有设置节点位置");
            return;
        }
        if(arrivedTimeMilli <= curTimeMilli){
            toast("到达时间小于当前时间");
            return;
        }
        if(verifyArrivedTimeMilli <= curTimeMilli){
            toast("乘坐交通工具到达的时间小于当前时间");
            return;
        }
        if(verifyLeavedTimeMilli <= curTimeMilli){
            toast("乘坐交通的出发时间小于当前时间");
            return;
        }
        if(TextUtils.isEmpty(leavedCity)){
            toast("没有设置出发城市");
            return;
        }
        if(TextUtils.isEmpty(arrivedCity)){
            toast("没有设置到达城市");
            return;
        }
        String transportationCode = transCode.getText().toString();
        if(transportationType != Transportation.TYPE_WALK && TextUtils.isEmpty(transportationCode)){
            String code = "";
            if(transportationType == Transportation.TYPE_PLANE)
                code="航班号";
            if(transportationType == Transportation.TYPE_TRAIN)
                code="列车号";
            toast("没有设置"+code);
            return;
        }


        Node node = new Node();
        node.setArrivedTime(arrivedTimeMilli);
        node.setAddress(address);
        node.setLatitude(latitude);
        node.setLongitude(longitude);
        node.setDescription(nodeIntroEt.getText().toString());
        node.setRemainTime(remainTimeMilli);
        node.setStayTime(stayTimeMilli);
        node.setTransportations(new RealmList<Transportation>());


        Transportation transportation = new Transportation();
        transportation.setType(transportationType);
        transportation.setStartTime(verifyLeavedTimeMilli);
        transportation.setEndTime(verifyArrivedTimeMilli);
        transportation.setStartCity(leavedCity);
        transportation.setEndCity(arrivedCity);
        transportation.setCode(transportationCode);
        node.getTransportations().add(transportation);
        node.setCreateTime(System.currentTimeMillis());

        EventBus.getDefault().post(new NodeEvent(node));
        finish();
    }


    private DateTimePicker dateTimePicker;
    private TimeDurationPickerDialog tdpDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_node);
        baseTime = getIntent().getLongExtra(Config.NODE_DAY_TIME,0);
        bindView("新节点("+ TimeUtil.getTime(baseTime,"yyyy.MM.dd")+")");
        initView();
    }

    private void initView() {
        setBarRightIcon(R.drawable.upload);
        transGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.new_node_trans_free:
                        transportationType = Transportation.TYPE_WALK;
                        transCodeParent.setVisibility(View.GONE);
                        transIcon.setImageResource(R.drawable.free);
                        break;
                    case R.id.new_node_trans_plane:
                        transportationType = Transportation.TYPE_PLANE;
                        transCodeParent.setVisibility(View.VISIBLE);
                        transDesc.setText("航班号");
                        transIcon.setImageResource(R.drawable.plane);
                        break;
                    case R.id.new_node_trans_train:
                        transportationType = Transportation.TYPE_TRAIN;
                        transCodeParent.setVisibility(View.VISIBLE);
                        transDesc.setText("列车号");
                        transIcon.setImageResource(R.drawable.train);
                        break;
                }
            }
        });
        dateTimePicker = new DateTimePicker(this,this);
        dateTimePicker.reset(baseTime);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Config.REQUEST_CODE_PICK_LOCATION && resultCode == RESULT_LOCATION){
            latitude = data.getDoubleExtra(Config.LATITUDE,0);
            longitude = data.getDoubleExtra(Config.LONGITUDE,0);
            address = data.getStringExtra(Config.LOC_ADDRESS);
            location.setText(address);
        }else if(requestCode == Config.REQUEST_CODE_PICK_LEAVED_CITY){
            String city = data.getStringExtra(Config.CITY_NAME);
            leavedCity = city;
            levelCityTv.setText(city);
        }else if(requestCode == Config.REQUEST_CODE_PICK_ARRIVED_CITY){
            String city = data.getStringExtra(Config.CITY_NAME);
            arrivedCityTv.setText(city);
            arrivedCity = city;
        }
    }


    @Override
    public void onClose(long time) {
        long mTime = dateTimePicker.timeOfHourAndMinute() + baseTime;
        switch (timePickTag){
            case 0:
                arrivedTimeMilli = mTime;
                arrivedTime.setText(TimeUtil.getTime(mTime,"HH:mm"));
                break;

            case 1:
                verifyLeavedTimeMilli = mTime;
                verifyLevelTimeTv.setText(TimeUtil.getTime(mTime,"HH:mm"));
                break;
            case 2:
                verifyArrivedTimeMilli = mTime;
                verifyArrivedTimeTv.setText(TimeUtil.getTime(mTime,"HH:mm"));
                break;
        }
    }

    @Override
    public void onDurationSet(TimeDurationPicker view, long duration) {
        if(tagTDP == 0){
            stayTimeMilli = duration;
            stayTimeTv.setText(view.getTimeDurationString());
        }else if(tagTDP == 1){
            remainTimeTv.setText(view.getTimeDurationString());
            remainTimeMilli = duration;
        }
    }
}
