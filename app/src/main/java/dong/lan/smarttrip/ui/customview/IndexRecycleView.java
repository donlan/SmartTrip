package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import dong.lan.model.features.ItemTextDisplay;
import dong.lan.smarttrip.R;

/**
 * Created by 梁桂栋 on 17-1-17 ： 下午4:03.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class IndexRecycleView extends FrameLayout implements IndexView.IndexTouchListener {

    private RecyclerView mRecyclerView;

    private int intArray[] = new int[27];
    private LabelTextView hintTextView;
    private IndexView indexBar;

    public IndexRecycleView(Context context) {
        this(context, null);
    }

    public IndexRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {

        for (int i = 0; i < 27; i++)
            intArray[i] = 0;
        mRecyclerView = new RecyclerView(getContext());
        indexBar = new IndexView(getContext());
        indexBar.setIndexTouchListener(this);

        ViewGroup.LayoutParams listParams = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


        ViewGroup.LayoutParams hintParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        hintTextView = new LabelTextView(getContext());
        hintTextView.setWidth(200);
        hintTextView.setHeight(200);
        hintTextView.setBgColor(Color.WHITE);
        hintTextView.setTextColor(Color.BLACK);
        hintTextView.setGravity(Gravity.CENTER);
        hintTextView.setRoundRadius(30);
        hintTextView.setTextSize(50);
        hintTextView.setClickAnimation(false);

        addView(mRecyclerView, listParams);
        addView(indexBar);
        addView(hintTextView, hintParams);

        hintTextView.setVisibility(GONE);
    }



    public void setItemTittle(List<? extends ItemTextDisplay> tittles) {
        mRecyclerView.addItemDecoration(new TittleItemDecoration(tittles, 30,null));
    }

    public void setIndexBarTextSize(int textSize) {
        indexBar.setmFontSize(textSize);
    }

    public void setIndexBarBgColor(int color) {
        indexBar.setmBgColor(color);
    }

    public void setIndexBarTextColor(int color) {
        indexBar.setmTextColor(color);
    }


    /**
     * @return 单纯返回内部封装的RecycleView
     */
    public RecyclerView innerRecycleView() {
        return mRecyclerView;
    }

    public void calculateIndex(List<? extends ItemTextDisplay> displays){
        if(displays != null && !displays.isEmpty()){
            int i = 0;
            String cur = "";
            int tag = (displays.get(0).display().charAt(0));

            for(int j = 0,s = displays.size();j<s;j++){
                if(!cur.equals(displays.get(j).display())){
                    cur = displays.get(j).display();
                    int x = cur.charAt(0);
                    Log.d("TAG", "calculateIndex: "+tag+","+x+","+cur+","+i);
                    intArray[x - tag] = i;
                }
                i++;
            }
        }
    }

    /**
     * 此处时重新调整 索引view 与索引显示view的位置
     *
     * @param changed
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //最右侧显示
        indexBar.layout(right - indexBar.getWidth(), 0, right, bottom);

        //居中显示
        hintTextView.layout((right - hintTextView.getWidth()) / 2, (bottom - hintTextView.getHeight()) / 2,
                (right + hintTextView.getWidth()) / 2, (bottom + hintTextView.getHeight()) / 2);
    }


    /**
     * 手指按下
     */
    @Override
    public void touchAt(String indexText, int position) {
        mRecyclerView.setAlpha(0.7f);
        hintTextView.setVisibility(VISIBLE);
        hintTextView.setText(indexText);
    }

    /**
     * 手指滑动
     *
     * @param indexText
     * @param position
     */
    @Override
    public void touchMove(String indexText, int position) {
        hintTextView.setText(indexText);
    }

    /**
     * 手指移开
     */
    @Override
    public void touchDone(String indexText,int position) {
        mRecyclerView.setAlpha(1f);
        hintTextView.setVisibility(GONE);
        mRecyclerView.scrollToPosition(intArray[position]);
    }


}
