package dong.lan.smarttrip.ui.travel;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Travel;
import com.tencent.qcloud.ui.base.BaseBarActivity;
import dong.lan.model.utils.TimeUtil;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-2-26 ： 下午2:11.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelShareActivity extends BaseBarActivity {

    @BindView(R.id.travel_share_tittle)
    TextView shareTittleTv;
    @BindView(R.id.travel_share_time_info)
    TextView shareTimeInfoTv;
    @BindView(R.id.travel_share_qrcode)
    ImageView shareQRCode;
    @BindView(R.id.travel_share_qq)
    TextView shareQQ;
    @BindView(R.id.travel_share_wx)
    TextView shareWX;
    @BindView(R.id.travel_share_sms)
    TextView shareSMS;


    private String travelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_travel_send_invite);
        bindView("旅行分享");

        initView();
    }

    private void initView() {
       travelId =  getIntent().getStringExtra(Config.TRAVEL_ID);
        if(TextUtils.isEmpty(travelId)){
            shareTittleTv.setText("无效的旅行ID");
            shareQQ.setEnabled(false);
            shareSMS.setEnabled(false);
            shareWX.setEnabled(false);
            shareQRCode.setImageResource(R.drawable.icon_qrcode_error);

        }else{
            Travel travel = Realm.getDefaultInstance().where(Travel.class)
                    .equalTo("id",travelId).findFirst();
            shareTittleTv.setText(travel.getName());
            shareTimeInfoTv.setText(TimeUtil.getTime(travel.getStartTime(),"yyyy.MM.dd  ")
                    +TimeUtil.getTimeDays(travel.getStartTime(), travel.getEndTime())+"天");

            Bitmap qrcode = EncodingUtils.createQRCode(Config.SHARE_BASE_URL+"?travelId="+travelId,
                    200,200,null);
            shareQRCode.setImageBitmap(qrcode);
        }
    }
}
