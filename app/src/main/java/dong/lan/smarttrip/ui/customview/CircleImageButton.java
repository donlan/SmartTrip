package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

import dong.lan.smarttrip.R;

/**
 * Created by 梁桂栋 on 17-1-18 ： 下午10:09.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class CircleImageButton extends ImageButton {

    private int bg_color = Color.BLUE;
    private Paint mPaint;

    public CircleImageButton(Context context) {
        this(context, null);
    }

    public CircleImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageButton);
            bg_color = ta.getColor(R.styleable.CircleImageButton_cib_bg_color, bg_color);
            ta.recycle();
        }
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(bg_color);
    }


    public void setBgColor(int bgColor) {
        this.bg_color = bgColor;
        mPaint.setColor(bgColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int d = Math.min(getWidth(),getHeight());
        canvas.drawCircle(d / 2, d / 2, d / 2, mPaint);
        super.onDraw(canvas);
    }
}
