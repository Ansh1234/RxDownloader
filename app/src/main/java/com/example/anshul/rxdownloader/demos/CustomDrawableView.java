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
import java.util.Random;

/**
 * Created by anshul on 2/2/17.
 */

public class CustomDrawableView extends View {
  private Paint mPaint;
  private Path mAnimPath;
  private Matrix mMatrix;
  private Bitmap mLike48, mLike32, mLike24, mLove48, mLove32, mLove24, mHaha48, mHaha32, mHaha24,
      mWow48, mWow32, mWow24, mSad48, mSad32, mSad24, mAngry48, mAngry32, mAngry24;
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
    mPaint = new Paint();

    mAnimPath = new Path();
    mMatrix = new Matrix();
    Resources res = getResources();
    //Like emoticons
    mLike48 = BitmapFactory.decodeResource(res, R.drawable.like_48);
    mLike32 = BitmapFactory.decodeResource(res, R.drawable.like_32);
    mLike24 = BitmapFactory.decodeResource(res, R.drawable.like_24);
    //Love emoticons
    mLove48 = BitmapFactory.decodeResource(res, R.drawable.love_48);
    mLove32 = BitmapFactory.decodeResource(res, R.drawable.love_32);
    mLove24 = BitmapFactory.decodeResource(res, R.drawable.love_24);
    //Haha emoticonss
    mHaha48 = BitmapFactory.decodeResource(res, R.drawable.haha_48);
    mHaha32 = BitmapFactory.decodeResource(res, R.drawable.haha_32);
    mHaha24 = BitmapFactory.decodeResource(res, R.drawable.haha_24);
    //Wow emoticons
    mWow48 = BitmapFactory.decodeResource(res, R.drawable.wow_48);
    mWow32 = BitmapFactory.decodeResource(res, R.drawable.wow_32);
    mWow24 = BitmapFactory.decodeResource(res, R.drawable.wow_24);
    //Sad emoticons
    mSad48 = BitmapFactory.decodeResource(res, R.drawable.sad_48);
    mSad32 = BitmapFactory.decodeResource(res, R.drawable.sad_32);
    mSad24 = BitmapFactory.decodeResource(res, R.drawable.sad_24);
    //Angry emoticons
    mAngry48 = BitmapFactory.decodeResource(res, R.drawable.angry_48);
    mAngry32 = BitmapFactory.decodeResource(res, R.drawable.angry_32);
    mAngry24 = BitmapFactory.decodeResource(res, R.drawable.angry_24);
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
        bitMap48 = mLove48;
        bitMap32 = mLove32;
        bitMap24 = mLove24;
        break;
      case HAHA:
        bitMap48 = mHaha48;
        bitMap32 = mHaha32;
        bitMap24 = mHaha24;
        break;
      case WOW:
        bitMap48 = mWow48;
        bitMap32 = mWow32;
        bitMap24 = mWow24;
        break;
      case SAD:
        bitMap48 = mSad48;
        bitMap32 = mSad32;
        bitMap24 = mSad24;
        break;
      case ANGRY:
        bitMap48 = mAngry48;
        bitMap32 = mAngry32;
        bitMap24 = mAngry24;
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
    int startXCordinate = 0;
    int startYCordinate = new Random().nextInt(300) + 100;
    LiveEmoticon liveEmoticon = new LiveEmoticon(emoticons, startXCordinate, startYCordinate);
    mLiveEmoticons.add(liveEmoticon);
    invalidate();
  }
}