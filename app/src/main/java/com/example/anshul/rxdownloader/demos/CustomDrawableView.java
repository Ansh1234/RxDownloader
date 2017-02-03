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
  private Paint mPaint;
  private Path mAnimPath;
  private Matrix mMatrix;
  private Bitmap mLike48, mLike32, mLike24, mLove48, mLove32, mLove24;
  private ArrayList<LiveEmoticon> mLiveEmoticons = new ArrayList<>();
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
    mLove48 = BitmapFactory.decodeResource(res, R.drawable.love_48);
    mLove32 = BitmapFactory.decodeResource(res, R.drawable.love_32);
    mLove24 = BitmapFactory.decodeResource(res, R.drawable.love_24);
  }

  protected void onDraw(Canvas canvas) {
    canvas.drawPath(mAnimPath, mPaint);
    drawAllLiveEmoticons(canvas);
  }

  private void drawAllLiveEmoticons(Canvas canvas) {
    ListIterator iterator = mLiveEmoticons.listIterator();
    while (iterator.hasNext()) {
      Object object = iterator.next();
      if (!(object instanceof LiveEmoticon)) {
        continue;
      }
      LiveEmoticon liveEmoticon = (LiveEmoticon) object;
      Integer xCordinate = liveEmoticon.getxCordinate() + 10;
      liveEmoticon.setxCordinate(xCordinate);
      if (xCordinate < mScreenWidth) {
        mMatrix.reset();
        mMatrix.postTranslate(xCordinate, 100);
        resizeImageSizeBasedOnXCordinates(canvas, liveEmoticon);
        invalidate();
      } else {
        iterator.remove();
      }
    }
  }

  private void resizeImageSizeBasedOnXCordinates(Canvas canvas, LiveEmoticon liveEmoticon) {
    if (liveEmoticon == null) {
      return;
    }
    int xCordinate = liveEmoticon.getxCordinate();
    Bitmap bitMap48 = null;
    Bitmap bitMap32 = null;
    Bitmap bitMap24 = null;

    Emoticons emoticons = liveEmoticon.getEmoticons();
    if (emoticons == null) {
      return;
    }

    switch (emoticons) {
      case LIKE:
        bitMap48 = mLike48;
        bitMap32 = mLike32;
        bitMap24 = mLike24;
        break;
      case LOVE:
        bitMap48 = mLike48;
        bitMap32 = mLike32;
        bitMap24 = mLike24;
        break;
    }

    if (xCordinate < mScreenWidth / 2) {
      canvas.drawBitmap(bitMap48, mMatrix, null);
    } else if (xCordinate < 3 / 4 * mScreenWidth) {
      canvas.drawBitmap(bitMap32, mMatrix, null);
    } else {
      canvas.drawBitmap(bitMap24, mMatrix, null);
    }
  }

  public void addView(Emoticons emoticons) {
    LiveEmoticon liveEmoticon = new LiveEmoticon(emoticons, 0, 0);
    mLiveEmoticons.add(liveEmoticon);
    invalidate();
  }
}