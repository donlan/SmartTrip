package dong.lan.smarttrip.adapters.checklist;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.model.bean.checklist.CheckGroup;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BaseItemClickListener;

/**
 * Created by 梁桂栋 on 2017/6/18 ： 19:25.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip:
 */

public class SectionTittle implements IItem {

    private CheckGroup checkGroup;


    public CheckGroup getCheckGroup() {
        return checkGroup;
    }

    public void setCheckGroup(CheckGroup checkGroup) {
        this.checkGroup = checkGroup;
    }

    public SectionTittle(CheckGroup checkGroup) {
        this.checkGroup = checkGroup;
    }

    @Override
    public boolean isSection() {
        return true;
    }

    @Override
    public int getLayoutViewType() {
        return R.layout.item_section_title;
    }

    @Override
    public void showData(IViewHolder holder) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.title.setText(checkGroup.getGroupName());
        viewHolder.info.setText(String.valueOf(checkGroup.getCheckItems().size()));
    }

    public void toggleCheck() {
        checkGroup.isExpand = !checkGroup.isExpand;
    }

    public static class ViewHolder extends SectionAdapter.ViewHolder {

        @BindView(R.id.text_section_info)
        public TextView info;
        @BindView(R.id.text_section)
        public TextView title;
        @BindView(R.id.toggle_button_section)
        public ToggleButton switcher;
        @OnClick(R.id.toggle_button_section)
        void sectionToggle(){
            clickListener.onClick(null,0,getLayoutPosition());
        }
        @OnClick(R.id.section_add)
        void sectionAdd(){
            clickListener.onClick(null,1,getLayoutPosition());
        }

        private BaseItemClickListener<Object> clickListener;

        public ViewHolder(View itemView, final BaseItemClickListener<Object> clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            ButterKnife.bind(this, itemView);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    clickListener.onClick(null,2,getLayoutPosition());
                    return true;
                }
            });
        }
    }
}
