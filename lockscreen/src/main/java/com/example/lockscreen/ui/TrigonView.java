package com.example.lockscreen.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Admin on 2017/6/1.
 */
public class TrigonView extends View{
    private float mWidth = 200;//边长

    private float moveX=0, moveY=0;
    private Paint mPaint;
    private Path path;
    private static int IS_UP=-1;

    public TrigonView(Context context){
        this(context, null);
    }

    public TrigonView(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public TrigonView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(moveX!=0&&moveY!=0){
//            drawTrigon(canvas);
            drawCircle(canvas);
        }
        if(IS_UP==1){
            //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清空
            mPaint.setColor(Color.rgb(60, 63, 65));
            canvas.drawPaint(mPaint);
        }
    }

    private void drawCircle(Canvas canvas){
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(moveX,moveY,mWidth/2,mPaint);
    }

    //
    private void drawTrigon(Canvas canvas){
        path = new Path();
        path.moveTo(moveX, (float) (moveY - (Math.sqrt(Math.pow(mWidth,2)-Math.pow(mWidth/2,2)))/2));
        path.lineTo(moveX - mWidth / 2, (float) (moveY + (Math.sqrt(Math.pow(mWidth,2)-Math.pow(mWidth/2,2)))/2));
        path.lineTo(moveX + mWidth / 2, (float) (moveY + (Math.sqrt(Math.pow(mWidth,2)-Math.pow(mWidth/2,2)))/2));
        path.close();
        canvas.drawPath(path, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        moveX = event.getX();
        moveY = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                IS_UP=1;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                    IS_UP=-1;
                    invalidate();
                break;
            default:
                break;
        }
        return true;
    }
}
