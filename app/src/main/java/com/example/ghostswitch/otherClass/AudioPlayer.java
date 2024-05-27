package com.example.ghostswitch.otherClass;

import com.example.ghostswitch.R;
import android.content.Context;
import android.media.MediaPlayer;

public class AudioPlayer {

    private static MediaPlayer mediaPlayer;

    public static void playAudioOne(Context context) {
        // Initialize MediaPlayer and play audio file
        mediaPlayer = MediaPlayer.create(context, R.raw.okay_light_on); // Replace your_audio_file with the name of your audio file
        mediaPlayer.start();
    }

    public static void playAudioTwo(Context context) {
        // Initialize MediaPlayer and play audio file
        mediaPlayer = MediaPlayer.create(context, R.raw.okay); // Replace your_audio_file with the name of your audio file
        mediaPlayer.start();
    }

    public static void playAudioError(Context context) {
        // Initialize MediaPlayer and play audio file
        mediaPlayer = MediaPlayer.create(context, R.raw.s_is_wrong); // Replace your_audio_file with the name of your audio file
        mediaPlayer.start();
    }

    public static void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Release MediaPlayer resources
            mediaPlayer = null;
        }
    }
}
