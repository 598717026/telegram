package com.example.telegram;

import java.util.Calendar;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class TeleGramMachine {

	public AudioTrack mAudioTrack = null;
	private short[] mPCMData = null;
	
	private boolean mPlayflag = false;

	int FFTN = 4096;
	int TGFN = 86;
	int AUDIOF = FFTN * 10;

	public TeleGramMachine() {
		CreatePCM();
		new AudioThread().start();
	}

	public void keydown() {
		mPlayflag = true;
//		playAudio();
	}

	public void keyup() {
		mPlayflag = false;
//		pauseAudio();
	}

	public void initAudio() {
		int minBufSize = AudioTrack.getMinBufferSize(AUDIOF,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT);

		// STREAM_ALARM：警告声
		// STREAM_MUSCI：音乐声，例如music等
		// STREAM_RING：铃声
		// STREAM_SYSTEM：系统声音
		// STREAM_VOCIE_CALL：电话声音
		mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, AUDIOF,
				AudioFormat.CHANNEL_CONFIGURATION_STEREO,
				AudioFormat.ENCODING_PCM_16BIT, minBufSize,
				AudioTrack.MODE_STREAM);

		mAudioTrack.setStereoVolume(1f, 1f);
		playAudio();
	}

	public void releaseAudio() {
		mAudioTrack.stop();
		mAudioTrack.release();
	}

	public void playAudio() {
		
		Log.i("TeleGramMachine", "play:" + Calendar.getInstance().getTimeInMillis());
		mAudioTrack.play();
	}

	public void pauseAudio() {
		Log.i("TeleGramMachine", "pause:" + Calendar.getInstance().getTimeInMillis());
		mAudioTrack.pause();
	}

	public void writepcmAudio() {

		mAudioTrack.write(mPCMData, 0, mPCMData.length);
	}

	private void CreatePCM() {

		mPCMData = new short[FFTN / 2];
		Complex[] x = new Complex[FFTN];

		for (int i = 0; i < FFTN; i++) {
			x[i] = new Complex(0, 0);
		}

		x[TGFN] = new Complex(10000 * FFTN, 0);

		Complex[] y = FFT.ifft(x);

		for (int i = 0; i < mPCMData.length / 2; i++) {
			mPCMData[i * 2] = (short) y[i].re();
			mPCMData[i * 2 + 1] = (short) y[i].re();
		}

	}

	class AudioThread extends Thread {
		short[] mAudiobuffer;

		AudioThread() {
			mAudiobuffer = mPCMData;
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			TeleGramMachine.this.releaseAudio();
			super.destroy();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			TeleGramMachine.this.initAudio();
			while (true) {

				if (mPlayflag == true)
				{
					TeleGramMachine.this.writepcmAudio();
				}
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	class AudioControlThread extends Thread {
		short[] mAudiobuffer;

		AudioControlThread() {
			mAudiobuffer = mPCMData;
		}

		@Override
		public void destroy() {
			// TODO Auto-generated method stub
			TeleGramMachine.this.releaseAudio();
			super.destroy();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

			TeleGramMachine.this.initAudio();
			while (true) {
				if (mPlayflag == true)
				{
					TeleGramMachine.this.writepcmAudio();
				}
				
				try {
					Thread.sleep(9);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	

}