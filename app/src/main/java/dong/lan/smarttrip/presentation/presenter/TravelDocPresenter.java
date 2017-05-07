package dong.lan.smarttrip.presentation.presenter;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.util.List;

import dong.lan.avoscloud.model.AVODocument;
import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.model.utils.FileUtils;
import dong.lan.model.BeanConvert;
import dong.lan.model.Config;
import dong.lan.model.bean.travel.Document;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.bean.user.Tourist;
import dong.lan.smarttrip.common.UserManager;
import dong.lan.smarttrip.presentation.presenter.features.ITravelDocument;
import dong.lan.smarttrip.presentation.viewfeatures.TravelDocumentView;
import dong.lan.smarttrip.utils.FileUtil;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by 梁桂栋 on 17-2-10 ： 上午12:27.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelDocPresenter implements ITravelDocument {

    private static final String TAG = TravelDocPresenter.class.getSimpleName();
    private TravelDocumentView view;
    private Travel travel = null;
    private Realm realm;
    private boolean isTasking = false;
    private RealmResults<Document> documents;

    public TravelDocPresenter(TravelDocumentView view) {
        this.view = view;
    }

    public void init(final String travelId) {
        realm = Realm.getDefaultInstance();
        if (realm.isInTransaction())
            realm.cancelTransaction();
        realm.beginTransaction();
        travel = realm.where(Travel.class).equalTo("id", travelId).findFirst();
        documents = realm.where(Document.class).equalTo("travel.id", travelId).findAllSortedAsync("role", Sort.DESCENDING);
        view.initDocList(documents,
                travel.getPermission(new Tourist(UserManager.instance().currentUser())));
        realm.commitTransaction();
        documents.addChangeListener(new RealmChangeListener<RealmResults<Document>>() {
            @Override
            public void onChange(RealmResults<Document> element) {

                if (element.isEmpty()) {
                    view.toast("尝试从服务器加载文档");
                    getDocumentsFromNet();
                }
                view.refreshDocList(0, 0);
            }
        });
    }

    private void getDocumentsFromNet() {

        if (travel == null)
            return;
        AVObject avObject = AVOTravel.createWithoutData("Travel", travel.getObjId());
        AVQuery<AVODocument> query = new AVQuery<>("Document");
        query.whereEqualTo("travel", avObject);
        query.include("documentFile");
        query.findInBackground(new FindCallback<AVODocument>() {
            @Override
            public void done(final List<AVODocument> documents, AVException e) {
                if (e == null && documents != null && !documents.isEmpty()) {
                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            for (AVODocument document : documents) {
                                Document doc = BeanConvert.toDocument(document);
                                realm.copyToRealm(doc);
                            }
                        }
                    });
                }
                view.refreshDocList(0, 0);
            }
        });
    }

    public void saveDoc(String url, final String path, final int level) {

        final AVFile file = new AVFile(FileUtils.PathToFileName(path), FileUtil.File2byte(path));
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                view.dismiss();
                if (e == null) {
                    final AVODocument doc = new AVODocument();
                    try {
                        doc.setTravel(AVOTravel.createWithoutData(AVOTravel.class, travel.getObjId()));
                    } catch (AVException e1) {
                        e1.printStackTrace();
                    }
                    doc.setRawDocument(file);
                    doc.setCreator(AVUser.getCurrentUser());
                    doc.setLocalPath(path);
                    doc.setRole(level);
                    doc.setUrl(file.getUrl());
                    doc.setName(FileUtils.PathToFileName(path));
                    doc.setCreateTime(System.currentTimeMillis());
                    doc.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e == null) {
                                if (!realm.isClosed()) {
                                    realm.executeTransaction(new Realm.Transaction() {
                                        @Override
                                        public void execute(Realm realm) {
                                            Document document = BeanConvert.toDocument(doc);
                                            document.setTravel(travel);
                                            realm.copyToRealm(document);
                                        }
                                    });
                                }
                                view.refreshDocList(0, 0);
                            }
                        }
                    });
                } else {
                    view.dialog("上传文档失败,错误码:" + e.getCode());
                }
            }
        }, new ProgressCallback() {
            @Override
            public void done(Integer integer) {
                view.alert("已上传:" + integer);
            }
        });
    }

    public void deleteDoc(Document document) {
        String objID = document.getObjId();
        realm.beginTransaction();
        document.deleteFromRealm();
        realm.commitTransaction();
        view.refreshDocList(0, 0);
        if (!TextUtils.isEmpty(objID))
            AVObject.createWithoutData("Document", objID).deleteEventually();
    }

    /**
     * 打开文件，优先从本地加载
     *
     * @param document
     */
    public void openDocument(final Document document) {
        File file = new File(Config.APP_DOCUMENTS_DIR + File.separator + document.getName());
        if (file.exists()) {
            view.toast(document.getName());
            String type = FileUtils.getFileType(document.getName());
            String mimeType = FileUtil.getMIMEType(type);
            try {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), mimeType);
                view.activity().startActivity(intent);
            } catch (Exception e) {
                view.toast("手机中没有能打开此文件的应用");
            }
        } else {
            if (isTasking) {
                view.toast("正在下载中");
                return;
            }
            isTasking = true;
            AVFile.withObjectIdInBackground(document.getObjId(), new GetFileCallback<AVFile>() {
                @Override
                public void done(final AVFile avFile, AVException e) {
                    avFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            view.dismiss();
                            isTasking = false;
                            if (e == null) {
                                FileUtil.byte2File(bytes, Config.APP_DOCUMENTS_DIR, document.getName());
                            } else {
                                e.printStackTrace();
                                view.dialog("下载文档失败,错误码:" + e.getCode());
                            }
                        }
                    }, new ProgressCallback() {
                        @Override
                        public void done(Integer integer) {
                            view.alert("已下载:" + integer);
                        }
                    });
                }
            });
        }
    }

}
