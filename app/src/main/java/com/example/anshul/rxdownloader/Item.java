package com.example.anshul.rxdownloader;

/**
 * Created by anshul on 14/2/17.
 */

public class Item {

  private String id;
  private String itemTitle;
  private int itemCoverId;
  private DownloadingStatus downloadingStatus;
  private String itemDownloadUrl;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getItemTitle() {
    return itemTitle;
  }

  public void setItemTitle(String itemTitle) {
    this.itemTitle = itemTitle;
  }

  public int getItemCoverId() {
    return itemCoverId;
  }

  public void setItemCoverId(int itemCoverId) {
    this.itemCoverId = itemCoverId;
  }

  public DownloadingStatus getDownloadingStatus() {
    return downloadingStatus;
  }

  public void setDownloadingStatus(DownloadingStatus downloadingStatus) {
    this.downloadingStatus = downloadingStatus;
  }

  public String getItemDownloadUrl() {
    return itemDownloadUrl;
  }

  public void setItemDownloadUrl(String itemDownloadUrl) {
    this.itemDownloadUrl = itemDownloadUrl;
  }
}
