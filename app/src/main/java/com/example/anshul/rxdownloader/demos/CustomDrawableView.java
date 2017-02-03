package com.example.anshul.rxdownloader.demos;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import com.example.anshul.rxdownloader.R;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by anshul on 2/2/17.
 */

public class CustomDrawableView extends View {
  private ShapeDrawable mDrawable;
  private Paint mPaint;
  private Path mAnimPath;
  private Matrix mMatrix;
  private Bitmap mLike48, mLike32, mLike24;
  private ArrayList<Integer> mLikeXCordinates = new ArrayList<>();
  private int mScreenWidth;

  public CustomDrawableView(Context activity) {
    super(activity);

  }

  public CustomDrawableView(Context activity, AttributeSet attrs) {
    super(activity, attrs);
  }

  public CustomDrawableView(Context activity, AttributeSet attrs, int defStyleAttr) {
    super(activity, attrs, defStyleAttr);
  }

  public void initView(Activity activity) {

    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    mScreenWidth = displayMetrics.widthPixels;
    int height = 500;

    mPaint = new Paint();

    mAnimPath = new Path();
    mMatrix = new Matrix();
    Resources res = getResources();
    mLike48 = BitmapFactory.decodeResource(res, R.drawable.like_48);
    mLike32 = BitmapFactory.decodeResource(res, R.drawable.like_32);
    mLike24 = BitmapFactory.decodeResource(res, R.drawable.like_24);
  }

  protected void onDraw(Canvas canvas) {
    canvas.drawPath(mAnimPath, mPaint);
    drawAllCurrentEmoticons(canvas);
  }

  private void drawAllCurrentEmoticons(Canvas canvas) {
    ListIterator iterator = mLikeXCordinates.listIterator();
    while (iterator.hasNext()) {
      Integer xCordinate = (Integer) iterator.next() + 10;
      if (xCordinate < mScreenWidth) {
        iterator.set(xCordinate);
        mMatrix.reset();
        mMatrix.postTranslate(xCordinate, 100);
        resizeImageSizeBasedOnXCordinates(canvas, xCordinate, mLike48, mLike32, mLike24);
        invalidate();
      } else {
        iterator.remove();
      }
    }
  }

  private void resizeImageSizeBasedOnXCordinates(Canvas canvas, int xCordinate, Bitmap bitMap48,
                                                 Bitmap bitMap32, Bitmap bitMap24) {
    if (xCordinate < mScreenWidth / 2) {
      canvas.drawBitmap(bitMap48, mMatrix, null);
    } else if (xCordinate < 3 / 4 * mScreenWidth) {
      canvas.drawBitmap(bitMap32, mMatrix, null);
    } else {
      canvas.drawBitmap(bitMap24, mMatrix, null);
    }
  }

  public void addView(Emoticons emoticons, Sizes sizes) {
    switch (emoticons) {
      case LIKE:
        mLikeXCordinates.add(0);
        invalidate();
        break;
      case LOVE:
        break;
      case HAHA:
        break;
      case WOW:
        break;
      case SAD:
        break;
      case ANGRY:
        break;
    }
  }
}