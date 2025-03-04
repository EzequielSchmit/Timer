package vista;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.*;

public class SoundManager {
	private boolean isLoaded = false, isRinging;
	private Clip clip;
	public SoundManager(String soundPathname) {
		
		try {
			File file = new File(soundPathname);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			isLoaded = true;
			clip = AudioSystem.getClip();
			clip.open(audioStream);
		
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		
	}
	
	public void startSound() {
		if (isLoaded) {// && clip != null) {
			isRinging = true;
			AudioFormat format = clip.getFormat();
            int frameRate = (int) format.getFrameRate();
            double 	startSecond = 1.2,
            		endSecond = 4;
            int startFrame = (int) (startSecond * frameRate);
            int endFrame = (int) (endSecond * frameRate);
            clip.setMicrosecondPosition((long)(startSecond*1_000_000));
			clip.setLoopPoints(startFrame, -1); //aca podria usar endFrame en vez de -1 si quisiera hacer que el loop termine antes que el final del archivo
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
}
