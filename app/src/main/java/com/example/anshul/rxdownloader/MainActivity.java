package com.example.anshul.rxdownloader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anshul.rxdownloader.demos.EmoticonsView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmoticonsView mEmoticonsView = new EmoticonsView(this);

        setContentView(mEmoticonsView);

    }
}
