package com.example.anshul.rxdownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anshul on 6/2/17.
 */

public class SongDownloaderIconView extends View {

  private final int CIRCLE_STROKE_WIDTH = 10;
  private int rotateAngle = 0;

  private DownloadingStatus downloadingStatus = DownloadingStatus.NOT_DOWNLOADED;
  private Bitmap bitmap;

  public SongDownloaderIconView(Context context) {
    super(context);
  }

  public SongDownloaderIconView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SongDownloaderIconView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public void updateDownloadingStatus(DownloadingStatus downloadingStatus) {
    this.downloadingStatus = downloadingStatus;
    invalidate();
  }

  @Override
  public void onDraw(Canvas canvas) {
    Matrix matrix = new Matrix();
    switch (downloadingStatus) {
      case NOT_DOWNLOADED:
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_download);
        break;
      case WAITING:
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_waiting);
        matrix.postRotate(rotateAngle++);;
        if (rotateAngle < 360) {
          invalidate();
        }
        break;
      case DOWNLOADED:
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_completed);

        break;
      case IN_PROGRESS:
        Paint paint = new Paint();
        RectF rectF = new RectF(0, 0, 100, 100);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        canvas.drawArc(rectF, -90, 90, false, paint);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(rectF, 0, 270, false, paint);
        return;

    }
    canvas.drawBitmap(bitmap,40,40,null);
   // canvas.drawBitmap(bitmap, matrix, null);
  }
}
