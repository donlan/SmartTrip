package base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-24 ： 下午10:51.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class ListFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public ListFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments ==null ? 0 : fragments.size();
    }


    @Override
    public long getItemId(int position) {
        return fragments.get(position).getArguments().getLong("id");
    }
}
