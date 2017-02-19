package com.example.anshul.rxdownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;

/**
 * Created by anshul on 14/2/17.
 */

public class DownloadItemHelper {

  public static ArrayList getItems(Context context) {
    ArrayList<DownloadableItem> downloadableItems = new ArrayList<>();

    if (context == null) {
      return downloadableItems;
    }

    Resources res = context.getResources();
    String[] imagesId = res.getStringArray(R.array.image_ids);
    String[] imagesDisplayNamesList = res.getStringArray(R.array.image_display_names_list);
    String[] imageDownloadUrlList = res.getStringArray(R.array.image_download_url_list);
    TypedArray imageDownloadCoverList = res.obtainTypedArray(R.array.image_download_cover_list);

    for (int i = 0; i < imagesId.length; i++) {
      DownloadableItem downloadableItem = new DownloadableItem();
      String itemId = imagesId[i];
      downloadableItem.setId(itemId);
      String downloadingStatus = getDownloadStatus(context, itemId);
      downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
      downloadableItem.setItemTitle(imagesDisplayNamesList[i]);
      downloadableItem.setItemCoverId(imageDownloadCoverList.getResourceId(i, 0));
      downloadableItem.setItemDownloadUrl(imageDownloadUrlList[i]);
      downloadableItems.add(downloadableItem);
    }
    return downloadableItems;
  }

  /**
   * This method returns the downloadable Item with the latest percent and downloading status
   * @param context
   * @param downloadableItem
   * @return
   */
  public static DownloadableItem getItem(Context context, DownloadableItem
      downloadableItem) {
    if (context == null || downloadableItem == null) {
      return downloadableItem;
    }
    String downloadingStatus = DownloadItemHelper.getDownloadStatus(context, downloadableItem.getId());
    int downloadPercent = DownloadItemHelper.getDownloadPercent(context, downloadableItem.getId());
    downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
    downloadableItem.setItemDownloadPercent(downloadPercent);
    return downloadableItem;
  }

  public static String getDownloadStatus(Context context, String itemId) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    return preferences.getString(Constants.DOWNLOAD_PREFIX + itemId,
        DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus());
  }

  public static void persistItemState(Context context, DownloadableItem downloadableItem) {
    DownloadItemHelper.setDownloadPercent(context, downloadableItem.getId(),
        downloadableItem.getItemDownloadPercent());
    DownloadItemHelper.setDownloadStatus(context, downloadableItem.getId(),
        downloadableItem.getDownloadingStatus());
  }

  public static void setDownloadStatus(Context context, String itemId, DownloadingStatus
      downloadingStatus) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(Constants.DOWNLOAD_PREFIX + itemId, downloadingStatus.getDownloadStatus());
    editor.commit();
  }

  public static void setDownloadPercent(Context context, String itemId, int percent) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putInt(Constants.PERCENT_PREFIX + itemId, percent);
    editor.commit();
  }

  public static int getDownloadPercent(Context context, String itemId) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    return preferences.getInt(Constants.PERCENT_PREFIX + itemId, 0);
  }
}
