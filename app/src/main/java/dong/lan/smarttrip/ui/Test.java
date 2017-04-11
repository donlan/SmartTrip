package dong.lan.smarttrip.ui;

import android.os.Bundle;

import dong.lan.smarttrip.R;
import com.tencent.qcloud.ui.base.BaseActivity;
import dong.lan.smarttrip.ui.customview.IndexRecycleView;
import dong.lan.smarttrip.ui.customview.IndexView;
import dong.lan.smarttrip.ui.customview.TagCloudView;

public class Test extends BaseActivity implements TagCloudView.OnTagClickListener, IndexView.IndexTouchListener {

    TagCloudView tagCloudView;

    IndexRecycleView indexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    @Override
    public void onTagClick(int postion) {
        toast(tagCloudView.getData(postion));
    }

    @Override
    public void touchAt(String indexText, int position) {

    }

    @Override
    public void touchMove(String indexText, int position) {

    }

    @Override
    public void touchDone(String indexText, int position) {

    }
}
