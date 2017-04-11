package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 梁桂栋 on 17-1-17 ： 下午4:22.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: 一个列表中显示A ... Z(包括#)的滑动索引View
 */

public class IndexView extends View {

    private static final String[] indexText = new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    private static final String TAG = IndexView.class.getSimpleName();

    private int mFontSize = 30;
    private int mTextColor = Color.DKGRAY;
    private int mBgColor = 0x22000000;
    private Paint mPaint;
    private int height;


    public int getmTextColor() {
        return mTextColor;
    }

    public void setmTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
        mPaint.setColor(mTextColor);
        invalidate();
    }

    public int getmFontSize() {
        return mFontSize;
    }

    public void setmFontSize(int mFontSize) {
        this.mFontSize = mFontSize;
        mPaint.setTextSize(mFontSize);
        invalidate();
    }

    public int getmBgColor() {
        return mBgColor;
    }

    public void setmBgColor(int mBgColor) {
        this.mBgColor = mBgColor;
        invalidate();
    }

    public IndexView(Context context, int fontSize, int textColor, int bgColor, IndexTouchListener listener) {
        this(context);
        this.listener = listener;
        mBgColor = bgColor;
        mFontSize = fontSize;
        mTextColor = textColor;
    }

    public IndexView(Context context) {
        this(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        mPaint = new Paint();
        mPaint.setTextSize(mFontSize);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int w = (int) mPaint.measureText("AB") + 10; // 加上 10像素的边距，单纯时为了显示更好
        setMeasuredDimension(w, h);
        int th = (int) (mPaint.getFontMetrics().descent-mPaint.getFontMetrics().ascent);
        while (h < th * indexText.length + 1) {
            mFontSize--;
            mPaint.setTextSize(mFontSize);
            mPaint.measureText("AB");
            th = (int) (mPaint.getFontMetrics().descent-mPaint.getFontMetrics().ascent);
        }
        Log.d(TAG, "onMeasure: "+h+","+th);
        height = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = indexText.length;
        canvas.drawColor(mBgColor);

        //每个字符的平均高度
        int perIndexHeight = height / count;
        Log.d(TAG, "onDraw: "+height+","+perIndexHeight);
        //从上而下画出每个字符
        for (int i = 0; i < count; i++) {
            mPaint.measureText(indexText[i]);
            int textHeight = (int) (mPaint.getFontMetrics().descent-mPaint.getFontMetrics().ascent);
            int textWidth = (int) mPaint.measureText(indexText[i]);

            int left = (getWidth() - textWidth) / 2;
            int top = perIndexHeight * i + (perIndexHeight + textHeight) / 2;
            Log.d(TAG, "onDraw: "+top);
            canvas.drawText(indexText[i], left, top, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ac = event.getAction();
        if (ac == MotionEvent.ACTION_DOWN) {
            int pos = getPositionByTouch(event.getY());
            if (listener != null)
                listener.touchAt(indexText[pos], pos);
        } else if (ac == MotionEvent.ACTION_MOVE) {
            int pos = getPositionByTouch(event.getY());
            if (listener != null)
                listener.touchMove(indexText[pos], pos);
        } else if (ac == MotionEvent.ACTION_UP || ac == MotionEvent.ACTION_CANCEL) {
            int pos = getPositionByTouch(event.getY());
            if (listener != null)
                listener.touchDone(indexText[pos], pos);
        }

        return true;
    }


    /**
     * 根据滑动的y坐标确定滑动到的字符
     *
     * @param y
     * @return
     */
    private int getPositionByTouch(float y) {
        int position = (int) (indexText.length * y / height);

        if (position < 0 || position >= indexText.length)
            position = 0;
        return position;
    }

    public void setIndexTouchListener(IndexTouchListener listener) {
        this.listener = listener;
    }

    private IndexTouchListener listener;

    /**
     * 滑动的回调接口
     */
    public interface IndexTouchListener {

        /**
         * 手指按下
         *
         * @param indexText
         * @param position
         */
        void touchAt(String indexText, int position);

        /**
         * 手指滑动
         *
         * @param indexText
         * @param position
         */
        void touchMove(String indexText, int position);

        /**
         * 滑动结束
         */
        void touchDone(String indexText, int position);
    }
}
