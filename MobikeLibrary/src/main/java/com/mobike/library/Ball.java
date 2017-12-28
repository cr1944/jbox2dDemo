package com.mobike.library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import org.jbox2d.dynamics.Body;

/**
 * Create time: 2017/12/26.
 */

public class Ball {
  private float x, y;
  private float w, h;
  private float rotation;
  private Drawable drawable;
  private boolean isCircle;
  private float forceLeft, forceRight;
  float addLeft, addRight;
  //Bitmap bitmap;
  //BitmapShader bitmapShader;
  //Paint bitmapPaint;
  private Body body;

  public Ball(Context context, float x, float y, int resId, boolean isCircle) {
    this.x = x;
    this.y = y;
    this.drawable = context.getResources().getDrawable(resId);
    this.w = drawable.getIntrinsicWidth();
    this.h = drawable.getIntrinsicHeight();
    this.isCircle = isCircle;
    //bitmapPaint = new Paint();
    //bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
    //bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    //bitmapPaint.setAntiAlias(true);
    //bitmapPaint.setShader(bitmapShader);
  }

  public float getX() {
    return x;
  }

  public float getY() {
    return y;
  }

  public void setX(float x) {
    this.x = x;
  }

  public void setY(float y) {
    this.y = y;
  }

  public float getWidth() {
    return w;
  }

  public void setWidth(float w) {
    this.w = w;
  }

  public float getHeight() {
    return h;
  }

  public void setHeight(float h) {
    this.h = h;
  }

  public Drawable getDrawable() {
    return drawable;
  }

  public void setDrawable(Drawable drawable) {
    this.drawable = drawable;
  }

  public boolean isCircle() {
    return isCircle;
  }

  public void setCircle(boolean circle) {
    isCircle = circle;
  }

  public float getRotation() {
    return rotation;
  }

  public void setRotation(float rotation) {
    this.rotation = rotation;
  }

  public Body getBody() {
    return body;
  }

  public void setBody(Body body) {
    this.body = body;
  }

  public float getForceLeft() {
    return forceLeft;
  }

  public void setForceLeft(float forceLeft) {
    this.forceLeft = forceLeft;
  }

  public float getForceRight() {
    return forceRight;
  }

  public void setForceRight(float forceRight) {
    this.forceRight = forceRight;
  }

  public float getAddLeft() {
    return addLeft;
  }

  public void setAddLeft(float addLeft) {
    this.addLeft = addLeft;
  }

  public float getAddRight() {
    return addRight;
  }

  public void setAddRight(float addRight) {
    this.addRight = addRight;
  }
}
