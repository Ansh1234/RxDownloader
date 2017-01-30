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

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

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

    Observable.interval(1000, TimeUnit.SECONDS).map(new Function<Long, Object>() {
      @Override
      public Object apply(Long aLong) throws Exception {
        getDownloadStatus(downloadId);
        return null;
      }
    });
    return;
  }

  private int getDownloadStatus(long downloadId) {
    Log.d("DownloadManager", "getDownloadStatus");
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
        .DOWNLOAD_SERVICE);
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(downloadId);
    int progress = -1;
    Cursor cursor = null;
    try {
      cursor = downloadManager.query(query);
      if (cursor == null || !cursor.moveToFirst()) {
        return progress;
      }
      float bytesDownloaded =
          cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
      float bytesTotal =
          cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
      int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
      int downloadStatus = cursor.getInt(columnIndex);

      progress = (int) (bytesDownloaded / bytesTotal) / 100;
      Log.d("DownloadManager percent", "" + (bytesDownloaded / bytesTotal) * 100);
      progressBar.setProgress(progress);
      switch (downloadStatus) {
        case DownloadManager.STATUS_FAILED:
          break;

        case DownloadManager.STATUS_SUCCESSFUL:
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
      return progress;
    }
  }
}
