package com.example.anshul.rxdownloader;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anshul on 15/1/17.
 */

public class ImagesListAdapter extends RecyclerView.Adapter {

  private final String[] imageNames;
  private final String[] imageDownloadUrls;

  public ImagesListAdapter(String[] imageUrls, String[] imageDownloadUrls) {
    this.imageNames = imageUrls;
    this.imageDownloadUrls = imageDownloadUrls;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext())
            .inflate(R.layout.layout_image_details, parent, false);
    return new ImageDetailsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (holder instanceof ImageDetailsViewHolder) {
      ((ImageDetailsViewHolder) holder).updateImageDetails(imageNames[position],imageDownloadUrls[position]);
    }
  }

  @Override
  public int getItemCount() {
    if (imageNames != null) {
      return imageNames.length;
    }
    return 0;
  }

}
