package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AbstractBinder;
import dong.lan.smarttrip.adapters.base.BaseAdapter;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.bean.world.City;

/**
 * Created by 梁桂栋 on 17-2-5 ： 上午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CityBinder extends AbstractBinder<City> {

    private CheckBox lastCheck = null;
    private City city;

    @Override
    public ViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<City> clickListener) {
        this.clickListener = clickListener;
    }

    public City getCity() {
        return city;
    }

    @Override
    public BaseAdapter<City> build() {
        return new BaseAdapter<>(this);
    }

    public class ViewHolder extends BaseHolder<City> {


        private final CheckBox cityContent;

        public ViewHolder(View itemView) {
            super(itemView);
            cityContent = (CheckBox) itemView.findViewById(R.id.item_city_content);
            cityContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastCheck == null) {
                        lastCheck = (CheckBox) v;
                    } else {
                        lastCheck.setChecked(false);
                        lastCheck = (CheckBox) v;
                    }
                    city = null;
                    city = valueAt(getLayoutPosition());
                }
            });
        }

        @Override
        public void bindData(City city) {
                cityContent.setText(city.getName()+"("+city.getOrigin()+")");
        }
    }
}
