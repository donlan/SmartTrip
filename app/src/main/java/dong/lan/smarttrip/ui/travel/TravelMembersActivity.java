package dong.lan.smarttrip.ui.travel;

import android.os.Bundle;
import android.text.TextUtils;

import dong.lan.smarttrip.base.BaseBarActivity;

import java.util.List;

import butterknife.BindView;
import dong.lan.model.Config;
import dong.lan.model.features.IUserInfo;
import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.base.BinderClickListener;
import dong.lan.smarttrip.adapters.binder.UserBinder;
import dong.lan.smarttrip.presentation.presenter.TravelMembersPresenter;
import dong.lan.smarttrip.presentation.presenter.features.ITravelMemberPresenter;
import dong.lan.smarttrip.presentation.viewfeatures.ITravelMembersView;
import dong.lan.smarttrip.ui.customview.IndexRecycleView;


public class TravelMembersActivity extends BaseBarActivity implements ITravelMembersView, BinderClickListener<IUserInfo> {

    @BindView(R.id.travel_members_list)
    IndexRecycleView membersList;

    private ITravelMemberPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_members);
        bindView("团队成员");
        initView();
    }

    private void initView() {
        String travelId = getIntent().getStringExtra(Config.TRAVEL_ID);
        if(TextUtils.isEmpty(travelId)){
            toast("缺少旅行id");
        }else {
            presenter = new TravelMembersPresenter(this);
            presenter.start(travelId);
        }
    }

    @Override
    public void initMembersList(List<IUserInfo> users) {
        membersList.setItemTittle(users);
        UserBinder binder = new UserBinder();
        binder.init(users);
        binder.setBinderClickListener(this);
        membersList.innerRecycleView().setAdapter(binder.build());
    }

    @Override
    public void removeUser(int position) {
        membersList.innerRecycleView().getAdapter().notifyItemRemoved(position);
    }

    @Override
    public void onClick(IUserInfo data, int position, int action) {
        if(action == ACTION_DELETE){
            presenter.delete(data.getIdentifier(),position);
        }else if(action == ACTION_CLICK){

        }
    }
}
