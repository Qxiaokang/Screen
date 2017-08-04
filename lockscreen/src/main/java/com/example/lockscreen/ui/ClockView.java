package com.example.lockscreen.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.example.lockscreen.R;
import com.example.lockscreen.utils.LogUtils;

import java.util.Calendar;

/**
 * Created by Admin on 2017/5/31.
 */
public class ClockView extends View{
    private Paint mPanit;//画笔
    private Paint mTextPaint;
    private float mRadius;//半径
    private float mTextSize;//文字大小
    private int mTextColor;//文字颜色
    private int sScale,mScale,bScale;//小，中，大刻度颜色
    private float HWide,MWide,SWide;//时，分，秒指针宽度
    private int mCircleColor;//外圆颜色
    private int mWidth,mHight;
    private float h_length,m_length,s_length;
    private int padding=8;
    private int widelength=30;
    public static  final int TIME_START=1;
    public ClockView(Context context){
        this(context,null);
    }
    public ClockView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public ClockView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ClockView);
        mRadius=typedArray.getDimension(R.styleable.ClockView_mRadius,200);
        mTextSize=typedArray.getDimension(R.styleable.ClockView_mTextSize,20);
        mTextColor=typedArray.getColor(R.styleable.ClockView_mTextColor,Color.BLACK);
        sScale=typedArray.getColor(R.styleable.ClockView_sScale,Color.GREEN);
        mScale=typedArray.getColor(R.styleable.ClockView_mScale,Color.BLUE);
        bScale=typedArray.getColor(R.styleable.ClockView_bScale,Color.RED);
        HWide=typedArray.getDimension(R.styleable.ClockView_HWide,15);
        MWide=typedArray.getDimension(R.styleable.ClockView_HWide,13);
        SWide=typedArray.getDimension(R.styleable.ClockView_SWide,10);
        mCircleColor=typedArray.getColor(R.styleable.ClockView_mCircleColor,Color.BLUE);
        h_length= (float) (mRadius*2/4.0)+widelength/4;
        m_length= (float) (mRadius*3/4.0)+widelength/4;
        s_length= mRadius+widelength/44;
        mPanit=new Paint();
        mPanit.setAntiAlias(true);
        mPanit.setDither(true);
        LogUtils.i("mradius:"+mRadius);
        mTextPaint=new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);//粗体
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),MeasureSpec.getSize(heightMeasureSpec));
    }
    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        mWidth=getMeasuredWidth()-getPaddingLeft()-getPaddingRight();
        mHight=getMeasuredHeight()-getPaddingTop()-getPaddingBottom();
        LogUtils.d("mwidth:"+mWidth+"  mhight:"+mHight);
        //绘圆
        drawCircle(canvas);
        //绘刻度
        drawScale(canvas);
        //绘指针
        drawNeedle(canvas);
    }
    private void drawNeedle(Canvas canvas){
        Calendar calendar=Calendar.getInstance();
        int hours=calendar.get(Calendar.HOUR);
        int min=calendar.get(Calendar.MINUTE);
        int sec=calendar.get(Calendar.SECOND);
        //时
        canvas.save();
        canvas.rotate(hours*30,mWidth/2,mHight/2);
        mPanit.setStrokeCap(Paint.Cap.ROUND);
        mPanit.setColor(bScale);
        mPanit.setStrokeWidth(HWide);
        canvas.drawLine(mWidth/2,mHight/2+(widelength-10)*2,mWidth/2,mHight/2+(widelength-20)*2-h_length,mPanit);
        canvas.restore();
        //分
        canvas.save();
        mPanit.setStrokeCap(Paint.Cap.ROUND);
        mPanit.setColor(mScale);
        mPanit.setStrokeWidth(MWide);
        canvas.rotate(min*6,mWidth/2,mHight/2);
        canvas.drawLine(mWidth/2,mHight/2+(widelength-5)*2,mWidth/2,mHight/2+(widelength-10)*2-m_length,mPanit);
        canvas.restore();
        //秒
        canvas.save();
        canvas.rotate(sec*6,mWidth/2,mHight/2);
        mPanit.setStrokeCap(Paint.Cap.ROUND);
        mPanit.setColor(sScale);
        mPanit.setStrokeWidth(SWide);
        canvas.drawLine(mWidth/2,mHight/2+widelength*2,mWidth/2,mHight/2+widelength*2-s_length,mPanit);
        canvas.restore();
        //中心圆
        mPanit.setStyle(Paint.Style.FILL);
        mPanit.setColor(sScale);
        canvas.drawCircle(mWidth/2,mHight/2,HWide/2+8,mPanit);
        handler.sendEmptyMessageDelayed(TIME_START,1000);
    }

    private void drawScale(Canvas canvas){
        int cMargin= (int) ((mHight-mRadius*2)/2);
        String text="";
        mPanit.setStyle(Paint.Style.STROKE);
        for(int i = 0; i < 60; i++){
            if(i==0||i==15||i==30||i==45)
            {
                mPanit.setStrokeWidth(8);
                mPanit.setColor(bScale);
                canvas.drawLine(mWidth/2,mHight/2-mRadius+padding,mWidth/2,mHight/2-mRadius+padding+widelength,mPanit);
                text=String.valueOf(i==0?12:i/5);
                canvas.save();
                //获取字体的高度
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                int textHeight= (int) (Math.ceil(fontMetrics.bottom+fontMetrics.top));
                canvas.rotate(-i*6,mWidth/2,cMargin+padding*4+widelength+textHeight/2);
                canvas.drawText(text,mWidth/2-mTextPaint.measureText(text)/2,cMargin+padding*4+widelength,mTextPaint);
                canvas.restore();
            }else if(i==5||i==10||i==20||i==25||i==35||i==40||i==50||i==55){
                mPanit.setStrokeWidth(6);
                mPanit.setColor(mScale);
                canvas.drawLine(mWidth/2,mHight/2-mRadius+padding,mWidth/2,mHight/2-mRadius+padding+widelength-10,mPanit);
                canvas.save();
                text=String.valueOf(i==0?12:i/5);
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                int textHeight= (int) (Math.ceil(fontMetrics.bottom+fontMetrics.top));
                canvas.rotate(-i*6,mWidth/2,cMargin+padding*4+widelength+textHeight/2);
                canvas.drawText(text,mWidth/2-mTextPaint.measureText(text)/2,cMargin+padding*4+widelength,mTextPaint);
                canvas.restore();
            }
            else {
                mPanit.setStrokeWidth(4);
                mPanit.setColor(sScale);
                canvas.drawLine(mWidth/2,mHight/2-mRadius+padding,mWidth/2,mHight/2-mRadius+padding+widelength-20,mPanit);
            }
            canvas.rotate(6,mWidth/2,mHight/2);
        }
//        canvas.drawLine(mWidth/2,mHight-cMargin-padding-widelength,mWidth/2,mHight-cMargin-padding,mPanit);
//        canvas.drawLine(padding+cMargin,mHight/2,padding+widelength+cMargin,mHight/2,mPanit);
//        canvas.drawLine(mWidth-padding-cMargin,mHight/2,mWidth-padding-widelength-cMargin,mHight/2,mPanit);
    }

    //画圆
    private void drawCircle(Canvas canvas){
        mPanit.setStyle(Paint.Style.STROKE);
        mPanit.setColor(mCircleColor);
        mPanit.setStrokeWidth(8);
        canvas.drawCircle(mWidth/2,mHight/2,mRadius,mPanit);
    }
    private int measureSize(int mMeasureSpec) {
        int result;
        int mode=MeasureSpec.getMode(mMeasureSpec);
        int size=MeasureSpec.getSize(mMeasureSpec);
        if(mode==MeasureSpec.EXACTLY){
            result=size;
        }else{
            result=500;
            if(mode==MeasureSpec.AT_MOST){
                result=Math.min(result,size);
            }
        }
        return  result;
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch(msg.what){
                case TIME_START:
                    invalidate();
                    break;
                default:
                    break;
            }
        }
    };
}
