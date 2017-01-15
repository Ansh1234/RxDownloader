package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by anshul on 15/1/17.
 */

public class ImageDetailsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private TextView imageName;
  private String imageDownloadUrl;
  private ImageView imageDownloadIcon;
  private Context context;
  private final String TAG = "DownloadManager";
  private ProgressBar progressBar;

  public ImageDetailsViewHolder(View itemView) {
    super(itemView);

    if (itemView == null) {
      return;
    }

    context = itemView.getContext();
    imageName = (TextView) itemView.findViewById(R.id.image_name);
    imageDownloadIcon = (ImageView) itemView.findViewById(R.id.icon_image_download);
    imageDownloadIcon.setOnClickListener(this);
    progressBar = (ProgressBar) itemView.findViewById(R.id.image_download_progress_bar);

  }

  public void updateImageDetails(String name, String imageDownloadUrl) {
    imageName.setText(name);
    this.imageDownloadUrl = imageDownloadUrl;
  }

  @Override
  public void onClick(View v) {
    if (context == null) {
      return;
    }
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
        .DOWNLOAD_SERVICE);
    Uri uri = Uri.parse(imageDownloadUrl);
    DownloadManager.Request request = new DownloadManager.Request(uri);
    final long downloadId = downloadManager.enqueue(request);

    ResourceSubscriber subscriber = new ResourceSubscriber<Long>() {
      @Override
      protected void onStart() {
        request(1);
      }

      @Override
      public void onNext(Long t) {
        Log.d(TAG, "received " + t);
        updateDownloadStatus(downloadId, this);
        request(1L);
      }

      @Override
      public void onError(Throwable t) {
        Log.d(TAG, "inside error");
      }

      @Override
      public void onComplete() {
        Log.d(TAG, "inside complete");
      }
    };
    Flowable flowable = Flowable.interval(1, TimeUnit.SECONDS).observeOn(Schedulers.io());
    flowable.subscribe(subscriber);

  }

  private void updateDownloadStatus(long downloadId, ResourceSubscriber subscriber) {
    Log.d("DownloadManager", "updateDownloadStatus");
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
        .DOWNLOAD_SERVICE);
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(downloadId);

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

      int progress = (int) (bytesDownloaded / bytesTotal) / 100;
      Log.d("DownloadManager percent", "" + (bytesDownloaded / bytesTotal) * 100);
      progressBar.setProgress(progress);
      switch (downloadStatus) {
        case DownloadManager.STATUS_FAILED:
          break;

        case DownloadManager.STATUS_SUCCESSFUL:
          subscriber.dispose();

          break;

        case DownloadManager.STATUS_PENDING:
        case DownloadManager.STATUS_RUNNING:
        case DownloadManager.STATUS_PAUSED:
          break;

        case DownloadManager.ERROR_FILE_ERROR:
          break;
      }
    } catch (Exception e) {
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}
