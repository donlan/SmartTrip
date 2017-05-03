package dong.lan.smarttrip.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.blankj.ALog;
import com.tencent.TIMFriendGenderType;
import com.tencent.TIMFriendshipManager;
import com.tencent.TIMUserProfile;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.tlslibrary.service.Constants;

import java.util.ArrayList;
import java.util.List;

import dong.lan.avoscloud.model.AVOUser;
import dong.lan.model.bean.user.User;
import dong.lan.model.permission.Permission;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.DelayInitView;
import dong.lan.smarttrip.common.EasyFragmentManager;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.presentation.presenter.HomePresenter;
import dong.lan.smarttrip.presentation.viewfeatures.HomeView;
import dong.lan.smarttrip.task.FirstConfigTask;
import dong.lan.smarttrip.task.TaskCallback;
import dong.lan.smarttrip.ui.travel.TravelFragment;
import io.realm.Realm;

/**
 * Tab页主界面
 */
public class HomeActivity extends AppCompatActivity implements HomeView, BottomNavigationBar.OnTabSelectedListener {
    private static final String TAG = HomeActivity.class.getSimpleName();

    private BottomNavigationBar bnb;
    private BottomNavigationItem bni[] = new BottomNavigationItem[4];
    private BadgeItem[] badgeItems = new BadgeItem[4];
    private FragmentManager fragmentManager;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);
        initView();
        presenter = new HomePresenter(this);

    }

    private void initView() {

        fragmentManager = getSupportFragmentManager();

        badgeItems[0] = new BadgeItem()
                .setBackgroundColor(Color.RED)
                .setBorderColor(Color.WHITE)
                .setTextColor(Color.WHITE).hide();
        badgeItems[1] = new BadgeItem()
                .setBackgroundColor(Color.RED)
                .setBorderColor(Color.WHITE)
                .setTextColor(Color.WHITE).hide();
        badgeItems[2] = new BadgeItem()
                .setBackgroundColor(Color.RED)
                .setBorderColor(Color.WHITE)
                .setTextColor(Color.WHITE).hide();
        badgeItems[3] = new BadgeItem()
                .setBackgroundColor(Color.RED)
                .setBorderColor(Color.WHITE)
                .setTextColor(Color.WHITE).hide();

        bni[0] = new BottomNavigationItem(R.drawable.bottom_bar_travel, "旅行")
                .setBadgeItem(badgeItems[0]).setActiveColor(getResources().getColor(R.color.colorPrimary));
        bni[1] = new BottomNavigationItem(R.drawable.bottom_bar_package, "行囊")
                .setBadgeItem(badgeItems[1]).setActiveColor(getResources().getColor(R.color.colorPrimary));
        bni[2] = new BottomNavigationItem(R.drawable.bottom_bar_trip, "游历")
                .setBadgeItem(badgeItems[2]).setActiveColor(getResources().getColor(R.color.colorPrimary));
        bni[3] = new BottomNavigationItem(R.drawable.bottom_bar_me, "我")
                .setBadgeItem(badgeItems[3]).setActiveColor(getResources().getColor(R.color.colorPrimary));

        bnb = (BottomNavigationBar) findViewById(R.id.bottom_bar);
        bnb.setBarBackgroundColor(R.color.white);
        bnb.setTabSelectedListener(this);
        bnb.addItem(bni[0]);
        bnb.addItem(bni[1]);
        bnb.addItem(bni[2]);
        bnb.addItem(bni[3]);


        List<Fragment> fragments = new ArrayList<>(4);

        fragments.add(TravelFragment.newInstance("旅行"));
        fragments.add(PackageFragment.newInstance("行囊"));
        fragments.add(TripFragment.newInstance("游历"));
        fragments.add(UserCenterFragment.newInstance("我"));


        final DelayInitView delayInitView = (DelayInitView) fragments.get(0);
        easyFragmentManager = new EasyFragmentManager(R.id.home_view_container, getSupportFragmentManager(), fragments);
        bnb.selectTab(0, true);
        bnb.initialise();
        //是否是第一次注册跳转过来的
        boolean isFromRegister = getIntent().getBooleanExtra(Constants.EXTRA_FROM_REGISTER, false);
        boolean isFromLogin = getIntent().getBooleanExtra(Constants.EXTRA_FROM_LOGIN, false);

        if (isFromRegister) {
            doFirstConfig(delayInitView); //注册跳过过来,需要做一次服务配置:1.用户存储文件路径创建(COS)
        } else {
            normalInit(delayInitView);
        }

        App.myApp().start(0);


    }

    /**
     * 正常进入给页面后进行的初始化:初始化用户信息
     */
    private void normalInit(final DelayInitView delayInitView) {
        AVOUser avoUser = AVOUser.getCurrentUser(AVOUser.class);
        UserManager.instance().initAvoUser(avoUser);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                User user = realm.where(User.class).equalTo("identifier", UserManager.instance().identifier()).findFirst();
                if (user == null) {
                    TIMFriendshipManager.getInstance().getSelfProfile(new TIMValueCallBack<TIMUserProfile>() {
                        @Override
                        public void onError(int i, String s) {
                            ALog.d(TAG, "onError: " + i + "," + s);
                        }

                        @Override
                        public void onSuccess(final TIMUserProfile timUserProfile) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.where(User.class).equalTo("identifier", timUserProfile.getIdentifier()).findAll().deleteAllFromRealm();
                            User user = new User();
                            user.setIdentifier(timUserProfile.getIdentifier());
                            user.setAvatarUrl(timUserProfile.getFaceUrl());
                            user.setSex(timUserProfile.getGender() == TIMFriendGenderType.Male ? 1 : 0);
                            user.setUsername(timUserProfile.getIdentifier());
                            user.setNickname(timUserProfile.getNickName());
                            user.setRemark(timUserProfile.getRemark());
                            user.setRole(Permission.LEVEL_OTHER);
                            user.setType(User.TYPE_SELF);
                            realm.copyToRealmOrUpdate(user);
                            UserManager.instance().initUser(user);
                            realm.commitTransaction();
                            delayInitView.start(null);
                        }
                    });
                } else {
                    UserManager.instance().initUser(realm.copyFromRealm(user));
                    delayInitView.start(null);
                }
            }
        });
    }

    private void doFirstConfig(final DelayInitView delayInitView) {
        new FirstConfigTask(new TaskCallback<String>() {
            @Override
            public void onTackCallback(String data) {
                delayInitView.start(null);
            }
        }).execute(UserManager.instance().identifier());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_more, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread) {
        if (noUnread)
            badgeItems[0].hide();
        else
            badgeItems[0].show();
    }


    private EasyFragmentManager easyFragmentManager;

    @Override
    public void onTabSelected(int position) {
        easyFragmentManager.show(position);
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public Activity activity() {
        return this;
    }
}
