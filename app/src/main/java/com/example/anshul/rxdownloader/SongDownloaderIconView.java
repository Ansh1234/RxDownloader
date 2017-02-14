package com.example.anshul.rxdownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by anshul on 6/2/17.
 */

public class SongDownloaderIconView extends View {

  private final int CIRCLE_STROKE_WIDTH = 10;
  private final double ANGLE_MULTIPLER = 3.6;
  private final int TOTAL_ANGLE = 360;
  private final int STARTING_ANGLE = -90;
  private final int RECTANGLE_SIZE = 60;
  private int rotateAngle = 0;
  private int progress = 0;
  private Paint mPaint;
  private Bitmap mIconImageNotDownloaded, mIconImageWaiting, mIconImageCompleted;
  private String itemId;

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


  public void init() {
    System.out.println("inside init");
    mPaint = new Paint();
    mIconImageNotDownloaded =
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_not_downloaded);
    mIconImageWaiting = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_waiting);
    mIconImageCompleted =
        BitmapFactory.decodeResource(getResources(), R.drawable.icon_image_completed);
  }

  public void updateDownloadingStatus(DownloadingStatus downloadingStatus) {
    this.downloadingStatus = downloadingStatus;
    invalidate();
  }

  public DownloadingStatus getDownloadingStatus() {
    return downloadingStatus;
  }

  public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
    this.downloadingStatus = downloadingStatus;
  }

  public String getItemId() {
    return itemId;
  }

  public void setItemId(String itemId) {
    this.itemId = itemId;
  }

  public void updateProgress(int progress) {
    this.progress = progress;
  }

  @Override
  public void onDraw(Canvas canvas) {

    Matrix matrix = new Matrix();

    switch (downloadingStatus) {
      case NOT_DOWNLOADED:
        ItemHelper.setDownloadStatus(getContext(),itemId,DownloadingStatus.NOT_DOWNLOADED);
        drawBitmapOnCanvas(canvas, matrix, mIconImageNotDownloaded);
        break;
      case DOWNLOADED:
        drawBitmapOnCanvas(canvas, matrix, mIconImageCompleted);
        ItemHelper.setDownloadStatus(getContext(),itemId,DownloadingStatus.DOWNLOADED);
        break;
      case IN_PROGRESS:
        drawInProgressIconOnCanvas(canvas);
        ItemHelper.setDownloadStatus(getContext(),itemId,DownloadingStatus.IN_PROGRESS);
        break;
      case WAITING:
        ItemHelper.setDownloadStatus(getContext(),itemId,DownloadingStatus.WAITING);
        matrix.postRotate(rotateAngle++);
        if (rotateAngle < TOTAL_ANGLE) {
          invalidate();
        }
        drawBitmapOnCanvas(canvas, matrix, mIconImageWaiting);
        break;
    }
  }

  private void drawInProgressIconOnCanvas(Canvas canvas) {
    //If the download percent is 50%, then the value of the arc should be 50*3.6 which is equal
    // to 180 degree which is half circle.
    double downloadedPercentAngle = progress * ANGLE_MULTIPLER;
    double notDownloadedPercentAngle = TOTAL_ANGLE - downloadedPercentAngle;
    float dX = (canvas.getWidth() - RECTANGLE_SIZE) / 2;
    float dY = (canvas.getHeight() - RECTANGLE_SIZE) / 2;
    RectF rectF = new RectF(dX, dY, RECTANGLE_SIZE + dX, RECTANGLE_SIZE + dY);

    //Draw the downloaded arc.
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(Color.GREEN);
    mPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
    canvas.drawArc(rectF, STARTING_ANGLE, (float) downloadedPercentAngle, false, mPaint);

    //Draw the not-downloaded arc.
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(Color.BLACK);
    mPaint.setStrokeWidth(CIRCLE_STROKE_WIDTH);
    canvas.drawArc(rectF, (float) (downloadedPercentAngle + STARTING_ANGLE),
        (float) notDownloadedPercentAngle, false, mPaint);


    // Draw text within the arc.
    mPaint.setStyle(Paint.Style.FILL);
    mPaint.setColor(Color.BLACK);
    mPaint.setTextSize(getResources().getDimension(R.dimen.image_icon_progress_text_size));
    mPaint.setTextAlign(Paint.Align.CENTER);
    dX = (int) canvas.getWidth() / 2;
    dY = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
    canvas.drawText(Integer.toString(progress), dX, dY, mPaint);
  }

  private void drawBitmapOnCanvas(Canvas canvas, Matrix matrix, Bitmap bitmap) {
    float dX = (canvas.getWidth() - bitmap.getWidth()) / 2;
    float dY = (canvas.getHeight() - bitmap.getHeight()) / 2;
    canvas.drawBitmap(bitmap, dX, dY, null);
  }
}
