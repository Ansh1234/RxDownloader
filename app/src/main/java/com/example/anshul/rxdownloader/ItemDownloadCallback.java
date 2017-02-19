package com.example.anshul.rxdownloader;

/**
 * Created by anshul on 18/2/17.
 */

public interface ItemDownloadCallback {

  void onDownloadEnqueued(DownloadableItem downloadableItem);

  void onDownloadStarted(DownloadableItem downloadableItem);

  void onDownloadComplete();
}
