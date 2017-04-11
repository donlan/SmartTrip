package dong.lan.smarttrip.ui.customview.popupView;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.ui.customview.RecycleViewDivider;

/**
 * Created by 梁桂栋 on 17-1-29 ： 下午2:16.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class MenuPopup {

    private static final int defaultLayoutRes = R.layout.item_menu;
    private static final String TAG = MenuPopup.class.getSimpleName();

    private PopupWindow popupWindow;
    private RecyclerView itemListView;
    private List<Binder> items;

    private int mLayoutRes;

    public MenuPopup(Context context) {
        popupWindow = new PopupWindow(context);
        popupWindow.setOutsideTouchable(true);
        if (Build.VERSION.SDK_INT >= 21) {
            popupWindow.setElevation(12f);
        }

        popupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.popup_bg));
        itemListView = new RecyclerView(context);
        itemListView.setLayoutManager(new LinearLayoutManager(context));

        itemListView.addItemDecoration(new RecycleViewDivider(context,
                LinearLayoutManager.VERTICAL, 10, Color.DKGRAY));
        popupWindow.setContentView(itemListView);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mLayoutRes = defaultLayoutRes;
    }


    public MenuPopup show(View anchor, int xoff, int yoff, int gravity) {
        if (popupWindow.isShowing())
            return this;

        if (Build.VERSION.SDK_INT >= 19) {
            popupWindow.showAsDropDown(anchor, xoff, yoff, gravity);
        } else {
            popupWindow.showAsDropDown(anchor, xoff, yoff);
        }
        return this;
    }

    public MenuPopup build() {
        ItemAdapter adapter = new ItemAdapter(items, mLayoutRes);
        itemListView.setAdapter(adapter);
        return this;
    }

    public boolean dismiss() {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        }
        return false;
    }

    public MenuPopup addItem(Binder item) {
        if (items == null)
            items = new ArrayList<>();
        items.add(item);
        return this;
    }


    public MenuPopup setItemViewLayoutRes(int layoutRes) {
        mLayoutRes = layoutRes;
        return this;
    }

    public MenuPopup setPopupBg(Drawable popupBg) {
        popupWindow.setBackgroundDrawable(popupBg);
        return this;
    }


    private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Binder> data;
        private int layoutRes;

        public ItemAdapter(List<Binder> data, int layoutRes) {
            this.data = data;
            this.layoutRes = layoutRes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return bindHolder(parent, viewType, layoutRes);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Object listener = holder.itemView.getTag();
            if (listener == null) {
                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null)
                            clickListener.onClick(holder.getLayoutPosition());
                    }
                };
                holder.itemView.setOnClickListener(l);
                holder.itemView.setTag(l);
            }
            data.get(position).bingData(holder, position);
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }


    public RecyclerView.ViewHolder bindHolder(ViewGroup parent, int viewType, int layoutRes) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutRes, null);
        return new MyViewHolder(view);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView text;

        public MyViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_menu_icon);
            text = (TextView) itemView.findViewById(R.id.item_menu_text);
        }
    }

    public MenuPopup setClickListener(ItemClickListener listener) {
        this.clickListener = listener;
        return this;
    }

    private ItemClickListener clickListener;

    public interface ItemClickListener {
        void onClick(int position);
    }

    public interface Binder {
        void bingData(RecyclerView.ViewHolder holder, int position);
    }


    public static class ItemBean implements Binder {
        int icon;
        String text;

        public ItemBean(int icon, String text) {
            this.text = text;
            this.icon = icon;
        }

        @Override
        public void bingData(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder mHolder = (MyViewHolder) holder;
            mHolder.icon.setImageResource(icon);
            mHolder.text.setText(text);
        }
    }

}
