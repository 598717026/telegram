package com.example.telegram;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

public class MainActivity extends Activity {

	private boolean mTouchDown = false;
	
	TeleGramMachine telegramm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		telegramm = new TeleGramMachine();
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int p = event.findPointerIndex(0);
		float x = event.getX(p);
		float y = event.getY(p);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchDown = true;

			telegramm.keydown();
			break;
		case MotionEvent.ACTION_UP:
			mTouchDown = false;
			
			telegramm.keyup();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}


}
