package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dong.lan.smarttrip.R;

/**
 * Created by 梁桂栋 on 17-1-25 ： 下午9:58.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TabIndexRecycleView extends RecyclerView {

    private String[] texts;

    private TextView lastText = null;

    public TabIndexRecycleView(Context context) {
        this(context, null);
    }

    public TabIndexRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndexRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
    }


    public void init(String[] texts, TabClickListener listener) {
        this.texts = texts;
        this.listener = listener;
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab_index, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tabText.setText(texts[position]);
        }

        @Override
        public int getItemCount() {
            return texts == null ? 0 : texts.length;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {


        TextView tabText;

        ViewHolder(View itemView) {
            super(itemView);

            tabText = (TextView) itemView.findViewById(R.id.item_tab_index_text);

            tabText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getLayoutPosition();
                        listener.OnClick(pos);
                    }
                    TextView view = (TextView) v;
                    view.setTextColor(getResources().getColor(R.color.colorPrimary));
                    if (lastText != null) {
                        lastText.setTextColor(getResources().getColor(R.color.desc_text_color));
                    }
                    lastText = view;
                }
            });
        }
    }

    private TabClickListener listener;

    public interface TabClickListener {
        void OnClick(int position);
    }

}
