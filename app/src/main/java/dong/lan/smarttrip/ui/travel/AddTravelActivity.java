package dong.lan.smarttrip.ui.travel;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.ui.TagsEditText;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.filecloud.utils.FileUtils;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.features.ITravel;
import dong.lan.model.utils.TimeUtil;
import dong.lan.permission.CallBack;
import dong.lan.permission.Permission;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.presentation.presenter.TravelHandlePresenter;
import dong.lan.smarttrip.presentation.presenter.features.ITravelHandlePresenter;
import dong.lan.smarttrip.presentation.viewfeatures.AddTravelView;
import dong.lan.smarttrip.ui.customview.DateTimePicker;
import dong.lan.smarttrip.utils.InputUtils;

/**
 * Created by 梁桂栋 on 16-10-13 ： 下午11:11.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */
public class AddTravelActivity extends BaseBarActivity implements AddTravelView, CallBack<List<String>> {


    private DateTimePicker dateTimePicker;
    private Uri imageUri;
    private Travel newTravel;
    private String travelId;

    @OnClick(R.id.bar_left)
    void back() {
        finish();
    }

    @BindView(R.id.bar_center)
    TextView tittle;
    @BindView(R.id.bar_right)
    ImageButton save;
    @BindView(R.id.travel_new_image)
    ImageView travelImage;
    @BindView(R.id.new_travel_name)
    EditText travelNameEt;
    @BindView(R.id.new_dst_location)
    TagsEditText travelLocationsEt;
    @BindView(R.id.travel_new_start_time)
    TextView travelStartTime;
    @BindView(R.id.travel_new_end_time)
    TextView travelEndTime;
    @BindView(R.id.new_travel_intro)
    EditText newTravelIntroEt;


    @OnClick({R.id.travel_new_change_image, R.id.travel_new_image})
    void pickImage() {
        Permission.instance().check(this, this, Collections.singletonList(Manifest.permission.READ_EXTERNAL_STORAGE));
    }

    @OnClick(R.id.travel_new_start_time)
    void setTravelStartTime(View view) {
        if (dateTimePicker == null)
            dateTimePicker = new DateTimePicker(this, callBack);
        else
            dateTimePicker.reset(System.currentTimeMillis());
        dateTimePicker.show();
        timePickTag = false;
    }

    @OnClick(R.id.travel_new_end_time)
    void setTravelEndTime(View view) {
        if (dateTimePicker == null)
            dateTimePicker = new DateTimePicker(this, callBack);
        else
            dateTimePicker.reset(System.currentTimeMillis());
        dateTimePicker.show();
        timePickTag = true;
    }


    @OnClick(R.id.bar_right)
    void save() {
        if (newTravel == null) {
            newTravel = new Travel();
        }
        String travelName = travelNameEt.getText().toString();
        List<String> dest = travelLocationsEt.getTags();
        String travelIntro = newTravelIntroEt.getText().toString();
        String imgPath = FileUtils.UriToPath(AddTravelActivity.this, imageUri);
        presenter.actionSave(travelName, dest, travelIntro, startTime, endTime, imgPath);

    }

    @OnClick(R.id.travel_new_doc)
    void addNewDoc() {
        presenter.toTravelDocAc();
    }

    @OnClick(R.id.travel_new_edit_line)
    void editTravelLine() {
        presenter.toTravelLineAc();
    }

    @OnClick(R.id.travel_new_user)
    void addTravelMember() {
        presenter.toTravelMembersAc();
    }

    @OnClick(R.id.travel_new_setting)
    void travelSetting() {
        presenter.toTravelOtherSetAc();
    }

    private DateTimePicker.CallBack callBack = new DateTimePicker.CallBack() {
        @Override
        public void onClose(long time) {
            if (!timePickTag) { //开始时间
                startTime = dateTimePicker.timeOfYearDay();
                travelStartTime.setText(TimeUtil.getTime(time, "yyyy.MM.dd"));
            } else { //结束时间
                endTime = dateTimePicker.timeOfYearDay();
                travelEndTime.setText(TimeUtil.getTime(time, "yyyy.MM.dd"));
            }
        }
    };

    private ITravelHandlePresenter presenter;
    private boolean timePickTag = false;
    private long startTime;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_travel);
        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        if (TextUtils.isEmpty(travelId)) {
            bindView("添加新旅行");
        } else {
            bindView("编辑旅行");
        }
        InputUtils.hideInputKeyboard(travelNameEt);
        presenter = new TravelHandlePresenter(this,travelId);
    }

    public void initView(ITravel travel) {
        if(travel==null) {
            save.setImageResource(R.drawable.upload);
        }else{
            save.setImageResource(R.drawable.update);
            Glide.with(this)
                    .load(travel.getImageUrl())
                    .error(R.drawable.demo_image1)
                    .into(travelImage);
            travelNameEt.setText(travel.getName());
            String dst = travel.getDestinations();
            if(!TextUtils.isEmpty(dst))
            for(int i = 0,s = dst.length();i<s;i++){
                travelLocationsEt.append(dst.charAt(i)+" ");
            }
            travelStartTime.setText(TimeUtil.getTime(travel.getStartTime(),"yyyy.MM.dd"));
            travelEndTime.setText(TimeUtil.getTime(travel.getEndTime(),"yyyy.MM.dd"));
            newTravelIntroEt.setText(travel.getIntroduce());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                if (imageUri != null)
                    travelImage.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity activity() {
        return this;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Permission.instance().handleRequestResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResult(List<String> result) {
        if (result == null) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent,
                    Config.REQUEST_CODE_PICK_IMAGE);
        }
    }
}
