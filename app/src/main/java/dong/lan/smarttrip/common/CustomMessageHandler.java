package dong.lan.smarttrip.common;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.baidu.location.BDLocation;
import com.tencent.TIMConversation;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import dong.lan.avoscloud.model.AVOGatherReply;
import dong.lan.avoscloud.model.AVONoticeReply;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.notice.Gather;
import dong.lan.model.bean.user.Tourist;
import dong.lan.model.bean.notice.Notice;
import dong.lan.model.features.IBaseReply;
import dong.lan.smarttrip.App;
import dong.lan.smarttrip.model.im.CustomMessage;
import dong.lan.model.bean.notice.ReplyData;
import io.realm.Realm;

/**
 * Created by 梁桂栋 on 17-3-26 ： 下午8:39.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CustomMessageHandler {

    private static final String TAG = CustomMessageHandler.class.getSimpleName();
    private static CustomMessageHandler handler;

    public static CustomMessageHandler instance() {
        if (handler == null)
            handler = new CustomMessageHandler();
        return handler;
    }

    public void handler(TIMMessage message) {
        if (message != null && message.getElement(0).getType() == TIMElemType.Custom) {
            TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
            String data = null;
            int code = -1;
            try {
                String str = new String(elem.getData(), "UTF-8");
                JSONObject jsonObj = new JSONObject(str);
                code = jsonObj.getInt("code");
                data = jsonObj.getString("data");
            } catch (UnsupportedEncodingException | JSONException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(data))
                return;
            switch (code) {
                //来自导游发送的集合信息
                case CustomMessage.Action.ACTION_GATHER:
                    if (message.isSelf())
                        return;
                    handlerReceiveGather(message, JSON.parseObject(data, Gather.class));
                    break;
                //游客收到集合消息后自动回复,用于统计接收人数
                case CustomMessage.Action.ACTION_GATHER_REPLY:
                    if (message.isSelf())
                        return;
                    handlerGatherReply(message, JSON.parseObject(data, ReplyData.class));
                    break;
                //来自导游发送的通知信息
                case CustomMessage.Action.ACTION_NOTICE:
                    if (message.isSelf())
                        return;
                    handlerReceiveNotice(message, JSON.parseObject(data, Notice.class));
                    break;
                //游客收到集合消息后通知回复,用于统计接收人数
                case CustomMessage.Action.ACTION_NOTICE_REPLY:

                    if (message.isSelf())
                        return;
                    handlerNoticeReply(message, JSON.parseObject(data, ReplyData.class));
                    break;
            }
        }
    }

    /**
     * @param message
     * @param replyData
     */
    private void handlerNoticeReply(final TIMMessage message, final ReplyData replyData) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Notice notice = realm.where(Notice.class)
                        .equalTo("id", String.valueOf(replyData.time))
                        .findFirst();
                Tourist tourist = realm.where(Tourist.class)
                        .equalTo("user.identifier", message.getSenderProfile().getIdentifier())
                        .findFirst();

                final AVONoticeReply avoNoticeReply = new AVONoticeReply();
                avoNoticeReply.setStatus(IBaseReply.STATUS_OK);
                avoNoticeReply.setReplyTime(System.currentTimeMillis());
                avoNoticeReply.setTourist(BeanConvert.toAvoTourist(tourist));
                avoNoticeReply.setNotice(BeanConvert.toAvoNotice(notice));
                avoNoticeReply.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    realm.copyToRealmOrUpdate(BeanConvert.toNoticeReply(avoNoticeReply));
                                }
                            });
                        }
                        {

                        }
                    }
                });
            }
        });
    }

    /**
     * 处理集合回复
     *
     * @param message
     * @param replyData
     */
    private void handlerGatherReply(final TIMMessage message, final ReplyData replyData) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Gather gather = realm.where(Gather.class)
                        .equalTo("id", String.valueOf(replyData.time))
                        .findFirst();

                Tourist tourist = realm.where(Tourist.class)
                        .equalTo("user.identifier", message.getSenderProfile().getIdentifier())
                        .findFirst();

                final AVOGatherReply avoGatherReply = new AVOGatherReply();
                avoGatherReply.setReplyTime(System.currentTimeMillis());
                avoGatherReply.setStatus(IBaseReply.STATUS_OK);
                avoGatherReply.setTourist(BeanConvert.toAvoTourist(tourist));
                avoGatherReply.setGather(BeanConvert.toAvoGather(gather));
                BDLocation location = App.myApp().locationService().getLastLocation();
                if (location == null)
                    avoGatherReply.setLocation(0, 0);
                else
                    avoGatherReply.setLocation(location.getIndoorNetworkState(), location.getLongitude());
                avoGatherReply.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(BeanConvert.toGatherReply(avoGatherReply));
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * 处理收到的通知
     *
     * @param timMessage
     * @param notice
     */
    private void handlerReceiveNotice(TIMMessage timMessage, final Notice notice) {
        TIMConversation conversation = timMessage.getConversation();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(notice);
            }
        });
        CustomMessage message = null;
        try {
            message = new CustomMessage("{code:" + CustomMessage.Action.ACTION_GATHER_REPLY
                    + ",data:" + new ReplyData(timMessage.getSenderProfile().getIdentifier(), notice.travelId).toJson() + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (message == null)
            return;
        conversation.sendMessage(message.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + i + "," + s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                Log.d(TAG, "onSuccess: " + message);
            }
        });
    }

    /**
     * 处理收到的集合信息
     *
     * @param timMessage
     * @param gather
     */
    private void handlerReceiveGather(TIMMessage timMessage, final Gather gather) {
        TIMConversation conversation = timMessage.getConversation();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(gather);
            }
        });
        CustomMessage message = null;
        try {
            message = new CustomMessage("{code:" + CustomMessage.Action.ACTION_NOTICE_REPLY
                    + ",data:" + new ReplyData(timMessage.getSenderProfile().getIdentifier(), gather.travelId).toJson() + "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (message == null)
            return;
        conversation.sendMessage(message.getMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "onError: " + i + "," + s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                Log.d(TAG, "onSuccess: " + message);
            }
        });
    }
}
