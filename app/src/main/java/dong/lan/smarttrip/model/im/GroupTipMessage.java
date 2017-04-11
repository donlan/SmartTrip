package dong.lan.smarttrip.model.im;

import android.content.Context;
import android.view.View;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMGroupTipsElemGroupInfo;
import com.tencent.TIMGroupTipsGroupInfoType;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import dong.lan.smarttrip.App;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.ChatAdapter;

/**
 * 群tips消息
 */
public class GroupTipMessage extends Message {

    private boolean shouldShow = true;

    public GroupTipMessage(TIMMessage message){
        this.message = message;
        TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        TIMGroupTipsType tipsType = e.getTipsType();
        List<TIMGroupTipsElemGroupInfo> groupInfos= e.getGroupInfoList();
        TIMGroupTipsElemGroupInfo groupInfo = groupInfos.get(0);
        if(groupInfo.getType() == TIMGroupTipsGroupInfoType.ModifyNotification){
            shouldShow = false;
        }
    }


    @Override
    public boolean shouldShow() {
        return shouldShow;
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        if(shouldShow) {
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            viewHolder.systemMessage.setVisibility(View.VISIBLE);
            viewHolder.systemMessage.setText(getSummary());
        }else{
            viewHolder.leftPanel.setVisibility(View.GONE);
            viewHolder.rightPanel.setVisibility(View.GONE);
            viewHolder.systemMessage.setVisibility(View.GONE);
        }
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        final TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Map.Entry<String,TIMGroupMemberInfo>> iterator = e.getChangedGroupMemberInfo().entrySet().iterator();
        shouldShow = true;
        switch (e.getTipsType()){
            case CancelAdmin:
            case SetAdmin:
                return App.myApp().getString(R.string.summary_group_admin_change);
            case Join:
                while(iterator.hasNext()){
                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(getName(item.getValue()));
                    stringBuilder.append(" ");
                }
                return stringBuilder +
                        App.myApp().getString(R.string.summary_group_mem_add);
            case Kick:
                return e.getUserList().get(0) +
                        App.myApp().getString(R.string.summary_group_mem_kick);
            case ModifyMemberInfo:
                while(iterator.hasNext()){
                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
                    stringBuilder.append(getName(item.getValue()));
                    stringBuilder.append(" ");
                }
                return stringBuilder +
                        App.myApp().getString(R.string.summary_group_mem_modify);
            case Quit:
                return e.getOpUser() +
                        App.myApp().getString(R.string.summary_group_mem_quit);
            case ModifyGroupInfo:
                return App.myApp().getString(R.string.summary_group_info_change);
        }
        return "";
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    private String getName(TIMGroupMemberInfo info){
        if (info.getNameCard().equals("")){
            return info.getUser();
        }
        return info.getNameCard();
    }
}
