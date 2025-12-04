package com.example.proyecto24_reproduccionpausacontinuacionaudio;

import android.os.Bundle;
import android.provider.MediaStore;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//IMPORTS DEL EJERCICIO
import android.view.View;
import android.widget.Button;
import android.media.MediaPlayer;


//CLASE PRINCIPAL
public class MainActivity extends AppCompatActivity {

    //variables de clase
    MediaPlayer mp;
    Button b5;
    int posicion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //TRASPASO DE VARIABLES
        b5 = (Button) findViewById(R.id.button5);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //funcion para destruir el audio
    public void destruir(){
        //si el media player no es nulo
        if (mp != null)
            //libera el media player
            mp.release();
    }

    //función para iniciar el audio
    public void iniciar (View v){
        //destruye el audio
        destruir();
        //crea el media player con el contexto y el recurso
        mp = MediaPlayer.create(this, R.raw.audio);
        //inicia el audio
        mp.start();
        //obtiene el estado del boton
        String op = b5.getText().toString();
        //si el boton es "no reproducir en forma circular"
        if (op.equals("no reproducir en forma circular"))
            //no pone el audio en modo circular
            mp.setLooping(false);
        else
            //pone el audio en modo no circular
            mp.setLooping(true);
    }

    //función para pausar el audio
    public void pausar(View v){
        //si el media player no es nulo y está reproduciendo
        if (mp != null && mp.isPlaying()) {
            //guarda la posición actual del audio
            posicion = mp.getCurrentPosition();
            //pausa el audio
            mp.pause();
        }
    }

    //función para continuar el audio
    public void continuar(View v){
        //si el media player no es nulo y no está reproduciendo
        if (mp != null && mp.isPlaying() == false) {
            //continua el audio desde la posición guardada
            mp.seekTo(posicion);
            //continua el audio
            mp.start();
        }
    }

    //función para detener el audio
    public void detener(View v){
        //si el media player no es nulo
        if (mp != null) {
            //detiene el audio
            mp.stop();
            //pone la posicion a cero
            posicion = 0;
        }
    }

    //función para reproducir en forma circular
    public void circular (View v) {
        //detiene el audio
        detener(null);
        //obtiene el estado del boton
        String op = b5.getText().toString();
        //si el boton es "no reproducir en forma circular"
        if (op.equals("no reproducir en forma circular"))
            //cambia el texto a reproducir en forma circular
            b5.setText("reproducir en forma circular");
        else
            //cambia el texto a no reproducir en forma circular
            b5.setText(" no reproducir en forma circular");
    }
}