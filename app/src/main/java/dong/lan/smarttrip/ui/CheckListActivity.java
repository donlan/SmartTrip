
package dong.lan.smarttrip.ui;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;

import com.blankj.ALog;
import com.tencent.qcloud.ui.Dialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.model.bean.checklist.CheckGroup;
import dong.lan.model.bean.checklist.CheckItem;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BaseItemClickListener;
import dong.lan.smarttrip.adapters.checklist.CheckListAdapter;
import dong.lan.smarttrip.adapters.checklist.SectionHelper;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.common.UserManager;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by 梁桂栋 on 2016/9/13.
 * Email: 760625325@qq.com
 * Github: github.com/donlan
 */

public class CheckListActivity extends BaseBarActivity implements CheckListAdapter.OnCheckClickListener, BaseItemClickListener<Object> {


    private static final String TAG = CheckListActivity.class.getSimpleName();
    @BindView(R.id.check_expand_list)
    ExpandableListView expandableListView;

    @BindView(R.id.section_list)
    RecyclerView sectionList;

    @BindView(R.id.bar_right)
    ImageButton addNewGroupBt;

    private String gn; //类别名称
    private CheckGroup copyGroup; //添加条目是从Realm中查询到的类别分组
    private CheckItem copyItem; //添加的条目

    @OnClick(R.id.bar_right)
    void addNewGroup() {
        addGroup();
    }

    private Realm realm;
    private CheckListAdapter adapter;
    private Dialog addGroupDialog;
    private Dialog addItemDialog;
    List<CheckGroup> checkGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        bindView("出行清单");
        initView();
    }

    private SectionHelper sectionHelper;

    private void initView() {
        addNewGroupBt.setImageResource(R.drawable.ic_note_add);
        sectionHelper = new SectionHelper();
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<CheckGroup> group = realm.where(CheckGroup.class)
                        .equalTo("creator.identifier", UserManager.instance().identifier())
                        .findAll();
                checkGroups = realm.copyFromRealm(group);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sectionHelper.init(checkGroups, sectionList, CheckListActivity.this);
                    }
                });
            }
        });

//        checkGroups.addChangeListener(new RealmChangeListener<RealmResults<CheckGroup>>() {
//            @Override
//            public void onChange(RealmResults<CheckGroup> checkGroups) {
//                adapter.notifyDataSetChanged();
//            }
//        });
//        adapter = new CheckListAdapter(this, checkGroups);
//        adapter.setOnCheckClickListener(this);
//        expandableListView.setAdapter(adapter);
//        expandableListView.setGroupIndicator(null);
//        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                Log.d(TAG, "onGroupClick: " + v.getClass().getSimpleName());
//                if (adapter.isGroupItemAdd(groupPosition)) {
//                    addGroup();
//                    return true;
//                }
//                return false;
//            }
//        });
//
//        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Log.d(TAG, "onChildClick: " + v.getClass().getSimpleName());
//                if (adapter.isItemAdd(groupPosition, childPosition)) {
//                    CheckGroup checkGroup = adapter.getGroupItem(groupPosition);
//                    if (checkGroup != null) {
//                        addItemBean(checkGroup);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
    }

    private void addItemBean(final CheckGroup checkGroup) {
        gn = checkGroup.getGroupName();
        if (addItemDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.view_two_edittext, null);
            final EditText name = (EditText) view.findViewById(R.id.view_edittext_one);
            final EditText remark = (EditText) view.findViewById(R.id.view_edittext_two);
            name.setHint("物品名称");
            remark.setHint("备注信息");
            addItemDialog = new Dialog(this);
            addItemDialog.setLeftText("取消")
                    .setRightText("确定")
                    .setView(view)
                    .setRemoveViews(false)
                    .setClickListener(new Dialog.DialogClickListener() {
                        @Override
                        public boolean onDialogClick(int which) {
                            if (which == Dialog.CLICK_LEFT)
                                return true;

                            realm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    String nameStr = name.getText().toString();
                                    String remarkStr = remark.getText().toString();
                                    if (TextUtils.isEmpty(nameStr)) {
                                        toast("物品名称不能为空");
                                        return;
                                    }
                                    CheckItem item = realm.createObject(CheckItem.class);
                                    item.setName(nameStr);
                                    item.setRemark(remarkStr);
                                    item.setCheck(false);
                                    CheckGroup group = realm.where(CheckGroup.class).equalTo("groupName", gn)
                                            .findFirst();
                                    group.getCheckItems().add(item);
                                    name.setText("");
                                    remark.setText("");
                                    ALog.d(group.getGroupName());
                                    copyGroup = realm.copyFromRealm(group);
                                    copyItem = realm.copyFromRealm(item);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            sectionHelper.addItem(copyGroup,copyItem);
                                        }
                                    });
                                }
                            });
                            return true;
                        }
                    });
        }
        addItemDialog.setMessageText("添加一个新的物品(" + gn + ")");
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
                            CheckGroup checkGroup = new CheckGroup();
                            checkGroup.setGroupName(name);
                            checkGroup.setCreator(UserManager.instance().currentUser());
                            checkGroup.setCheckItems(new RealmList<CheckItem>());
                            realm.copyToRealmOrUpdate(checkGroup);
                            realm.commitTransaction();
                            editText.setText("");
                            sectionHelper.addGroup(checkGroup);
                            return true;
                        }
                    });
        }
        addGroupDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (checkGroups != null)
            checkGroups.clear();
        if (!realm.isClosed())
            realm.close();
        sectionHelper.destroy();
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
            if (checkItem != null && checkBox.isChecked() != checkItem.isCheck()) {
                realm.beginTransaction();
                checkItem.setCheck(checkBox.isChecked());
                realm.commitTransaction();
            }
        }
        Log.d(TAG, "onCheckClick: " + groupPosition + "," + action);
    }

    @Override
    public void onClick(Object data, int action, final int position) {
        switch (action) {
            case 0: //标题开关
                sectionHelper.toggle(position);
                break;
            case 1: //添加item
                addItemBean(sectionHelper.getGroup(position));
                break;
            case 2: //长按标题
                new Dialog(this)
                        .setMessageText("是否删除本类别及其包含的所有条目？")
                        .setRightText("删除所有")
                        .setLeftText("取消")
                        .setClickListener(new Dialog.DialogClickListener() {
                            @Override
                            public boolean onDialogClick(int which) {
                                if(which == Dialog.CLICK_RIGHT){
                                    sectionHelper.deleteGroup(position);
                                }
                                return true;
                            }
                        })
                        .show();
                break;
            case 3: //item点击
                sectionHelper.itemClick(position);
                break;
            case 4: //item长按
                sectionHelper.deleteItem(position);
                break;
        }
    }


}
