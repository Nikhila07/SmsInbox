package com.example.smsinbox;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class Splash extends Activity{
MediaPlayer ourSong; 
	@Override
	protected void onCreate(Bundle Nikhila) {
		// TODO Auto-generated method stub
		super.onCreate(Nikhila);
		setContentView(R.layout.splash);
		 ourSong = MediaPlayer.create(Splash.this,R.raw.samsung);
		 ourSong.start();
		Thread timer =new Thread(){
			public void run(){
				try{
					sleep(2000);

				} catch (InterruptedException e){
					e.printStackTrace();
					
					
				} finally{
				/*	Intent openStartingPoint = new Intent("com.example.smsinbox.Smsinbox");
				    startActivity(openStartingPoint);*/
				    Intent openStartingPoint = new Intent(getApplicationContext(), Smsinbox.class);
					startActivity(openStartingPoint);
					
				}
				
				
			}
			
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}
	
	

}

