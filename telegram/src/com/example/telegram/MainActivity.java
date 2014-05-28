package com.example.telegram;

import java.io.FileInputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button mButton;
	private boolean mTouchDown = false;
	public MyAudioTrack maudiotrack = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ControlThread();

		/*
		 * mButton = (Button) findViewById(R.id.buttonpcm);
		 * mButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub // final Runnable audioRecRun = new Runnable() { // public void
		 * run() { MyAudioTrack maudiotrack = new MyAudioTrack(44100,
		 * AudioFormat.CHANNEL_CONFIGURATION_STEREO,
		 * AudioFormat.ENCODING_PCM_16BIT); maudiotrack.init(); short[] data =
		 * new short[44100];
		 * 
		 * byte[] buffer = null; int length = 0; String fileName =
		 * "/sdcard/radio.pcm";
		 * 
		 * // 也可以用String fileName = "mnt/sdcard/Y.txt";
		 * 
		 * String res = "";
		 * 
		 * try {
		 * 
		 * FileInputStream fin = new FileInputStream(fileName);
		 * 
		 * // FileInputStream fin = openFileInput(fileName);
		 * 
		 * // 用这个就不行了，必须用FileInputStream
		 * 
		 * length = fin.available();
		 * 
		 * buffer = new byte[length];
		 * 
		 * fin.read(buffer);
		 * 
		 * fin.close();
		 * 
		 * } catch (Exception e) {
		 * 
		 * e.printStackTrace();
		 * 
		 * } maudiotrack.playAudioTrack(buffer, 0, length); Log.i("maudiotrack",
		 * "playAudioTrack(data, 0, 5120)"); new
		 * AlertDialog.Builder(MainActivity.this) .setMessage("这是第二个提示")
		 * .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		 * public void onClick( DialogInterface dialoginterface, int i) { //
		 * 按钮事件 } }).show();
		 * 
		 * maudiotrack.release(); data = null; // } // }; } });
		 */
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		maudiotrack.release();
		super.onDestroy();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int p = event.findPointerIndex(0);
		float x = event.getX(p);
		float y = event.getY(p);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchDown = true;

			break;
		case MotionEvent.ACTION_UP:
			mTouchDown = false;
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	public void ControlThread() {
		int FFTN = 4096;
		int TGFN = 84;
		short[] buffer = new short[FFTN * 2];
		Complex[] x = new Complex[FFTN];

		for (int i = 0; i < FFTN; i++) {
			x[i] = new Complex(0, 0);
		}

		x[TGFN] = new Complex(10000 * FFTN, 0);

		Complex[] y = FFT.ifft(x);

		for (int i = 0; i < FFTN; i++) {
			buffer[i * 2] = (short) y[i].re();
			buffer[i * 2 + 1] = (short) y[i].re();
		}

		int AudioF = FFTN * 10;

		maudiotrack = new MyAudioTrack(AudioF, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);
		maudiotrack.init();

		new AThread(buffer).start();
	}

	class AThread extends Thread {
		short[] abuffer;

		AThread(short[] buffer) {
			abuffer = buffer;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {

				if (true == mTouchDown) {

					maudiotrack.playAudioTrack(abuffer, 0, abuffer.length);
					Log.i("maudiotrack", "playAudioTrack(data, 0, 5120)");

				} else {

				}

				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

}
