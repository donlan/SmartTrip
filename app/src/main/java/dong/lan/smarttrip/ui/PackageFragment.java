package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.base.BaseFragment;

import dong.lan.smarttrip.ui.customview.TabItemRecycleView;

/**
 * Created by 梁桂栋 on 17-1-18 ： 下午9:52.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class PackageFragment extends BaseFragment implements TabItemRecycleView.TabClickListener {


    public static BaseFragment newInstance(String tittle) {
        PackageFragment fragment = new PackageFragment();
        Bundle b = new Bundle();
        b.putString(KEY_TITTLE, tittle);
        fragment.setArguments(b);
        return fragment;
    }


    private TabItemRecycleView packageList;
    private String[] itemDescTexts = new String[]{
            "物品清单", "天气查询", "时刻表", "货币兑换",
            "语音翻译", "电子导航", "行程记账", "更多"};
    private int[] itemSrcIds = new int[]{
            R.drawable.tool_goods_list, R.drawable.tool_weather,
            R.drawable.tool_airplane, R.drawable.tool_money_exchange,
            R.drawable.tool_translate, R.drawable.tool_navigation,
            R.drawable.tool_tally, R.drawable.tool_more};
    private int[] itemBgValues = new int[]{
            0xff4caf50, 0xfff67f23, 0xff4e96ea, 0xff9c27b0,
            0xff4e96e9, 0xff339900, 0xff8b572a, 0xff00bcd4};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (content == null) {
            content = inflater.inflate(R.layout.fragment_package, container, false);
            packageList = (TabItemRecycleView) content.findViewById(R.id.package_list);
            packageList.init(itemDescTexts, itemSrcIds, itemBgValues, this);
        }
        return content;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        packageList = null;
    }


    @Override
    public void OnClick(String val, int position) {
        switch (position) {
            case 0: //物品清单
                startActivity(new Intent(getContext(),CheckListActivity.class));
                break;
            case 1: //天气

                break;
            case 2: //时刻表

                break;
            case 3://货币兑换

                break;

            case 4: //翻译

                break;
            case 5://导航

                break;
            case 6://记账

                break;
        }
    }
}
