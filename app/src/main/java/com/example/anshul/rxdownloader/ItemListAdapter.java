package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anshul on 15/1/17.
 */

public class ItemListAdapter extends RecyclerView.Adapter {

  private final ArrayList<Item> itemsList;

  private int currentCount = 0;
  private DownloadManager downloadManager;
  private static final String TAG = ItemListAdapter.class.getSimpleName();
  private ItemDownloadPercentObserver mItemDownloadPercentObserver;
  private DownloadRequestsSubscriber mDownloadRequestsSubscriber;
  private WeakReference<Context> contextWeakReference;

  public ItemListAdapter(Context context, ArrayList<Item> itemList) {
    this.itemsList = itemList;
    this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
    mItemDownloadPercentObserver.init();
    mDownloadRequestsSubscriber = new DownloadRequestsSubscriber
        (downloadManager, this);

    mDownloadRequestsSubscriber.init();
    mDownloadRequestsSubscriber.setPercentageObservableEmitter(
        mItemDownloadPercentObserver.getPercentageObservableEmitter());
    mItemDownloadPercentObserver.setDownloadRequestsSubscription(
        mDownloadRequestsSubscriber.getDownloadRequestsSubscription());
    contextWeakReference = new WeakReference<Context>(context);
  }

  public int getCurrentCount() {
    return currentCount;
  }

  public void setCurrentCount(int currentCount) {
    this.currentCount = currentCount;
  }

  public void performCleanUp() {
    Log.d(TAG, "performing clean up of the resources");
    mItemDownloadPercentObserver.performCleanUp();
    mDownloadRequestsSubscriber.performCleanUp();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_image_details, parent, false);
    ItemDetailsViewHolder itemDetailsViewHolder =
        new ItemDetailsViewHolder(view, mDownloadRequestsSubscriber.getDownloadsFlowableEmitter());
    return itemDetailsViewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemDetailsViewHolder) {
      Item item = itemsList.get(position);
      String downloadingStatus = ItemHelper.getDownloadStatus(contextWeakReference.get(),
          item.getId());
      item.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
      ((ItemDetailsViewHolder) holder).updateImageDetails(item);
    }
  }

  @Override
  public int getItemCount() {
    if (itemsList == null) {
      return 0;
    }
    return itemsList.size();
  }
}
