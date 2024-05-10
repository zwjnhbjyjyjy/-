package controller.thread;

import controller.GameController;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

/**
 *@ClassName: GameMusicPlayer
 *@Description:  该类为音乐播放类
 *@author:Xiongting
 *@CreateDate:2022/12/27-2022/12/29
 */
public class GameMusicPlayer extends Thread{
    private List<String> files;
    private SourceDataLine line;
    @Override
    public void run(){
        startMusic();
    }
    public GameMusicPlayer(){
        files=new ArrayList<String>();
        files.add("music/bgm0.wav");
        files.add("music/bgm1.wav");
        files.add("music/bgm2.wav");
    }

    /**
     * 开启音乐
     */
    public void startMusic(){
        int i=0;
        byte[] buffer=new byte[4096];
        while (true){
            try {
                File file=new File(files.get(i));
                InputStream stream=new FileInputStream(file);
                InputStream bufferedIn=new BufferedInputStream(stream);
                AudioInputStream is= AudioSystem.getAudioInputStream(bufferedIn);
                AudioFormat format=is.getFormat();
                line=AudioSystem.getSourceDataLine(format);
                line.open(format);
                line.start();
                while (is.available()>0){
                    if(!GameController.isPlayingmusic()){
                        int len=is.read(buffer);
                        line.write(buffer,0,len);
                    }
                }
                line.drain();
                line.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            i++;
            i=i%3;
        }
    }

    /**
     * 关闭音乐
     */
    public void overMusic(){
        GameController.setPlayingmusic(true);
    }
    /**
     * 重启音乐
     */
    public void restartMusic(){
        GameController.setPlayingmusic(false);
    }
}
