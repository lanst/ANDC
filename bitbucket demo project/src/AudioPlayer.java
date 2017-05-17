import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * <p>This class will be responsible for playing<br>
 *    the music and sound effects<br>
 *    of game.<p>
 * @author Norlan Prudente
 *
 */
public class AudioPlayer {
	private static final int _LONG_PERIOD = 1;
	private static final int DELAY = 1000;
	private Clip audio;
	private boolean loop;

	/**
	 * <p>Needs to provide the full path location of the file</p>
	 * @param fileName
	 */
	public AudioPlayer(String fileName){
		//create a file
		File sound = new File(fileName);

		try {
			//turn it into a clip
			audio = AudioSystem.getClip();
			audio.open(AudioSystem.getAudioInputStream(sound));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * <p>Stop what is currently playing(if the same clip)<br>
	 *    and play the sound</p>
	 */
	public void Play() {
		//if there was no clip to play
		if (audio == null)
			return;

		//starts playing the music
		Stop();
		audio.setFramePosition(0);
		audio.start();
	}

	/**
	 * <p>Stop the sound effect/Music</p>
	 */
	public void Stop() {
		//check if it is running
		if (audio.isRunning())
			audio.stop();
	}

	/**
	 * <p>close the sound</p>
	 */
	public void Close() {
		Stop();
		audio.close();
	}

	/**
	 * <p>Check if music is running</p>
	 * @return
	 */
	public boolean isRunning(){
		return audio.isRunning();
	}
	
	/**
	 * make the music loop
	 */
	public void LoopMusic(){
		Timer timer = new Timer();
		TimerTask task = new TimerTask(){
			public void run(){
				if (!isRunning())
					Play();
				
				timer.cancel();
				LoopMusic();
			}
		};
		
		if (loop)
			timer.scheduleAtFixedRate(task, DELAY, _LONG_PERIOD);
		else
		{
			//to stop everything on looping the music
			timer.cancel();
			task.cancel();
			Stop();
		}
	}
	
	/**
	 * <p>loop music</p>
	 */
	public void startLooping(){
		loop = true;
	}
	
	/**
	 * <p>stop the loop</p>
	 */
	public void stopLooping(){
		loop = false;
	}
}
