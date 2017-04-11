package dong.lan.smarttrip.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import com.tencent.qcloud.ui.base.BaseBarActivity;

/**
 * Created by 梁桂栋 on 17-3-3 ： 下午8:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class QRCodeActivity extends BaseBarActivity {

    @BindView(R.id.qrcode_img)
    ImageView qrcodeImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        String tittle = getIntent().getStringExtra(Config.BAR_TITTLE);
        String content = getIntent().getStringExtra(Config.QRCODE_CONTENT);
        bindView(tittle);
        Bitmap codeBmp = EncodingUtils.createQRCode(content, 300, 300, BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        qrcodeImage.setImageBitmap(codeBmp);
    }
}
