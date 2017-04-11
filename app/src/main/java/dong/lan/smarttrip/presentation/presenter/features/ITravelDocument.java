package dong.lan.smarttrip.presentation.presenter.features;

import dong.lan.model.bean.travel.Document;

/**
 * Created by 梁桂栋 on 17-2-22 ： 下午2:10.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface ITravelDocument {

    void init(String travelId);

    void saveDoc(String url, String path, int level);

    void deleteDoc(Document document);

    void openDocument(Document document);

}
