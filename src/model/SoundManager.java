package model;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;

public class SoundManager {
	private boolean isLoaded = false, isRinging;
	private Clip clip;
	private FloatControl control;
	public SoundManager(String soundPathname) {
		AudioInputStream audioStream = null;
		File file = new File(soundPathname);
		try {
			audioStream = AudioSystem.getAudioInputStream(file);
			isLoaded = true;
		} catch (UnsupportedAudioFileException e){
			try {
				audioStream = AudioSystem.getAudioInputStream(new File("resources/piano-sound.wav"));
				System.err.println("Error al cargar archivo de audio solicitado. Se usará el sonido por defecto.");
				isLoaded = true;
			} catch (UnsupportedAudioFileException | IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			if (isLoaded) {
				clip = AudioSystem.getClip();
				clip.open(audioStream);
				control = (FloatControl) clip.getControl(Type.MASTER_GAIN);
			}
		} catch (LineUnavailableException | IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startSound() {
		if (isLoaded) {
			isRinging = true;
			AudioFormat format = clip.getFormat();
            int frameRate = (int) format.getFrameRate();
            double 	startSecond = 1.2,
            		endSecond = 4;
            int startFrame = (int) (startSecond * frameRate);
            int endFrame = (int) (endSecond * frameRate);
            clip.setMicrosecondPosition((long)(startSecond*1_000_000));
			clip.setLoopPoints(startFrame, -1);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	
	public void stopSound() {
		if (isLoaded) {
			clip.stop();
			clip.flush();
			isRinging = false;
		}
	}
	
	public boolean isRinging() {
		return isRinging;
	}
	
	/**
	 * Sets the alarm volume within the range of 0 to 100.
	 * If the provided value is below 0, it will be set to 0.
	 * If it exceeds 100, it will be set to 100.
	 * @param volumen The desired alarm volume level.
	 */
	public void setVolume(int volume) {
		volume = volume < 0 ? 0 : volume > 100 ? 100 : volume;
		float volumeRange = control.getMaximum() - control.getMinimum();
		control.setValue(control.getMinimum() + (volume/100.0f)*volumeRange);
	}
}
