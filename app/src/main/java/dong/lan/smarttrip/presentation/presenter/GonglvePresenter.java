package dong.lan.smarttrip.presentation.presenter;

import dong.lan.smarttrip.model.GonglveHelper;
import dong.lan.smarttrip.presentation.viewfeatures.GonglveView;

/**
 * Created by 梁桂栋 on 16-11-2 ： 下午8:29.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class GonglvePresenter  {
    private static final String TAG = "GonglvePresenter";
    GonglveView view;
    GonglveHelper gonglveHelper;
    public GonglvePresenter(GonglveView view){
        this.view = view;
        gonglveHelper = GonglveHelper.getInstance();
    }

    public void loadDate(){
        view.showLoadingView(true);
//        gonglveHelper.loadDate(new Subscriber<List<Gonglve>>() {
//            @Override
//            public void onCompleted() {
//                view.showLoadingView(false);
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                view.showMessage("出错啦");
//                Log.d(TAG, "onError: "+throwable.toString());
//                view.showLoadingView(false);
//            }
//
//            @Override
//            public void onNext(List<Gonglve> gonglves) {
//                if(gonglves==null || gonglves.isEmpty()){
//                    view.showMessage("没有攻略哟");
//                }else{
//                    view.setupAdapter(gonglves);
//                }
//            }
//        });
    }

    public void refresh() {
//        gonglveHelper.loadDate(new Subscriber<List<Gonglve>>() {
//            @Override
//            public void onCompleted() {
//                view.updateListStatus();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                view.updateListStatus();
//                view.showMessage("加载新内容出错啦");
//                Log.d(TAG, "refresh onError: "+throwable.getMessage());
//            }
//
//            @Override
//            public void onNext(List<Gonglve> gonglves) {
//                view.resetAdapter(gonglves);
//                view.showMessage("加载数据："+(gonglves==null?0:gonglves.size()));
//            }
//        });
    }

    public void loadMore() {
//        gonglveHelper.loadMore(new Subscriber<List<Gonglve>>() {
//            @Override
//            public void onCompleted() {
//                view.updateListStatus();
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                view.updateListStatus();
//                view.showMessage("加载新内容出错啦");
//                Log.d(TAG, "loadMore onError: "+throwable.getMessage());
//            }
//
//            @Override
//            public void onNext(List<Gonglve> gonglves) {
//                view.addToAdapter(gonglves);
//                view.showMessage("新增数据："+(gonglves==null?0:gonglves.size()));
//            }
//        });
    }
}
