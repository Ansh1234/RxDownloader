package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;

import io.reactivex.FlowableEmitter;
import io.reactivex.ObservableEmitter;

/**
 * Created by anshul on 6/2/17.
 */

public class RxDownloadManagerHelper {

  public static long downloadImage(DownloadManager downloadManager, String imageDownloadUrl) {
    System.out.println("the downloadUrl is " + imageDownloadUrl);
    Uri uri = Uri.parse(imageDownloadUrl);
    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.setTitle("Image");
    request.setDescription("Image Download");
    return downloadManager.enqueue(request);
  }

  public static void queryDownloadPercents(final DownloadManager downloadManager,
                                           final DownloadableObject downloadableObject,
                                           final ObservableEmitter percentFlowableEmiitter) {
    long downloadId = downloadableObject.getDownloadId();
    System.out.println("getDownloadStatus image url is");
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(downloadId);
    int progress = -1;
    Cursor cursor = null;
    try {
      cursor = downloadManager.query(query);
      if (cursor == null || !cursor.moveToFirst()) {
        return;
      }
      float bytesDownloaded =
          cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
      float bytesTotal =
          cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
      int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
      int downloadStatus = cursor.getInt(columnIndex);

      System.out.println("bytes downloaded " + bytesDownloaded);
      System.out.println("bytes total " + bytesTotal);
      progress = (int) ((bytesDownloaded / bytesTotal) * 100);
      Log.d("DownloadManager percent", progress + "");
      downloadableObject.setDownloadPercent(progress);
      percentFlowableEmiitter.onNext(downloadableObject);
      switch (downloadStatus) {
        case DownloadManager.STATUS_FAILED:
          break;

        case DownloadManager.STATUS_SUCCESSFUL:
          break;

        case DownloadManager.STATUS_PENDING:
        case DownloadManager.STATUS_RUNNING:
        case DownloadManager.STATUS_PAUSED:
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              queryDownloadPercents(downloadManager, downloadableObject, percentFlowableEmiitter);
            }
          }, 100);
          break;

        case DownloadManager.ERROR_FILE_ERROR:
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
      System.out.println("the progress is " + progress);
      return;
    }
  }
}
