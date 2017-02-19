package com.example.anshul.rxdownloader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by anshul on 15/1/17.
 */

public class ItemListActivity extends AppCompatActivity {

  private ItemListAdapter itemListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Set up the view.
    setContentView(R.layout.activity_download_item_list);
    RecyclerView itemsListView = (RecyclerView) findViewById(R.id.download_items_list);

    //Get a list of downloadable items.
    ArrayList<DownloadableItem> downloadableItemList = DownloadItemHelper.getItems(this);
    itemListAdapter = new ItemListAdapter(this, downloadableItemList,itemsListView);
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    itemsListView.setLayoutManager(linearLayoutManager);
    itemsListView.setAdapter(itemListAdapter);
  }


  @Override
  public void onStop() {
    super.onStop();
    if (isFinishing() && itemListAdapter != null) {
      itemListAdapter.performCleanUp();
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
