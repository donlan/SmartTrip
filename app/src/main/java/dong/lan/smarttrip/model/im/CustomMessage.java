package dong.lan.smarttrip.model.im;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.tencent.TIMCustomElem;
import com.tencent.TIMMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.ChatAdapter;
import dong.lan.smarttrip.notice.NoticeFactory;
import dong.lan.model.bean.notice.NoticeShow;

/**
 * 自定义消息
 */
public class CustomMessage extends Message {


    private String TAG = CustomMessage.class.getSimpleName();

    private final int TYPE_TYPING = 14;

    private int code;
    private String desc;
    private String data;
    private NoticeShow noticeShow;

    public CustomMessage(TIMMessage message) {
        this.message = message;
        TIMCustomElem elem = (TIMCustomElem) message.getElement(0);
        parse(elem.getData());

    }

    public CustomMessage(String json) throws JSONException {
        message = new TIMMessage();
        JSONObject dataJson = new JSONObject(json);
        int ac = dataJson.getInt("code");
        String dataStr = dataJson.getString("data");
        data = dataJson.toString();
        TIMCustomElem elem = new TIMCustomElem();
        elem.setData(data.getBytes());
        message.addElement(elem);
    }


    public int getAction() {
        return code;
    }

    public void setAction(int code) {
        this.code = code;
    }

    private void parse(byte[] data) {
        code = Action.ACTION_INVALID;
        try {
            String str = new String(data, "UTF-8");
            JSONObject jsonObj = new JSONObject(str);
            code = jsonObj.getInt("code");
            this.data = jsonObj.getString("data");
            Log.d(TAG, "parse: "+this.data);
            switch (code) {
                case Action.ACTION_ONLY_SHOW:
                    desc = this.data;
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "parse json error");
        }
    }

    @Override
    public boolean shouldShow() {
        return code >= 0;
    }

    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, final Context context) {
        viewHolder.rightDesc.setVisibility(View.GONE);
        viewHolder.leftAvatar.setVisibility(View.GONE);
        viewHolder.rightAvatar.setVisibility(View.GONE);
        viewHolder.sending.setVisibility(View.GONE);
        viewHolder.sender.setVisibility(View.GONE);
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        if (viewHolder.noticePanel.getVisibility() == View.GONE) {
            viewHolder.noticePanel.setVisibility(View.VISIBLE);
            viewHolder.noticePanel.setOnClickListener(null);
            viewHolder.noticePanel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(noticeShow!=null){
                        noticeShow.jump(context);
                    }
                }
            });
        }
        try {
            noticeShow = null;
            noticeShow = NoticeFactory.parseNotice(code,data);
            if(noticeShow!=null){
                viewHolder.noticeTittle.setText(noticeShow.getShowTittle());
                viewHolder.noticeContent.setText(noticeShow.getShowContent());
                if(noticeShow.type() == NoticeShow.TYPE_GATHER)
                    viewHolder.noticeIcon.setImageResource(R.drawable.gather_icon_40);
                else if(noticeShow.type() == NoticeShow.TYPE_NOTICE)
                    viewHolder.noticeIcon.setImageResource(R.drawable.notice_icon_40);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return null;
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {

    }

    public static class Action {
        public static final int ACTION_INVALID = -1;
        public static final int ACTION_ONLY_SHOW = 1;
        public static final int ACTION_TYPING = 0;
        public static final int ACTION_GATHER = 1000;
        public static final int ACTION_GATHER_REPLY = -1001;
        public static final int ACTION_NOTICE = 2000;
        public static final int ACTION_NOTICE_REPLY = -2001;
    }

}
