package com.example.anshul.rxdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableEmitter;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by anshul on 15/1/17.
 */

public class ImageDetailsViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private TextView imageName;
  private String imageDownloadUrl;
  private ImageView imageDownloadIcon;
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
    imageName = (TextView) itemView.findViewById(R.id.image_name);
    imageDownloadIcon = (ImageView) itemView.findViewById(R.id.icon_image_download);
    imageDownloadIcon.setOnClickListener(this);
    progressBar = (ProgressBar) itemView.findViewById(R.id.image_download_progress_bar);
    this.flowableEmitter = flowableEmitter;
  }

  public void updateImageDetails(String name, String imageDownloadUrl) {
    imageName.setText(name);
    this.imageDownloadUrl = imageDownloadUrl;
  }

  @Override
  public void onClick(View v) {
    DownloadableObject downloadableObject = new DownloadableObject();
    downloadableObject.setImageDetailsViewHolder(this);
    downloadableObject.setDownloadImageUrl(imageDownloadUrl);
    flowableEmitter.onNext(downloadableObject);
  }
}
