package dong.lan.smarttrip.adapters.base;

import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 梁桂栋 on 17-2-11 ： 下午11:41.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public abstract class AbstractBinder<T> implements AdapterBinder<T> {

    private List<T> data;
    private List<T> cache;
    private boolean isCache = false;
    protected BinderClickListener<T> clickListener;
    private BaseAdapter<T> baseAdapter;

    @Override
    public void setCache(List<T> cache) {
        this.cache = cache;
    }

    @Override
    public void showCache(boolean showCache) {
        isCache = showCache;
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void init(List<T> data) {
        this.data = data;
    }

    @Override
    public T valueAt(int position) {
        if (isCache)
            return cache.get(position);
        return data.get(position);
    }

    @Override
    public int size() {
        if (isCache)
            return cache == null ? 0 : cache.size();
        return data == null ? 0 : data.size();
    }

    @Override
    public BaseAdapter<T> build() {
        baseAdapter = new BaseAdapter<>(this);
        return baseAdapter;
    }


}
