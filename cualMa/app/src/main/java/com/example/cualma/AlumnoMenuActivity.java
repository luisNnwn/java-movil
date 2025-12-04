package com.example.cualma;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AlumnoMenuActivity extends AppCompatActivity {

    private TextInputEditText etCarnetAlumno;
    private TextInputLayout tilCarnetAlumno;
    private CualMaDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alumno_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar (por si más adelante quieres usar setSupportActionBar)
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        tilCarnetAlumno = findViewById(R.id.tilCarnetAlumno);
        etCarnetAlumno  = findViewById(R.id.etCarnetAlumno);

        dbHelper = new CualMaDbHelper(this);
    }

    // Método que ya está referenciado en el XML con android:onClick="ingresarAlumnoMenuActivity2"
    public void ingresarAlumnoMenuActivity2 (android.view.View v){
        String carnet = (etCarnetAlumno.getText() != null)
                ? etCarnetAlumno.getText().toString().trim()
                : "";

        // Validar vacío
        if (carnet.isEmpty()) {
            if (tilCarnetAlumno != null) {
                tilCarnetAlumno.setError("Ingrese su carnet");
            }
            Toast.makeText(this, "El carnet no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        } else if (tilCarnetAlumno != null) {
            tilCarnetAlumno.setError(null);
        }

        // Buscar alumno en BD
        Alumno alumno = dbHelper.obtenerAlumnoPorCarnet(carnet);

        if (alumno == null) {
            if (tilCarnetAlumno != null) {
                tilCarnetAlumno.setError("Carnet no registrado");
            }
            Toast.makeText(this, "Este carnet no existe en el sistema", Toast.LENGTH_SHORT).show();
            return;
        }

        // Si existe, pasamos al menú 2 con la info del alumno
        Intent intent = new Intent(getApplicationContext(), AlumnoMenuActivity2.class);
        intent.putExtra("CARNET_ALUMNO", alumno.getCarnet());
        intent.putExtra("NOMBRE_COMPLETO", alumno.getNombreCompleto());
        intent.putExtra("FACULTAD", alumno.getFacultad());
        intent.putExtra("CARRERA", alumno.getCarrera());
        startActivity(intent);
    }
}
