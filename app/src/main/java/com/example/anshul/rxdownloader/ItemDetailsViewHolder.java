package com.example.anshul.rxdownloader;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.reactivex.FlowableEmitter;

/**
 * Created by anshul on 15/1/17.
 */

public class ItemDetailsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private TextView imageName;
  private String imageDownloadUrl;
  private SongDownloaderIconView imageDownloadIcon;
  private FlowableEmitter flowableEmitter;
  private ImageView itemCoverIcon;

  public ItemDetailsViewHolder(View itemView, FlowableEmitter flowableEmitter) {
    super(itemView);

    if (itemView == null) {
      return;
    }

    imageName = (TextView) itemView.findViewById(R.id.item_name);
    imageDownloadIcon = (SongDownloaderIconView) itemView.findViewById(R.id.icon_image_download);
    itemCoverIcon = (ImageView) itemView.findViewById(R.id.icon_item_cover);
    imageDownloadIcon.init();
    imageDownloadIcon.setOnClickListener(this);
    this.flowableEmitter = flowableEmitter;
  }

  public void updateImageDetails(String name, String imageDownloadUrl, int drawableId) {
    imageName.setText(name);
    itemCoverIcon.setImageResource(drawableId);
    this.imageDownloadUrl = imageDownloadUrl;
  }

  @Override
  public void onClick(View v) {
    DownloadingStatus downloadingStatus = imageDownloadIcon.getDownloadingStatus();

    //Only when the icon is in not downloaded state, then do the following.
    if (downloadingStatus == DownloadingStatus.NOT_DOWNLOADED) {
      setImageToWaitingState();
      DownloadableObject downloadableObject = new DownloadableObject();
      downloadableObject.setItemViewHolder(this);
      downloadableObject.setItemDownloadUrl(imageDownloadUrl);
      flowableEmitter.onNext(downloadableObject);
    }
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
