package com.example.anshul.rxdownloader;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by anshul on 15/1/17.
 */

public class ImagesListActivity extends AppCompatActivity {

  private ImagesListAdapter imagesListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_list);
    RecyclerView imagesListView = (RecyclerView) findViewById(R.id.images_list);
    String[] imagesDisplayNamesList = getResources().getStringArray(R.array
        .image_display_names_list);
    String[] imageDownloadUrlList = getResources().getStringArray(R.array.image_download_url_list);
    TypedArray imageDownloadCoverList = getResources().obtainTypedArray(R.array
        .image_download_cover_list);
    imagesListAdapter = new ImagesListAdapter(this, imagesDisplayNamesList,
        imageDownloadUrlList,imageDownloadCoverList);
    imagesListView.setLayoutManager(new LinearLayoutManager(this));
    imagesListView.setAdapter(imagesListAdapter);
  }


  @Override
  public void onStop() {
    super.onStop();
    if(isFinishing() && imagesListAdapter!=null){
      imagesListAdapter.performCleanUp();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

  }
}
