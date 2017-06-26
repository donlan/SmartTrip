package dong.lan.smarttrip.adapters.checklist;

/**
 * Created by 梁桂栋 on 2017/6/18 ： 15:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SectionedExpandableGridRecyclerView:
 */

public interface IItem {

    boolean isSection();

    int getLayoutViewType();

    void showData(IViewHolder holder);
}
