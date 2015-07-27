package cn.sdu.online.findteam.resource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import cn.sdu.online.findteam.R;

public class WiperSwitch extends View {
    public static final int SWITCH_OFF = 0;// 关闭状态
    public static final int SWITCH_ON = 1;// 打开状态
    public static final int SWITCH_SCROLING = 2;// 滚动状态
    // 用于显示的文本,默认为打开和关闭
    private String mOnText = "是";
    private String mOffText = "否";

    /**
     * SwitchButton切换开关的状态，默认为关闭状态
     */
    private int mSwitchStatus = SWITCH_OFF;

    private int mBmpWidth = 0;
    private int mBmpHeight = 0;
    private int mThumbWidth = 0;
    private OnSwitchChangedListener listener;

    private int mSrcX = 0, mDstX = 0;

    private boolean mHasScrolled = false;// 表示是否发生过滚动

    // 开关状态图
    Bitmap mSwitch_off, mSwitch_on, mSwitch_thumb;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public WiperSwitch(Context context) {
        this(context, null);
    }

    public WiperSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WiperSwitch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    /**
     * 初始化图片
     */
    private void initView() {
        Resources res = getResources();
        mSwitch_off = BitmapFactory.decodeResource(res,
                R.drawable.switch_off);
        mSwitch_on = BitmapFactory.decodeResource(res, R.drawable.switch_on);
        mSwitch_thumb = BitmapFactory.decodeResource(res,
                R.drawable.slipper);
        mBmpWidth = mSwitch_on.getWidth();
        mBmpHeight = mSwitch_on.getHeight();
        mThumbWidth = mSwitch_thumb.getWidth();
    }

    @Override
    public void setLayoutParams(LayoutParams params) {
        params.width = mBmpWidth;
        params.height = mBmpHeight;
        super.setLayoutParams(params);
    }

    /**
     * 设置开关上面的文本
     *
     * @param onText  控件打开时要显示的文本
     * @param offText 控件关闭时要显示的文本
     */
    public void setText(String onText, String offText) {
        mOnText = onText;
        mOffText = offText;
        invalidate();// 使整个视图无效，重新绘制，用来刷新View
    }

    /**
     * 设置开关的状态
     *
     * @param flag true为开，false为关闭
     */
    public void setStatus(boolean flag) {
        mSwitchStatus = flag ? SWITCH_ON : SWITCH_OFF;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mSrcX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mDstX = Math.max((int) event.getX(), 10);
                mDstX = Math.min(mDstX, 62);
                if (mSrcX == mDstX)
                    return true;
                mHasScrolled = true;
                TranslateAnimationRunnable move_runnable = new TranslateAnimationRunnable(
                        mSrcX, mDstX, 0);
                new Thread(move_runnable).start();
                mSrcX = mDstX;
                break;
            case MotionEvent.ACTION_UP:
                if (mHasScrolled == false)// 如果没有发生过滑动，就意味着这是一次单击过程
                {
                    mSwitchStatus = Math.abs(mSwitchStatus - 1);
                    int xFrom = 10, xTo = 62;
                    if (mSwitchStatus == SWITCH_OFF) {
                        xFrom = 62;
                        xTo = 10;
                    }
                    TranslateAnimationRunnable runnable = new TranslateAnimationRunnable(
                            xFrom, xTo, 1);
                    new Thread(runnable).start();
                } else {
                    invalidate();
                    mHasScrolled = false;
                }
                // 状态改变的时候 回调事件函数
                if (listener != null) {
                    listener.onSwitchChanged(this, mSwitchStatus);
                }
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘图的时候 内部用到了一些数值的硬编码，其实不太好，
        // 主要是考虑到图片的原因，图片周围有透明边界，所以要有一定的偏移
        // 硬编码的数值只要看懂了代码，其实可以理解其含义，可以做相应改进。
        mPaint.setTextSize(30);// 设置画笔文字大小
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);// 设置文字字体
        if (mSwitchStatus == SWITCH_OFF) {
            drawBitmap(canvas, null, null, mSwitch_off);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.translate(mSwitch_thumb.getWidth(), 0);
            canvas.drawText(mOffText, 0, 35, mPaint);
        } else if (mSwitchStatus == SWITCH_ON) {
            drawBitmap(canvas, null, null, mSwitch_on);
            int count = canvas.save();
            canvas.translate(mSwitch_on.getWidth() - mSwitch_thumb.getWidth(),
                    0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            mPaint.setColor(Color.WHITE);
            canvas.restoreToCount(count);
            canvas.drawText(mOnText, 17, 35, mPaint);
        } else {
            mSwitchStatus = mDstX > 35 ? SWITCH_ON : SWITCH_OFF;
            // 首先画出打开按钮样式
            drawBitmap(canvas, new Rect(0, 0, mDstX, mBmpHeight), new Rect(0,
                    0, (int) mDstX, mBmpHeight), mSwitch_on);
            mPaint.setColor(Color.WHITE);
            canvas.drawText(mOnText, 17, 20, mPaint);

            // 在画出关闭按钮的样式
            int count = canvas.save();
            canvas.translate(mDstX, 0);
            drawBitmap(canvas, new Rect(mDstX, 0, mBmpWidth, mBmpHeight),
                    new Rect(0, 0, mBmpWidth - mDstX, mBmpHeight), mSwitch_off);
            canvas.restoreToCount(count);

            // 画出关闭的文字
            count = canvas.save();
            // clipRect()截取画布中的一个区域,在这个区域的基础上画上关闭的文字
            canvas.clipRect(mDstX, 0, mBmpWidth, mBmpHeight);
            canvas.translate(mThumbWidth, 0);
            mPaint.setColor(Color.rgb(105, 105, 105));
            canvas.drawText(mOffText, 0, 20, mPaint);
            canvas.restoreToCount(count);

            // 画出那个圆点按钮
            count = canvas.save();
            canvas.translate(mDstX - mThumbWidth / 2, 0);
            drawBitmap(canvas, null, null, mSwitch_thumb);
            canvas.restoreToCount(count);
        }
    }

    /**
     * 画出Bitmap
     *
     * @param canvas
     * @param src
     * @param dst
     * @param bitmap
     */
    private void drawBitmap(Canvas canvas, Rect src, Rect dst, Bitmap bitmap) {
        dst = (dst == null ? new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight()) : dst);
        Paint paint = new Paint();
        canvas.drawBitmap(bitmap, src, dst, paint);
    }

    public interface OnSwitchChangedListener {
        /**
         * 状态改变 回调函数
         *
         * @param status SWITCH_ON表示打开 SWITCH_OFF表示关闭
         */
        public abstract void onSwitchChanged(WiperSwitch switchButton,
                                             int status);
    }

    /**
     * 为开关控件设置状态改变监听函数
     *
     * @param listener
     */
    public void setOnSwitchChangedListener(OnSwitchChangedListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    /**
     * 改变控件状态的动画线程
     *
     * @author loonggg
     */
    private class TranslateAnimationRunnable implements Runnable {
        private int srcX, dstX;
        private int duration;

        /**
         * 滑动动画
         *
         * @param srcX     滑动起始点
         * @param dstX     滑动终止点
         * @param duration 是否采用动画，1采用，0不采用
         */
        public TranslateAnimationRunnable(float srcX, float dstX, int duration) {
            this.srcX = (int) srcX;
            this.dstX = (int) dstX;
            this.duration = duration;
        }

        @Override
        public void run() {
            final int patch = (dstX > srcX ? 5 : -5);
            if (duration == 0) {
                WiperSwitch.this.mSwitchStatus = SWITCH_SCROLING;
                WiperSwitch.this.postInvalidate();
            } else {
                int x = srcX + patch;
                while (Math.abs(x - dstX) > 5) {
                    mDstX = x;
                    WiperSwitch.this.mSwitchStatus = SWITCH_SCROLING;
                    WiperSwitch.this.postInvalidate();
                    x += patch;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mDstX = dstX;
                WiperSwitch.this.mSwitchStatus = mDstX > 35 ? SWITCH_ON
                        : SWITCH_OFF;
                WiperSwitch.this.postInvalidate();
            }
        }

    }

}

