package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by anshul on 15/1/17.
 */

public class ItemListAdapter extends RecyclerView.Adapter implements
    ItemDownloadCallback, ItemPercentCallback {

  private final ArrayList<DownloadableItem> itemsList;

  private int currentDownloadsCount = 0;
  private final DownloadManager downloadManager;
  private static final String TAG = ItemListAdapter.class.getSimpleName();
  private final ItemDownloadPercentObserver mItemDownloadPercentObserver;
  private final DownloadRequestsSubscriber mDownloadRequestsSubscriber;
  private final WeakReference<Context> contextWeakReference;
  private final RecyclerView recyclerView;

  public ItemListAdapter(Context context, ArrayList<DownloadableItem> downloadableItemList,
                         RecyclerView recyclerView) {
    this.itemsList = downloadableItemList;
    this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    this.contextWeakReference = new WeakReference(context);
    this.recyclerView = recyclerView;

    //Observable for percent of individual downloads.
    mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
    //Observable for download request
    mDownloadRequestsSubscriber = new DownloadRequestsSubscriber(this);
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_image_details, parent, false);
    ItemDetailsViewHolder itemDetailsViewHolder =
        new ItemDetailsViewHolder(view, contextWeakReference.get(), this);
    return itemDetailsViewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (!(holder instanceof ItemDetailsViewHolder)) {
      return;
    }
    DownloadableItem downloadableItem = DownloadItemHelper.getItem(contextWeakReference
        .get(), itemsList.get(position));
    ((ItemDetailsViewHolder) holder).updateImageDetails(downloadableItem);
  }

  /**
   * This callback is called when the user clicks on any item for download.
   *
   * @param downloadableItem - Item to be downloaded.
   */
  public void onDownloadEnqueued(DownloadableItem downloadableItem) {
    if (mDownloadRequestsSubscriber.getDownloadsFlowableEmitter() == null) {
      return;
    }
    mDownloadRequestsSubscriber.getDownloadsFlowableEmitter().onNext(downloadableItem);
  }

  @Override
  public int getItemCount() {
    if (itemsList == null) {
      return 0;
    }
    return itemsList.size();
  }

  /**
   * This callback is called when the item starts getting downloaded.
   *
   * @param downloadableItem - Item to be downloaded.
   */
  @Override
  public void onDownloadStarted(DownloadableItem downloadableItem) {
    //Increment the current number of downloads by 1
    currentDownloadsCount++;
    String downloadUrl = downloadableItem.getItemDownloadUrl();
    long downloadId =
        RxDownloadManagerHelper.enqueueDownload(downloadManager, downloadUrl);
    if (downloadId == Constants.INVLALID_ID) {
      return;
    }
    downloadableItem.setDownloadId(downloadId);
    downloadableItem.setDownloadingStatus(DownloadingStatus.IN_PROGRESS);
    updateDownloadableItem(downloadableItem);
    RxDownloadManagerHelper.queryDownloadPercents(downloadManager, downloadableItem,
        mItemDownloadPercentObserver.getPercentageObservableEmitter());
  }

  @Override
  public void onDownloadComplete() {
    //Decrement the current number of downloads by 1
    currentDownloadsCount--;
    mDownloadRequestsSubscriber.getDownloadRequestsSubscription()
        .request(Constants.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS - currentDownloadsCount);
  }


  public void performCleanUp() {
    Log.d(TAG, "performing clean up of the resources");
    mItemDownloadPercentObserver.performCleanUp();
    mDownloadRequestsSubscriber.performCleanUp();
  }

  @Override
  public void updateDownloadableItem(DownloadableItem downloadableItem) {
    if (downloadableItem == null || contextWeakReference.get() == null) {
      return;
    }

    DownloadItemHelper.persistItemState(contextWeakReference.get(), downloadableItem);

    int position = Integer.parseInt(downloadableItem.getId()) - 1;
    ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder)
        recyclerView.findViewHolderForLayoutPosition(position);

    //It means that the viewholder is not currently displayed.
    if (itemDetailsViewHolder == null) {
      if (downloadableItem.getItemDownloadPercent() == Constants.DOWNLOAD_COMPLETE_PERCENT) {
        downloadableItem.setDownloadingStatus(DownloadingStatus.DOWNLOADED);
        DownloadItemHelper.persistItemState(contextWeakReference.get(), downloadableItem);
        onDownloadComplete();
      }
      return;
    }
    itemDetailsViewHolder.updateImageDetails(downloadableItem);
  }
}
