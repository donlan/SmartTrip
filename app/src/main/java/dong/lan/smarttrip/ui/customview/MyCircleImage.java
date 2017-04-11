package dong.lan.smarttrip.ui.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;

/**
 * Created by 梁桂栋 on 17-1-30 ： 上午11:42.
 * Email:       760625325@qq.com
 * GitHub:      github.com/donlan
 * description: SmartTrip
 */

public class MyCircleImage extends ImageView {


    private static final int KEY_SHADOW_COLOR = 503316480;
    private static final int FILL_SHADOW_COLOR = 1023410176;
    private static final float X_OFFSET = 0.0F;
    private static final float Y_OFFSET = 1.75F;
    private static final float SHADOW_RADIUS = 3.5F;
    private static final int SHADOW_ELEVATION = 4;
    private Animation.AnimationListener mListener;
    int mShadowRadius;


    public MyCircleImage(Context context) {
        this(context, null);
    }

    public MyCircleImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCircleImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        float density = this.getContext().getResources().getDisplayMetrics().density;
        int shadowYOffset = (int) (density * 1.75F);
        int shadowXOffset = (int) (density * 0.0F);
        this.mShadowRadius = (int) (density * 3.5F);
        ShapeDrawable circle;
        if (this.elevationSupported()) {
            circle = new ShapeDrawable(new OvalShape());
            ViewCompat.setElevation(this, 4.0F * density);
        } else {
            MyCircleImage.OvalShadow oval = new MyCircleImage.OvalShadow(this.mShadowRadius);
            circle = new ShapeDrawable(oval);
            ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, circle.getPaint());
            circle.getPaint().setShadowLayer((float) this.mShadowRadius, (float) shadowXOffset, (float) shadowYOffset, 503316480);
            int padding = this.mShadowRadius;
            this.setPadding(padding, padding, padding, padding);
        }

        circle.getPaint().setColor(Color.WHITE);
        ViewCompat.setBackground(this, circle);
    }


    private boolean elevationSupported() {
        return Build.VERSION.SDK_INT >= 21;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!this.elevationSupported()) {
            this.setMeasuredDimension(this.getMeasuredWidth() + this.mShadowRadius * 2, this.getMeasuredHeight() + this.mShadowRadius * 2);
        }

    }

    public void setAnimationListener(Animation.AnimationListener listener) {
        this.mListener = listener;
    }

    public void onAnimationStart() {
        super.onAnimationStart();
        if (this.mListener != null) {
            this.mListener.onAnimationStart(this.getAnimation());
        }

    }

    public void onAnimationEnd() {
        super.onAnimationEnd();
        if (this.mListener != null) {
            this.mListener.onAnimationEnd(this.getAnimation());
        }

    }

    public void setBackgroundColorRes(int colorRes) {
        this.setBackgroundColor(ContextCompat.getColor(this.getContext(), colorRes));
    }

    public void setBackgroundColor(int color) {
        if (this.getBackground() instanceof ShapeDrawable) {
            ((ShapeDrawable) this.getBackground()).getPaint().setColor(color);
        }

    }

    private class OvalShadow extends OvalShape {
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint = new Paint();

        OvalShadow(int shadowRadius) {
            MyCircleImage.this.mShadowRadius = shadowRadius;
            this.updateRadialGradient((int) this.rect().width());
        }

        protected void onResize(float width, float height) {
            super.onResize(width, height);
            this.updateRadialGradient((int) width);
        }

        public void draw(Canvas canvas, Paint paint) {
            int viewWidth = MyCircleImage.this.getWidth();
            int viewHeight = MyCircleImage.this.getHeight();
            canvas.drawCircle((float) (viewWidth / 2), (float) (viewHeight / 2), (float) (viewWidth / 2), this.mShadowPaint);
            canvas.drawCircle((float) (viewWidth / 2), (float) (viewHeight / 2), (float) (viewWidth / 2 - MyCircleImage.this.mShadowRadius), paint);
        }

        private void updateRadialGradient(int diameter) {
            this.mRadialGradient = new RadialGradient((float) (diameter / 2), (float) (diameter / 2), (float) MyCircleImage.this.mShadowRadius, new int[]{1023410176, 0}, (float[]) null, Shader.TileMode.CLAMP);
            this.mShadowPaint.setShader(this.mRadialGradient);
        }
    }
}
