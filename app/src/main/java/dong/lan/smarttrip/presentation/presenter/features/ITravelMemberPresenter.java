package dong.lan.smarttrip.presentation.presenter.features;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午5:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelMemberPresenter {

    void start(String travelId);

    void delete(String userId,int position);
}
