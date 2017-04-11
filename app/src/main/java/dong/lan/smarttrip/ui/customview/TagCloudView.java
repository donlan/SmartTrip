package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dong.lan.smarttrip.R;

/**
 * Created by 梁桂栋 on 17-1-16 ： 下午3:30.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TagCloudView extends RecyclerView {


    public TagCloudView(Context context) {
        this(context, null);
    }

    public TagCloudView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagCloudView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(new TagCloudLayoutManager());
    }


    public void setData(List<String> data) {
        this.data = data;
        setAdapter(new Adapter());
    }




    private List<String> data;

    public String getData(int position){
        return data.get(position);
    }

    private class Adapter extends RecyclerView.Adapter<TagViewHolder> {


        @Override
        public TagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, null);
            final TagViewHolder holder = new TagViewHolder(view);
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onTagClick(holder.getLayoutPosition());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(TagViewHolder holder, int position) {
            holder.labelTextView.setText(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }
    }


    private static class TagViewHolder extends RecyclerView.ViewHolder {

        LabelTextView labelTextView;

        public TagViewHolder(View itemView) {
            super(itemView);
            labelTextView = (LabelTextView) itemView.findViewById(R.id.ltv);
        }
    }


    public void setOnTagClickListener(OnTagClickListener listener) {
        this.listener = listener;
    }


    private OnTagClickListener listener;

    public interface OnTagClickListener {
        void onTagClick(int postion);
    }


}
