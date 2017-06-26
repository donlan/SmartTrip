package dong.lan.smarttrip.adapters.checklist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BaseItemClickListener;

/**
 * Created by 梁桂栋 on 2017/6/18 ： 15:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SectionedExpandableGridRecyclerView:
 */

public class SectionAdapter<T extends IItem> extends RecyclerView.Adapter<SectionAdapter.ViewHolder> {

    private List<T> datas;

    public SectionAdapter(List<T> datas,BaseItemClickListener<Object> clickListener) {
        this.datas = datas;
        this.clickListener = clickListener;
    }

    private BaseItemClickListener<Object> clickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.item_section_title){
            return new SectionTittle.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false),clickListener);
        }else{
            return new SectionItem.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(viewType, parent, false),clickListener);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        datas.get(position).showData(holder);
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getLayoutViewType();
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
