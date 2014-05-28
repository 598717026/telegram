package com.example.telegram;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class TeleGramMachine {

	public AudioTrack mAudioTrack = null;
	private short[] mPCMData = null;

	int FFTN = 4096;
	int TGFN = 84;
	int AUDIOF = FFTN * 10;

	public TeleGramMachine() {
		CreatePCM();
		new AudioThread().start();
	}

	public void keydown() {
		mAudioTrack.play();
	}

	public void keyup() {
		mAudioTrack.pause();
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

	}

	public void releaseAudio() {
		mAudioTrack.stop();
		mAudioTrack.release();
	}

	public void writepcmAudio() {

		mAudioTrack.write(mPCMData, 0, mPCMData.length);
	}

	private void CreatePCM() {

		mPCMData = new short[FFTN * 2];
		Complex[] x = new Complex[FFTN];

		for (int i = 0; i < FFTN; i++) {
			x[i] = new Complex(0, 0);
		}

		x[TGFN] = new Complex(10000 * FFTN, 0);
		x[TGFN - 2] = new Complex(3000 * FFTN, 0);

		Complex[] y = FFT.ifft(x);

		for (int i = 0; i < FFTN; i++) {
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
				TeleGramMachine.this.writepcmAudio();
			}
		}

	}
}