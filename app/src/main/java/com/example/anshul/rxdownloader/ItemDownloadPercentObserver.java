package com.example.anshul.rxdownloader;

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
  private final ItemPercentCallback callback;

  public ItemDownloadPercentObserver(ItemPercentCallback callback) {
    this.callback=callback;
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

  public ObservableEmitter getPercentageObservableEmitter() {
    return percentageObservableEmitter;
  }

  private Observer getObserver() {
    return new Observer() {
      @Override
      public void onSubscribe(Disposable d) {
        downloadPercentDisposable = d;
      }

      @Override
      public void onNext(Object value) {
        if (!(value instanceof DownloadableItem)) {
          return;
        }
        callback.updateDownloadableItem((DownloadableItem) value);
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
