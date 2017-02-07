package com.example.anshul.rxdownloader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
  private final String TAG = "DownloadManager";
  private FlowableEmitter flowableEmitter;

  public ImageDetailsViewHolder(View itemView, FlowableEmitter
      flowableEmitter) {
    super(itemView);

    if (itemView == null) {
      return;
    }

    imageName = (TextView) itemView.findViewById(R.id.item_name);
    imageDownloadIcon = (SongDownloaderIconView) itemView.findViewById(R.id.icon_image_download);
    imageDownloadIcon.init();
    imageDownloadIcon.setOnClickListener(this);
    imageDownloadIcon.bringToFront();
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
    downloadableObject.setItemViewHolder(this);
    downloadableObject.setItemDownloadUrl(imageDownloadUrl);
    flowableEmitter.onNext(downloadableObject);
  }

  public void setImageToWaitingState() {
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.WAITING);
  }

  public void setImageToCompletedState() {
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.DOWNLOADED);
  }

  public void setImageInProgressState(int progress) {
    imageDownloadIcon.updateProgress(progress);
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.IN_PROGRESS);
  }
}
