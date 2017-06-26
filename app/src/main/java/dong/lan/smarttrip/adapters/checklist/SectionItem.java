package dong.lan.smarttrip.adapters.checklist;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dong.lan.library.RelativeCarView;
import dong.lan.model.bean.checklist.CheckItem;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BaseItemClickListener;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2017/6/18 ： 19:25.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip:
 */

public class SectionItem implements IItem {

    private CheckItem checkItem;

    public SectionItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }


    public CheckItem getCheckItem() {
        return checkItem;
    }

    public void setCheckItem(CheckItem checkItem) {
        this.checkItem = checkItem;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    @Override
    public int getLayoutViewType() {
        return R.layout.item_section_item;
    }

    @Override
    public void showData(IViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (checkItem.isCheck()) {
            viewHolder.container.setBackgroundColor(0xffFFECBA);
        } else {
            viewHolder.container.setBackgroundColor(0xffffffff);
        }
        viewHolder.content.setText(checkItem.getName());
        viewHolder.desc.setText(checkItem.getRemark());
    }

    public void toggleCheck() {
        checkItem.setCheck(!checkItem.isCheck());
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CheckItem item = realm.where(CheckItem.class)
                        .equalTo("timeStamp",checkItem.getTimeStamp())
                        .findFirst();
                if(item!=null)
                    item.setCheck(checkItem.isCheck());
            }
        });
    }


    public static class ViewHolder extends SectionAdapter.ViewHolder {

        @BindView(R.id.item_section_container)
        public RelativeCarView container;
        @BindView(R.id.item_section_text)
        public TextView content;
        @BindView(R.id.item_section_desc)
        public TextView desc;

        private BaseItemClickListener<Object> clickListener;

        public ViewHolder(View itemView, final BaseItemClickListener<Object> clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onClick(null,4,getLayoutPosition());
                    return true;
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(null,3,getLayoutPosition());
                }
            });
        }
    }
}
