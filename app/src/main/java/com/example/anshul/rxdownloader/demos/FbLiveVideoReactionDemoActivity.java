package com.example.anshul.rxdownloader.demos;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.anshul.rxdownloader.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Random;

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
  private final int MINIMUM_DURATION_BETWEEN_EMOTICONS = 200; // in milliseconds
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
  private Animation animation, bounceAnimation;
  @BindView(R.id.emoticons_flowing_container)
  RelativeLayout emoticonsFlowingContainer;
  private int index = 0;
  private int[] sizes = new int[]{24, 32, 48};


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fb_live_video_reaction_demo);
    ButterKnife.bind(this);


    bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.sequential);
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


        ImageView imageView = new ImageView(FbLiveVideoReactionDemoActivity.this);


        String name = "like_" + sizes[index % 3];

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.slide);

        Emoticons emoticons = (Emoticons) ((Timed) o).value();
        switch (emoticons) {
          case LIKE:
            name = "like_" + sizes[index % 3];
            break;
          case LOVE:
            name = "love_" + sizes[index % 3];
            break;
          case HAHA:
            name = "haha_" + sizes[index % 3];
            break;
          case WOW:
            name = "wow_" + sizes[index % 3];
            break;
          case SAD:
            name = "sad_" + sizes[index % 3];
            break;
          case ANGRY:
            name = "angry_" + sizes[index % 3];
            break;
        }

        index++;
        int resource =
            getResources().getIdentifier(name, "drawable", "com.example.anshul.rxdownloader");
        imageView.setImageResource(resource);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        Random random = new Random();
        int margin = random.nextInt(350);
        layoutParams.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(layoutParams);
        emoticonsFlowingContainer.addView(imageView);

        imageView.startAnimation(animation);
        animation.setAnimationListener(new CustomAnimationListener(emoticonsFlowingContainer,
            imageView));


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
