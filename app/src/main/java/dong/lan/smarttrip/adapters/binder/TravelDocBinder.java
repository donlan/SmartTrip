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
import dong.lan.model.bean.travel.Document;

/**
 * Created by 梁桂栋 on 17-2-11 ： 下午11:20.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TravelDocBinder extends AbstractBinder<Document> {

    private int userLevel;

    public TravelDocBinder(int userLevel) {
        this.userLevel = userLevel;
    }

    @Override
    public BaseHolder<Document> bindViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_documemt, null);
        return new ViewHolder(view);
    }

    @Override
    public void setBinderClickListener(BinderClickListener<Document> clickListener) {
        this.clickListener = clickListener;
    }


    class ViewHolder extends BaseHolder<Document> {

        @BindView(R.id.item_doc_img)
        ImageView img;
        @BindView(R.id.item_doc_desc)
        TextView docDesc;

        @OnClick(R.id.item_doc_delete)
        void deleteDoc() {
            if (clickListener != null)
                clickListener.onClick(valueAt(getLayoutPosition()), getLayoutPosition(), 1);
        }

        @OnClick(R.id.item_doc_content)
        void itemClick() {
            if (clickListener != null)
                clickListener.onClick(valueAt(getLayoutPosition()), getLayoutPosition(), 0);
        }

        @BindView(R.id.item_doc_parent)
        SwipeMenuLayout parent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (userLevel != 0) {
                parent.setSwipeEnable(false);
            }
        }

        @Override
        public void bindData(Document document) {
            docDesc.setText(document.getName());
        }
    }
}
