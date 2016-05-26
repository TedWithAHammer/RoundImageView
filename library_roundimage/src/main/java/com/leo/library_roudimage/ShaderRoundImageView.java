package com.leo.library_roudimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by Leo on 2016/5/26.
 */
public class ShaderRoundImageView extends ImageView {
    int type = 0;
    int roundRadius = 10;
    float circleRadius;
    private static final int TYPE_CIRCLE = 0;
    private static final int TYPE_ROUND = 1;
    int viewWidth;

    Matrix mMatrix = new Matrix();
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    BitmapShader mBitmapShader;

    public ShaderRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        type = ta.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        roundRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            viewWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            circleRadius = viewWidth / 2;
            setMeasuredDimension(viewWidth, viewWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        setUpShader();
        if (type == TYPE_CIRCLE) {
            canvas.drawCircle(circleRadius, circleRadius, circleRadius, mPaint);
        } else {
            canvas.drawRoundRect(roundRec, roundRadius, roundRadius, mPaint);
        }
    }

    RectF roundRec;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (type == TYPE_ROUND) {
            roundRec = new RectF(0, 0, getWidth(), getHeight());
        }
    }

    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        Bitmap bitmap = drawable2Bitmap(drawable);
        mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f; //bitmap:canvas ratio
        if (type == TYPE_CIRCLE) {
            int minWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = viewWidth * 1.0f / minWidth;
        } else {
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        if (scale < 1.0f) {
            setScaleType(ScaleType.CENTER_INSIDE);
        } else {
            setScaleType(ScaleType.CENTER_CROP);
            mMatrix.setScale(scale, scale);
            mBitmapShader.setLocalMatrix(mMatrix);
            mPaint.setShader(mBitmapShader);
        }
    }

    private Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}
