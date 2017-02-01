package com.example.anshul.rxdownloader.demos;

import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by anshul on 1/2/17.
 */

public class CustomAnimationListener implements Animation.AnimationListener {

  private RelativeLayout parent;
  private ImageView child;

  public CustomAnimationListener(RelativeLayout parent, ImageView child) {
    this.parent = parent;
    this.child = child;
  }

  @Override
  public void onAnimationStart(Animation animation) {
    child.startAnimation(animation);
  }

  @Override
  public void onAnimationEnd(Animation animation) {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        parent.removeView(child);
      }
    }, 1000);
  }

  @Override
  public void onAnimationRepeat(Animation animation) {

  }
}
