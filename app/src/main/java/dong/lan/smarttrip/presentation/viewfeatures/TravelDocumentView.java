package dong.lan.smarttrip.presentation.viewfeatures;

import dong.lan.smarttrip.base.ProgressView;

import dong.lan.model.bean.travel.Document;
import dong.lan.smarttrip.presentation.presenter.features.IActivityFeature;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-2-10 ： 上午12:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface TravelDocumentView extends ProgressView ,IActivityFeature{


    void initDocList(RealmResults<Document> documents, int userLevel);

    void refreshDocList(int position, int action);

}
