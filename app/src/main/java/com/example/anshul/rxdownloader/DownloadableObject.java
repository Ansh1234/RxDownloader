package com.example.anshul.rxdownloader;

/**
 * Created by anshul on 6/2/17.
 */

public class DownloadableObject {

  private ImageDetailsViewHolder imageDetailsViewHolder;
  private long downloadId;
  private String downloadImageUrl;
  private long downloadPercent;

  public ImageDetailsViewHolder getImageDetailsViewHolder() {
    return imageDetailsViewHolder;
  }

  public void setImageDetailsViewHolder(
      ImageDetailsViewHolder imageDetailsViewHolder) {
    this.imageDetailsViewHolder = imageDetailsViewHolder;
  }

  public long getDownloadId() {
    return downloadId;
  }

  public void setDownloadId(long downloadId) {
    this.downloadId = downloadId;
  }

  public String getDownloadImageUrl() {
    return downloadImageUrl;
  }

  public void setDownloadImageUrl(String downloadImageUrl) {
    this.downloadImageUrl = downloadImageUrl;
  }

  public long getDownloadPercent() {
    return downloadPercent;
  }

  public void setDownloadPercent(long downloadPercent) {
    this.downloadPercent = downloadPercent;
  }
}
