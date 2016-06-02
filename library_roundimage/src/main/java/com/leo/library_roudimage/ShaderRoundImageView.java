package com.leo.library_roudimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
    private static final int TYPE_LOOP = 2;
    int viewWidth;

    Matrix mMatrix = new Matrix();
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mLoopPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    BitmapShader mBitmapShader;
    SweepGradient mSweepGradient;
    int mBorderColor;
    int mBorderWidth;

    public ShaderRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        type = ta.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        roundRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_cornerRadius,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        mBorderColor = ta.getColor(R.styleable.RoundImageView_borderColor, Color.WHITE);
        mBorderWidth = ta.getDimensionPixelOffset(R.styleable.RoundImageView_borderWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE || type == TYPE_LOOP) {
            viewWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            circleRadius = viewWidth / 2 - mBorderWidth;
            setMeasuredDimension(viewWidth, viewWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        setUpShader();
        if (type == TYPE_CIRCLE) {
            canvas.drawCircle(circleRadius, circleRadius, circleRadius, mPaint);
        } else if (type == TYPE_LOOP) {
            mLoopPaint.setShader(mSweepGradient);
            canvas.drawCircle(viewWidth / 2, viewWidth / 2, viewWidth / 2, mLoopPaint);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            canvas.drawCircle(viewWidth / 2, viewWidth / 2, circleRadius, mPaint);
            mPaint.setXfermode(null);
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
        Log.i("------", "bitmap original width：" + bitmap.getWidth() + "height" + bitmap.getHeight());
        if (type == TYPE_CIRCLE) {
            int minWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = viewWidth * 1.0f / minWidth;
        } else if (type == TYPE_LOOP) {
            int minWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
            scale = viewWidth * 1.0f / minWidth;
            mSweepGradient = new SweepGradient(viewWidth / 2, viewWidth / 2, mBorderColor, mBorderColor);
        } else {
            scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight() * 1.0f / bitmap.getHeight());
        }
        mMatrix.setScale(scale, scale);
        //显示图片中心
        if (bitmap.getWidth() * 1.0 / bitmap.getHeight() < 1) {
            mMatrix.postTranslate(0, -getHeight() / 2);
        } else {
            mMatrix.postTranslate(-getWidth() / 2, 0);
        }
        mBitmapShader.setLocalMatrix(mMatrix);
        Log.i("------", "bitmap new width：" + bitmap.getWidth() + "height" + bitmap.getHeight());
        mPaint.setShader(mBitmapShader);
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
