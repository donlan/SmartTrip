package dong.lan.smarttrip.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.baidu.location.BDLocation;
import com.tencent.TIMCallBack;
import com.tencent.TIMLogLevel;
import com.tencent.qcloud.presentation.business.InitBusiness;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.event.FriendshipEvent;
import com.tencent.qcloud.presentation.event.GroupEvent;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.presentation.event.RefreshEvent;
import com.tencent.qcloud.presentation.presenter.SplashPresenter;
import com.tencent.qcloud.presentation.viewfeatures.SplashView;
import com.tencent.qcloud.tlslibrary.activity.HostLoginActivity;
import com.tencent.qcloud.tlslibrary.service.Constants;
import com.tencent.qcloud.tlslibrary.service.TLSService;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;
import com.tencent.qcloud.ui.NotifyDialog;

import java.util.ArrayList;
import java.util.List;

import dong.lan.avoscloud.AVOConfig;
import dong.lan.map.service.LocationService;
import dong.lan.model.bean.travel.Travel;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.common.SPHelper;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.common.WildDog;

import com.tencent.qcloud.presentation.business.UserInfo;

import dong.lan.smarttrip.model.wilddog.User;
import dong.lan.smarttrip.utils.PushUtil;
import dong.lan.weather.Api;

public class SplashActivity extends FragmentActivity implements SplashView, TIMCallBack {

    SplashPresenter presenter;
    private int LOGIN_RESULT_CODE = 100;
    private int GOOGLE_PLAY_RESULT_CODE = 200;
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private boolean isFirst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clearNotification();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        final List<String> permissionsList = new ArrayList<>();



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() == 0) {
                delayInit();
            } else {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        } else {
            delayInit();
        }
    }


    /**
     * 跳转到主界面
     */
    @Override
    public void navToHome() {
        //登录之前要初始化群和好友关系链缓存
        FriendshipEvent.getInstance().init();
        GroupEvent.getInstance().init();
        LoginBusiness.loginIm(UserInfo.getInstance().getId(),
                UserInfo.getInstance().getUserSig(), this);
    }

    /**
     * 跳转到登录界面
     */
    @Override
    public void navToLogin() {
        Intent intent = new Intent(getApplicationContext(), HostLoginActivity.class);
        startActivityForResult(intent, LOGIN_RESULT_CODE);
    }

    /**
     * 是否已有用户登录
     */
    @Override
    public boolean isUserLogin() {
        return UserInfo.getInstance().getId() != null && (!TLSService.getInstance().needLogin(UserInfo.getInstance().getId()));
    }

    /**
     * imsdk登录失败后回调
     */
    @Override
    public void onError(int i, String s) {
        Log.d(TAG, "onError: " + i + "," + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navToLogin();
                    }
                });
                break;
            case 6200:
                Toast.makeText(this, getString(R.string.login_error_timeout), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
            default:
                Toast.makeText(this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                navToLogin();
                break;
        }

    }

    /**
     * imsdk登录成功后回调
     */
    @Override
    public void onSuccess() {

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();

        if (isFirst) {
            UserManager.needLoadingPager(false);
        }



        AVOConfig.login(UserManager.instance().identifier(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                Log.d(TAG, "done: " + avUser + "," + e);
            }
        });

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(Constants.EXTRA_FROM_LOGIN, getIntent().getBooleanExtra(Constants.EXTRA_FROM_LOGIN, false));
        intent.putExtra(Constants.EXTRA_FROM_REGISTER, getIntent().getBooleanExtra(Constants.EXTRA_FROM_REGISTER, false));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (LOGIN_RESULT_CODE == requestCode) {
            Log.d(TAG, "login error no " + TLSService.getLastErrno());
            if (0 == TLSService.getLastErrno()) {
                String id = TLSService.getInstance().getLastUserIdentifier();
                UserInfo.getInstance().setId(id);
                UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
                navToHome();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    delayInit();
                } else {
                    Toast.makeText(this, getString(R.string.need_permission), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private boolean isInit = false;

    //初始化当前需要使用的SDK服务，首次使用会延迟加载
    private void delayInit() {

        //防止多次初始化
        if (isInit)
            return;
        isInit = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isFirst = UserManager.isFirst();

                SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
                int loglvl = pref.getInt("loglvl", TIMLogLevel.ERROR.ordinal());
                //初始化IMSDK
                InitBusiness.start(App.myApp(), loglvl);
                //初始化TLS
                TlsBusiness.init(App.myApp());
                //设置刷新监听
                RefreshEvent.getInstance();

                IMBusiness();
            }
        }, 1000);
    }

    //验证登录等逻辑
    private void IMBusiness() {
        String id = TLSService.getInstance().getLastUserIdentifier();
        UserInfo.getInstance().setId(id);
        UserInfo.getInstance().setUserSig(TLSService.getInstance().getUserSig(id));
        presenter = new SplashPresenter(this);
        presenter.start();
    }


    /**
     * 判断小米推送是否已经初始化
     */
    private boolean shouldMiInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 清除所有通知栏通知
     */
    private void clearNotification() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.instance().putBoolean(Config.SP_IS_FIRST, true);
    }
}
