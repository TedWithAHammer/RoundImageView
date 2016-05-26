package com.leo.library_roudimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

/**
 * Created by Leo on 2016/5/26.
 */
public class XFModeRoundImageView extends ImageView {

    private static final int TYPE_ROUND = 1;
    private static final int TYPE_CIRCLE = 1;
    private int viewType;
    private int cornorRadius;

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public XFModeRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        viewType = ta.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        cornorRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.RGB_565);
        float scale = 1.0f;
        Canvas shapeCanvas = new Canvas(bitmap);
        if (viewType == TYPE_CIRCLE) {

        }
    }
}
