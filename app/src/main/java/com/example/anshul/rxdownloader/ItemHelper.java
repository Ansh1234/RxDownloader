package com.example.anshul.rxdownloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshul on 14/2/17.
 */

public class ItemHelper {

  public static ArrayList getItems(Context context) {
    ArrayList<Item> items = new ArrayList<>();

    if (context == null) {
      return items;
    }

    Resources res = context.getResources();
    String[] imagesId = res.getStringArray(R.array.image_ids);
    String[] imagesDisplayNamesList = res.getStringArray(R.array.image_display_names_list);
    String[] imageDownloadUrlList = res.getStringArray(R.array.image_download_url_list);
    TypedArray imageDownloadCoverList = res.obtainTypedArray(R.array.image_download_cover_list);

    for (int i = 0; i < imagesId.length; i++) {
      Item item = new Item();
      String itemId = imagesId[i];
      item.setId(itemId);
      String downloadingStatus = getDownloadStatus(context, itemId);
      item.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
      item.setItemTitle(imagesDisplayNamesList[i]);
      item.setItemCoverId(imageDownloadCoverList.getResourceId(i, 0));
      item.setItemDownloadUrl(imageDownloadUrlList[i]);
      items.add(item);
    }
    return items;
  }

  public static String getDownloadStatus(Context context, String itemId) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    return preferences.getString(itemId, DownloadingStatus.NOT_DOWNLOADED.getDownloadStatus());
  }

  public static void setDownloadStatus(Context context, String itemId, DownloadingStatus
      downloadingStatus) {
    SharedPreferences preferences =
        context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context
            .MODE_PRIVATE);
    SharedPreferences.Editor editor = preferences.edit();
    editor.putString(itemId, downloadingStatus.getDownloadStatus());
    editor.commit();
  }
}
