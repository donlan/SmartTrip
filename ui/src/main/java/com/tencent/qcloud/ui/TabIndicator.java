package com.tencent.qcloud.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tencent.qcloud.ui.utils.DisplayUtils;

/**
 * Created by 梁桂栋 on 17-3-4 ： 下午1:31.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class TabIndicator extends View {

    private Paint textPaint;
    private Paint indicatorPaint;
    private int checkedColor = 0xffffffff;
    private int uncheckedColor = 0xaaffffff;
    private int indicatorColor = 0xffffffff;
    private int indicatorLength = 20;
    private int textSize = 20;
    private String tabs[];
    private int[] itemsStartX;
    private int indicatorHeight;
    private int itemMargin;
    private int index = 0;
    private int indicatorPadding;
    private RectF indicatorBound;
    private int length = -1;


    public TabIndicator(Context context) {
        this(context, null);
    }

    public TabIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TabIndicator);
            textSize = ta.getDimensionPixelSize(R.styleable.TabIndicator_ti_textSize, 20);
            checkedColor = ta.getColor(R.styleable.TabIndicator_ti_checkedColor, checkedColor);
            uncheckedColor = ta.getColor(R.styleable.TabIndicator_ti_uncheckedColor, uncheckedColor);
            indicatorColor = ta.getColor(R.styleable.TabIndicator_ti_indicatorColor, indicatorColor);
            indicatorLength = ta.getDimensionPixelSize(R.styleable.TabIndicator_ti_indicatorLength, indicatorLength);
            String text = ta.getString(R.styleable.TabIndicator_ti_items);
            if (!TextUtils.isEmpty(text)) {
                tabs = text.split(" ");
                itemsStartX = new int[tabs.length];
            }
            itemMargin = ta.getDimensionPixelSize(R.styleable.TabIndicator_ti_itemMargin, 10);
            ta.recycle();
        }

        indicatorHeight = DisplayUtils.dip2px(getContext(), 4);
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);


        indicatorPaint = new Paint();
        indicatorPaint.setColor(indicatorColor);
        indicatorPaint.setAntiAlias(true);

        indicatorPadding = DisplayUtils.dip2px(getContext(), 8);
        indicatorBound = new RectF(0, 0, 0, 0);

    }

    int textY = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        if (tabs != null) {
            int textHeight = (int) (textPaint.getFontMetrics().bottom - textPaint.getFontMetrics().top);
            textY = (getPaddingTop() + textHeight);
            height = textHeight + getPaddingBottom() + getPaddingTop()
                    + indicatorHeight + DisplayUtils.dip2px(getContext(), indicatorPadding);
        }
        setMeasuredDimension(w, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (tabs != null) {
            int width = getWidth();
            int count = tabs.length;
            if (length == -1) {
                length = 0;
                for (int i = 0; i < count; i++) {
                    length += textPaint.measureText(tabs[i]);
                    if (i != count - 1)
                        length += itemMargin;
                }
            }
            int startX = (width - length) / 2;
            for (int i = 0; i < count; i++) {
                itemsStartX[i] = startX;
                if (index == i) {
                    int tx = (int) textPaint.measureText(tabs[i]);
                    textPaint.setColor(checkedColor);
                    int left = startX + tx / 2 - indicatorLength / 2;
                    int top = textY + indicatorHeight + indicatorPadding;
                    indicatorBound.set(left, top, left + indicatorLength, top + indicatorHeight);
                    int r = indicatorHeight / 2;
                    canvas.drawRoundRect(indicatorBound, r, r, indicatorPaint);
                } else {
                    textPaint.setColor(uncheckedColor);
                }
                canvas.drawText(tabs[i], startX, textY, textPaint);
                startX += textPaint.measureText(tabs[i]) + ((i != count - 1) ? itemMargin : 0);
            }

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ac = event.getAction();
        switch (ac) {
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                handleClick(x);

        }

        return super.onTouchEvent(event);
    }


    private void handleClick(int x) {
        int thisIndex = index;
        int ix = indicatorPadding / 2;
        for (int i = 0, s = itemsStartX.length; i < s; i++) {
            int tx = (int) (textPaint.measureText(tabs[i]) / 2 + ix);
            int cx = (int) (itemsStartX[i] + textPaint.measureText(tabs[i]) / 2);
            if (Math.abs(cx - x) <= tx) {
                thisIndex = i;
            }
        }
        if (tabClickListener != null && thisIndex != index) {
            tabClickListener.onTabClick(thisIndex);
        }
        if(thisIndex != index) {
            index = thisIndex;
            invalidate();
        }
    }

    public void setIndex(int index){
        if(index!= this.index){
            this.index = index;
            invalidate();
        }
    }
    public int getCurIndex() {
        return index;
    }

    public void setTabClickListener(TabClickListener clickListener) {
        tabClickListener = clickListener;
    }

    private TabClickListener tabClickListener;

    public interface TabClickListener {
        void onTabClick(int index);
    }
}
