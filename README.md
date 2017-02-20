#Downloader Demo using RxJava

## Overview
This is a demo project for downloading songs in Android using **Android Download Manager**. I have used RxJava for this demo project. 
There are two main features of the demo application
* Only 2 songs can be downloaded at a time. So even if the user clicks multiple songs to download at once, only 2 will be 
actually downloaded at a time and the rest of the downloads will be enqueued.
* The download percent is shown on the UI. But it will be only shown when the difference between the current download percent
and the last shown download percent is greater than 5.

Using RxJava I was able to achieve the following features relatively easily.


## Demo
&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ![](rxdownloader_demo.gif)

