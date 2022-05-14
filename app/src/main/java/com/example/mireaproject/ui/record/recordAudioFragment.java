package com.example.mireaproject.ui.record;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mireaproject.MainActivity;
import com.example.mireaproject.R;

import java.io.File;
import java.io.IOException;


public class recordAudioFragment extends Fragment {
    private final String TAG = MainActivity.class.getSimpleName();
    private final int REQUEST_CODE_PERMISSION = 100;

    private String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };
    private boolean isWork;

    private Button startRecordButton;
    private Button stopRecordButton;
    private MediaRecorder mediaRecorder;
    private File audioFile;

    private Button startPlayButton;
    private Button stopPlayButton;

    ActivityResultLauncher<String[]> permissionsRequest;

    public recordAudioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_record_audio, container, false);

        startRecordButton =(Button) view.findViewById(R.id.startRecord);
        startRecordButton.setOnClickListener(this::onRecordStartClick);
        stopRecordButton =(Button) view.findViewById(R.id.stopRecord);
        stopRecordButton.setOnClickListener(this::onStopRecordClick);

        stopPlayButton =(Button) view.findViewById(R.id.playAudio);
        startPlayButton.setOnClickListener(this::startPlayClick);
        stopPlayButton =(Button) view.findViewById(R.id.stopAudio);
        stopPlayButton.setOnClickListener(this::stopPlayClick);


        mediaRecorder = new MediaRecorder();


        permissionsRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), isGranted -> {
                    if (isGranted.containsValue(false)){
                        permissionsRequest.launch(PERMISSIONS);
                    } else {
                        isWork = true;
                    }
                });
        isWork = hasPermissions(getContext(), PERMISSIONS);
        if (!isWork) {
            permissionsRequest.launch(PERMISSIONS);
        }
        return view;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onRecordStartClick(View view) {
        try {
            stopRecordButton.setEnabled(false);
            stopRecordButton.setEnabled(true);
            stopRecordButton.requestFocus();
            startRecording();
        } catch (Exception e) {
            Log.e(TAG, "Caught io exception " + e.getMessage());
        }
    }

    private void startRecording() throws IOException {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d(TAG, "sd-card success");
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            if (audioFile == null) {
                audioFile = new File(getContext().getExternalFilesDir(
                        Environment.DIRECTORY_MUSIC), "mirea.3gp");
            }
            mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(getContext(), "Recording started!", Toast.LENGTH_SHORT).show();
        }
    }


    public void onStopRecordClick(View view) {
        startRecordButton.setEnabled(true);
        stopRecordButton.setEnabled(false);
        startRecordButton.requestFocus();
        stopRecording();
        processAudioFile();
        startPlayButton.setEnabled(true);
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            Log.d(TAG, "stopRecording");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            Toast.makeText(getContext(), "You are not recording right now!", Toast.LENGTH_SHORT).show();
        }
    }
    private void processAudioFile() {
        Log.d(TAG, "processAudioFile");
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();

        values.put(MediaStore.Audio.Media.TITLE, "audio" + audioFile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());

        ContentResolver contentResolver = getActivity().getContentResolver();

        Log.d(TAG, "audioFile: " + audioFile.canRead());
        Uri baseUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(baseUri, values);
        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }


    public void startPlayClick(View view){
        MyAudioService.path = audioFile.getAbsolutePath();

        getActivity().startService(
                new Intent(getContext(), MyAudioService.class));
        Log.d("PlayerState", "Playing start");
    }
    public void stopPlayClick(View view){
        getActivity().stopService(
                new Intent(getContext(), MyAudioService.class));
        Log.d("PlayerState", "Playing stop");
    }

}