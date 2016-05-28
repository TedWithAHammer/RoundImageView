package com.leo.library_roudimage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
    private int type;
    private int cornorRadius;
    private int circleRadius;

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public XFModeRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        type = ta.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);
        cornorRadius = ta.getDimensionPixelSize(R.styleable.RoundImageView_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (type == TYPE_CIRCLE) {
            int width = Math.min(getWidth(), getHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null)
            return;
        circleRadius = Math.min(getWidth() / 2, getHeight() / 2);
        float scale=1.0f;
        int actWidth=drawable.getIntrinsicWidth();
        int actHeight=drawable.getIntrinsicHeight();
        Bitmap bmp=Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        Canvas temp=new Canvas(bmp);
        scale= Math.min(actWidth/getWidth(),actHeight/getHeight());
        drawable.setBounds(0,0, (int) (getHeight()*scale), (int) (getWidth()*scale));
        drawable.draw(temp);
        Bitmap maskBmp = getMaskBitMap();

        mPaint.reset();
        mPaint.setFilterBitmap(false);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        temp.drawBitmap(maskBmp, 0, 0, mPaint);
        mPaint.setXfermode(null);
        canvas.drawBitmap(bmp,0,0,mPaint);
    }

    Bitmap getMaskBitMap() {
        Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, paint);
        return result;
    }
}
