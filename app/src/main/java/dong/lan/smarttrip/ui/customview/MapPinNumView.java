package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.tencent.qcloud.ui.utils.DisplayUtils;

/**
 * Created by 梁桂栋 on 17-3-21 ： 下午10:48.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class MapPinNumView extends View {

    private static final String TAG = MapPinNumView.class.getSimpleName();
    private String num;
    private int bgColor;
    private int textSize;
    private int textColor;


    private Paint paint;
    private int textWidth;
    private int mWidth;
    private float textHeigth;
    private RectF bound;
    private int radius;

    public MapPinNumView(Context context, String num, int bgColor, int textSize, int textColor) {
        super(context);
        this.num = num;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.textSize = DisplayUtils.sp2px(getContext(), textSize);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(bgColor);
        paint.setTextSize(this.textSize);
        bound = new RectF(0, 0, 0, 0);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        radius = 0;
        textWidth = (int) paint.measureText(num);
        textHeigth = paint.getFontMetrics().descent - paint.getFontMetrics().ascent;
        mWidth = textWidth + DisplayUtils.dip2px(getContext(), 20);
        radius = mWidth / 2;
        bound.set(0, 0, radius, radius);
        setMeasuredDimension(mWidth + getPaddingLeft() + getPaddingRight(),
                (int) (mWidth + radius / Math.sin(Math.PI / 4)) + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(mWidth / 2, mWidth / 2);
        canvas.rotate(45);
        canvas.drawRect(bound, paint);
        canvas.restore();
        paint.setColor(bgColor);
        canvas.drawCircle(radius, mWidth / 2, mWidth / 2, paint);
        paint.setColor(textColor);
        canvas.drawText(num, (mWidth - textWidth) / 2, (mWidth + textHeigth) / 2, paint);

    }
}
