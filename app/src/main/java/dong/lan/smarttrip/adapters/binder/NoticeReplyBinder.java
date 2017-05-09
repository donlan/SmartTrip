package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.model.bean.notice.NoticeReply;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AbstractRealmBinder;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BaseRealmAdapter;
import dong.lan.smarttrip.adapters.base.BinderClickListener;

import static dong.lan.smarttrip.adapters.base.BinderClickListener.ACTION_CLICK;

/**
 * Created by 梁桂栋 on 17-2-5 ： 上午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeReplyBinder extends AbstractRealmBinder<NoticeReply> {

    @Override
    public ViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_handle, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<NoticeReply> clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public BaseRealmAdapter<NoticeReply> build(){
        baseAdapter = new BaseRealmAdapter<>(this);
        return baseAdapter;
    }

    public  class ViewHolder extends BaseHolder<NoticeReply> {
        @BindView(R.id.item_user_handle_name)
        TextView username;
        @BindView(R.id.item_user_handle_remark)
        TextView remark;
        @OnClick(R.id.item_user_handle_call)
        void call(){
            valueAt(getLayoutPosition()).call(itemView.getContext());
            if(clickListener!=null){
                clickListener.onClick(valueAt(getLayoutPosition()),getLayoutPosition(),1);
            }
        }

        @OnClick(R.id.item_user_handle_msg)
        void sendMessage(){
            valueAt(getLayoutPosition()).sms(itemView.getContext());
            if(clickListener!=null){
                clickListener.onClick(valueAt(getLayoutPosition()),getLayoutPosition(),2);
            }
        }



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickListener!=null){
                        clickListener.onClick(valueAt(getLayoutPosition()),getLayoutPosition(),ACTION_CLICK);
                    }
                }
            });
        }

        @Override
        public void bindData(NoticeReply reply) {

            remark.setText(reply.tips());
            username.setText(reply.displayName());
        }
    }
}
