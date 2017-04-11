package dong.lan.smarttrip.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.presentation.presenter.RoadPathPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.RoadPathView;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午5:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class RoadPathFragment extends Fragment implements RoadPathView {

    RoadPathPresenter presenter  = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_road_path,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new RoadPathPresenter(this);
    }
}
