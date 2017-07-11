package dong.lan.weather.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.ALog;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dong.lan.labelTextview.LabelTextView;
import dong.lan.weather.Api;
import dong.lan.weather.R;
import dong.lan.weather.ToastUtil;
import dong.lan.weather.WeatherRealm;
import dong.lan.weather.bean.WeatherCity;
import dong.lan.weather.bean.CityParse;
import dong.lan.weather.bean.WeatherItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-7-6 ： 下午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CityQueryFragment extends Fragment {


    private Realm realm;

    public static Fragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putLong("id", 0x123);
        CityQueryFragment fragment = new CityQueryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private View content;
    private TextView locationCityTv;
    private EditText cityQueryEt;
    private RecyclerView cityResultList;
    private LabelTextView commit;
    private String curCityId = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (content == null) {
            content = inflater.inflate(R.layout.fragment_city_query, container, false);
            locationCityTv = (TextView) content.findViewById(R.id.weather_cur_loc_city);
            cityQueryEt = (EditText) content.findViewById(R.id.weather_search);
            cityResultList = (RecyclerView) content.findViewById(R.id.weather_search_result);
            cityQueryEt.addTextChangedListener(textWatcher);
            commit = (LabelTextView) content.findViewById(R.id.city_commit);
            cityResultList.setLayoutManager(new GridLayoutManager(getContext(), 1));

            commit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commitCurrentCity();
                }
            });

            Realm realm = WeatherRealm.get().realm();
            realm.beginTransaction();
            long c = realm.where(WeatherCity.class).count();
            realm.commitTransaction();
            if (c <= 0) {
                ToastUtil.show(getContext(), "为你初始化城市景点数据中..");
                CityParse.parse(getContext());
            }

        }
        return content;
    }

    /**
     * 订阅当前选中城市的天气信息
     */
    private void commitCurrentCity() {
        if (TextUtils.isEmpty(curCityId)) {
            new AlertDialog.Builder(getContext())
                    .setMessage("请先选者一个城市或者景点")
                    .show();
        } else {
            realm = WeatherRealm.get().realm();
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    WeatherItem weatherItem = realm.where(WeatherItem.class).equalTo("id", curCityId)
                            .findFirst();
                    if (weatherItem == null) {
                        weatherItem = new WeatherItem();
                        weatherItem.id = curCityId;
                        weatherItem.lastUpdateTime = System.currentTimeMillis();
                        weatherItem.weatherCity = realm.where(WeatherCity.class).equalTo("id", curCityId).findFirst();
                        realm.copyToRealm(weatherItem);
                        EventBus.getDefault().post(weatherItem);
                    }
                }
            });

        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 0) {
                if (realm == null)
                    realm = WeatherRealm.get().realm();
                if (!realm.isClosed()) {
                    realm.beginTransaction();
                    RealmResults<WeatherCity> cities = realm.where(WeatherCity.class).like("name", s.toString()).findAll();
                    realm.commitTransaction();
                    if (cities.isEmpty()) {
                        ToastUtil.show(getContext(), "无匹配的地点");
                    } else {
                        cityList.clear();
                        cityList.addAll(cities);
                        if (cityResultList.getAdapter() == null) {
                            cityResultList.setAdapter(new CityAdapter());
                        }
                        cityResultList.getAdapter().notifyDataSetChanged();
                        ALog.d(cityList.size());
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (realm != null)
            realm.close();
        realm = null;
        cityList.clear();
        cityList = null;
    }


    private List<WeatherCity> cityList = new ArrayList<>();

    private class CityAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_weather_city, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WeatherCity city = cityList.get(position);
            holder.name.setText(city.name);
            holder.letter.setText(city.letter);
            ALog.d(city.name);
        }

        @Override
        public int getItemCount() {
            return cityList.size();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView letter;

        public ViewHolder(View itemView) {
            super(itemView);
            ALog.d(itemView.getClass().getSimpleName());
            name = (TextView) itemView.findViewById(R.id.item_query_city_name);
            letter = (TextView) itemView.findViewById(R.id.item_query_city_letter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCity(cityList.get(getLayoutPosition()));
                }
            });
        }
    }

    private void selectCity(WeatherCity weatherCity) {
        curCityId = weatherCity.id;
        locationCityTv.setText(weatherCity.name);
    }
}
