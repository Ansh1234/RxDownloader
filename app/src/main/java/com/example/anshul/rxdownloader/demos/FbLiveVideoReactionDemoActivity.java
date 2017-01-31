package com.example.anshul.rxdownloader.demos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.anshul.rxdownloader.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import butterknife.BindView;
import butterknife.ButterKnife;
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
  @BindView(R.id.like_emoticon)
  ImageView likeEmoticonButton;
  @BindView(R.id.love_emoticon)
  ImageView loveEmoticonButton;
  @BindView(R.id.haha_emoticon)
  ImageView hahaEmoticonButton;
  @BindView(R.id.wow_emoticon)
  ImageView wowEmoticonButton;
  @BindView(R.id.sad_emoticon)
  ImageView sadEmoticonButton;
  @BindView(R.id.angry_emoticon)
  ImageView angryEmoticonButton;
  @BindView(R.id.like_emoticon_flow)
  ImageView likeEmoticonFlow;
  @BindView(R.id.love_emoticon_flow)
  ImageView loveEmoticonFlow;
  @BindView(R.id.haha_emoticon_flow)
  ImageView hahaEmoticonFlow;
  @BindView(R.id.wow_emoticon_flow)
  ImageView wowEmoticonFlow;
  @BindView(R.id.sad_emoticon_flow)
  ImageView sadEmoticonFlow;
  @BindView(R.id.angry_emoticon_flow)
  ImageView angryEmoticonFlow;
  private Animation animation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fb_live_video_reaction_demo);
    ButterKnife.bind(this);

    animation = AnimationUtils.loadAnimation(getApplicationContext(),
        R.anim.slide);

    FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
      @Override
      public void subscribe(final FlowableEmitter e) throws Exception {
        convertClickEventToStream(e);
      }
    };
    //Give the backpressure strategey as BUFFER, so that the click items do not drop.
    Flowable emoticonsFlowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
    Flowable<Timed> emoticonsTimedFlowable = emoticonsFlowable.timestamp();
    subscriber = getSubscriber();
    emoticonsTimedFlowable.subscribeWith(subscriber);
  }

  private Subscriber getSubscriber() {
    return new Subscriber() {
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
            likeEmoticonFlow.setVisibility(View.VISIBLE);
            likeEmoticonFlow.startAnimation(animation);
            break;
          case LOVE:
            loveEmoticonFlow.setVisibility(View.VISIBLE);
            loveEmoticonFlow.startAnimation(animation);
            break;
          case HAHA:
            hahaEmoticonFlow.setVisibility(View.VISIBLE);
            hahaEmoticonFlow.startAnimation(animation);
            break;
          case WOW:
            wowEmoticonFlow.setVisibility(View.VISIBLE);
            wowEmoticonFlow.startAnimation(animation);
            break;
          case SAD:
            sadEmoticonFlow.setVisibility(View.VISIBLE);
            sadEmoticonFlow.startAnimation(animation);
            break;
          case ANGRY:
            angryEmoticonFlow.setVisibility(View.VISIBLE);
            angryEmoticonFlow.startAnimation(animation);
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
  }

  @Override
  public void onStop() {
    super.onStop();
    if (emoticonSubscription != null) {
      emoticonSubscription.cancel();
    }
  }

  private void convertClickEventToStream(final FlowableEmitter e) {
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
}
