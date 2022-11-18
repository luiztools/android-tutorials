package com.luiztools.taskz;

import com.luiztools.taskz.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class SplashActivity extends Activity implements Runnable {
	
	ImageView imageView1;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        imageView1 = (ImageView)findViewById(R.id.imageView1);
        imageView1.setOnTouchListener(new ImageView.OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				}
				
				return false;
			}
        });
        
        Handler h = new Handler();
        h.postDelayed(this, 5000);
	}

	@Override
	public void run() {
		startActivity(new Intent(SplashActivity.this, MainActivity.class));
		finish();
	}
}
