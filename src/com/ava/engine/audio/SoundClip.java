package com.ava.engine.audio;
import com.ava.engine.Input;

import javax.sound.sampled.*;
import java.io.*;
public class SoundClip {
    private Clip clip;
    private FloatControl gainControl;
public SoundClip(String path){
    try {
    InputStream audioSrc = SoundClip.class.getResourceAsStream(path);
    InputStream bufferedIn = new BufferedInputStream(audioSrc);

        AudioInputStream ais = AudioSystem.getAudioInputStream(bufferedIn);
        AudioFormat baseFormat  = ais.getFormat();
        AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                    baseFormat.getSampleRate(),
                16,baseFormat.getChannels(),baseFormat.getChannels()*2, baseFormat.getSampleRate(),false);
        AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat,ais);
        clip = AudioSystem.getClip();
        clip.open(dais);
        gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);


    } catch (UnsupportedAudioFileException | IOException| LineUnavailableException e) {
        e.printStackTrace();
    }

}
public void play(){
    if(clip == null)
        return;
    stop();
    clip.setFramePosition(0);
    while (!clip.isRunning())
        clip.start();
}
public void stop(){
    if(clip.isRunning())
   clip.stop();
}
public void close(){
    stop();
    clip.drain();
    clip.close();
}
public void loop(){
    clip.loop(Clip.LOOP_CONTINUOUSLY);
    play();
}

    public void setVolume(float Value) {
        gainControl.setValue(Value);
    }
    public boolean isRunning(){
    return clip.isRunning();
    }
}
