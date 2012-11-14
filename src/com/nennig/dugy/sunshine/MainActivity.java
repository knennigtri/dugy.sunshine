package com.nennig.dugy.sunshine;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static final String TAG = "dugy.main";
	private Bitmap bitmapImage; 
	String[] audioAssets;
	private MediaPlayer player;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        player = new MediaPlayer();
        
        try {
			audioAssets = AssetManagement.getShuffledAssetPhotos(this);
			for(String name:audioAssets){
			     Log.d(TAG,"Asset: " +name);    
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Log.d(TAG, "Num of Assets = "+audioAssets.length);
        InputStream iStream = null;
		try {
			ImageView photoView = (ImageView) findViewById(R.id.main_imageView);
			iStream = MainActivity.this.getAssets().open("sunshine.png");
			
			bitmapImage = AssetManagement.drawNextPhoto(iStream, 500, 500);
			
			photoView.setImageBitmap(bitmapImage);
			photoView.setOnTouchListener(new OnTouchListener() {
	 			@Override
	 			public boolean onTouch(View arg0, MotionEvent arg1) {
	 				
					playNextAudioClip();
	 				return false;
	 			}
	         });
		} catch (IOException e) {
			Log.d(TAG, "Exception Was Thrown.");
			Log.d(TAG, e.toString());
		}
        
    }

    private void playNextAudioClip(){
    	player.stop();
    	AssetFileDescriptor afd;
    	Random rand = new Random();
    	int randNum = rand.nextInt(audioAssets.length);
    	Log.d(TAG,"Asset: "+audioAssets[randNum]);
    	try {
        	afd = getAssets().openFd(audioAssets[randNum]);
        	player = new MediaPlayer();
			//player.setDataSource(afd.getFileDescriptor());
            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
            player.prepare();
			
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void onPause() {
        super.onPause();
        player.stop();
    }
    public void onStop() {
        super.onStop();
    	player.stop();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
