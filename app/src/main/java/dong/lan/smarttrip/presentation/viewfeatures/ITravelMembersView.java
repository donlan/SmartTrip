package dong.lan.smarttrip.presentation.viewfeatures;

import com.tencent.qcloud.ui.base.ProgressView;

import java.util.List;

import dong.lan.model.features.IUserInfo;

/**
 * Created by 梁桂栋 on 17-2-27 ： 下午5:48.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelMembersView extends ProgressView {

    void initMembersList(List<IUserInfo> users);

    void removeUser(int position);
}
