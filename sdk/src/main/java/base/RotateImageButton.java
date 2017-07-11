package base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.blankj.ALog;

/**
 * Created by 梁桂栋 on 17-7-7 ： 下午5:55.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class RotateImageButton extends android.support.v7.widget.AppCompatImageButton {
    public RotateImageButton(Context context) {
        super(context);
    }

    public RotateImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float angle = 0;
    private final int step = 10;
    private final int STATE_LOADING = 1;
    private final int STATE_FINISH = 0;
    private int state = STATE_FINISH;

    private boolean isFirst = true;

    @Override
    protected void onDraw(Canvas canvas) {

        if (state == STATE_LOADING) {
            angle += step;
            if (angle >= 360) {
                angle = angle - 360;
            }
            canvas.rotate(angle,getWidth()/2,getHeight()/2);
            postInvalidateDelayed(100);
        } else {
            canvas.rotate(360 - angle,getWidth()/2,getHeight()/2);
        }
        super.onDraw(canvas);
    }

    public boolean isStarted(){
        return state == STATE_LOADING;
    }

    public void start() {
        state = STATE_LOADING;
        invalidate();
    }

    public void finish() {
        state = STATE_FINISH;
        invalidate();
    }
}
