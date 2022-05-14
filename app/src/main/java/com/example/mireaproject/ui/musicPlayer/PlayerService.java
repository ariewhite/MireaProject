package com.example.mireaproject.ui.musicPlayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.example.mireaproject.R;

import java.io.File;
import java.io.FileInputStream;

public class PlayerService extends Service {
    private MediaPlayer mediaPlayer;
    private TextView textView;

    public PlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate(){
        View view = null;

        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        mediaPlayer.setLooping(true);

        textView = view.findViewById(R.id.textView2);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        mediaPlayer.start();
        textView.setText("Moonlight by XXX");
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
    }
}