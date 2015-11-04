package com.unique.circleprogressimage.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

import com.unique.circleprogressimage.R;

/**
 * Created by daiyiming on 2015/8/10.
 */
public class CircleProgressImage extends ImageView {

    private Paint slipPaint = null;
    private Paint maskPaint = null;
    private Paint textPaint = null;

    private int borderColor = 0;
    private int borderWidth = 0;
    private int progress = 0;

    private Bitmap shape = null;
    private Bitmap mask = null;

    private boolean isNewMask = true;

    public CircleProgressImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);

        TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.CircleProgressImageView);

        borderColor = typedArray.getColor(R.styleable.CircleProgressImageView_borderColor, Color.parseColor("#D41313"));
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.CircleProgressImageView_borderWidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getContext().getResources().getDisplayMetrics()));
        int textColor = typedArray.getDimensionPixelOffset(R.styleable.CircleProgressImageView_textColor, Color.WHITE);
        int textSize= typedArray.getDimensionPixelSize(R.styleable.CircleProgressImageView_textSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
        progress = typedArray.getInt(R.styleable.CircleProgressImageView_progress, 0);

        slipPaint = new Paint();
        slipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        slipPaint.setFilterBitmap(false);

        maskPaint = new Paint();
        maskPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        maskPaint.setFilterBitmap(false);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize); //设置20sp大小的文字
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        try {
            int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
            drawable.setBounds(0, 0, getWidth(), getHeight());
            drawable.draw(canvas);
            //切割
            if (shape == null || shape.isRecycled()) {
                shape = getShape(getWidth(), getHeight());
            }
            canvas.drawBitmap(shape, 0, 0, slipPaint);
            //画圆环
            Paint ringPaint = new Paint();
            ringPaint.setAntiAlias(true);
            ringPaint.setStyle(Paint.Style.STROKE);
            ringPaint.setColor(borderColor);
            ringPaint.setStrokeWidth(borderWidth);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getWidth() - borderWidth) / 2, ringPaint);
            //画透明格子
            if (isNewMask) {
                mask = getMask(getWidth(), getHeight());
                isNewMask = false;
            }
            canvas.drawBitmap(mask, 0, 0, maskPaint);

            //画文字
            String text = progress + "%";
            Rect textRect = new Rect();
            textPaint.getTextBounds(text, 0, text.length(), textRect);
            canvas.drawText(text, (getWidth() - textRect.width()) / 2, (getHeight() + textRect.height()) / 2, textPaint);

            canvas.restoreToCount(layer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap getShape(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        RectF localRectF = new RectF(0, 0, width, height);
        Paint paint = new Paint();
        paint.setAntiAlias(true); //去锯齿
        canvas.drawOval(localRectF, paint);
        return bitmap;
    }

    private Bitmap getMask(int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true); //去锯齿
        paint.setColor(borderColor);
        paint.setAlpha(160);
        if (this.progress <= 100) {
            canvas.drawRect(0, ((float) height / 100) * (100 - progress), width, height, paint);
        } else {
            canvas.drawRect(0, 0, width, height, paint);
        }
        return bitmap;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        isNewMask = true;
        this.invalidate();
    }

}
