package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.internal.app.ToolbarActionBar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by anshul on 15/1/17.
 */

public class ImagesListAdapter extends RecyclerView.Adapter {

  private final String[] imageNames;
  private final String[] imageDownloadUrls;
  private final TypedArray itemDownloadCovers;

  private int currentCount = 0;
  private DownloadManager downloadManager;
  private static final String TAG = ImagesListAdapter.class.getSimpleName();
  private ItemDownloadPercentObserver mItemDownloadPercentObserver;
  private DownloadRequestsSubscriber mDownloadRequestsSubscriber;

  public ImagesListAdapter(Context context, String[] imageUrls, String[] imageDownloadUrls, TypedArray
      itemDownloadCovers) {
    this.imageNames = imageUrls;
    this.imageDownloadUrls = imageDownloadUrls;
    this.itemDownloadCovers = itemDownloadCovers;
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
      ((ItemDetailsViewHolder) holder).updateImageDetails(imageNames[position],
          imageDownloadUrls[position], itemDownloadCovers.getResourceId(position,-1));
    }
  }

  @Override
  public int getItemCount() {
    if (imageNames != null) {
      return imageNames.length;
    }
    return 0;
  }
}
