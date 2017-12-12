package com.asura.library_textratingbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Created by Asura on 2017/12/12 12:21.
 *         分段选择评级View
 */
public class TextRatingBar extends View {

    /**
     * 画布 left 起始点
     */
    private int mLeft;
    /**
     * 画布 top 起始点
     */
    private int mTop;
    /**
     * 当前 rating
     */
    private int mRating;
    /**
     * 上一次 rating
     */
    private int mPreRating;
    /**
     * rating 总个数
     */
    private int mCount;
    /**
     * 每个 rating 的间距
     */
    private int mUnitSize;

    /**
     * 画笔对象
     */
    private Paint mPaint;

    /**
     * 文字在 bar 上面
     */
    private static final int TOP = 0;

    /**
     * 文字在 bar 下面
     */
    private static final int BOTTOM = 1;

    //默认值
    private static final int DEFAULT_BASE_LINE_COLOR = 0XFFFF0000;
    private static final int DEFAULT_BASE_LINE_SELECT_COLOR = 0XFF00FF00;
    private static final int DEFAULT_BASE_LINE_WIDTH = 4;
    private static final int DEFAULT_BASE_DIVIDER_HEIGHT = 20;
    private static final int DEFAULT_BASE_DIVIDER_WIDTH = 4;
    private static final int DEFAULT_THUMB_RADIUS = 30;
    private static final int DEFAULT_THUMB_COLOR = 0XFF00FF00;
    private static final int DEFAULT_THUMB_STROKE_WIDTH = 4;
    private static final int DEFAULT_TEXT_POSITION = BOTTOM;
    private static final int DEFAULT_TEXT_SIZE = 36;
    private static final int DEFAULT_TEXT_MARGIN = 60;
    private static final String[] DEFAULT_TEXTS = new String[]{"10%", "25%", "75%", "100%"};

    /**
     * bar 线条颜色
     */
    private int mBaseLineColor = DEFAULT_BASE_LINE_COLOR;
    /**
     * bar 线条选中的颜色
     */
    private int mBaseLineSelectColor = DEFAULT_BASE_LINE_SELECT_COLOR;
    /**
     * bar 线条宽度
     */
    private int mBaseLineWidth = DEFAULT_BASE_LINE_WIDTH;
    /**
     * 分割线高度
     */
    private int mDividerHeight = DEFAULT_BASE_DIVIDER_HEIGHT;
    /**
     * 分割线宽度
     */
    private int mDividerWidth = DEFAULT_BASE_DIVIDER_WIDTH;
    /**
     * 指示器半径
     */
    private int mThumbRadius = DEFAULT_THUMB_RADIUS;
    /**
     * 指示器颜色
     */
    private int mThumbColor = DEFAULT_THUMB_COLOR;
    /**
     * 指示器外圈宽度
     */
    private int mThumbStrokeWidth = DEFAULT_THUMB_STROKE_WIDTH;
    /**
     * rating 文字的位置
     */
    private int mTextPosition = DEFAULT_TEXT_POSITION;
    /**
     * rating 文字大小
     */
    private int mTextSize = DEFAULT_TEXT_SIZE;
    /**
     * rating 文字与 bar 的间距
     */
    private int mTextMargin = DEFAULT_TEXT_MARGIN;
    /**
     * rating 上文字数组
     */
    private String[] mTexts = DEFAULT_TEXTS;

    public TextRatingBar(Context context) {
        this(context, null);
    }

    public TextRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mCount = mTexts.length;
    }

    /**
     * 初始化属性值 attrs
     *
     * @param context 上下文对象
     * @param attrs   自定义属性集合
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextRatingBar);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.TextRatingBar_trb_baseLineColor) {
                mBaseLineColor = typedArray.getColor(attr, DEFAULT_BASE_LINE_COLOR);
            } else if (attr == R.styleable.TextRatingBar_trb_baseLineSelectColor) {
                mBaseLineSelectColor = typedArray.getColor(attr, DEFAULT_BASE_LINE_SELECT_COLOR);
            } else if (attr == R.styleable.TextRatingBar_trb_baseLineWidth) {
                mBaseLineWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_THUMB_STROKE_WIDTH);
            } else if (attr == R.styleable.TextRatingBar_trb_dividerHeight) {
                mDividerHeight = typedArray.getDimensionPixelSize(attr, DEFAULT_BASE_DIVIDER_HEIGHT);
            } else if (attr == R.styleable.TextRatingBar_trb_dividerWidth) {
                mDividerWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_BASE_DIVIDER_WIDTH);
            } else if (attr == R.styleable.TextRatingBar_trb_thumbRadius) {
                mThumbRadius = typedArray.getDimensionPixelSize(attr, DEFAULT_THUMB_RADIUS);
            } else if (attr == R.styleable.TextRatingBar_trb_thumbColor) {
                mThumbColor = typedArray.getColor(attr, DEFAULT_THUMB_COLOR);
            } else if (attr == R.styleable.TextRatingBar_trb_thumbStrokeWidth) {
                mThumbStrokeWidth = typedArray.getDimensionPixelSize(attr, DEFAULT_THUMB_STROKE_WIDTH);
            } else if (attr == R.styleable.TextRatingBar_trb_textPosition) {
                mTextPosition = typedArray.getInt(attr, TOP);
            } else if (attr == R.styleable.TextRatingBar_trb_textSize) {
                mTextSize = typedArray.getDimensionPixelSize(attr, UIUtils.sp2px(context, DEFAULT_TEXT_SIZE));
            } else if (attr == R.styleable.TextRatingBar_trb_textMargin) {
                mTextMargin = typedArray.getDimensionPixelSize(attr, UIUtils.dip2px(context, DEFAULT_TEXT_MARGIN));
            } else if (attr == R.styleable.TextRatingBar_trb_texts) {
                mTexts = convertCharSequenceArrayToStringArray(typedArray.getTextArray(attr));
            }
        }
        //回收
        typedArray.recycle();
    }

    /**
     * 转换成字符串数组
     */
    private String[] convertCharSequenceArrayToStringArray(CharSequence[] textArray) {
        if (textArray == null) {
            return null;
        }
        String[] result = new String[textArray.length];
        for (int i = 0; i < textArray.length; i++) {
            result[i] = textArray[i].toString();
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //重新测量宽高
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        Log.i("Asura", getMeasuredWidth() + " * " + getMeasuredHeight());
        //这里要算上指示器的半径
        mLeft = getPaddingLeft() + mThumbRadius;
        mTop = getPaddingTop();
        //选项条宽度 = 总宽度 - 指示器 thumb 的直径，因为两边要空出 thumb 的占位
        int barWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - 2 * mThumbRadius;
        mUnitSize = barWidth / (mCount - 1);
        Log.i("Asura", "barWidth=" + barWidth + "   mUnitSize=" + mUnitSize);
    }

    private int measureHeight(int heightMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //将画出的视图元素高度进行相加
            if (mThumbRadius > mDividerHeight / 2) {
                result = getPaddingTop() + getPaddingBottom() + mTextSize + mTextMargin + mThumbRadius * 2;
            } else {
                result = getPaddingTop() + getPaddingBottom() + mTextSize + mTextMargin + mDividerHeight;
            }
            if (specMode == MeasureSpec.AT_MOST) {
                //取最小值
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private int measureWidth(int widthMeasureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = 300;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        //文字在底部的情况
        if (mTextPosition == BOTTOM) {
            //bar 线的 y 坐标
            int lineY = 0;
            //需要比较分割线高度的一半与指示器的半径谁更大
            if (mDividerHeight / 2 > mThumbRadius) {
                lineY = mTop + mDividerHeight / 2;
            } else {
                lineY = mTop + mThumbRadius;
            }
            //画未选中的线
            mPaint.setStrokeWidth(mBaseLineWidth);
            mPaint.setColor(mBaseLineColor);
            canvas.drawLine(mLeft, lineY, mLeft + (mCount - 1) * mUnitSize, lineY, mPaint);
            //画分割线
            mPaint.setStrokeWidth(mDividerWidth);
            for (int i = 0; i < mCount; i++) {
                mPaint.setColor(mRating >= i ? mBaseLineSelectColor : mBaseLineColor);
                //画分割线
                canvas.drawLine(mLeft + i * mUnitSize, lineY - mDividerHeight / 2, mLeft + i * mUnitSize, lineY + mDividerHeight / 2, mPaint);

                mPaint.setColor(mRating == i ? mBaseLineSelectColor : mBaseLineColor);
                mPaint.setTextSize(mTextSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                String text = mTexts[i];
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                //画text
                float baseline = 0;
                if (mDividerHeight / 2 > mThumbRadius) {
                    baseline = lineY + mThumbRadius + mTextMargin - fontMetrics.top;
                } else {
                    baseline = lineY + mDividerHeight / 2 + mTextMargin - fontMetrics.top;
                }
                //画文字
                canvas.drawText(text, mLeft + i * mUnitSize, baseline, mPaint);
            }
            //画选中的线(比未选中线条粗1像素，更好遮挡)
            mPaint.setStrokeWidth(mBaseLineWidth + 1);
            mPaint.setColor(mBaseLineSelectColor);
            canvas.drawLine(mLeft, lineY, mLeft + mRating * mUnitSize, lineY, mPaint);
            //绘制外圆
            this.mPaint.setColor(mThumbColor);
            this.mPaint.setStrokeWidth(mThumbStrokeWidth);
            canvas.drawCircle(mLeft + mRating * mUnitSize, lineY, mThumbRadius, this.mPaint);
            mPaint.setColor(Color.WHITE);
            //绘制内圆
            canvas.drawCircle(mLeft + mRating * mUnitSize, lineY, mThumbRadius - mThumbStrokeWidth, mPaint);
        } else if (mTextPosition == TOP) {
            //文字在顶部的情况
            //画分割线
            mPaint.setStrokeWidth(mDividerWidth);
            //分割线的 y 坐标
            int dividerY = 0;
            for (int i = 0; i < mCount; i++) {
                mPaint.setColor(mRating == i ? mBaseLineSelectColor : mBaseLineColor);
                mPaint.setTextSize(mTextSize);
                mPaint.setTextAlign(Paint.Align.CENTER);
                String text = mTexts[i];
                Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
                float baseline = mTop - fontMetrics.top;
                //画文字
                canvas.drawText(text, mLeft + i * mUnitSize, baseline, mPaint);
//                canvas.drawRect(bounds, mPaint);
                //画分割线
                if (mDividerHeight / 2 > mThumbRadius) {
                    dividerY = mTop + mTextSize + mTextMargin;
                } else {
                    dividerY = mTop + mTextSize + mTextMargin + (mThumbRadius - mDividerHeight / 2);
                }
                mPaint.setColor(mRating >= i ? mBaseLineSelectColor : mBaseLineColor);
                canvas.drawLine(mLeft + i * mUnitSize, dividerY,
                        mLeft + i * mUnitSize, dividerY + mDividerHeight, mPaint);
            }
            //画未选中的线
            mPaint.setStrokeWidth(mBaseLineWidth);
            mPaint.setColor(mBaseLineColor);
            canvas.drawLine(mLeft, dividerY + mDividerHeight / 2, mLeft + (mCount - 1) * mUnitSize, dividerY + mDividerHeight / 2, mPaint);
            //画选中的线(比未选中线条粗1像素，更好遮挡)
            mPaint.setStrokeWidth(mBaseLineWidth + 1);
            mPaint.setColor(mBaseLineSelectColor);
            canvas.drawLine(mLeft, dividerY + mDividerHeight / 2, mLeft + mRating * mUnitSize, dividerY + mDividerHeight / 2, mPaint);
            //绘制外圆
            this.mPaint.setColor(mThumbColor);
            this.mPaint.setStrokeWidth(mThumbStrokeWidth);
            canvas.drawCircle(mLeft + mRating * mUnitSize, dividerY + mDividerHeight / 2, mThumbRadius, this.mPaint);
            mPaint.setColor(Color.WHITE);
            //绘制内圆
            canvas.drawCircle(mLeft + mRating * mUnitSize, dividerY + mDividerHeight / 2, mThumbRadius - mThumbStrokeWidth, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            for (int i = 0; i < mCount; i++) {
                float distance = mLeft + i * mUnitSize - x;
                if (Math.abs(distance) < 100) {
                    setRating(i);
                    break;
                }
            }
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (onRatingListener != null) {
                if (mPreRating != mRating) {
                    mPreRating = mRating;
                    onRatingListener.onRatingChanged(mRating, mTexts[mRating]);
                }
            }
        }
        return true;
    }

    /**
     * 设置 rating 值
     *
     * @param rating rating 值
     */
    public void setRating(int rating) {
        if (rating >= mCount) {
            rating = 0;
        }
        mRating = rating;
        invalidate();
    }

    /**
     * 设置 rating 文字数组
     *
     * @param texts 文字数组
     */
    public void setTexts(String[] texts) {
        if (texts == null) {
            throw new NullPointerException("texts can not be null ！");
        }
        this.mTexts = texts;
        invalidate();
    }

    private OnRatingListener onRatingListener;

    /**
     * 设置 rating 值改变监听器
     *
     * @param onRatingListener rating 值改变监听器
     * @see OnRatingListener#onRatingChanged(int, String)
     */
    public void setOnRatingListener(OnRatingListener onRatingListener) {
        this.onRatingListener = onRatingListener;
    }

    public interface OnRatingListener {
        /**
         * rating 值改变时回调
         *
         * @param rating     rating值
         * @param ratingText rating 文字
         */
        void onRatingChanged(int rating, String ratingText);
    }
}