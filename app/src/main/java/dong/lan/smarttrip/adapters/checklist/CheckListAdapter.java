package dong.lan.smarttrip.adapters.checklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.ALog;

import dong.lan.model.bean.checklist.CheckGroup;
import dong.lan.model.bean.checklist.CheckItem;
import dong.lan.smarttrip.R;
import io.realm.Realm;
import io.realm.RealmResults;


/**
 * Created by 梁桂栋 on 17-3-7 ： 下午12:23.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CheckListAdapter extends BaseExpandableListAdapter {

    private RealmResults<CheckGroup> checkGroups;
    private Context mContext;

    public CheckListAdapter(Context context, RealmResults<CheckGroup> checkGroups) {
        this.checkGroups = checkGroups;
        mContext = context;
    }

    @Override
    public int getGroupCount() {
        return checkGroups == null ? 1 : checkGroups.size() + 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        CheckGroup checkGroup = checkGroups.get(groupPosition);
        return checkGroup.getCheckItems() == null ? 1 : checkGroup.getCheckItems().size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (groupPosition == getGroupCount() - 1)
            return null;
        return checkGroups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition == getGroupCount() - 1)
            return null;
        if (childPosition == getChildrenCount(groupPosition) - 1)
            return null;
        return checkGroups.get(groupPosition).getCheckItems().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean isGroupItemAdd(int groupPosition) {
        return groupPosition == getGroupCount() - 1;
    }

    public boolean isItemAdd(int groupPosition, int childPosition) {
        return groupPosition < getGroupCount() - 1 && childPosition == getChildrenCount(groupPosition) - 1;
    }


    public CheckGroup getGroupItem(int groupPosition) {
        if (groupPosition < getGroupCount() - 1)
            return checkGroups.get(groupPosition);
        return null;
    }

    public CheckItem getItem(int groupPosition, int childPosition) {
        if (groupPosition < getGroupCount() - 1 && childPosition < getChildrenCount(groupPosition) - 1)
            return checkGroups.get(groupPosition).getCheckItems().get(childPosition);
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        int type = getGroupType(groupPosition);
        GroupHolder groupHolder = null;

        if (type == TYPE_ADD) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_check_group, null);
            }
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_check_group, null);

            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }
            if (groupHolder == null) {
                groupHolder = new GroupHolder(convertView);
                convertView.setTag(groupHolder);
                groupHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCheckClickListener != null)
                            onCheckClickListener.onCheckClick(null, groupPosition, -1, 1);
                    }
                });
            }
            if (isExpanded) {
                groupHolder.delete.setVisibility(View.GONE);
                groupHolder.tag.setBackgroundResource(R.drawable.open);
            } else {
                groupHolder.tag.setBackgroundResource(R.drawable.close);
                groupHolder.delete.setVisibility(View.VISIBLE);
            }
            CheckGroup checkGroup = checkGroups.get(groupPosition);
            int count = getChildrenCount(groupPosition) - 1;
            groupHolder.groupname.setText(checkGroup.getGroupName());
            groupHolder.contentNum.setText(count <= 0 ? "无" : count + "");
        }
        ALog.d("TAG", "getGroupView: " + groupPosition );
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int type = getChildType(groupPosition, childPosition);
        if (type == TYPE_ADD) {
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_check_item, null);
            }
        } else {
            final CheckItemHolder itemHolder;
            if (convertView == null) {
                itemHolder = new CheckItemHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_check_bean, null);
                itemHolder.tag = (CheckBox) convertView.findViewById(R.id.item_check_bean_tag);
                itemHolder.content = (TextView) convertView.findViewById(R.id.item_check_bean_name);
                itemHolder.remark = (TextView) convertView.findViewById(R.id.item_check_bean_remark);

                itemHolder.delete = (ImageView) convertView.findViewById(R.id.item_check_bean_delete);
                convertView.setTag(itemHolder);

                itemHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCheckClickListener != null)
                            onCheckClickListener.onCheckClick(null, groupPosition, childPosition, 1);
                    }
                });
                itemHolder.tag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onCheckClickListener != null)
                            onCheckClickListener.onCheckClick(itemHolder.tag, groupPosition, childPosition, 2);
                    }
                });

            } else {
                itemHolder = (CheckItemHolder) convertView.getTag();
            }
            CheckItem checkItem = checkGroups.get(groupPosition).getCheckItems().get(childPosition);
            itemHolder.content.setText(checkItem.getName());
            itemHolder.remark.setText(checkItem.getRemark());
            itemHolder.tag.setChecked(checkItem.isCheck());
        }
        ALog.d("TAG", "getChildView: " + groupPosition + "->" + childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private int TYPE_NORMAL = 0;
    private int TYPE_ADD = 1;

    @Override
    public int getGroupType(int groupPosition) {
        if (isGroupItemAdd(groupPosition))
            return TYPE_ADD;
        return TYPE_NORMAL;
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        if (isItemAdd(groupPosition, childPosition))
            return TYPE_ADD;
        return TYPE_NORMAL;
    }

    @Override
    public int getChildTypeCount() {
        return 2;
    }

    public void deleteGroup(Realm realm, int groupPosition) {
        realm.beginTransaction();
        checkGroups.deleteFromRealm(groupPosition);
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    public void deleteItem(Realm realm, int groupPosition, int childPosition) {
        realm.beginTransaction();
        checkGroups.get(groupPosition).getCheckItems().deleteFromRealm(childPosition);
        realm.commitTransaction();
        notifyDataSetChanged();
    }

    private class GroupHolder {
        public TextView groupname;
        public TextView contentNum;
        public ImageView tag;
        public ImageView delete;

        public GroupHolder(View convertView) {
            set(convertView);
        }

        public void set(View convertView) {
            if (groupname == null || tag == null || contentNum == null)
                groupname = (TextView) convertView.findViewById(R.id.item_check_groupName);
            contentNum = (TextView) convertView.findViewById(R.id.item_check_contentNum);
            tag = (ImageView) convertView.findViewById(R.id.item_check_groupTag);
            delete = (ImageView) convertView.findViewById(R.id.item_check_group_delete);
        }

    }

    private OnCheckClickListener onCheckClickListener;

    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener) {
        this.onCheckClickListener = onCheckClickListener;
    }

    public interface OnCheckClickListener {
        void onCheckClick(View view, int groupPosition, int childPosition, int action);
    }


    private class CheckItemHolder {
        public TextView content;
        public TextView remark;
        public CheckBox tag;
        public ImageView delete;
    }

}
