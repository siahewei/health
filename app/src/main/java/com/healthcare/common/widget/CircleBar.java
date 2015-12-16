package com.healthcare.common.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.healthcare.common.utils.DisplayUtils;

/**
 * project     healthcare
 *
 * @author hewei
 * @verstion 15/12/11
 */
public class CircleBar extends View{

    private RectF mColorWheelRectangle = new RectF();// 圆圈的矩形范围
    private Paint mDefaultWheelPaint;// 绘制底部灰色圆圈的画笔

    private int circleStrokeWith = 20;
    private int pressExtraStrokeWith = 2;
    private int centTxtSize = 0;
    private int otherTxtSize = 1;
    private int lineHight;
    private float mColorWheelRadius;// 圆圈普通状态下的半径

    private Paint circlePaint;
    private Paint textPaint;
    private Paint otherPaint;

    private Paint weightPaint;

    BarAnimation anim;// 动画类

    private float weight;

    private int mText;// 中间文字内容
    private int mCount;// 为了达到数字增加效果而添加的变量，他和mText其实代表一个意思
    private float mProgressAni;// 为了达到蓝色扇形增加效果而添加的变量，他和mProgress其实代表一个意思
    private float mProgress;// 扇形弧度

    public CircleBar(Context context) {
        super(context);
        init();
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        circleStrokeWith = dip2px(getContext(), 20);        // width of cirlce
        pressExtraStrokeWith = dip2px(getContext(), 2);     //
        centTxtSize = DisplayUtils.sp2px(60);
        otherTxtSize = DisplayUtils.sp2px(30);
        lineHight = dip2px(getContext(), 70);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(0xFF29a6f6);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(circleStrokeWith);

        // 绘制底部灰色圆圈的画笔
        mDefaultWheelPaint = new Paint();
        mDefaultWheelPaint.setAntiAlias(true);
        mDefaultWheelPaint.setColor(Color.parseColor("#d9d6c3"));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWith);


        textPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.parseColor("#6DCAEC"));
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(centTxtSize);

        otherPaint = new Paint(Paint.LINEAR_TEXT_FLAG);
        otherPaint.setAntiAlias(true);
        otherPaint.setColor(Color.parseColor("#a1a3a6"));
        otherPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        otherPaint.setTextAlign(Paint.Align.LEFT);
        otherPaint.setTextSize(otherTxtSize);


        weightPaint = new Paint();
        weightPaint.setStyle(Paint.Style.FILL);
        weightPaint.setColor(0x8075d572);

        if (weight < 0.3){
            weightPaint.setColor(0x8075d572);
        }else if (weight < 0.7){
            weightPaint.setColor(0x80a3e9a4);
        }else {
            weightPaint.setColor(0x80d0f8ce);
        }



        mText = 0;
        mProgress = 0;
        anim = new BarAnimation();
        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
    }



    public static int dip2px(Context context, float dpValue){
        float scale = context.getResources().getDisplayMetrics().density;

        return (int)(dpValue * scale + 0.5f);

    }


    public class BarAnimation extends Animation {

        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime <= 1.0f) {
                mProgressAni = interpolatedTime * mProgress;
                //mCount = (int) (interpolatedTime * mText);
            } else {
               // mProgressAni = mProgress;
                //mCount = mText;
            }
            postInvalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        int halfHeight = getHeight() / 2;
        int halfWidth = getWidth() / 2;
        int radius = halfHeight < halfWidth ? halfHeight : halfWidth;
        // 圆圈的矩形范围 绘制底部灰色圆圈的画笔
        canvas.drawCircle(halfWidth, halfHeight, radius - 25,
                mDefaultWheelPaint);

        // mColorWheelRectangle是绘制蓝色扇形的画笔
        mColorWheelRectangle.top = halfHeight - radius + 25f;
        mColorWheelRectangle.bottom = halfHeight + radius - 25f;
        mColorWheelRectangle.left = halfWidth - radius + 25f;
        mColorWheelRectangle.right = halfWidth + radius - 25f;
        // 根据mProgressAni（角度）画扇形

        mCount = 10000;

        canvas.drawArc(mColorWheelRectangle, -90, 360 * (mProgressAni / (float)mCount), false,
                circlePaint);


        Rect bounds = new Rect();
        String middleText = null;// 中间的文字
        String upText = null;// 上面文字
        String downText = null;// 底部文字


        upText = "步数";
        downText = "目标:10000";
        middleText = String.valueOf((int)mProgressAni);

        canvas.drawCircle(mColorWheelRectangle.centerX(), mColorWheelRectangle.centerY(), weight * mColorWheelRadius / 3, weightPaint);

        // 中间文字的画笔
        textPaint.getTextBounds(middleText, 0, middleText.length(), bounds);
        // drawText各个属性的意思(文字,x坐标,y坐标,画笔)
        canvas.drawText(middleText, (mColorWheelRectangle.centerX())
                        - (textPaint.measureText(middleText) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2, textPaint);

        otherPaint.getTextBounds(upText, 0, upText.length(), bounds);

        canvas.drawText(
                upText,
                (mColorWheelRectangle.centerX())
                        - (otherPaint.measureText(upText) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2
                        - lineHight, otherPaint);

        otherPaint.getTextBounds(downText, 0, downText.length(), bounds);
        canvas.drawText(downText, (mColorWheelRectangle.centerX())
                        - (otherPaint.measureText(downText) / 2),
                mColorWheelRectangle.centerY() + bounds.height() / 2
                        + lineHight, otherPaint);



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getDefaultSize(getSuggestedMinimumHeight(),
                heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        mColorWheelRadius = min - circleStrokeWith - pressExtraStrokeWith;

        // set方法的参数意思：left,top,right,bottom
        mColorWheelRectangle.set(circleStrokeWith + pressExtraStrokeWith,
                circleStrokeWith + pressExtraStrokeWith, mColorWheelRadius,
                mColorWheelRadius);
    }

    public void setText(int text){
        mText = text;
        postInvalidate();
    }

    public void startCustomAnimation(){
        this.startAnimation(anim);
    }

    public void setProgress(float progress, float value){
        mProgress = progress;
        mProgressAni = progress;

        weight = value / 5.0f;

        if (weight >1){
            weight = 1;
        }

        postInvalidate();
    }
}
