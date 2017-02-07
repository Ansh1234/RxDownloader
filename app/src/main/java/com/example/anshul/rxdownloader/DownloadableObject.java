package com.example.anshul.rxdownloader;

/**
 * Created by anshul on 6/2/17.
 */

public class DownloadableObject {

  private ItemDetailsViewHolder itemViewHolder;
  private long itemDownloadId;
  private String itemDownloadUrl;
  private long currentDownloadPercent;
  private long lastEmittedDownloadPercent = -1;

  public ItemDetailsViewHolder getItemViewHolder() {
    return itemViewHolder;
  }

  public void setItemViewHolder(
      ItemDetailsViewHolder itemViewHolder) {
    this.itemViewHolder = itemViewHolder;
  }

  public long getItemDownloadId() {
    return itemDownloadId;
  }

  public void setItemDownloadId(long itemDownloadId) {
    this.itemDownloadId = itemDownloadId;
  }

  public String getItemDownloadUrl() {
    return itemDownloadUrl;
  }

  public void setItemDownloadUrl(String itemDownloadUrl) {
    this.itemDownloadUrl = itemDownloadUrl;
  }

  public long getCurrentDownloadPercent() {
    return currentDownloadPercent;
  }

  public void setCurrentDownloadPercent(long currentDownloadPercent) {
    this.currentDownloadPercent = currentDownloadPercent;
  }

  public long getLastEmittedDownloadPercent() {
    return lastEmittedDownloadPercent;
  }

  public void setLastEmittedDownloadPercent(long lastEmittedDownloadPercent) {
    this.lastEmittedDownloadPercent = lastEmittedDownloadPercent;
  }
}
