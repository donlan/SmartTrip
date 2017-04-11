package dong.lan.smarttrip.ui.travel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.presentation.presenter.TravelBagPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.TravelBagView;

/**
 * Created by 梁桂栋 on 16-10-8 ： 下午4:15.
 * Email:       760625325@qqcom
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelBagFragment extends Fragment implements TravelBagView {

    TravelBagPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_travel_bag,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new TravelBagPresenter(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
