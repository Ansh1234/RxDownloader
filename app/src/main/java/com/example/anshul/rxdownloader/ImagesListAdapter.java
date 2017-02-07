package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
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
  private final int MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS = 2;
  private int currentCount = 0;
  private Subscription downloadRequestsSubscription;
  private FlowableEmitter downloadsFlowableEmitter;
  private ObservableEmitter percentageObservableEmitter;
  private Disposable downloadPercentDisposable;
  private DownloadManager downloadManager;
  private static final String TAG = ImagesListAdapter.class.getSimpleName();

  public ImagesListAdapter(Context context, String[] imageUrls, String[] imageDownloadUrls) {
    this.imageNames = imageUrls;
    this.imageDownloadUrls = imageDownloadUrls;
    this.downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    subscribeToDownloadRequests();
    observeIndividaulItemDownloadPercents();
  }

  private void observeIndividaulItemDownloadPercents() {
    ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe() {
      @Override
      public void subscribe(ObservableEmitter e) throws Exception {
        percentageObservableEmitter = e;
      }
    };

    final Observable observable = Observable.create(observableOnSubscribe);

    final Observer subscriber = new Observer() {
      @Override
      public void onSubscribe(Disposable d) {
        downloadPercentDisposable = d;
      }

      @Override
      public void onNext(Object value) {
        if (!(value instanceof DownloadableObject)) {
          return;
        }
        ItemDetailsViewHolder itemDetailsViewHolder = ((DownloadableObject) value)
            .getItemViewHolder();
        Integer downloadPercent = (int) ((DownloadableObject) value).getCurrentDownloadPercent();
        itemDetailsViewHolder.setImageInProgressState(downloadPercent);
        if (downloadPercent == Constants.DOWNLOAD_COMPLETE_PERCENT) {
          currentCount--;
          downloadRequestsSubscription.request(MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS - currentCount);
          itemDetailsViewHolder.setImageToCompletedState();
        }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    };
    observable.subscribeWith(subscriber);
  }

  private void subscribeToDownloadRequests() {

    FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
      @Override
      public void subscribe(FlowableEmitter e) throws Exception {
        downloadsFlowableEmitter = e;
      }
    };

    final Flowable flowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
    final Subscriber subscriber = new Subscriber() {
      @Override
      public void onSubscribe(Subscription s) {
        downloadRequestsSubscription = s;
        downloadRequestsSubscription.request(MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS);
      }

      @Override
      public void onNext(Object o) {
        if (!(o instanceof DownloadableObject)) {
          return;
        }

        currentCount++;
        DownloadableObject downloadableObject = (DownloadableObject) o;
        downloadableObject.getItemViewHolder().setImageInProgressState(0);
        System.out.println("The value of onNext is " + downloadableObject);
        long downloadId = RxDownloadManagerHelper.submitRequestToDownloadManager(downloadManager, (
            downloadableObject.getItemDownloadUrl()));
        if (downloadId == -1) {
          return;
        }
        downloadableObject.setItemDownloadId(downloadId);
        RxDownloadManagerHelper.queryDownloadPercents(downloadManager, downloadableObject,
            percentageObservableEmitter);
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onComplete() {

      }
    };
    flowable.subscribeWith(subscriber);
  }

  public void performCleanUp() {
    Log.d(TAG, "performing clean up of the resources");
    if (!downloadPercentDisposable.isDisposed()) {
      downloadPercentDisposable.dispose();
    }
    if (downloadRequestsSubscription != null) {
      downloadRequestsSubscription.cancel();
    }
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.layout_image_details, parent, false);
    ItemDetailsViewHolder itemDetailsViewHolder =
        new ItemDetailsViewHolder(view, downloadsFlowableEmitter);
    return itemDetailsViewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ItemDetailsViewHolder) {
      ((ItemDetailsViewHolder) holder).updateImageDetails(imageNames[position],
          imageDownloadUrls[position]);
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
