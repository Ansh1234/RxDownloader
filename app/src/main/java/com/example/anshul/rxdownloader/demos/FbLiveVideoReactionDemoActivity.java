package com.example.anshul.rxdownloader.demos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

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
  private Subscription emoticonSubscription;
  private Subscriber subscriber;
  private final int MINIMUM_DURATION_BETWEEN_EMOTICONS = 2000; // in milliseconds

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fb_live_video_reaction_demo);

    final ImageButton likeEmoticonButton = (ImageButton) findViewById(R.id.like_emoticon);
    final ImageButton loveEmoticonButton = (ImageButton) findViewById(R.id.love_emoticon);
    final ImageButton hahaEmoticonButton = (ImageButton) findViewById(R.id.haha_emoticon);
    final ImageButton wowEmoticonButton = (ImageButton) findViewById(R.id.wow_emoticon);
    final ImageButton sadEmoticonButton = (ImageButton) findViewById(R.id.sad_emoticon);
    final ImageButton angryEmoticonButton = (ImageButton) findViewById(R.id.angry_emoticon);
    final ImageView likeEmoticonFlow = (ImageView) findViewById(R.id.like_emoticon_flow);
    final ImageView loveEmoticonFlow = (ImageView) findViewById(R.id.love_emoticon_flow);
    final ImageView hahaEmoticonFlow = (ImageView) findViewById(R.id.haha_emoticon_flow);
    final ImageView wowEmoticonFlow = (ImageView) findViewById(R.id.wow_emoticon_flow);
    final ImageView sadEmoticonFlow = (ImageView) findViewById(R.id.sad_emoticon_flow);
    final ImageView angryEmoticonFlow = (ImageView) findViewById(R.id.angry_emoticon_flow);
    final Animation animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
        R.anim.slide);


    //TODO anshul.jain is this the best way for creating a view observable?
    FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
      @Override
      public void subscribe(final FlowableEmitter e) throws Exception {
        likeEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.LIKE);
          }
        });

        loveEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.LOVE);
          }
        });
        hahaEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.HAHA);
          }
        });

        wowEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.WOW);
          }
        });
        sadEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.SAD);
          }
        });

        angryEmoticonButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            e.onNext(Emoticons.ANGRY);
          }
        });
      }
    };


    Flowable emoticonsFlowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
    Flowable<Timed> emoticonsTimedFlowable = emoticonsFlowable.timestamp();
    subscriber = new Subscriber() {
      @Override
      public void onSubscribe(Subscription s) {
        emoticonSubscription = s;
        emoticonSubscription.request(1);
      }

      @Override
      public void onNext(final Object o) {
        if (o == null || !(o instanceof Timed)) {
          return;
        }
        System.out.println("The object is " + o.toString());

        Emoticons emoticons = (Emoticons) ((Timed) o).value();
        switch (emoticons) {
          case LIKE:
            System.out.println("inside like");
            likeEmoticonFlow.startAnimation(animSlide);
            break;
          case LOVE:
            System.out.println("inside love");
            loveEmoticonFlow.startAnimation(animSlide);
            break;
          case HAHA:
            hahaEmoticonFlow.startAnimation(animSlide);
            break;
          case WOW:
            wowEmoticonFlow.startAnimation(animSlide);
            break;
          case SAD:
            sadEmoticonFlow.startAnimation(animSlide);
            break;
          case ANGRY:
            angryEmoticonFlow.startAnimation(animSlide);
            break;
        }

        if (o instanceof Timed) {
          long currentTimeStamp = System.currentTimeMillis();
          long diffInMillis = currentTimeStamp - ((Timed) o).time();
          if (diffInMillis > MINIMUM_DURATION_BETWEEN_EMOTICONS) {
            emoticonSubscription.request(1);
          } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                emoticonSubscription.request(1);
              }
            }, MINIMUM_DURATION_BETWEEN_EMOTICONS - diffInMillis);
          }
        }
      }

      @Override
      public void onError(Throwable t) {

      }

      @Override
      public void onComplete() {

      }
    };
    emoticonsTimedFlowable.subscribeWith(subscriber);
  }
}
