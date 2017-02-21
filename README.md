#Downloader Demo using RxJava

## Overview
This project is for downloading items(songs, images etc) in Android using RxJava2. There are, however 2 conditions which I have set for downloading.
* Only 2 items can be downloaded at a time. So even if the user clicks multiple items to download, only 2 of them will be actually downloaded at a time and the rest of the downloads will be en queued.
* The download percent is shown to the user. But only if the difference between the current percentage and the previously shown percentage is greater than 5 percent.

Using RxJava I was able to achieve the following features relatively easily.


## Demo
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ![](rxdownloader_demo.gif)

I wrote a blog about how I used RxJava2 for achieving this. [Here](https://medium.com/@anshuljain/rxjava2-demo2-downloading-songs-in-android-2ebf91ac3a9a#.ksiae7jwc) is the post.
