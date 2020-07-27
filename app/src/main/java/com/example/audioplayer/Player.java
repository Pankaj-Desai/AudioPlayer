package com.example.audioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity {

    Button pause,next,prev;
    TextView lable;
    SeekBar seekbar;
    String songname;

    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        next=(Button)findViewById(R.id.but2);
        prev=(Button)findViewById(R.id.but3);
        pause=(Button)findViewById(R.id.but1);

        lable=(TextView)findViewById(R.id.text1);

        seekbar=(SeekBar)findViewById(R.id.seekbar1);

        getSupportActionBar().setTitle("Now Playing");

        updateseekbar = new Thread(){
            @Override
            public void run() {
                int Duration = mediaPlayer.getDuration();
                int currentPosition = 0;
                while( currentPosition<Duration){
                    try{
                        sleep(500);
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekbar.setProgress(currentPosition);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        songname = mySongs.get(position).getName().toString();
        String sname = i.getStringExtra("songname");
        lable.setText(sname);
        lable.setSelected(true);
        position = bundle.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());
        mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
        mediaPlayer.start();
        seekbar.setMax(mediaPlayer.getDuration());
        updateseekbar.start();

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekbar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    pause.setBackgroundResource(R.drawable.play_butt);
                    mediaPlayer.pause();
                }
                else{
                    pause.setBackgroundResource(R.drawable.pause_butt);
                    mediaPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer .create(getApplicationContext(),u);
                songname = mySongs.get(position).getName().toString();
                lable.setText(songname);
                mediaPlayer.start();
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                songname = mySongs.get(position).getName().toString();
                lable.setText(songname);
                mediaPlayer.start();
            }
        });
    }
}