package dong.lan.smarttrip.presentation.viewfeatures;

import com.tencent.qcloud.presentation.viewfeatures.MvpView;

import java.util.List;

import dong.lan.smarttrip.model.Gonglve;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午8:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface GonglveView extends MvpView {
    void loadData();
    void refresh();
    void setupAdapter(List<Gonglve> gonglves);

    void resetAdapter(List<Gonglve> gonglves);

    void addToAdapter(List<Gonglve> gonglves);

    void showMessage(String text);

    void showLoadingView(boolean show);

    void updateListStatus();

}
