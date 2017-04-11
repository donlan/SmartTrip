package dong.lan.smarttrip.common;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


import java.util.List;

/**
 * Created by 梁桂栋 on 17-3-4 ： 上午12:40.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class EasyFragmentManager {
    private FragmentManager fragmentManager;
    private FragmentTransaction mCurTransaction = null;
    private List<Fragment> fragments;
    private Fragment curFragment = null;

    private int count = 0;
    private int curIndex = -1;

    private int containerId;

    public EasyFragmentManager(int containerId, @NonNull FragmentManager fragmentManager, @NonNull List<Fragment> fragments) {
        this.fragmentManager = fragmentManager;
        this.fragments = fragments;
        count = fragments.size();
        this.containerId = containerId;
        show(0);
    }

    public void show(int position) {
        if (position > count - 1 || position == curIndex)
            return;

        if (mCurTransaction == null) {
            mCurTransaction = fragmentManager.beginTransaction();
        }
        String name = makeFragmentName(containerId, position);
        Fragment fragment = fragmentManager.findFragmentByTag(name);
        if (curFragment != null) {
            curFragment.setMenuVisibility(false);
            curFragment.setUserVisibleHint(false);
            mCurTransaction.detach(curFragment);
        }
        if (fragment != null) {
            mCurTransaction.attach(fragment);
        } else {
            fragment = fragments.get(position);
            mCurTransaction.add(containerId, fragment,
                    makeFragmentName(containerId, position));
        }
        fragment.setMenuVisibility(true);
        fragment.setUserVisibleHint(true);
        curFragment = fragment;
        mCurTransaction.commit();
        mCurTransaction = null;
        curIndex = position;
    }

    private String makeFragmentName(int viewId, long id) {
        return "android:switcher:" + viewId + ":" + id;
    }
}
