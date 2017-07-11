package dong.lan.weather.view;

import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;

import dong.lan.labelTextview.LabelTextView;
import dong.lan.weather.R;
import dong.lan.weather.ToastUtil;
import dong.lan.weather.WeatherRealm;
import dong.lan.weather.bean.CityDeleteEvent;
import dong.lan.weather.bean.CityParse;
import dong.lan.weather.bean.WeatherCity;
import dong.lan.weather.bean.WeatherItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 17-7-6 ： 下午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CityManagerFragment extends Fragment {


    private Realm realm;

    public static Fragment newInstance() {
        Bundle bundle = new Bundle();
        bundle.putLong("id", 0x124);
        CityManagerFragment fragment = new CityManagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private View content;
    private RecyclerView myCityList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (content == null) {
            content = inflater.inflate(R.layout.fragment_manager_city, container, false);
            myCityList = (RecyclerView) content.findViewById(R.id.city_list);
            myCityList.setLayoutManager(new GridLayoutManager(getContext(), 3));
            Realm realm = WeatherRealm.get().realm();
            realm.beginTransaction();
            cityList.addAll(realm.copyFromRealm(realm.where(WeatherItem.class)
                    .findAll()));
            realm.commitTransaction();
            if (cityList == null || cityList.isEmpty()) {
                ToastUtil.show(getContext(), "右滑添加一个天气城市");
            }
            myCityList.setAdapter(new CityAdapter());
        }
        return content;
    }


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


    private List<WeatherItem> cityList = new ArrayList<>();

    private class CityAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_my_city, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            WeatherItem city = cityList.get(position);
            holder.city.setText(city.city());
        }

        @Override
        public int getItemCount() {
            return cityList.size();
        }
    }


    private class ViewHolder extends RecyclerView.ViewHolder {

        TextView city;

        public ViewHolder(View itemView) {
            super(itemView);
            ALog.d(itemView.getClass().getSimpleName());
            city = (TextView) itemView.findViewById(R.id.item_my_city_tv);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    deleteCity(getLayoutPosition());
                    return true;
                }
            });
        }
    }

    private void deleteCity(final int position) {
        final WeatherItem weatherCity = cityList.get(position);
        new AlertDialog.Builder(getContext())
                .setTitle("删除天气")
                .setMessage("你确定删除 " + weatherCity.city() + " 的天气订阅吗？")
                .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cityList.remove(position);
                        myCityList.getAdapter().notifyItemRemoved(position);
                        int id = weatherCity.id.hashCode();
                        Realm realm = WeatherRealm.get().realm();
                        realm.beginTransaction();
                        realm.where(WeatherItem.class).equalTo("id", weatherCity.id())
                                .findAll().deleteAllFromRealm();
                        realm.commitTransaction();
                        realm.close();
                        EventBus.getDefault().post(new CityDeleteEvent(id));
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
