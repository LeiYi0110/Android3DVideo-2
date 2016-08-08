package com.tdtm.lei.my3dvideo3;

import android.media.MediaPlayer;

import com.google.android.exoplayer.ExoPlayer;

/**
 * Created by lei on 6/13/16.
 */
public class MediaPlayerInstance {


    public static  class  SingletonHolder{
        private static final MediaPlayerInstance INSTANCE = new MediaPlayerInstance();

    }

    private MediaPlayerInstance (){

        player =  ExoPlayer.Factory.newInstance(2);
        generalPlayer = new MediaPlayer();
    }

    public ExoPlayer player;
    public MediaPlayer generalPlayer;

    public static final MediaPlayerInstance getInstance(){
        return SingletonHolder.INSTANCE;
    }

}


/*
*  1 public class Singleton {
 2     private static class SingletonHolder {
 3     private static final Singleton INSTANCE = new Singleton();
 4     }
 5     private Singleton (){}
 6     public static final Singleton getInstance() {
 7         return SingletonHolder.INSTANCE;
 8     }
 9 }  */