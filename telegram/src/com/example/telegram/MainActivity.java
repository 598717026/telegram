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
	public MyAudioTrack maudiotrack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		ControlThread();
		
		/*mButton = (Button) findViewById(R.id.buttonpcm);
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// final Runnable audioRecRun = new Runnable() {
				// public void run() {
				MyAudioTrack maudiotrack = new MyAudioTrack(44100,
						AudioFormat.CHANNEL_CONFIGURATION_STEREO,
						AudioFormat.ENCODING_PCM_16BIT);
				maudiotrack.init();
				short[] data = new short[44100];

				byte[] buffer = null;
				int length = 0;
				String fileName = "/sdcard/radio.pcm";

				// 也可以用String fileName = "mnt/sdcard/Y.txt";

				String res = "";

				try {

					FileInputStream fin = new FileInputStream(fileName);

					// FileInputStream fin = openFileInput(fileName);

					// 用这个就不行了，必须用FileInputStream

					length = fin.available();

					buffer = new byte[length];

					fin.read(buffer);

					fin.close();

				} catch (Exception e) {

					e.printStackTrace();

				}
				maudiotrack.playAudioTrack(buffer, 0, length);
				Log.i("maudiotrack", "playAudioTrack(data, 0, 5120)");
				new AlertDialog.Builder(MainActivity.this)
						.setMessage("这是第二个提示")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									public void onClick(
											DialogInterface dialoginterface,
											int i) {
										// 按钮事件
									}
								}).show();

				maudiotrack.release();
				data = null;
				// }
				// };
			}
		});*/
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int p = event.findPointerIndex(0);
		float x = event.getX(p);
		float y = event.getY(p);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTouchDown = true;
			
			MyAudioTrack maudiotrack = new MyAudioTrack(40960,
					AudioFormat.CHANNEL_CONFIGURATION_STEREO,
					AudioFormat.ENCODING_PCM_16BIT);
			maudiotrack.init();
/*			short[] data = new short[44100];

			byte[] buffer = null;
			int length = 0;
			String fileName = "/sdcard/radio.pcm";

			// 也可以用String fileName = "mnt/sdcard/Y.txt";

			String res = "";

			try {

				FileInputStream fin = new FileInputStream(fileName);

				// FileInputStream fin = openFileInput(fileName);

				// 用这个就不行了，必须用FileInputStream

				length = fin.available();

				buffer = new byte[length];

				fin.read(buffer);

				fin.close();

			} catch (Exception e) {

				e.printStackTrace();

			}*/
			

			short[] buffer = new short[40960 * 2];
			Complex[] x1 = new Complex[4096];

			for (int i  = 0;i < 4096;i++)
			{
				x1[i] = new Complex(0, 0);
			}
			

			x1[84] = new Complex(1000000, 0);

			Complex[] y1 = FFT.ifft(x1);
			
			for (int i  = 0;i < 40960;i++)
			{
				buffer[i * 2] = (short) y1[i % 4096].re();
				buffer[i * 2 + 1] = (short) y1[i % 4096].re();
			}
			
			int length = 40960 * 2;

			
			maudiotrack.playAudioTrack(buffer, 0, length);
			Log.i("maudiotrack", "playAudioTrack(data, 0, 5120)");
			new AlertDialog.Builder(MainActivity.this)
					.setMessage("这是第二个提示")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface,
										int i) {
									// 按钮事件
								}
							}).show();

			maudiotrack.release();
			buffer = null;


			
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
		maudiotrack = new MyAudioTrack(44100,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);
		maudiotrack.init();
		
		short[] buffer = new short[44100 * 2];
		Complex[] x = new Complex[44100];

		for (int i  = 0;i < 44100;i++)
		{
			x[i] = new Complex(0, 0);
		}
		

		x[840] = new Complex(1, 0);

		Complex[] y = FFT.ifft(x);
		
		for (int i  = 0;i < 44100;i++)
		{
			buffer[i * 2] = (short) y[i].re();
			buffer[i * 2 + 1] = (short) y[i].re();
		}

		for (int i = 0;i < 100;i++)
		{
			
			Log.i("maudiotrack", "buff:" + i + "," + buffer[i]);
		}
		
		final Runnable audioRun = new Runnable() {
			public void run() {
				while (true) {

					if (true == mTouchDown) {

						byte[] buffer = null;
						int length = 0;
						maudiotrack.playAudioTrack(buffer , 0, length );
						Log.i("maudiotrack", "playAudioTrack(data, 0, 5120)");

						buffer = null;

					} else {

					}

					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};

		maudiotrack.release();
	}

}
