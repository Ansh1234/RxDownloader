package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.ObservableEmitter;

/**
 * Created by anshul on 7/2/17.
 */

public class DownloadRequestsSubscriber {

  private Subscription downloadRequestsSubscription;
  private FlowableEmitter downloadsFlowableEmitter;
  private final DownloadManager downloadManager;
  private final ImagesListAdapter imagesListAdapter;
  private ObservableEmitter percentageObservableEmitter;
  private final String TAG = DownloadRequestsSubscriber.class.getSimpleName();


  public DownloadRequestsSubscriber(DownloadManager downloadManager,
                                    ImagesListAdapter imagesListAdapter) {
    this.downloadManager = downloadManager;
    this.imagesListAdapter = imagesListAdapter;
  }

  public void setPercentageObservableEmitter(ObservableEmitter percentageObservableEmitter
  ) {
    this.percentageObservableEmitter = percentageObservableEmitter;
  }

  public Subscription getDownloadRequestsSubscription() {
    return downloadRequestsSubscription;
  }

  public FlowableEmitter getDownloadsFlowableEmitter() {
    return downloadsFlowableEmitter;
  }

  public void init() {
    FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
      @Override
      public void subscribe(FlowableEmitter e) throws Exception {
        downloadsFlowableEmitter = e;
      }
    };

    final Flowable flowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
    final Subscriber subscriber = getSubscriber();
    flowable.subscribeWith(subscriber);
  }

  private Subscriber getSubscriber() {
    return new Subscriber() {
      @Override
      public void onSubscribe(Subscription s) {
        downloadRequestsSubscription = s;
        downloadRequestsSubscription.request(Constants.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS);
      }

      @Override
      public void onNext(Object o) {
        if (!(o instanceof DownloadableObject)) {
          return;
        }

        imagesListAdapter.setCurrentCount(imagesListAdapter.getCurrentCount() + 1);
        DownloadableObject downloadableObject = (DownloadableObject) o;
        downloadableObject.getItemViewHolder().setImageInProgressState(0);
        Log.d(TAG, "Received: " + downloadableObject.toString());
        long downloadId = RxDownloadManagerHelper.submitRequestToDownloadManager(downloadManager, (
            downloadableObject.getItemDownloadUrl()));
        if (downloadId == Constants.INVLALID_ID) {
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
  }

  public void performCleanUp() {
    if (downloadRequestsSubscription != null) {
      downloadRequestsSubscription.cancel();
    }
  }
}
