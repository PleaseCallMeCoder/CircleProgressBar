package com.coder.circlebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.DecimalFormat;

public class CircleBar extends View {

    private RectF mColorWheelRectangle = new RectF();//定义一个矩形,包含矩形的四个单精度浮点坐标
    private Paint mDefaultWheelPaint;
    private Paint mColorWheelPaint;//进度条的画笔
    private Paint mColorWheelPaintCentre;
    private float circleStrokeWidth;
    private float mSweepAnglePer;
    private float mPercent;
    private int stepnumber, stepnumbernow;
    private float pressExtraStrokeWidth;
    private BarAnimation anim;
    private int stepnumbermax = 12;// 默认最大时间
    private DecimalFormat fnum = new DecimalFormat("#.0");// 格式为保留小数点后一位

    private int mColors[];
    private Context context;

    public CircleBar(Context context) {
        super(context);
        this.context = context;
        init(null, 0);
    }

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs, 0);
    }

    public CircleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        /**
         * 设置画笔渐变色
         */
        mColors = new int[]{0xFF6BB7ED, 0xFF47D9CE, 0xFF56CADC};
        Shader s = new SweepGradient(0, 0, mColors, null);
        mColorWheelPaint = new Paint();
        mColorWheelPaint.setShader(s);
        mColorWheelPaint.setStyle(Paint.Style.STROKE);// 空心,只绘制轮廓线
        mColorWheelPaint.setStrokeCap(Paint.Cap.ROUND);// 圆角画笔
        mColorWheelPaint.setAntiAlias(true);// 去锯齿

        mColorWheelPaintCentre = new Paint();
        mColorWheelPaintCentre.setColor(Color.rgb(214, 246, 254));
        mColorWheelPaintCentre.setStyle(Paint.Style.STROKE);
        mColorWheelPaintCentre.setStrokeCap(Paint.Cap.ROUND);
        mColorWheelPaintCentre.setAntiAlias(true);

        mDefaultWheelPaint = new Paint();
        mDefaultWheelPaint.setColor(Color.rgb(127, 127, 127));
        mDefaultWheelPaint.setStyle(Paint.Style.STROKE);
        mDefaultWheelPaint.setStrokeCap(Paint.Cap.ROUND);
        mDefaultWheelPaint.setAntiAlias(true);

        anim = new BarAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * drawArc (RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint)
         * oval是RecF类型的对象，其定义了椭圆的形状
         * startAngle指的是绘制的起始角度，钟表的3点位置对应着0度，如果传入的startAngle小于0或者大于等于360，那么用startAngle对360进行取模后作为起始绘制角度。
         * sweepAngle指的是从startAngle开始沿着钟表的顺时针方向旋转扫过的角度。如果sweepAngle大于等于360，那么会绘制完整的椭圆弧。如果sweepAngle小于0，那么会用sweepAngle对360进行取模后作为扫过的角度。
         * useCenter是个boolean值，如果为true，表示在绘制完弧之后，用椭圆的中心点连接弧上的起点和终点以闭合弧；如果值为false，表示在绘制完弧之后，弧的起点和终点直接连接，不经过椭圆的中心点。
         */
        canvas.drawArc(mColorWheelRectangle, 0, 359, false, mDefaultWheelPaint);
        canvas.drawArc(mColorWheelRectangle, 0, 359, false, mColorWheelPaintCentre);
        canvas.drawArc(mColorWheelRectangle, 270, mSweepAnglePer, false, mColorWheelPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形
        circleStrokeWidth = Textscale(35, min);// 圆弧的宽度
        pressExtraStrokeWidth = Textscale(2, min);// 圆弧离矩形的距离
        mColorWheelRectangle.set(circleStrokeWidth + pressExtraStrokeWidth,
                circleStrokeWidth + pressExtraStrokeWidth, min
                        - circleStrokeWidth - pressExtraStrokeWidth, min
                        - circleStrokeWidth - pressExtraStrokeWidth);// 设置矩形
        mColorWheelPaint.setStrokeWidth(circleStrokeWidth - 5);
        mColorWheelPaintCentre.setStrokeWidth(circleStrokeWidth + 5);
        mDefaultWheelPaint.setStrokeWidth(circleStrokeWidth - Textscale(2, min));
        mDefaultWheelPaint.setShadowLayer(Textscale(10, min), 0, 0, Color.rgb(127, 127, 127));// 设置阴影
    }

    /**
     * 进度条动画
     *
     * @author Administrator
     */
    public class BarAnimation extends Animation {
        public BarAnimation() {

        }

        /**
         * 每次系统调用这个方法时， 改变mSweepAnglePer，mPercent，stepnumbernow的值，
         * 然后调用postInvalidate()不停的绘制view。
         */
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                mPercent = Float.parseFloat(fnum.format(interpolatedTime
                        * stepnumber * 100f / stepnumbermax));// 将浮点值四舍五入保留一位小数
                mSweepAnglePer = interpolatedTime * stepnumber * 360
                        / stepnumbermax;
                stepnumbernow = (int) (interpolatedTime * stepnumber);
            } else {
                mPercent = Float.parseFloat(fnum.format(stepnumber * 100f
                        / stepnumbermax));// 将浮点值四舍五入保留一位小数
                mSweepAnglePer = stepnumber * 360 / stepnumbermax;
                stepnumbernow = stepnumber;
            }
            postInvalidate();
        }
    }

    /**
     * 根据控件的大小改变绝对位置的比例
     *
     * @param n
     * @param m
     * @return
     */
    public float Textscale(float n, float m) {
        return n / 500 * m;
    }

    /**
     * 更新步数和设置一圈动画时间
     *
     * @param stepnumber
     * @param time
     */
    public void update(int stepnumber, int time) {
        this.stepnumber = stepnumber;
        anim.setDuration(time);
        //setAnimationTime(time);
        this.startAnimation(anim);
    }

    /**
     * 设置每天的最大步数
     *
     * @param Maxstepnumber
     */
    public void setMaxstepnumber(int Maxstepnumber) {
        stepnumbermax = Maxstepnumber;
    }

    /**
     * 设置进度条颜色
     *
     * @param red
     * @param green
     * @param blue
     */
    public void setColor(int red, int green, int blue) {
        mColorWheelPaint.setColor(Color.rgb(red, green, blue));
    }

    /**
     * 设置动画时间
     *
     * @param time
     */
    public void setAnimationTime(int time) {
        anim.setDuration(time * stepnumber / stepnumbermax);// 按照比例设置动画执行时间
    }

}
