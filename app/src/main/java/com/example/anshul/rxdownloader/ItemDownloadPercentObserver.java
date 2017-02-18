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
  private final ItemListAdapter itemListAdapter;

  public ItemDownloadPercentObserver(ItemListAdapter itemListAdapter) {
    this.itemListAdapter = itemListAdapter;
  }

  public ObservableEmitter getPercentageObservableEmitter() {
    return percentageObservableEmitter;
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
        if (!(value instanceof DownloadableItem)) {
          return;
        }
        itemListAdapter.setData((DownloadableItem) value);
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
