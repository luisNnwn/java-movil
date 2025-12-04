package com.example.cualma;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class AlumnoMenuActivity2 extends AppCompatActivity {

    private String carnetAlumno;
    private String nombreCompleto;
    private String facultad;
    private String carrera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alumno_menu2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Recuperar datos del intent
        carnetAlumno   = getIntent().getStringExtra("CARNET_ALUMNO");
        nombreCompleto = getIntent().getStringExtra("NOMBRE_COMPLETO");
        facultad       = getIntent().getStringExtra("FACULTAD");
        carrera        = getIntent().getStringExtra("CARRERA");

        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            nombreCompleto = "Estudiante";
        }

        // Configurar toolbar con el nombre del alumno
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(nombreCompleto);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    public void salir (android.view.View v){
        Intent intent = new Intent(getApplicationContext(), seleccionRol.class);
        startActivity(intent);
        finish();
    }

    public void abrirMisMaterias (android.view.View v){
        Intent intent = new Intent(getApplicationContext(), FormAlumnoMaterias.class);
        // Pasamos el contexto del alumno para que luego filtres por él
        intent.putExtra("CARNET_ALUMNO", carnetAlumno);
        intent.putExtra("NOMBRE_COMPLETO", nombreCompleto);
        intent.putExtra("FACULTAD", facultad);
        intent.putExtra("CARRERA", carrera);
        startActivity(intent);
    }

    public void abrirMisDocentes (android.view.View v){
        Intent intent = new Intent(getApplicationContext(), FormAlumnoDocentes.class);
        intent.putExtra("CARNET_ALUMNO", carnetAlumno);
        intent.putExtra("NOMBRE_COMPLETO", nombreCompleto);
        intent.putExtra("FACULTAD", facultad);
        intent.putExtra("CARRERA", carrera);
        startActivity(intent);
    }

    public void abrirEditarDatos (android.view.View v){
        Intent intent = new Intent(getApplicationContext(), FormAlumnoDatos.class);
        intent.putExtra("CARNET_ALUMNO", carnetAlumno);
        intent.putExtra("NOMBRE_COMPLETO", nombreCompleto);
        intent.putExtra("FACULTAD", facultad);
        intent.putExtra("CARRERA", carrera);
        startActivity(intent);
    }

    public void abrirCatalogoMaterias(android.view.View v) {
        Intent intent = new Intent(getApplicationContext(), CatalogoMateriasAlumno.class);
        intent.putExtra("CARNET_ALUMNO", carnetAlumno);
        intent.putExtra("NOMBRE_COMPLETO", nombreCompleto);
        intent.putExtra("FACULTAD", facultad);
        intent.putExtra("CARRERA", carrera);
        startActivity(intent);
    }
}
