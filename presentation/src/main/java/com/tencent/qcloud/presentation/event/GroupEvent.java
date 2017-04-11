package com.tencent.qcloud.presentation.event;


import android.content.pm.LabeledIntent;

import com.tencent.TIMGroupAssistantListener;
import com.tencent.TIMGroupCacheInfo;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupSettings;
import com.tencent.TIMManager;

import java.util.List;
import java.util.Observable;

/**
 * 群相关数据缓存，底层IMSDK会维护本地存储
 */
public class GroupEvent extends Observable implements TIMGroupAssistantListener {

    private final String TAG = "GroupInfo";


    private GroupEvent() {
    }

    private static GroupEvent instance = new GroupEvent();

    public static GroupEvent getInstance() {
        return instance;
    }

    public void init() {
        //开启IMSDK本地存储
        TIMManager.getInstance().enableGroupInfoStorage(true);
        TIMManager.getInstance().setGroupAssistantListener(this);
        TIMGroupSettings settings = new TIMGroupSettings();
        TIMGroupSettings.Options options = settings.new Options();
        options.addCustomTag("nick");
        settings.setGroupInfoOptions(settings.new Options());
        settings.setMemberInfoOptions(options);
        TIMManager.getInstance().initGroupSettings(settings);
    }


    @Override
    public void onMemberJoin(String s, List<TIMGroupMemberInfo> list) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.MEMBER_ADD, s, list));
    }

    @Override
    public void onMemberQuit(String s, List<String> list) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.MEMBER_QUIT, s, list));
    }

    @Override
    public void onMemberUpdate(String s, List<TIMGroupMemberInfo> list) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.MEMBER_UPDATE, s, list));
    }

    @Override
    public void onGroupAdd(TIMGroupCacheInfo timGroupCacheInfo) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.ADD, timGroupCacheInfo));
    }


    @Override
    public void onGroupDelete(String s) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.DEL, s));
    }

    @Override
    public void onGroupUpdate(TIMGroupCacheInfo timGroupCacheInfo) {
        setChanged();
        notifyObservers(new NotifyCmd(NotifyType.UPDATE, timGroupCacheInfo));
    }


    /**
     * 通知上层用的数据
     */
    public class NotifyCmd {
        public final NotifyType type;
        public final Object data;
        public final String id;

        NotifyCmd(NotifyType type, Object data) {
            this.type = type;
            this.data = data;
            this.id = "";
        }

        NotifyCmd(NotifyType type, String id, Object data) {
            this.type = type;
            this.data = data;
            this.id = id;
        }

    }

    public enum NotifyType {
        REFRESH,//刷新
        ADD,//添加群
        DEL,//删除群
        UPDATE,//更新群信息
        MEMBER_ADD, //用户加群
        MEMBER_QUIT, // 用户退群
        MEMBER_UPDATE //群用户信息更新
    }


}
