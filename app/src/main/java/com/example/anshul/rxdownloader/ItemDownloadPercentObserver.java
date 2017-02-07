package com.example.anshul.rxdownloader;

import org.reactivestreams.Subscription;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by anshul on 7/2/17.
 */

public class ItemDownloadPercentObserver {

  private ObservableEmitter percentageObservableEmitter;
  private Disposable downloadPercentDisposable;
  private Subscription downloadRequestsSubscription;
  private final ImagesListAdapter imagesListAdapter;

  public ItemDownloadPercentObserver(ImagesListAdapter imagesListAdapter) {
    this.imagesListAdapter = imagesListAdapter;
  }

  public ObservableEmitter getPercentageObservableEmitter() {
    return percentageObservableEmitter;
  }

  public void setDownloadRequestsSubscription(
      Subscription downloadRequestsSubscription) {
    this.downloadRequestsSubscription = downloadRequestsSubscription;
  }

  public void init() {
    ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe() {
      @Override
      public void subscribe(ObservableEmitter e) throws Exception {
        percentageObservableEmitter = e;
      }
    };

    final Observable observable = Observable.create(observableOnSubscribe);

    final Observer subscriber = getObserver();
    observable.subscribeWith(subscriber);
  }

  private Observer getObserver() {
    return new Observer() {
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
          imagesListAdapter.setCurrentCount(imagesListAdapter.getCurrentCount() - 1);
          downloadRequestsSubscription.request(Constants.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS -
              imagesListAdapter.getCurrentCount());
          itemDetailsViewHolder.setImageToCompletedState();
        }
      }

      @Override
      public void onError(Throwable e) {
        if (downloadPercentDisposable != null) {
          downloadPercentDisposable.dispose();
        }
      }

      @Override
      public void onComplete() {
        if (downloadPercentDisposable != null) {
          downloadPercentDisposable.dispose();
        }
      }
    };
  }

  public void performCleanUp() {
    if (downloadPercentDisposable != null) {
      downloadPercentDisposable.dispose();
    }
  }
}
