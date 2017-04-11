package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.qcloud.ui.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AbstractBinder;
import dong.lan.smarttrip.adapters.base.BaseAdapter;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.features.IUserInfo;

import static dong.lan.smarttrip.adapters.base.BinderClickListener.ACTION_CLICK;
import static dong.lan.smarttrip.adapters.base.BinderClickListener.ACTION_DELETE;

/**
 * Created by 梁桂栋 on 17-2-5 ： 上午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class UserBinder extends AbstractBinder<IUserInfo> {

    @Override
    public ViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_handle, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<IUserInfo> clickListener) {
        this.clickListener = clickListener;
    }


    @Override
    public BaseAdapter<IUserInfo> build(){
        return new BaseAdapter<>(this);
    }

    public  class ViewHolder extends BaseHolder<IUserInfo> {
        @BindView(R.id.item_user_avatar)
        CircleImageView avatar;
        @BindView(R.id.item_username)
        TextView username;
        @OnClick(R.id.item_user_delete)
        void deleteUser(){
            if(clickListener!=null){
                clickListener.onClick(valueAt(getLayoutPosition()),getLayoutPosition(),ACTION_DELETE);
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
        public void bindData(IUserInfo user) {
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.head_me)
                    .into(avatar);
            username.setText(user.getUsername());
        }
    }
}
