package dong.lan.smarttrip.ui.customview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

import dong.lan.model.features.ItemTextDisplay;

/**
 * Created by 梁桂栋 on 17-1-17 ： 下午1:36.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TittleItemDecoration extends RecyclerView.ItemDecoration {


    private List<? extends ItemTextDisplay> tittles;
    private int mTittleBg = 0xFFE6E6E6;
    private int mTittleColor = 0xee000000;
    private Paint mPaint;
    private Rect mBound;
    private int padding = 16;
    private int mTittleHeight = 40;
    private int mTittleSize = 20;


    public TittleItemDecoration(List<? extends ItemTextDisplay> tittles, int mTittleSize, ItemTextDisplay textDisplay) {

        this(tittles, 0xFFE6E6E6, 0xee000000, mTittleSize, textDisplay);
    }

    public TittleItemDecoration(List<? extends ItemTextDisplay> tittles, int defaultTittleBg, int defaultTittleColor, int defaultTittleSize, ItemTextDisplay textDisplay) {
        this.tittles = tittles;
        this.mTittleBg = defaultTittleBg;
        this.mTittleColor = defaultTittleColor;
        this.mPaint = new Paint();
        this.mTittleSize = defaultTittleSize;

        mBound = new Rect();
        mPaint.setTextSize(mTittleSize);
        mPaint.measureText("我们");
        mTittleHeight = (int) (mPaint.getFontMetrics().descent - mPaint.getFontMetrics().ascent + 30);
        mPaint.setAntiAlias(true);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();
        if (position > -1) {
            if (position == 0) {
                outRect.set(0, mTittleHeight + padding, 0, 0);
            } else {
                if (!tittles.get(position).equals(tittles.get(position - 1))) {
                    outRect.set(0, mTittleHeight + padding, 0, 0);
                } else
                    outRect.set(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childView.getLayoutParams();
            int position = params.getViewLayoutPosition();
            if (position > -1) {
                if (position == 0) {
                    drawTittle(c, left, right, childView, params, position);
                } else {
                    if (!tittles.get(position).equals(tittles.get(position - 1))) {
                        drawTittle(c, left, right, childView, params, position);
                    }
                }
            }
        }
    }

    private void drawTittle(Canvas c, int left, int right, View childView, RecyclerView.LayoutParams params, int position) {

        String tittle = tittles.get(position).display();
        mPaint.setColor(mTittleBg);
        c.drawRect(left, childView.getTop() - params.topMargin - mTittleHeight,
                right, childView.getTop() - params.topMargin, mPaint);

        mPaint.setColor(mTittleColor);

        mPaint.getTextBounds(tittle, 0, tittle.length(), mBound);

        c.drawText(tittle, childView.getPaddingLeft() + 20,
                childView.getTop() - params.topMargin - (mTittleHeight - mBound.height()) / 2, mPaint);

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int pos = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
            pos = llm.findFirstVisibleItemPosition();
        }
        if (pos != -1) {
            String tittle = tittles.get(pos).display();
            View childView = parent.findViewHolderForLayoutPosition(pos).itemView;
            mPaint.setColor(mTittleBg);
            c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(),
                    parent.getRight() - parent.getPaddingRight(),
                    parent.getPaddingTop() + mTittleHeight, mPaint);
            mPaint.setColor(mTittleColor);
            mPaint.getTextBounds(tittle, 0, tittle.length(), mBound);

            c.drawText(tittle, childView.getPaddingLeft() + 20,
                    parent.getPaddingTop() + mTittleHeight - (mTittleHeight - mBound.height()) / 2, mPaint);
        }
    }


}
