package com.example.cualma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class seleccionRol extends AppCompatActivity {

    //COMO LA IMAGEN OCUPA EL ESPACIO NO FUNCIONABA
    //private CardView cardViewAdministrador;
    //private CardView cardViewEstudiante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seleccion_rol);

        ImageButton imgButton1 = findViewById(R.id.imgButton1);
        ImageButton imgButton2 = findViewById(R.id.imgButton2);

        imgButton1.setOnClickListener(v -> {
            Intent intent = new Intent(seleccionRol.this, AdminMenuActivity.class);
            startActivity(intent);
        });

        // OnClick para Estudiante
        imgButton2.setOnClickListener(v -> {
            Intent intent = new Intent(seleccionRol.this, AlumnoMenuActivity.class);
            startActivity(intent);
        });
    }
}
