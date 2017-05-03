package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SignUpCallback;
import com.tencent.qcloud.tlslibrary.customview.EditTextWithClearButton;
import com.tencent.qcloud.tlslibrary.customview.EditTextWithListPopupWindow;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.avoscloud.Secure;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseActivity;
import dong.lan.smarttrip.common.TLSService;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSSmsRegListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by 梁桂栋 on 2017/4/29.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.selectCountryCode_hostRegister)
    EditTextWithListPopupWindow smsCodePicker;
    @BindView(R.id.phoneNumber_hostRegister)
    EditTextWithClearButton phoneInput;
    @BindView(R.id.btn_requireCheckCode_hostRegister)
    Button requireSmsBbt;
    @BindView(R.id.checkCode_hostRegister)
    EditText smsCodeEt;
    private TLSService tlsService;
    private String countryCode;
    private String phoneNumber;
    private TLSSmsRegListener smsRegListener;


    @OnClick(R.id.btn_requireCheckCode_hostRegister)
    void requireSmsCode() {
        countryCode = smsCodePicker.getText().toString();
        countryCode = countryCode.substring(countryCode.indexOf('+') + 1);              // 解析国家码
        phoneNumber = phoneInput.getText().toString();      // 获取手机号

        // 1. 判断手机号是否有效
        if (!Util.validPhoneNumber(countryCode, phoneNumber)) {
            toast("请输入有效的手机号");
            return;
        }

        // 2. 请求验证码
        tlsService.smsRegAskCode(countryCode, phoneNumber, smsRegListener);
    }

    @OnClick(R.id.btn_hostRegister)
    void register() {
        countryCode = smsCodePicker.getText().toString();
        countryCode = countryCode.substring(countryCode.indexOf('+') + 1);              // 解析国家码
        phoneNumber = phoneInput.getText().toString();      // 获取手机号
        String checkCode = smsCodeEt.getText().toString();          // 获取验证码

        // 1. 判断手机号是否有效
        if (!Util.validPhoneNumber(countryCode, phoneNumber)) {
            toast("请输入有效的手机号");
            return;
        }

        // 2. 判断验证码是否为空（是否请求了验证码由上层控制）
        if (checkCode.length() == 0) {
            toast("请输入验证码");
            return;
        }

        // 3. 向TLS验证手机号和验证码
        tlsService.smsRegVerifyCode(checkCode, smsRegListener);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindView();
        tlsService = TLSService.getInstance();
        smsRegListener = new SmsRegListener();
    }


    /**
     * 短信注册监听器
     */
    class SmsRegListener implements TLSSmsRegListener {

        // 请求下发短信成功
        @Override
        public void OnSmsRegAskCodeSuccess(int reaskDuration, int expireDuration) {
            toast("请求下发短信成功,验证码" + expireDuration / 60 + "分钟内有效");

            // 在获取验证码按钮上显示重新获取验证码的时间间隔
            Util.startTimer(requireSmsBbt, "获取验证码", "重新获取", reaskDuration, 1);
        }

        // 重新请求下发短信成功
        @Override
        public void OnSmsRegReaskCodeSuccess(int reaskDuration, int expireDuration) {
            toast("注册短信重新下发,验证码" + expireDuration / 60 + "分钟内有效");
        }

        // 短信验证成功，接下来只需要用户确认操作，然后调用SmsRegCommit 完成注册流程
        @Override
        public void OnSmsRegVerifyCodeSuccess() {
            tlsService.smsRegCommit(smsRegListener);
        }

        // 最终注册成功，接下来可以引导用户进行短信登录
        @Override
        public void OnSmsRegCommitSuccess(final TLSUserInfo userInfo) {

            AVOUser avoUser = new AVOUser();
            avoUser.setUsername(userInfo.identifier);
            avoUser.setPassword(Secure.MD5(userInfo.identifier));
            avoUser.setMobile(userInfo.identifier.substring(3));
            avoUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        toast("短信注册成功,为你自动登录中...");
                        Intent intent = new Intent();
                        intent.putExtra(Constants.IDENTIFIER, userInfo.identifier);
                        intent.putExtra(Constants.COUNTRY_CODE, countryCode);
                        intent.putExtra(Constants.PHONE_NUMBER, phoneNumber);
                        intent.putExtra(Constants.EXTRA_FROM_REGISTER, true);
                        setResult(1001, intent);
                        finish();
                    } else {
                        toast("注册失败，错误码：" + e.getCode());
                    }
                }
            });

        }

        // 无密码注册过程中任意一步都可以到达这里
        // 可以根据tlsErrInfo 中ErrCode, Title, Msg 给用户弹提示语，引导相关操作
        @Override
        public void OnSmsRegFail(TLSErrInfo errInfo) {
            Util.notOK(RegisterActivity.this, errInfo);
        }

        // 无密码注册过程中任意一步都可以到达这里
        // 顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可
        @Override
        public void OnSmsRegTimeout(TLSErrInfo errInfo) {
            Util.notOK(RegisterActivity.this, errInfo);
        }
    }


}
