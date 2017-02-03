package com.example.anshul.rxdownloader.demos;

/**
 * Created by anshul on 3/2/17.
 */

public class LiveEmoticon {

  private Emoticons emoticons;
  private int xCordinate;
  private int yCordinate;

  public LiveEmoticon(Emoticons emoticons, int xCordinate, int yCordinate) {
    this.emoticons = emoticons;
    this.xCordinate = xCordinate;
    this.yCordinate = yCordinate;
  }

  public Emoticons getEmoticons() {
    return emoticons;
  }

  public void setEmoticons(Emoticons emoticons) {
    this.emoticons = emoticons;
  }

  public int getxCordinate() {
    return xCordinate;
  }

  public void setxCordinate(int xCordinate) {
    this.xCordinate = xCordinate;
  }

  public int getyCordinate() {
    return yCordinate;
  }

  public void setyCordinate(int yCordinate) {
    this.yCordinate = yCordinate;
  }
}
