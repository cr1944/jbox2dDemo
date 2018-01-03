package com.mobike.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * Create time: 2018/1/2.
 */

public class MyView extends View {
  private static final float dt = 1f / 60;
  private static final int velocityIterations = 3;
  private static final int positionIterations = 10;
  private static final float friction = 0.3f;
  private static final float density = 0.5f;
  private static final float restitution = 0.3f;
  private static final float ratio = 50;
  private static final float gravity = 10f;
  private World world;
  private int width, height;
  private int handRadius;
  private int handDistance;
  private boolean enable = true;
  private Body leftHand, rightHand;
  private Body stick;
  private Body ball;
  private int stickWidth, stickHeight;
  private int ballRadius;
  private Paint paint;

  public MyView(Context context) {
    super(context);
    init(context);
  }

  public MyView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    handRadius = dp2px(context, 5);
    handDistance = dp2px(context, 200);
    stickHeight = dp2px(context, 10);
    stickWidth = dp2px(context, 300);
    ballRadius = dp2px(context, 8);
    paint = new Paint();
    paint.setAntiAlias(true);
  }

  private void createWorld() {
    if(world == null){
      world = new World(new Vec2(0, gravity));
      world.setAllowSleep(false);
      createTopAndBottomBounds();
      createLeftAndRightBounds();
      createHand();
      createStick();
      createBall();
    }
  }

  private void createTopAndBottomBounds() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;

    float boxWidth = pixelsToMeters(width);
    float boxHeight = 10;
    PolygonShape box = createPolygonShape(boxWidth, boxHeight);

    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    fixtureDef.density = density;
    fixtureDef.friction = friction;
    fixtureDef.restitution = restitution;

    bodyDef.position.set(boxWidth / 2, - boxHeight / 2);
    Body topBody = world.createBody(bodyDef);
    topBody.createFixture(fixtureDef);

    bodyDef.position.set(boxWidth / 2, pixelsToMeters(height)+ boxHeight / 2);
    Body bottomBody = world.createBody(bodyDef);
    bottomBody.createFixture(fixtureDef);
  }

  private void createLeftAndRightBounds() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;

    float boxWidth = 10;
    float boxHeight = pixelsToMeters(height);
    PolygonShape box = createPolygonShape(boxWidth, boxHeight);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    fixtureDef.density = density;
    fixtureDef.friction = friction;
    fixtureDef.restitution = restitution;

    bodyDef.position.set(-boxWidth / 2, boxHeight / 2);
    Body leftBody = world.createBody(bodyDef);
    leftBody.createFixture(fixtureDef);


    bodyDef.position.set(pixelsToMeters(width) + boxWidth / 2, boxHeight / 2);
    Body rightBody = world.createBody(bodyDef);
    rightBody.createFixture(fixtureDef);
  }

  private void createHand() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.STATIC;
    CircleShape circleShape = createCircleShape(pixelsToMeters(handRadius));
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = circleShape;
    fixtureDef.density = density;
    fixtureDef.friction = friction;
    fixtureDef.restitution = restitution;
    bodyDef.position.set(pixelsToMeters((width - handDistance) / 2), pixelsToMeters(height - handRadius * 2));
    leftHand = world.createBody(bodyDef);
    leftHand.createFixture(fixtureDef);
    bodyDef.position.set(pixelsToMeters((width + handDistance) / 2), pixelsToMeters(height - handRadius * 2));
    rightHand = world.createBody(bodyDef);
    rightHand.createFixture(fixtureDef);
  }

  private void createStick() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    float boxWidth = pixelsToMeters(stickWidth);
    float boxHeight = pixelsToMeters(stickHeight);
    PolygonShape box = createPolygonShape(boxWidth, boxHeight);
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    fixtureDef.density = density;
    fixtureDef.friction = friction;
    fixtureDef.restitution = restitution;
    bodyDef.position.set(pixelsToMeters(width / 2), pixelsToMeters(height - handRadius * 2 - stickHeight * 2));
    stick = world.createBody(bodyDef);
    stick.createFixture(fixtureDef);
    //stick.setLinearVelocity(new Vec2(0, 0.5f));
  }

  private void createBall() {
    BodyDef bodyDef = new BodyDef();
    bodyDef.type = BodyType.DYNAMIC;
    CircleShape box = createCircleShape(pixelsToMeters(ballRadius));
    FixtureDef fixtureDef = new FixtureDef();
    fixtureDef.shape = box;
    fixtureDef.density = density;
    fixtureDef.friction = friction;
    fixtureDef.restitution = restitution;
    bodyDef.position.set(pixelsToMeters(width / 2), pixelsToMeters(height - handRadius * 2 - stickHeight * 4));
    ball = world.createBody(bodyDef);
    ball.createFixture(fixtureDef);
    //stick.setLinearVelocity(new Vec2(0, 0.5f));
  }

  private CircleShape createCircleShape(float radius) {
    CircleShape circleShape = new CircleShape();
    circleShape.setRadius(radius);
    return circleShape;
  }

  private PolygonShape createPolygonShape(float width, float height) {
    PolygonShape polygonShape = new PolygonShape();
    polygonShape.setAsBox(width / 2, height / 2);
    return polygonShape;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    this.width = w;
    this.height = h;
    this.world = null;
    createWorld();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        break;
      case MotionEvent.ACTION_MOVE:
        break;
      case MotionEvent.ACTION_UP:
        break;
      default:
        break;
    }
    return super.onTouchEvent(event);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    if(!enable){
      return;
    }
    world.step(dt, velocityIterations, positionIterations);
    float left_cx = metersToPixels(leftHand.getPosition().x);
    float left_cy = metersToPixels(leftHand.getPosition().y);
    paint.setColor(0xffff0000);
    canvas.drawCircle(left_cx, left_cy, handRadius, paint);
    float right_cx = metersToPixels(rightHand.getPosition().x);
    float right_cy = metersToPixels(rightHand.getPosition().y);
    paint.setColor(0xff00ff00);
    canvas.drawCircle(right_cx, right_cy, handRadius, paint);
    float ball_cx = metersToPixels(ball.getPosition().x);
    float ball_cy = metersToPixels(ball.getPosition().y);
    paint.setColor(0xff00fff0);
    canvas.drawCircle(ball_cx, ball_cy, ballRadius, paint);
    canvas.save();
    float stick_left = metersToPixels(stick.getPosition().x) - stickWidth / 2;
    float stick_top = metersToPixels(stick.getPosition().y) - stickHeight / 2;
    float stick_right = metersToPixels(stick.getPosition().x) + stickWidth / 2;
    float stick_bottom = metersToPixels(stick.getPosition().y) + stickHeight / 2;
    float stick_radius = stickHeight / 2;
    paint.setColor(0xff0000ff);
    canvas.rotate(radiansToDegrees(stick.getAngle() % 360), metersToPixels(stick.getPosition().x), metersToPixels(stick.getPosition().y));
    canvas.drawRoundRect(stick_left, stick_top, stick_right, stick_bottom, stick_radius, stick_radius, paint);
    canvas.restore();
    postInvalidate();
  }

  public void moveLeftHand(float dy) {
    Vec2 newPosition = leftHand.getPosition();
    newPosition.y += pixelsToMeters(dy);
    leftHand.setTransform(newPosition, leftHand.getAngle());
  }

  public void moveRightHand(float dy) {
    Vec2 newPosition = rightHand.getPosition();
    newPosition.y += pixelsToMeters(dy);
    rightHand.setTransform(newPosition, rightHand.getAngle());
  }

  private static float radiansToDegrees(float radians) {
    return radians / 3.14f * 180f;
  }

  private static float degreesToRadians(float degrees){
    return (degrees / 180f) * 3.14f;
  }

  public static float metersToPixels(float meters) {
    return meters * ratio;
  }

  public static float pixelsToMeters(float pixels) {
    return pixels / ratio;
  }

  public static int dp2px(Context var0, float var1) {
    float var2 = var0.getResources().getDisplayMetrics().density;
    return (int) (var1 * var2 + 0.5F);
  }
}
