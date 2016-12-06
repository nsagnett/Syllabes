package com.syllabes.res;

import android.media.MediaPlayer;
import android.os.Handler;

public class OnWaitingCompletionListener implements MediaPlayer.OnCompletionListener {

    private Runnable operation;

    public OnWaitingCompletionListener(Runnable operation) {
        this.operation = operation;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        new Handler().postDelayed(operation, 1000);
    }
}
