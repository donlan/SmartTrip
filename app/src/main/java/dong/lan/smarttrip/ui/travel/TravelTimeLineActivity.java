package dong.lan.smarttrip.ui.travel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import dong.lan.model.Config;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseActivity;

/**
 * Created by 梁桂栋 on 2017/5/4.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class TravelTimeLineActivity extends BaseActivity {




    String travelId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_time_line);

        travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        if(TextUtils.isEmpty(travelId)){
            finish();
        }else{

        }
    }
}
