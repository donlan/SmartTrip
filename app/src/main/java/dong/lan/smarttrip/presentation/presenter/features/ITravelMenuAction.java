package dong.lan.smarttrip.presentation.presenter.features;

/**
 * Created by 梁桂栋 on 17-2-23 ： 下午5:12.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelMenuAction {

    void createNewTravel();

    void joinTravel();

    void startQRScan();

    void handleScanResult(String scanRes);
}
