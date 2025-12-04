package com.example.reproductormusica;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageButton;

public class MainActivity extends AppCompatActivity {

    private TextView txtSongTitle;
    private SeekBar seekBar;
    private ListView listView;
    private AppCompatImageButton btnPlayPause, btnPrev, btnNext;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private int currentIndex = 0;
    private boolean isUserSeeking = false;

    // Tus canciones en res/raw/
    private int[] songs = {
            R.raw.canttakemyeyesofyou,
            R.raw.goodluckcharm,
            R.raw.staywithme,
            R.raw.cantstop,
            R.raw.somethingaboutus,
            R.raw.mrroboto
    };

    private String[] songNames = {
            "Can't take my eyes of you",
            "Good Luck Charm",
            "Stay with me",
            "Can't stop",
            "Something about us",
            "Mr. Roboto"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSongTitle = findViewById(R.id.txtSongTitle);
        seekBar = findViewById(R.id.seekBar);
        listView = findViewById(R.id.listViewSongs);

        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, songNames);
        listView.setAdapter(adapter);

        // Evento: al tocar una canción
        listView.setOnItemClickListener((parent, view, position, id) -> {
            currentIndex = position;
            playSong();
        });

        // Play / Pause
        btnPlayPause.setOnClickListener(v -> {
            if (mediaPlayer == null) {
                playSong();
            } else if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play);
            } else {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
            }
        });

        // Siguiente canción
        btnNext.setOnClickListener(v -> {
            currentIndex = (currentIndex + 1) % songs.length;
            playSong();
        });

        // Anterior
        btnPrev.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.getCurrentPosition() > 3000) {
                // Rebobinar si ya avanzó más de 3 segundos
                mediaPlayer.seekTo(0);
            } else {
                currentIndex = (currentIndex - 1 + songs.length) % songs.length;
                playSong();
            }
        });

        // SeekBar: usuario arrastra
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isUserSeeking = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mediaPlayer != null) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }
                isUserSeeking = false;
            }
        });
    }

    // ============================
    //       MÉTODOS PRINCIPALES
    // ============================

    private void playSong() {
        // Detener si había algo sonando
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(this, songs[currentIndex]);
        txtSongTitle.setText(songNames[currentIndex]);
        mediaPlayer.start();
        btnPlayPause.setImageResource(R.drawable.ic_pause);

        // Configurar SeekBar
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setProgress(0);

        // Cuando la canción termine → pasa a la siguiente
        mediaPlayer.setOnCompletionListener(mp -> {
            currentIndex = (currentIndex + 1) % songs.length;
            playSong();
        });

        // Empieza el loop de actualización de SeekBar
        updateSeekBar();
    }

    // Mantiene la barra sincronizada con la música
    private void updateSeekBar() {
        if (mediaPlayer == null) return;

        if (!isUserSeeking) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
        }

        handler.postDelayed(this::updateSeekBar, 200);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
