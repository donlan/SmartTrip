package dong.lan.smarttrip.ui.im;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;

import dong.lan.smarttrip.R;
import com.tencent.qcloud.ui.base.BaseBarActivity;

public class ApplyGroupActivity extends BaseBarActivity implements TIMCallBack {

    private final String TAG = "ApplyGroupActivity";

    private String identify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_group);
        bindView("旅行加入申请");
        identify = getIntent().getStringExtra("identify");
        TextView des = (TextView) findViewById(R.id.description);
        des.setText("申请加入 " + identify);
        final EditText editText = (EditText) findViewById(R.id.input);
        TextView btnSend = (TextView) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupManagerPresenter.applyJoinGroup(identify, editText.getText().toString(), ApplyGroupActivity.this);
            }
        });
    }

    @Override
    public void onError(int i, String s) {
        if (i == 10013) {
            //已经是群成员
            Toast.makeText(this, getString(R.string.group_member_already), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, getResources().getString(R.string.send_success), Toast.LENGTH_SHORT).show();
        finish();
    }
}