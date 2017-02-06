package com.example.anshul.rxdownloader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import io.reactivex.FlowableEmitter;

/**
 * Created by anshul on 15/1/17.
 */

public class ImageDetailsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private TextView imageName;
  private String imageDownloadUrl;
  private SongDownloaderIconView imageDownloadIcon;
  private Context context;
  private final String TAG = "DownloadManager";
  private ProgressBar progressBar;
  private FlowableEmitter flowableEmitter;

  public ImageDetailsViewHolder(View itemView, FlowableEmitter
      flowableEmitter) {
    super(itemView);

    if (itemView == null) {
      return;
    }

    context = itemView.getContext();
    imageName = (TextView) itemView.findViewById(R.id.music_name);
    imageDownloadIcon = (SongDownloaderIconView) itemView.findViewById(R.id.icon_image_download);
    imageDownloadIcon.setOnClickListener(this);
    imageDownloadIcon.bringToFront();
    progressBar = (ProgressBar) itemView.findViewById(R.id.image_download_progress_bar);
    this.flowableEmitter = flowableEmitter;
  }

  public void updateImageDetails(String name, String imageDownloadUrl) {
    imageName.setText(name);
    this.imageDownloadUrl = imageDownloadUrl;
  }

  @Override
  public void onClick(View v) {
    setImageToWaitingState();
    DownloadableObject downloadableObject = new DownloadableObject();
    downloadableObject.setImageDetailsViewHolder(this);
    downloadableObject.setDownloadImageUrl(imageDownloadUrl);
    flowableEmitter.onNext(downloadableObject);
  }

  public void setImageToWaitingState() {
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.WAITING);
  }

  public void setImageToCompletedState() {
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.DOWNLOADED);
  }

  public void setImageToProgressState() {
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.IN_PROGRESS);
  }


//  public void setImageToProgressState() {
//    GradientDrawable drawable = new GradientDrawable();
//    drawable.setColor(Color.TRANSPARENT);
//    drawable.setShape(GradientDrawable.OVAL);
//    drawable.setStroke(2, Color.parseColor("#ff0000"));
//    drawable.setSize(2 * 10, 2 * 10);
//    imageDownloadIcon.setImageDrawable(drawable);
//
//    ArcShape shape = new ArcShape(90, 90);
//    ShapeDrawable shapeDrawable = new ShapeDrawable(shape);
//    shapeDrawable.getPaint().setColor(Color.parseColor("#ff0000"));
//    imageDownloadIcon.setBackgroundDrawable(shapeDrawable);
//
//  }
}
