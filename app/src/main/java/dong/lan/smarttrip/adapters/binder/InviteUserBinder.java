package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AdapterBinder;
import dong.lan.smarttrip.adapters.base.BaseAdapter;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.features.IUserInfo;

/**
 * Created by 梁桂栋 on 17-2-5 ： 上午12:01.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class InviteUserBinder implements AdapterBinder<IUserInfo> {


    private List<IUserInfo> users;
    private boolean[] selectIndex ;
    @Override
    public void init(List<IUserInfo> data) {
        this.users = data;
        selectIndex = new boolean[data.size()];
    }

    @Override
    public void setCache(List<IUserInfo> cache) {

    }

    @Override
    public void showCache(boolean showCache) {

    }

    @Override
    public ViewHolder bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_invite_user, null);
        return new ViewHolder(view);
    }

    @Override
    public IUserInfo valueAt(int position) {
        return users.get(position);
    }


    @Override
    public int size() {
        return users == null ? 0 : users.size();
    }

    @Override
    public void setBinderClickListener(BinderClickListener<IUserInfo> clickListener) {
    }


    public List<IUserInfo> getSelectedUsers(){
        List<IUserInfo> selectUsers = new ArrayList<>();
        for(int i = 0,s = selectIndex.length;i<s;i++){
            if(selectIndex[i])
                selectUsers.add(users.get(i));
        }
        return selectUsers;
    }

    @Override
    public BaseAdapter<IUserInfo> build(){
        return new BaseAdapter<IUserInfo>(this);
    }

    public  class ViewHolder extends BaseHolder<IUserInfo> {

        @BindView(R.id.item_sms_invite_user_avatar)
        CircleImageView avatar;
        @BindView(R.id.item_sms_invite_check)
        CheckBox selector;
        @BindView(R.id.item_sms_invite_status)
        TextView status;
        @BindView(R.id.item_sms_invite_user_name)
        TextView username;
        @OnCheckedChanged(R.id.item_sms_invite_check)
        void select(CompoundButton compoundButton,boolean isChecked){
            selectIndex[getLayoutPosition()] = isChecked;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindData(IUserInfo user) {
            Glide.with(itemView.getContext())
                    .load(user.getAvatarUrl())
                    .error(R.drawable.head_me)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.head)
                    .into(avatar);
            status.setText("");
            username.setText(user.getUsername());
        }
    }
}
