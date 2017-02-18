package com.example.anshul.rxdownloader;

import android.content.Context;
import android.media.browse.MediaBrowser;
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
  private SongDownloaderIconView imageDownloadIcon;
  private ImageView itemCoverIcon;
  private DownloadableItem downloadableItem;
  private Context context;
  private ItemDownloaderCallback callback;
  public ItemDetailsViewHolder(View itemView, Context context,
                               ItemDownloaderCallback callback) {
    super(itemView);

    if (itemView == null) {
      return;
    }

    imageName = (TextView) itemView.findViewById(R.id.item_name);
    imageDownloadIcon = (SongDownloaderIconView) itemView.findViewById(R.id.icon_image_download);
    itemCoverIcon = (ImageView) itemView.findViewById(R.id.icon_item_cover);
    imageDownloadIcon.init();
    imageDownloadIcon.setOnClickListener(this);
    this.context = context;
    this.callback  = callback;
  }

  public void updateImageDetails(DownloadableItem downloadableItem) {
    this.downloadableItem = downloadableItem;
    imageName.setText(downloadableItem.getItemTitle());
    itemCoverIcon.setImageResource(downloadableItem.getItemCoverId());
    imageDownloadIcon.setItemId(downloadableItem.getId());
    imageDownloadIcon.updateDownloadingStatus(downloadableItem.getDownloadingStatus());
    if(downloadableItem.getDownloadingStatus()==DownloadingStatus.IN_PROGRESS){
      setImageInProgressState(downloadableItem.getItemDownloadPercent(),downloadableItem.getId());
    }

  }

  @Override
  public void onClick(View v) {
    DownloadingStatus downloadingStatus = imageDownloadIcon.getDownloadingStatus();
    //Only when the icon is in not downloaded state, then do the following.
    if (downloadingStatus == DownloadingStatus.NOT_DOWNLOADED) {
      setImageToWaitingState(downloadableItem.getId());
      callback.onNewDownload(downloadableItem);
    }
  }

  public void setImageToWaitingState(String itemId) {
    if (!downloadableItem.getId().equalsIgnoreCase(itemId)) {
      return;
    }
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.WAITING);
  }

  public void setImageToCompletedState(String itemId) {
    if (!downloadableItem.getId().equalsIgnoreCase(itemId)) {
      return;
    }
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.DOWNLOADED);
  }

  public void setImageInProgressState(int progress, String itemId) {
    ItemHelper.setDownloadPercent(context, itemId, progress);
    if (!downloadableItem.getId().equalsIgnoreCase(itemId)) {
      return;
    }
    imageDownloadIcon.updateProgress(progress);
    imageDownloadIcon.updateDownloadingStatus(DownloadingStatus.IN_PROGRESS);
  }
}
