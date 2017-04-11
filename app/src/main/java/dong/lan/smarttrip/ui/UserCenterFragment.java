package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.qcloud.ui.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.presentation.presenter.UserCenterPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.UserCenterView;
import com.tencent.qcloud.ui.base.BaseFragment;
import dong.lan.smarttrip.ui.im.FriendsActivity;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午4:15.
 * Email:       760625325@qqcom
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class UserCenterFragment extends BaseFragment implements UserCenterView {


    public static BaseFragment newInstance(String tittle) {
        UserCenterFragment fragment = new UserCenterFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }


    @BindView(R.id.me_avatar)
    CircleImageView avatar;
    @BindView(R.id.me_username)
    TextView username;
    @BindView(R.id.me_identifier)
    TextView identifier;
    @BindView(R.id.me_signature)
    TextView signature;

    @OnClick(R.id.me_qrcode)
    void toMeQRCodeAc() {
        Intent codeIntent = new Intent(getActivity(), QRCodeActivity.class);
        String content = Config.USER_QRCODE_URL + "/add/" + UserManager.instance().identifier();
        codeIntent.putExtra(Config.BAR_TITTLE, "我的二维码");
        codeIntent.putExtra(Config.QRCODE_CONTENT, content);
        startActivity(codeIntent);
    }


    @OnClick(R.id.me_about)
    void toAboutAc() {

    }

    @OnClick(R.id.me_favorite)
    void toFavoriteAc() {

    }

    @OnClick(R.id.me_feedback)
    void toFeedbackAc() {

    }

    @OnClick(R.id.me_friends)
    void toFriendsAc() {
        startActivity(new Intent(getActivity(), FriendsActivity.class));
    }

    @OnClick(R.id.me_setting)
    void toSettingAc() {

    }

    @OnClick(R.id.me_logout)
    void logout() {
        UserManager.instance().logout();
        getActivity().finish();
    }


    UserCenterPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(content == null) {
            content = inflater.inflate(R.layout.fragment_me, container, false);
            bindView(content);
        }
        return content;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new UserCenterPresenter(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
