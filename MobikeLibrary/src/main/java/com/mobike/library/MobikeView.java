package com.mobike.library;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kimi on 2017/7/8 0008.
 * Email: 24750@163.com
 */

public class MobikeView extends View {

    private Mobike mMobike;
    private List<Ball> children = new ArrayList<>();
    private Ball touchedChild;
    private VelocityTracker mVelocityTracker;
    private int mPointerId;

    public MobikeView(@NonNull Context context) {
        this(context,null);
    }

    public MobikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //setWillNotDraw(false);
        mMobike = new Mobike(this);
        mVelocityTracker = VelocityTracker.obtain();
    }

    public void addBall(Ball ball) {
        children.add(ball);
    }

    public List<Ball> getBallList() {
        return children;
    }

    public Ball getTouchedChild() {
        return touchedChild;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMobike.onSizeChanged(w,h);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mMobike.onLayout(changed);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mMobike.onDraw();
        for (Ball ball : children) {
            if (!ball.isCircle()) {
                ball.setForceLeft(ball.getForceLeft() + ball.getAddLeft());
                mMobike.moveLeft(ball, ball.getForceLeft());
                ball.setForceRight(ball.getForceRight() + ball.getAddRight());
                mMobike.moveRight(ball, ball.getForceRight());
            }
            int left = (int) (ball.getX() - ball.getWidth() / 2);
            int top = (int) (ball.getY() - ball.getHeight() / 2);
            int right = (int) (ball.getX() + ball.getWidth() / 2);
            int bottom = (int) (ball.getY() + ball.getHeight() / 2);
            ball.getDrawable().setBounds(left, top, right, bottom);
            canvas.save();
            canvas.rotate(ball.getRotation(), ball.getX(), ball.getY());
            ball.getDrawable().draw(canvas);
            canvas.restore();
        }
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mVelocityTracker.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPointerId = event.getPointerId(0);
                touchedChild = getTouchedChild(x, y);
                if (touchedChild != null) {
                    mMobike.setTransform(touchedChild, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.computeCurrentVelocity(100, 100);
                final float velocityX = mVelocityTracker.getXVelocity(mPointerId);
                final float velocityY = mVelocityTracker.getYVelocity(mPointerId);
                if (touchedChild != null) {
                    touchedChild.setX(x);
                    touchedChild.setY(y);
                    mMobike.setTransform(touchedChild, velocityX, velocityY);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (touchedChild != null) {
                    touchedChild = null;
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private Ball getTouchedChild(float x, float y) {
        for (Ball ball : children) {
            if (x >= ball.getX() - ball.getWidth() / 2 && x <= ball.getX() + ball.getWidth() / 2
                && y >= ball.getY() - ball.getHeight() / 2 && y <= ball.getY() + ball.getHeight() / 2) {
                return ball;
            }
        }
        return null;
    }

    public Mobike getmMobike(){
        return this.mMobike;
    }
}
