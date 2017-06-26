package dong.lan.smarttrip.adapters.checklist;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.ALog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dong.lan.model.bean.checklist.CheckGroup;
import dong.lan.model.bean.checklist.CheckItem;
import dong.lan.smarttrip.adapters.base.BaseItemClickListener;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 2017/6/18 ： 19:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip:
 */

public class SectionHelper {


    private Map<String, SectionTittle> groupMap;
    private List<IItem> iItems;
    private SectionAdapter<IItem> adapter;

    public void init(List<CheckGroup> groups, RecyclerView recyclerView, BaseItemClickListener<Object> clickListener) {
        iItems = new ArrayList<>();
        groupMap = new HashMap<>();
        for (CheckGroup group :
                groups) {
            SectionTittle sectionTittle = new SectionTittle(group);
            groupMap.put(group.getGroupName(), sectionTittle);
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(recyclerView.getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return iItems.get(position).isSection() ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SectionAdapter<>(iItems, clickListener);
        recyclerView.setAdapter(adapter);
        reset(null);
    }


    private void reset(SectionTittle sectionTittle) {
        iItems.clear();
        for (Map.Entry<String, SectionTittle> entry : groupMap.entrySet()) {
            SectionTittle tittle = entry.getValue();
            iItems.add(tittle);
            if (!tittle.getCheckGroup().isExpand || sectionTittle != null && sectionTittle.equals(tittle) && !sectionTittle.getCheckGroup().isExpand) {
                continue;
            }
            for (CheckItem i : tittle.getCheckGroup().getCheckItems()) {
                SectionItem sectionItem = new SectionItem(i);
                iItems.add(sectionItem);
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void addGroup(CheckGroup group) {
        SectionTittle sectionTittle = new SectionTittle(group);
        groupMap.remove(group.getGroupName());
        groupMap.put(group.getGroupName(), sectionTittle);
        iItems.add(sectionTittle);
        adapter.notifyDataSetChanged();
    }

    public void addItem(CheckGroup group, CheckItem item) {
        ALog.d(group.getGroupName());
        groupMap.get(group.getGroupName()).getCheckGroup().getCheckItems().add(item);
        reset(null);
    }


    public CheckGroup getGroup(int position) {
        return ((SectionTittle) iItems.get(position)).getCheckGroup();
    }

    public void itemClick(int position) {
        SectionItem sectionItem = (SectionItem) iItems.get(position);
        sectionItem.toggleCheck();
        adapter.notifyItemChanged(position);
    }

    public void deleteGroup(final int position) {
       final SectionTittle sectionTittle = groupMap.remove(getGroup(position).getGroupName());
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(CheckGroup.class)
                        .equalTo("groupName", sectionTittle.getCheckGroup().getGroupName())
                        .findFirst().deleteFromRealm();
            }
        });
        reset(null);
    }

    public void deleteItem(int position) {
        IItem iItem = iItems.remove(position);
        final SectionItem sectionItem = (SectionItem) iItem;
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(CheckItem.class)
                        .equalTo("timeStamp", sectionItem.getCheckItem().getTimeStamp())
                        .findFirst().deleteFromRealm();
            }
        });
        for(int i = position-1;i>=0;i--){
            if(iItems.get(i) instanceof SectionTittle){
                SectionTittle sectionTittle = (SectionTittle) iItems.get(i);
                sectionTittle.getCheckGroup().getCheckItems()
                        .remove(((SectionItem) iItem).getCheckItem());
                adapter.notifyItemChanged(i);
                break;
            }
        }
        adapter.notifyItemRemoved(position);
    }

    public void toggle(int position) {
        SectionTittle sectionTittle = (SectionTittle) iItems.get(position);
        sectionTittle.toggleCheck();
        reset(sectionTittle);
    }

    public void destroy() {
        iItems.clear();
        iItems = null;
        groupMap.clear();
        groupMap = null;
    }
}
