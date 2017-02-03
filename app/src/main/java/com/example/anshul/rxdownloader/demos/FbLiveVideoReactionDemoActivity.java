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
  private final int MINIMUM_DURATION_BETWEEN_EMOTICONS = 400; // in milliseconds
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
  private Animation bounceAnimation;
  @BindView(R.id.custom_view)
  EmoticonsView emoticonsView;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //Set the view and do all the necessary init.
    setContentView(R.layout.activity_fb_live_video_reaction_demo);
    ButterKnife.bind(this);
    emoticonsView.initView(this);
    bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.sequential);

    //Create an instance of FlowableOnSubscribe which will convert click events to streams
    FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
      @Override
      public void subscribe(final FlowableEmitter e) throws Exception {
        convertClickEventToStream(e);
      }
    };
    //Give the backpressure strategey as BUFFER, so that the click items do not drop.
    Flowable emoticonsFlowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
    //Convert the stream to a timed stream, as we require the timestamp of each event
    Flowable<Timed> emoticonsTimedFlowable = emoticonsFlowable.timestamp();
    subscriber = getSubscriber();
    //Subscribe
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

        Emoticons emoticons = (Emoticons) ((Timed) o).value();
        switch (emoticons) {
          case LIKE:
            emoticonsView.addView(Emoticons.LIKE);
            break;
          case LOVE:
            emoticonsView.addView(Emoticons.LOVE);
            break;
          case HAHA:
            emoticonsView.addView(Emoticons.HAHA);
            break;
          case WOW:
            emoticonsView.addView(Emoticons.WOW);
            break;
          case SAD:
            emoticonsView.addView(Emoticons.SAD);
            break;
          case ANGRY:
            emoticonsView.addView(Emoticons.ANGRY);
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
        //Do nothing
      }

      @Override
      public void onComplete() {
        if (emoticonSubscription != null) {
          emoticonSubscription.cancel();
        }
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
        likeEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.LIKE);
      }
    });

    loveEmoticonButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        loveEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.LOVE);
      }
    });
    hahaEmoticonButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        hahaEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.HAHA);
      }
    });

    wowEmoticonButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        wowEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.WOW);
      }
    });

    sadEmoticonButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sadEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.SAD);
      }
    });

    angryEmoticonButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        angryEmoticonButton.startAnimation(bounceAnimation);
        e.onNext(Emoticons.ANGRY);
      }
    });
  }
}
