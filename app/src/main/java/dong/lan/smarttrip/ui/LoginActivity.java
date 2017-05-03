package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.tencent.qcloud.tlslibrary.customview.EditTextWithClearButton;
import com.tencent.qcloud.tlslibrary.customview.EditTextWithListPopupWindow;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.avoscloud.Secure;
import dong.lan.avoscloud.model.AVOUser;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseActivity;
import dong.lan.smarttrip.common.TLSService;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSSmsLoginListener;
import tencent.tls.platform.TLSUserInfo;

/**
 * Created by 梁桂栋 on 2017/4/29.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.selectCountryCode_hostLogin)
    EditTextWithListPopupWindow smsCodePicker;
    @BindView(R.id.phoneNumber_hostLogin)
    EditTextWithClearButton phoneInput;
    @BindView(R.id.btn_requireCheckCode_hostLogin)
    Button requireSmsBbt;
    @BindView(R.id.checkCode_hostLogin)
    EditText smsCodeEt;


    private TLSService tlsService;

    private SmsLoginListener smsLoginListener;
    private String countrycode;
    private String phoneNumber;
    private boolean fromLogin = true;
    private boolean fromRegister = false;
    private AVOUser avoUser;

    @OnClick(R.id.btn_requireCheckCode_hostLogin)
    void requireSmsCode() {
        countrycode = smsCodePicker.getText().toString();
        countrycode = countrycode.substring(countrycode.indexOf('+') + 1);        // 解析国家码
        phoneNumber = phoneInput.getText().toString();   // 获取手机号

        // 1. 判断手机号是否有效
        if (!Util.validPhoneNumber(countrycode, phoneNumber)) {
            toast("请输入有效的手机号");
            return;
        }

        // 2. 请求验证码
        tlsService.smsLoginAskCode(countrycode, phoneNumber, smsLoginListener);
    }

    @OnClick(R.id.btn_hostLogin)
    void login() {
        countrycode = smsCodePicker.getText().toString();
        countrycode = countrycode.substring(countrycode.indexOf('+') + 1);        // 解析国家码
        phoneNumber = phoneInput.getText().toString();   // 获取手机号
        String checkCode = smsCodeEt.getText().toString();       // 获取验证码

        // 1. 判断手机号是否有效
        if (!Util.validPhoneNumber(countrycode, phoneNumber)) {
            toast("请输入有效的手机号");
            return;
        }

        // 2. 判断验证码是否为空（是否请求了验证码由上层控制）
        if (checkCode.length() == 0) {
            toast("请输入验证码");
            return;
        }

        // 3. 向TLS验证手机号和验证码
        tlsService.smsLoginVerifyCode(checkCode, smsLoginListener);

    }

    @OnClick(R.id.hostRegisterNewUser)
    void register() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initTLSLogin();
    }


    private void initTLSLogin() {
        tlsService = TLSService.getInstance();
        tlsService.initTlsSdk(App.myApp());
        TLSUserInfo userInfo = tlsService.getLastUserInfo();
        if (userInfo != null) {
            String phoneNumber = userInfo.identifier;
            phoneNumber = phoneNumber.substring(phoneNumber.indexOf('-') + 1);
            phoneInput.setText(phoneNumber);
        }
        smsLoginListener = new SmsLoginListener();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) { //从注册页面返回
            if (resultCode == 1001) { //注册成功
                String phoneCode = data.getStringExtra(Constants.COUNTRY_CODE);
                String phone = data.getStringExtra(Constants.PHONE_NUMBER);
                String identifier = data.getStringExtra(Constants.IDENTIFIER);
                toast("为你自动登录中...");

                AVOUser.logInInBackground(identifier, Secure.MD5(identifier), new LogInCallback<AVUser>() {
                    @Override
                    public void done(AVUser avUser, AVException e) {
                        if (e == null) {
                            fromRegister = true;
                            tlsService.smsLogin(countrycode, phoneNumber, smsLoginListener);
                        } else {
                            toast("自动登录失败，请手动登录");
                        }
                    }
                });
            } else {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 短信登录监听器
     */
    public class SmsLoginListener implements TLSSmsLoginListener {

        // 请求下发短信成功，(reaskDuration s) 时间内不可以重新请求下发短信
        @Override
        public void OnSmsLoginAskCodeSuccess(int reaskDuration, int expireDuration) {
            toast("请求下发短信成功,验证码" + expireDuration / 60 + "分钟内有效");

            // 在获取验证码按钮上显示重新获取验证码的时间间隔
            Util.startTimer(requireSmsBbt, "获取验证码", "重新发送", reaskDuration, 1);
        }

        // 重新请求下发短信成功
        @Override
        public void OnSmsLoginReaskCodeSuccess(int reaskDuration, int expireDuration) {
            toast("登录短信重新下发,验证码" + expireDuration / 60 + "分钟内有效");
        }

        // 短信验证通过，下一步调用登录接口TLSSmsLogin完成登录
        @Override
        public void OnSmsLoginVerifyCodeSuccess() {
            String identifier = countrycode + "-" + phoneNumber;
            AVOUser.logInInBackground(identifier, Secure.MD5(identifier), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (e == null) {
                        tlsService.smsLogin(countrycode, phoneNumber, smsLoginListener);
                    } else {
                        toast("登录失败，错误码：" + e.getCode());
                    }
                }
            });
        }

        // 登录成功了，在这里可以获取用户数据
        @Override
        public void OnSmsLoginSuccess(TLSUserInfo userSigInfo) {
            toast("登录成功");
            TLSService.setLastErrno(0);
            jumpToSuccActivity();
        }

        // 短信登录过程中任意一步都可以到达这里
        // 可以根据tlsErrInfo 中ErrCode, Title, Msg 给用户弹提示语，引导相关操作
        @Override
        public void OnSmsLoginFail(TLSErrInfo errInfo) { // 短信登录失败
            toast(errInfo.Msg);
            TLSService.setLastErrno(-1);
            jumpToFailActivity();
        }

        // 短信登录过程中任意一步都可以到达这里，
        // 顾名思义，网络超时，可能是用户网络环境不稳定，一般让用户重试即可
        @Override
        public void OnSmsLoginTimeout(TLSErrInfo errInfo) {
            toast(errInfo.Msg);
        }
    }

    private void jumpToFailActivity() {

    }

    private void jumpToSuccActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.EXTRA_FROM_LOGIN, fromLogin);
        intent.putExtra(Constants.EXTRA_FROM_REGISTER, fromRegister);
        startActivity(intent);
        finish();

    }
}
