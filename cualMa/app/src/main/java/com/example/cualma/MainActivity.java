package com.example.cualma;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports del ejercicio
import android.os.Handler;


//esta activity solo va a mostrar  la imagen principal
//por unos segundos, luego se va a mostrar el onboarding
public class MainActivity extends AppCompatActivity {



    //se define el tiempo de espera para cargar la otra activity
    int tiempoEspera = 3000;

    //instanciamos un objeto de tipo Handler
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), onboardingCualMa.class);
                startActivity(intent);
                finish();
            }
        }, tiempoEspera);
    }
}