package com.example.anshul.rxdownloader;

/**
 * Created by anshul on 6/2/17.
 */

public enum DownloadingStatus {
  //The item has not been started for downloading.
  NOT_DOWNLOADED,
  //The item has been started for downloading, but due to other downloads, it is in waiting.
  WAITING,
  //The item is downloading.
  IN_PROGRESS,
  //The item has been downloaded.
  DOWNLOADED;
}
