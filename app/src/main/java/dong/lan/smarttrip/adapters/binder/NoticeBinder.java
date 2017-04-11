package dong.lan.smarttrip.adapters.binder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.AbstractBinder;
import dong.lan.smarttrip.adapters.base.BaseHolder;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.model.bean.notice.NoticeShow;

/**
 * Created by 梁桂栋 on 17-3-27 ： 下午2:26.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class NoticeBinder extends AbstractBinder<NoticeShow> {


    @Override
    public BaseHolder<NoticeShow> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice,null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<NoticeShow> clickListener) {
        this.clickListener = clickListener;
    }

    class ViewHolder extends BaseHolder<NoticeShow>{

        @BindView(R.id.notice_parent)
        SwipeMenuLayout parent;
        @BindView(R.id.notice_type_iocn)
        ImageView typeIcon;
        @BindView(R.id.notice_content)
        TextView content;
        @BindView(R.id.notice_extras)
        TextView extras;
        @OnClick(R.id.notice_container)
        void itemClick(View view){
            valueAt(getLayoutPosition()).jump(view.getContext());
        }
        @OnClick(R.id.notice_delete)
        void delete(){
            if(clickListener!=null)
                clickListener.onClick(valueAt(getLayoutPosition()),getLayoutPosition(),1);
        }
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void bindData(NoticeShow noticeShow) {
            content.setText(noticeShow.getShowContent());
            extras.setText(noticeShow.showExtras());
            if(noticeShow.type() == NoticeShow.TYPE_GATHER){
                typeIcon.setImageResource(R.drawable.gather_icon_40);
            }else{
                typeIcon.setImageResource(R.drawable.notice_icon_40);
            }
        }
    }
}
