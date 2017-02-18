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

public class ItemListAdapter extends RecyclerView.Adapter implements ItemDownloaderCallback {

  private final ArrayList<DownloadableItem> itemsList;

  private int currentCount = 0;
  private final DownloadManager downloadManager;
  private static final String TAG = ItemListAdapter.class.getSimpleName();
  private ItemDownloadPercentObserver mItemDownloadPercentObserver;
  private DownloadRequestsSubscriber mDownloadRequestsSubscriber;
  private WeakReference<Context> contextWeakReference;
  private RecyclerView recyclerView;

  public ItemListAdapter(Context context, ArrayList<DownloadableItem> downloadableItemList,
                         RecyclerView recyclerView) {
    this.itemsList = downloadableItemList;
    this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    //Observable for percent of individual downloads.
    mItemDownloadPercentObserver = new ItemDownloadPercentObserver(this);
    mItemDownloadPercentObserver.init();

    //Observable for download request
    mDownloadRequestsSubscriber = new DownloadRequestsSubscriber
        (downloadManager, this);
    mDownloadRequestsSubscriber.init();
    mDownloadRequestsSubscriber.setPercentageObservableEmitter(
        mItemDownloadPercentObserver.getPercentageObservableEmitter());
    contextWeakReference = new WeakReference(context);
    this.recyclerView = recyclerView;
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
        new ItemDetailsViewHolder(view, contextWeakReference.get(), this);
    return itemDetailsViewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemDetailsViewHolder) {
      System.out.println("onBin");
      DownloadableItem downloadableItem = itemsList.get(position);
      String downloadingStatus = ItemHelper.getDownloadStatus(contextWeakReference.get(),
          downloadableItem.getId());
      int downloadPercent =
          ItemHelper.getDownloadPercent(contextWeakReference.get(), downloadableItem
              .getId());
      if (downloadableItem.getId().equalsIgnoreCase("1")) {
        System.out.println("receiving");
        System.out.println("percent " + downloadableItem.getItemDownloadPercent());
        System.out.println("status" + downloadableItem.getDownloadingStatus());
      }
      downloadableItem.setDownloadingStatus(DownloadingStatus.getValue(downloadingStatus));
      downloadableItem.setItemDownloadPercent(downloadPercent);
      ((ItemDetailsViewHolder) holder).updateImageDetails(downloadableItem);
      if (downloadPercent == Constants.DOWNLOAD_COMPLETE_PERCENT) {
        setCurrentCount(getCurrentCount() - 1);
        mDownloadRequestsSubscriber.getDownloadRequestsSubscription()
            .request(Constants.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
                getCurrentCount());
        ((ItemDetailsViewHolder) holder).setImageToCompletedState(downloadableItem.getId());
      }
    }
  }

  public void setData(DownloadableItem downloadableItem) {
    if (downloadableItem == null) {
      return;
    }
    ItemHelper.setDownloadPercent(contextWeakReference.get(), downloadableItem.getId(),
        downloadableItem
            .getItemDownloadPercent());
    ItemHelper.setDownloadStatus(contextWeakReference.get(), downloadableItem.getId(),
        downloadableItem.getDownloadingStatus());

    int position = Integer.parseInt(downloadableItem.getId()) - 1;
    ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder)
        recyclerView.findViewHolderForLayoutPosition(position);
    if (itemDetailsViewHolder == null) {
      return;
    }

    Integer downloadPercent = (int) (downloadableItem.getItemDownloadPercent());
    itemDetailsViewHolder.setImageInProgressState(downloadPercent, downloadableItem.getId());
    if (downloadPercent == Constants.DOWNLOAD_COMPLETE_PERCENT) {
      setCurrentCount(getCurrentCount() - 1);
      mDownloadRequestsSubscriber.getDownloadRequestsSubscription()
          .request(Constants.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
              getCurrentCount());
      itemDetailsViewHolder.setImageToCompletedState(downloadableItem.getId());
    }
  }

  public void onNewDownload(DownloadableItem downloadableItem) {
    mDownloadRequestsSubscriber.getDownloadsFlowableEmitter().onNext(downloadableItem);
  }

  @Override
  public int getItemCount() {
    if (itemsList == null) {
      return 0;
    }
    return itemsList.size();
  }
}
