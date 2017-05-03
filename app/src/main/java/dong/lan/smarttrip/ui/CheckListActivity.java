
package dong.lan.smarttrip.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.tencent.qcloud.ui.Dialog;

import java.io.File;

import butterknife.BindView;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.checklist.CheckListAdapter;
import dong.lan.model.bean.checklist.CheckGroup;
import dong.lan.model.bean.checklist.CheckItem;
import dong.lan.smarttrip.base.BaseBarActivity;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 2016/9/13.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class CheckListActivity extends BaseBarActivity implements CheckListAdapter.OnCheckClickListener {


    private static final String TAG = CheckListActivity.class.getSimpleName();
    @BindView(R.id.check_expand_list)
    ExpandableListView expandableListView;


    private Realm realm;
    private CheckListAdapter adapter;
    private Dialog addGroupDialog;
    private Dialog addItemDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        bindView("出行清单");


        initView();

    }

    private void initView() {

        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<CheckGroup> checkGroups = realm.where(CheckGroup.class).findAll();
        realm.commitTransaction();
        adapter = new CheckListAdapter(this, checkGroups);
        adapter.setOnCheckClickListener(this);
        expandableListView.setAdapter(adapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG, "onGroupClick: " + v.getClass().getSimpleName());
                if (adapter.isGroupItemAdd(groupPosition)) {
                    addGroup();
                    return true;
                } else {

                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG, "onChildClick: " + v.getClass().getSimpleName());
                if (adapter.isItemAdd(groupPosition, childPosition)) {
                    CheckGroup checkGroup = adapter.getGroupItem(groupPosition);
                    if (checkGroup != null) {
                        addItemBean(checkGroup);
                        return true;
                    }
                } else {

                }
                return false;
            }
        });
    }

    private void addItemBean(final CheckGroup checkGroup) {
        File flie = new File("f");
        flie.exists();
        if (addItemDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_two_edittext, null);
            final EditText name = (EditText) view.findViewById(R.id.view_edittext_one);
            final EditText remark = (EditText) view.findViewById(R.id.view_edittext_two);
            name.setHint("物品名称");
            remark.setHint("备注信息");
            addItemDialog = new Dialog(this);
            addItemDialog.setLeftText("取消")
                    .setRightText("确定")
                    .setMessageText("添加一个新的物品")
                    .setView(view)
                    .setRemoveViews(false)
                    .setClickListener(new Dialog.DialogClickListener() {
                        @Override
                        public boolean onDialogClick(int which) {
                            if (which == Dialog.CLICK_LEFT)
                                return true;
                            String nameStr = name.getText().toString();
                            String remarkStr = remark.getText().toString();
                            if (TextUtils.isEmpty(nameStr)) {
                                toast("物品名称不能为空");
                                return false;
                            }
                            realm.beginTransaction();
                            CheckItem item = realm.createObject(CheckItem.class);
                            item.setName(nameStr);
                            item.setRemark(remarkStr);
                            item.setCheck(false);
                            checkGroup.getCheckItems().add(item);
                            realm.commitTransaction();
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                    });

        }
        addItemDialog.show();
    }

    private void addGroup() {
        if (addGroupDialog == null) {

            View view = LayoutInflater.from(this).inflate(R.layout.view_one_edittext, null);
            final EditText editText = (EditText) view.findViewById(R.id.view_edittext_one);
            editText.setHint("清单组名");
            editText.setBackgroundResource(R.drawable.rect_stroke_8);
            addGroupDialog = new Dialog(this);
            addGroupDialog.setLeftText("取消")
                    .setRightText("确定")
                    .setMessageText("添加一个新的清单组")
                    .setView(view)
                    .setRemoveViews(false)
                    .setClickListener(new Dialog.DialogClickListener() {
                        @Override
                        public boolean onDialogClick(int which) {
                            if (which == Dialog.CLICK_LEFT)
                                return true;
                            String name = editText.getText().toString();
                            if (TextUtils.isEmpty(name)) {
                                toast("组名不能为空");
                                return false;
                            }
                            realm.beginTransaction();
                            CheckGroup checkGroup = realm.createObject(CheckGroup.class);
                            checkGroup.setGroupName(name);
                            checkGroup.setCheckItems(new RealmList<CheckItem>());
                            realm.commitTransaction();
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                    });

        }
        addGroupDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onCheckClick(View view, int groupPosition, int childPosition, int action) {
        if (action == 1 && childPosition == -1) {
            adapter.deleteGroup(realm, groupPosition);
        } else if (action == 1 && childPosition != -1) {
            adapter.deleteItem(realm, groupPosition, childPosition);
        } else if (action == 2 && groupPosition != -1 && childPosition != -1) {
            CheckBox checkBox = (CheckBox) view;
            CheckItem checkItem = adapter.getItem(groupPosition, childPosition);
            if (checkBox.isChecked() != checkItem.isCheck()) {
                realm.beginTransaction();
                checkItem.setCheck(checkBox.isChecked());
                realm.commitTransaction();
            }
        }
        Log.d(TAG, "onCheckClick: " + groupPosition + "," + action);
    }
}
