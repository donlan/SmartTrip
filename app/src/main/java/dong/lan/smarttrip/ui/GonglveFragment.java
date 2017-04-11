package dong.lan.smarttrip.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.model.Gonglve;
import dong.lan.smarttrip.presentation.presenter.GonglvePresenter;
import dong.lan.smarttrip.presentation.viewfeatures.GonglveView;
import dong.lan.smarttrip.ui.customview.MyListView;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午9:27.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GonglveFragment extends Fragment implements GonglveView {


    private static final String TAG = "GonglveFragment";

    GonglvePresenter presenter;
    Unbinder unbinder;
    Adapter adapter;

    @BindView(R.id.loading_pb)
    ProgressBar loadingBar;
    @BindView(R.id.gonglve_list)
    MyListView listView;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_gonglve, container, false);
            unbinder = ButterKnife.bind(this, view);
            loadData();
            listView.setLoadingListener(new MyListView.LoadingListener() {
                @Override
                public void onRefresh() {
                    presenter.refresh();
                }

                @Override
                public void onLoadMore() {
                    presenter.loadMore();
                }
            });
        }
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new GonglvePresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void loadData() {
        showLoadingView(true);
        presenter.loadDate();
    }

    @Override
    public void refresh() {
        showLoadingView(true);
    }

    @Override
    public void setupAdapter(List<Gonglve> gonglves) {
        if (adapter == null)
            adapter = new Adapter(gonglves);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showMessage(adapter.getItem(position).toString());
                Intent intent = new Intent(getContext(),GonglveDetailActivity.class);
                intent.putExtra("URL",((Gonglve)adapter.getItem(position)).getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    public void resetAdapter(List<Gonglve> gonglves) {
        adapter.gonglves.removeAll(gonglves);
        adapter.gonglves.addAll(gonglves);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addToAdapter(List<Gonglve> gonglves) {
        int index = adapter.getCount();
        adapter.gonglves.addAll(gonglves);
        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(index);
    }

    @Override
    public void showMessage(String text) {
        Snackbar.make(getView(), text, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingView(boolean show) {
        loadingBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void updateListStatus() {
        listView.setLoading(false);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        view = null;
    }

    class Adapter extends BaseAdapter {
        List<Gonglve> gonglves;

        public Adapter(List<Gonglve> gonglves) {
            this.gonglves = gonglves;

        }

        @Override
        public int getCount() {
            return gonglves == null ? 0 : gonglves.size();
        }

        @Override
        public Object getItem(int position) {
            return gonglves.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return convertView;
        }

        class Holder {
            TextView tittle;
            TextView text;

            public Holder(TextView tittle, TextView text) {
                this.tittle = tittle;
                this.text = text;
            }
        }
    }
}
