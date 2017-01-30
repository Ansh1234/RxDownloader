package com.example.anshul.rxdownloader.demos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.anshul.rxdownloader.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Timed;

/**
 * Created by anshul on 29/1/17.
 */

public class FbLiveVideoReactionDemoActivity extends AppCompatActivity {
  private Subscription subscription;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fb_live_video_reaction_demo);

    final Button likeButton = (Button) findViewById(R.id.like_button);
    final Button commentButton = (Button) findViewById(R.id.comment_button);

    //TODO anshul.jain is this the best way for creating a view observable?
    FlowableOnSubscribe flowableOnSubscribe1 = new FlowableOnSubscribe() {
      @Override
      public void subscribe(final FlowableEmitter e) throws Exception {
        likeButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext("Like");
          }
        });
      }
    };

    FlowableOnSubscribe flowableOnSubscribe2 = new FlowableOnSubscribe() {
      @Override
      public void subscribe(final FlowableEmitter e) throws Exception {
        commentButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext("Comment");
          }
        });
      }
    };


    Flowable flowable1 = Flowable.create(flowableOnSubscribe1, BackpressureStrategy.DROP);
    Flowable flowable2 = Flowable.create(flowableOnSubscribe2, BackpressureStrategy.DROP);

    Flowable flowable = Flowable.merge(flowable1, flowable2);
    Flowable<Timed> timedFlowable = flowable.timestamp();
    timedFlowable.subscribeWith(new Subscriber() {
      @Override
      public void onSubscribe(Subscription s) {
        subscription = s;
        subscription.request(1);
      }

      @Override
      public void onNext(final Object o) {
        System.out.println("The object is " + o);
        if (o instanceof Timed) {
          long currentEmittedTimestamp = System.currentTimeMillis();
          if (currentEmittedTimestamp - ((Timed) o).time() > 2000) {
            subscription.request(1);
          } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                subscription.request(1);
              }
            }, 2000 - (currentEmittedTimestamp - ((Timed) o).time()));
          }
        }
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onComplete() {

      }
    });
  }
}
