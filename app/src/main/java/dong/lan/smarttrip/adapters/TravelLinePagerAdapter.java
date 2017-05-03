package dong.lan.smarttrip.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.blankj.ALog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dong.lan.avoscloud.model.AVONode;
import dong.lan.avoscloud.model.AVOTravel;
import dong.lan.model.BeanConvert;
import dong.lan.model.bean.travel.Node;
import dong.lan.model.bean.travel.Travel;
import dong.lan.model.utils.TimeUtil;
import dong.lan.smarttrip.ui.customview.AddLineNodeListView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class TravelLinePagerAdapter extends PagerAdapter {


    private static final String TAG = TravelLinePagerAdapter.class.getSimpleName();
    private List<View> views;
    private Travel travel;
    private Realm realm;

    public TravelLinePagerAdapter(Context context, String travelId, int size, long startTime, AddLineNodeListView.OnNodeClickListener nodeClickListener) {
        views = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        travel = realm.where(Travel.class).equalTo("id", travelId).findFirst();
        RealmResults<Node> nodes = realm.where(Node.class).equalTo("travel.id", travelId)
                .findAllSorted("arrivedTime", Sort.ASCENDING);
        realm.commitTransaction();
        int index = 0;
        int lastDay = -1;
        int nodeNums = nodes.size();
        for (int i = 0; i < size; i++) {
            List<Node> mNodes = new ArrayList<>();
            long time = calendar.getTimeInMillis();
            for (int n = index; n < nodeNums; n++) {
                Node node = nodes.get(n);
                calendar.setTimeInMillis(node.getArrivedTime());
                int day = calendar.get(Calendar.DATE);
                if (day != lastDay && lastDay != -1) {
                    index = n;
                    lastDay = day;
                    break;
                }
                lastDay = day;
                mNodes.add(node);
                index++;
            }

            calendar.setTimeInMillis(time);
            AddLineNodeListView addLineNodeListView = new AddLineNodeListView(context,
                    calendar.getTimeInMillis(), mNodes, nodeClickListener);
            addLineNodeListView.setHeaderText("第 " + (i + 1) + " 天 ( " +
                    TimeUtil.getTime(calendar.getTimeInMillis(), "yyyy.MM.dd") + " )");
            views.add(addLineNodeListView);
            calendar.add(Calendar.DATE, 1);
        }
    }


    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position), 0);
        return views.get(position);
    }

    public void addNode(final Node node) {
        node.setTravel(travel);
        final AVONode avoNode = BeanConvert.toAvoNode(node);
        try {
            avoNode.setTravel(AVObject.createWithoutData(AVOTravel.class,getTravelObjId()));
        } catch (AVException e) {
            e.printStackTrace();
        }
        avoNode.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    node.setObjId(avoNode.getObjectId());
                    Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.copyToRealmOrUpdate(node);
                        }
                    });
                }else{
                    ALog.d(e);
                }
            }
        });
    }

    public String getTravelObjId() {
       return  travel == null ? null :travel.getObjId();
    }
}
