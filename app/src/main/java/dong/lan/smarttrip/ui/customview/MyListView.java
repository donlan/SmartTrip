package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import dong.lan.smarttrip.R;

/**
 * Created by 梁桂栋 on 16-11-3 ： 上午11:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class MyListView extends ListView implements AbsListView.OnScrollListener {

    public static final int STATUS_HEADER = 1;
    public static final int STATUS_FOOTER = 2;
    public static final int STATUS_NONE = 0;
    private static final String TAG = "MyListView";

    private int status = STATUS_NONE;
    private int loadingTag = STATUS_NONE;

    private volatile boolean isLoading = false;

    private View header;
    private View footer;

    public MyListView(Context context) {
        this(context, null);
    }

    public MyListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        header = LayoutInflater.from(getContext()).inflate(R.layout.list_header, null);
        footer = LayoutInflater.from(getContext()).inflate(R.layout.list_footer, null);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (view.getFirstVisiblePosition() == 0 ) {
            status = STATUS_HEADER;
        } else if (view.getLastVisiblePosition() == count-1 ) {
            status = STATUS_FOOTER;
        } else
            status = STATUS_NONE;
    }

    int count;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        count = totalItemCount;
    }

    private void refresh() {
        if (isLoading)
            return;
        isLoading = true;
        addHeaderView(header);
        loadingTag = STATUS_HEADER;
        if (listener != null)
            listener.onRefresh();
    }

    private void loadMore() {
        if (isLoading)
            return;
        isLoading = true;
        addFooterView(footer);
        loadingTag = STATUS_FOOTER;
        if (listener != null)
            listener.onLoadMore();
    }

    float y = 0;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int ac = ev.getAction();
        switch (ac) {
            case MotionEvent.ACTION_DOWN:
                y = ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                    y = y - ev.getY();
                    if (y < -20 && status == STATUS_HEADER) {
                        refresh();
                    } else if (y > 20 && status == STATUS_FOOTER) {
                        loadMore();
                    }
                break;
        }

        return super.onTouchEvent(ev);
    }


    public void setLoading(boolean isLoading) {
        this.isLoading = isLoading;
        if (!isLoading) {
            if (loadingTag == STATUS_HEADER) {
                removeHeaderView(header);
            } else if (loadingTag == STATUS_FOOTER) {
                removeFooterView(footer);
            }
            loadingTag = STATUS_NONE;
        }
    }

    private LoadingListener listener;

    public void setLoadingListener(LoadingListener loadingListener) {
        this.listener = loadingListener;
    }

    public interface LoadingListener {
        void onRefresh();

        void onLoadMore();
    }
}
