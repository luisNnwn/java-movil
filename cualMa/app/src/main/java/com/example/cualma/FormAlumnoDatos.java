package com.example.cualma;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class FormAlumnoDatos extends AppCompatActivity {

    private TextInputEditText etCarnet, etNombres, etApellidos;
    private Spinner spFacultad, spCarrera;
    private CualMaDbHelper db;
    private Alumno alumno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_alumno_datos);

        db = new CualMaDbHelper(this);

        // === Recibir carnet desde el intent ===
        String carnet = getIntent().getStringExtra("CARNET_ALUMNO");
        alumno = db.obtenerAlumnoPorCarnet(carnet);

        if (alumno == null) {
            Toast.makeText(this, "Error: alumno no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // === Toolbar ===
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // === Referencias UI ===
        etCarnet   = findViewById(R.id.etCarnet);
        etNombres  = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        spFacultad = findViewById(R.id.spFacultadAlumno);
        spCarrera  = findViewById(R.id.spCarreraAlumno);

        // === Llenar información del alumno ===
        etCarnet.setText(alumno.carnet);
        etNombres.setText(alumno.nombres);
        etApellidos.setText(alumno.apellidos);

        // ====== SPINNERS ======
        // Aquí simplemente colocamos una lista con UNA opción (la actual)
        // porque NO queremos que el alumno pueda cambiarla.

        ArrayAdapter<String> adFacultad =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{ alumno.facultad });
        adFacultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacultad.setAdapter(adFacultad);

        ArrayAdapter<String> adCarrera =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{ alumno.carrera });
        adCarrera.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarrera.setAdapter(adCarrera);

        // === DESHABILITAR SPINNERS (no editables) ===
        spFacultad.setEnabled(false);
        spCarrera.setEnabled(false);

        // === DESHABILITAR CARNET ===
        etCarnet.setEnabled(false);

        // -------------------------------------------------------------------------
        // GUARDAR CAMBIOS
        // -------------------------------------------------------------------------
        findViewById(R.id.btnGuardarMisDatos).setOnClickListener(v -> {

            String nuevosNombres   = etNombres.getText().toString().trim();
            String nuevosApellidos = etApellidos.getText().toString().trim();

            if (nuevosNombres.isEmpty() || nuevosApellidos.isEmpty()) {
                Toast.makeText(this, "Debe llenar nombres y apellidos", Toast.LENGTH_SHORT).show();
                return;
            }

            Alumno actualizado = new Alumno(
                    alumno.carnet,
                    nuevosNombres,
                    nuevosApellidos,
                    alumno.facultad,
                    alumno.carrera
            );

            int rows = db.actualizarAlumno(actualizado);

            if (rows > 0) {
                Toast.makeText(this, "Datos personales actualizados", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se pudo actualizar", Toast.LENGTH_SHORT).show();
            }
        });

        // -------------------------------------------------------------------------
        // ELIMINAR PERFIL DEL SISTEMA
        // -------------------------------------------------------------------------
        findViewById(R.id.btnEliminarMisDatos).setOnClickListener(v -> {

            new AlertDialog.Builder(FormAlumnoDatos.this)
                    .setTitle("Eliminar información personal")
                    .setMessage("¿Seguro que desea eliminar su registro?\nEsto no se puede deshacer.")
                    .setPositiveButton("Eliminar", (dialog, which) -> {

                        int rows = db.eliminarAlumno(alumno.carnet);

                        if (rows > 0) {
                            Toast.makeText(this, "Perfil eliminado correctamente", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(getApplicationContext(), seleccionRol.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }
}
