package dong.lan.smarttrip.ui.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.model.Config;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.model.bean.notice.NoticeShow;
import dong.lan.smarttrip.base.BaseFragment;
import dong.lan.smarttrip.ui.customview.TabItemRecycleView;
import dong.lan.smarttrip.ui.notice.NoticeListActivity;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午9:27.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelingInfoFragment extends BaseFragment implements TabItemRecycleView.TabClickListener {
    private String[] tabTexts = new String[]{"团队名单", "旅行文件", "房间安排", "紧急电话", "旅行修改", "二维码","集合","通知"};
    private int[] tabSrcIds = new int[]{R.drawable.two_user, R.drawable.file,
            R.drawable.house, R.drawable.mobile, R.drawable.setting_bs,
            R.drawable.qrcode_white,R.drawable.gather_icon_40,R.drawable.notice_icon_40};
    private int[] tabBg = new int[]{0xff4caf50, 0xfff67f23, 0xff4e96ea,
            0xfff44336, 0xffd81e06, 0xff1296db,0xff28CC9E,0xff1ca606};


    public static TravelingInfoFragment newInstance(String travelId) {
        TravelingInfoFragment infoFragment = new TravelingInfoFragment();
        Bundle data = new Bundle();
        data.putString(Config.TRAVEL_ID, travelId);
        infoFragment.setArguments(data);
        return infoFragment;
    }

    @BindView(R.id.travel_info_tab_list)
    TabItemRecycleView infoTabs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_travel_info_center, container, false);
            bindView(content);
            infoTabs.init(tabTexts, tabSrcIds, tabBg, this);
        }
        return content;
    }


    @Override
    public void OnClick(String val, int position) {
        switch (position) {
            case 0: //旅行名单
                Intent gmIntent = new Intent(getContext(), TravelMembersActivity.class);
                gmIntent.putExtra(Config.TRAVEL_ID,getArguments().getString(Config.TRAVEL_ID));
                startActivity(gmIntent);
                break;
            case 1: //旅行文件
                Intent pickDocIntent = new Intent(getContext(), TravelDocActivity.class);
                pickDocIntent.putExtra(Config.TRAVEL_ID, getArguments().getString(Config.TRAVEL_ID));
                startActivity(pickDocIntent);
                break;

            case 2: //房间安排

                break;
            case 3: //紧急电话

                break;
            case 4: //旅行修改
                Intent editIntent = new Intent(getContext(),AddTravelActivity.class);
                editIntent.putExtra(Config.TRAVEL_ID,getArguments().getString(Config.TRAVEL_ID));
                startActivity(editIntent);
                break;
            case 5: //旅行二维码
                Intent shareIntent = new Intent(getContext(), TravelShareActivity.class);
                shareIntent.putExtra(Config.TRAVEL_ID, getArguments().getString(Config.TRAVEL_ID));
                startActivity(shareIntent);
                break;
            case 6:
                Intent intent = new Intent(getContext(), NoticeListActivity.class);
                intent.putExtra(Config.NOTICE_TYPE, NoticeShow.TYPE_GATHER);
                intent.putExtra(Config.BAR_TITTLE,"集合");
                intent.putExtra(Config.TRAVEL_ID,getArguments().getString(Config.TRAVEL_ID));
                intent.putExtra(Config.IDENTIFIER,UserManager.instance().identifier());
                startActivity(intent);
                break;
            case 7:
                Intent noticeIntent = new Intent(getContext(), NoticeListActivity.class);
                noticeIntent.putExtra(Config.NOTICE_TYPE, NoticeShow.TYPE_NOTICE);
                noticeIntent.putExtra(Config.BAR_TITTLE,"通知");
                noticeIntent.putExtra(Config.TRAVEL_ID,getArguments().getString(Config.TRAVEL_ID));
                noticeIntent.putExtra(Config.IDENTIFIER,UserManager.instance().identifier());
                startActivity(noticeIntent);
                break;

        }
    }
}
