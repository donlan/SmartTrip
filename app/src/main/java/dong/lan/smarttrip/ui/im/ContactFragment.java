package dong.lan.smarttrip.ui.im;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import dong.lan.smarttrip.R;
import dong.lan.smarttrip.adapters.ExpandGroupListAdapter;
import dong.lan.smarttrip.model.im.FriendProfile;
import dong.lan.smarttrip.model.im.FriendshipInfo;
import dong.lan.smarttrip.model.im.GroupInfo;
import com.tencent.qcloud.ui.base.BaseFragment;

/**
 * 联系人界面
 */
public class ContactFragment extends BaseFragment implements  View.OnClickListener, Observer {

    private ExpandGroupListAdapter mGroupListAdapter;
    private ExpandableListView mGroupListView;
    private LinearLayout mNewFriBtn, mPublicGroupBtn, mChatRoomBtn,mPrivateGroupBtn;
    Map<String, List<FriendProfile>> friends;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (content == null){
            content = inflater.inflate(R.layout.fragment_contact, container, false);
            mGroupListView = (ExpandableListView) content.findViewById(R.id.groupList);
            mNewFriBtn = (LinearLayout) content.findViewById(R.id.btnNewFriend);
            mNewFriBtn.setOnClickListener(this);
            mPublicGroupBtn = (LinearLayout) content.findViewById(R.id.btnPublicGroup);
            mPublicGroupBtn.setOnClickListener(this);
            mChatRoomBtn = (LinearLayout) content.findViewById(R.id.btnChatroom);
            mChatRoomBtn.setOnClickListener(this);
            mPrivateGroupBtn = (LinearLayout) content.findViewById(R.id.btnPrivateGroup);
            mPrivateGroupBtn.setOnClickListener(this);

            friends = FriendshipInfo.getInstance().getFriends();
            mGroupListAdapter = new ExpandGroupListAdapter(getActivity(), FriendshipInfo.getInstance().getGroups(), friends);
            mGroupListView.setAdapter(mGroupListAdapter);
            mGroupListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
                    friends.get(FriendshipInfo.getInstance().getGroups().get(groupPosition)).get(childPosition).onClick(getActivity());
                    return false;
                }
            });
            FriendshipInfo.getInstance().addObserver(this);
            mGroupListAdapter.notifyDataSetChanged();
        }
        return content;
    }


    @Override
    public void onResume(){
        super.onResume();
        mGroupListAdapter.notifyDataSetChanged();
    }



    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnNewFriend) {
            Intent intent = new Intent(getActivity(), FriendshipManageMessageActivity.class);
            getActivity().startActivity(intent);
        }
        if (view.getId() == R.id.btnPublicGroup) {
            showGroups(GroupInfo.publicGroup);
        }
        if (view.getId() == R.id.btnChatroom) {
            showGroups(GroupInfo.chatRoom);
        }
        if (view.getId() == R.id.btnPrivateGroup) {
            showGroups(GroupInfo.privateGroup);
        }
    }




    private void showGroups(String type){
        Intent intent = new Intent(getActivity(), GroupListActivity.class);
        intent.putExtra("type", type);
        getActivity().startActivity(intent);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof FriendshipInfo){
            mGroupListAdapter.notifyDataSetChanged();
        }
    }
}
