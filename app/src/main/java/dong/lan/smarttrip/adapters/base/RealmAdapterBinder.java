package dong.lan.smarttrip.adapters.base;

import android.view.ViewGroup;

import java.util.List;

import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午11:00.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface RealmAdapterBinder<T extends RealmModel> {

    void init(RealmResults<T> data);

    void setCache(List<T> cache);

    void showCache(boolean showCache);

    BaseHolder<T> bindViewHolder(ViewGroup parent, int viewType);

    T valueAt(int position);

    BaseRealmAdapter<T> build();

    int size();

    void setBinderClickListener(BinderClickListener<T> clickListener);
}
