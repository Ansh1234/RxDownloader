package com.example.anshul.rxdownloader;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by anshul on 7/2/17.
 */

public class DownloadRequestsSubscriber {

  private Subscription downloadRequestsSubscription;
  private FlowableEmitter downloadsFlowableEmitter;
  private final String TAG = DownloadRequestsSubscriber.class.getSimpleName();
  private ItemDownloadCallback itemDownloadCallback;


  public DownloadRequestsSubscriber(ItemDownloadCallback itemDownloadCallback) {
    this.itemDownloadCallback = itemDownloadCallback;
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

  public Subscription getDownloadRequestsSubscription() {
    return downloadRequestsSubscription;
  }

  public FlowableEmitter getDownloadsFlowableEmitter() {
    return downloadsFlowableEmitter;
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
        if (!(o instanceof DownloadableItem)) {
          return;
        }
        itemDownloadCallback.onDownloadStarted((DownloadableItem)o);
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
