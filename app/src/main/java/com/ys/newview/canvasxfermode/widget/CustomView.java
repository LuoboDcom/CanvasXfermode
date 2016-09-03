package com.ys.newview.canvasxfermode.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ys.newview.canvasxfermode.R;

/**  自定义view
 * Created by ys on 2016/9/2.
 */

public class CustomView extends View{


    private Paint mPaint3;
    private PorterDuffXfermode pdXfermode;
    private String  modeStr;

    private int screenW   = 800;
    private int screenH   = 300; //屏幕宽高

    private int width = 200;
    private int height = 200;

    private Bitmap srcBitmap, dstBitmap;     //上层SRC的Bitmap和下层Dst的Bitmap

    public CustomView(Context context) {
        this(context,null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomView);
        int mode = array.getInt(R.styleable.CustomView_Xfermode,0);

        init(mode);
    }

    private void init(int mode) {
        mPaint3 = new Paint();
        mPaint3.setColor(Color.BLACK);
        mPaint3.setTextSize(40);
        PorterDuff.Mode mMode;
        switch (mode){
            case 0:
            default:
                mMode = PorterDuff.Mode.CLEAR;
                modeStr = "CLEAR";
                break;
            case 1:
                mMode = PorterDuff.Mode.SRC;
                modeStr = "SRC";
                break;
            case 2:
                mMode = PorterDuff.Mode.DST;
                modeStr = "DST";
                break;
            case 3:
                mMode = PorterDuff.Mode.SRC_OVER;
                modeStr = "SRC_OVER";
                break;
            case 4:
                mMode = PorterDuff.Mode.DST_OVER;
                modeStr = "DST_OVER";
                break;
            case 5:
                mMode = PorterDuff.Mode.SRC_IN;
                modeStr = "SRC_IN";
                break;
            case 6:
                mMode = PorterDuff.Mode.DST_IN;
                modeStr = "DST_IN";
                break;
            case 7:
                mMode = PorterDuff.Mode.SRC_OUT;
                modeStr = "SRC_OUT";
                break;
            case 8:
                mMode = PorterDuff.Mode.DST_OUT;
                modeStr = "DST_OUT";
                break;
            case 9:
                mMode = PorterDuff.Mode.SRC_ATOP;
                modeStr = "SRC_ATOP";
                break;
            case 10:
                mMode = PorterDuff.Mode.DST_ATOP;
                modeStr = "DST_ATOP";
                break;
            case 11:
                mMode = PorterDuff.Mode.XOR;
                modeStr = "XOR";
                break;
            case 12:
                mMode = PorterDuff.Mode.ADD;
                modeStr = "ADD";
                break;
            case 13:
                mMode = PorterDuff.Mode.MULTIPLY;
                modeStr = "MULTIPLY";
                break;
            case 14:
                mMode = PorterDuff.Mode.SCREEN;
                modeStr = "SCREEN";
                break;
            case 15:
                mMode = PorterDuff.Mode.OVERLAY;
                modeStr = "OVERLAY";
                break;
            case 16:
                mMode = PorterDuff.Mode.DARKEN;
                modeStr = "DARKEN";
                break;
            case 17:
                mMode = PorterDuff.Mode.LIGHTEN;
                modeStr = "LIGHTEN";
                break;
        }

        pdXfermode = new PorterDuffXfermode(mMode);

        //实例化两个Bitmap
        srcBitmap = makeSrc(width, height);
        dstBitmap = makeDst(width, height);
    }

    //定义一个绘制圆形Bitmap的方法
    private Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.rgb(255,203,68));
        c.drawOval(new RectF(0, 0, w * 3 / 4, h * 3 / 4), p);
        return bm;
    }

    //定义一个绘制矩形的Bitmap的方法
    private Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.rgb(103,169,255));
        c.drawRect(w / 3, h / 3, w * 19 / 20, h * 19 / 20, p);
        return bm;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        screenW = getWidth();
        screenH = getHeight();
        Log.i("Custom","screenW="+screenW+"===screenH="+screenH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        // 原上图层
        canvas.drawBitmap(srcBitmap, (screenW / 3 - width) / 2, (screenH* 3 / 4  - height) / 2, paint);
        // 原下图层
        canvas.drawBitmap(dstBitmap, (screenW / 3 - width) / 2 + screenW / 3, (screenH* 3 / 4  - height) / 2, paint);

        canvas.drawText("srcBitmap",(screenW / 3 - width) / 2,screenH * 3 / 4,mPaint3);
        canvas.drawText("dstBitmap",(screenW / 3 - width) / 2 + screenW / 3,screenH * 3 / 4,mPaint3);

        //创建一个图层，在图层上演示图形混合后的效果
        int sc = canvas.saveLayer(0, 0, screenW, screenH, null, Canvas.MATRIX_SAVE_FLAG |
                Canvas.CLIP_SAVE_FLAG |
                Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(dstBitmap, (screenW / 3 - width) / 2 + screenW / 3 * 2,
                (screenH* 3 / 4  - height) / 2, paint);     //绘制i
        //设置Paint的Xfermode
        paint.setXfermode(pdXfermode);
        canvas.drawBitmap(srcBitmap, (screenW / 3 - width) / 2 + screenW / 3 * 2,
                (screenH* 3 / 4  - height) / 2, paint);

        canvas.drawText(modeStr,(screenW / 3 - width) / 2 + screenW / 3 * 2,screenH * 3 / 4,mPaint3);
        paint.setXfermode(null);
        // 还原画布
        canvas.restoreToCount(sc);

    }
}
