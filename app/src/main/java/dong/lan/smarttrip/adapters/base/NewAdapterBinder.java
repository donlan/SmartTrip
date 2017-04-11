package dong.lan.smarttrip.adapters.base;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-4 ： 下午11:00.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public interface NewAdapterBinder<AdapterData> {

    void init(List<AdapterData> data);

    BaseHolder<AdapterData> bindViewHolder(ViewGroup parent, int viewType);

    AdapterData valueAt(int position);

    BaseAdapter<AdapterData> build();

    int size();

    void setBinderClickListener(BinderClickListener<AdapterData> clickListener);
}
