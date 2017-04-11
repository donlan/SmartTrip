package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.ui.PackageFragment;

/**
 * Created by 梁桂栋 on 17-1-25 ： 下午9:58.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TabItemRecycleView extends RecyclerView {

    private String[] texts;
    private int[] imgIds;
    private int[] bgIds;

    public TabItemRecycleView(Context context) {
        this(context, null);
    }

    public TabItemRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new GridLayoutManager(context, 4));
    }


    public void init(String[] texts, int imgIds[], int bgIds[], TabClickListener listener) {
        this.texts = texts;
        this.imgIds = imgIds;
        this.bgIds = bgIds;
        this.listener = listener;
        this.setAdapter(new Adapter());
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_package_tools, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.itemDesc.setText(texts[position]);
            holder.toolsItem.setImageResource(imgIds[position]);
            holder.toolsItem.setBgColor(bgIds[position]);
        }

        @Override
        public int getItemCount() {
            return texts == null ? 0 : texts.length;
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageButton toolsItem;

        TextView itemDesc;

        ViewHolder(View itemView) {
            super(itemView);
            toolsItem = (CircleImageButton) itemView.findViewById(R.id.item_package_tool);

            itemDesc = (TextView) itemView.findViewById(R.id.item_package_desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getLayoutPosition();
                        listener.OnClick(texts[pos], pos);
                    }
                }
            });
        }
    }

    private TabClickListener listener;

    public interface TabClickListener {
        void OnClick(String val, int position);
    }
}
