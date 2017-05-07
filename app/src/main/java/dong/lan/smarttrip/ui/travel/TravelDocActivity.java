package dong.lan.smarttrip.ui.travel;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.tencent.qcloud.ui.Dialog;

import butterknife.BindView;
import butterknife.OnClick;
import dong.lan.model.utils.FileUtils;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Document;
import dong.lan.model.permission.Permission;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.smarttrip.adapters.binder.TravelDocBinder;
import dong.lan.smarttrip.base.BaseBarActivity;
import dong.lan.smarttrip.presentation.presenter.TravelDocPresenter;
import dong.lan.smarttrip.presentation.presenter.features.ITravelDocument;
import dong.lan.smarttrip.presentation.viewfeatures.TravelDocumentView;
import dong.lan.smarttrip.ui.customview.TittleItemDecoration;
import io.realm.RealmResults;

public class TravelDocActivity extends BaseBarActivity implements TravelDocumentView, RadioGroup.OnCheckedChangeListener, BinderClickListener<Document> {


    private static final String TAG = TravelDocActivity.class.getSimpleName();
    @BindView(R.id.bar_right)
    ImageButton addNewDocIb;
    @BindView(R.id.travel_document_list)
    RecyclerView docList;

    @OnClick(R.id.bar_right)
    void addNewDocument() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,
                Config.REQUEST_CODE_PICK_DOCUMENT);
    }


    private ITravelDocument presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traval_documents);
        bindView("旅行文档");

        initView();
    }

    private void initView() {
        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        if (TextUtils.isEmpty(travelId)) {
            toast("缺少旅行ID,请先保存旅行再添加文件");
            finish();
        } else {
            docList.setLayoutManager(new GridLayoutManager(this, 1));
            presenter = new TravelDocPresenter(this);
            presenter.init(travelId);
        }
    }


    private View chooseLevelView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_CODE_PICK_DOCUMENT && resultCode == RESULT_OK) {
            if (data != null) {
                final String path = FileUtils.UriToPath(this, data.getData());

                if (chooseLevelView == null) {
                    chooseLevelView = LayoutInflater.from(this).inflate(R.layout.dialog_document_choose, null);
                    ((RadioGroup) (chooseLevelView.findViewById(R.id.doc_level_parent))).setOnCheckedChangeListener(this);
                }
                new Dialog(this)
                        .setClickListener(new Dialog.DialogClickListener() {
                            @Override
                            public boolean onDialogClick(int which) {
                                if (which == Dialog.CLICK_RIGHT) {
                                    alert("开始上传....");
                                    presenter.saveDoc(
                                            path,
                                            path,
                                            level);
                                }
                                return true;
                            }
                        })
                        .setView(chooseLevelView)
                        .setMessageText("确定添加旅行文件：【 " + FileUtils.PathToFileName(path) + " 】 ? \n访问权限：")
                        .show();


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private int level = 0;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.doc_level_guider:
                level = Permission.LEVEL_GUIDER;
                break;
            case R.id.doc_level_lingdui:
                level = Permission.LEVEL_LINGDUI;
                break;
            case R.id.doc_level_youke:
                level = Permission.LEVEL_YOUKE;
                break;
            case R.id.doc_level_other:
                level = Permission.LEVEL_OTHER;
                break;
            default:
                level = Permission.LEVEL_GUIDER;

        }
    }

    @Override
    public void initDocList(RealmResults<Document> documents, int userLevel) {
        if (userLevel == 0 && (documents == null || documents.isEmpty())) {
            toast("无旅行文档，请点击右上角上传新文件");
        }
        if (userLevel == 0)
            addNewDocIb.setImageResource(R.drawable.more);
        TravelDocBinder docBinder = new TravelDocBinder(userLevel);
        docBinder.init(documents);
        docBinder.setBinderClickListener(this);
        docList.setAdapter(docBinder.build());
        docList.addItemDecoration(new TittleItemDecoration(documents, 40, null));
    }

    @Override
    public void refreshDocList(int position, int action) {
        if(docList == null)
            return;
        docList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onClick(final Document data, int position, int action) {
        Log.d(TAG, "onClick: " + action + "," + data);
        if (action == 1) { //删除按钮
            new AlertDialog.Builder(this)
                    .setTitle("确定删除文件?")
                    .setMessage(data.getName())
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            presenter.deleteDoc(data);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else { //正常点击
            presenter.openDocument(data);
        }
    }

    @Override
    public Activity activity() {
        return this;
    }
}
