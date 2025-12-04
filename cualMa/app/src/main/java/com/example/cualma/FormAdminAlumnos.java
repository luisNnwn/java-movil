package com.example.cualma;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FormAdminAlumnos extends AppCompatActivity {

    private MaterialButton btnAgregarAlumno;
    private RecyclerView rvAlumnos;

    private CualMaDbHelper dbHelper;
    private final List<Alumno> listaAlumnos = new ArrayList<>();
    private AlumnosAdapter adapter;

    private final String[] FACULTADES = {
            "Facultad de Arte y Diseño",
            "Facultad de Ingeniería y Sistemas",
            "Facultad de Ciencias Económicas",
            "Facultad de Ciencias Jurídicas y Sociales"
    };

    private final String[] CARRERAS = {
            "Arquitectura",
            "Ingeniería en Diseño y Desarrollo de Videojuegos",
            "Licenciatura en Relaciones Públicas y Comunicaciones",
            "Licenciatura en Relaciones Internacionales"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_admin_alumnos);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new CualMaDbHelper(this);

        btnAgregarAlumno = findViewById(R.id.btnAgregarAlumno);
        rvAlumnos = findViewById(R.id.rvAlumnos);

        rvAlumnos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AlumnosAdapter(listaAlumnos);
        rvAlumnos.setAdapter(adapter);

        TextInputEditText etBuscarAlumno = findViewById(R.id.etBuscarAlumno);

        etBuscarAlumno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarAlumnos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        btnAgregarAlumno.setOnClickListener(v -> mostrarDialogAlumno(null));

        cargarAlumnos();
    }

    private void cargarAlumnos() {
        listaAlumnos.clear();
        listaAlumnos.addAll(dbHelper.obtenerTodosAlumnos()); // si no tenés este método te lo hago
        adapter.notifyDataSetChanged();
    }

    private void filtrarAlumnos(String texto) {
        String query = texto.toLowerCase().trim();

        // Si el campo está vacío, restauramos la lista completa
        if (query.isEmpty()) {
            cargarAlumnos();
            return;
        }

        List<Alumno> filtrados = new ArrayList<>();

        for (Alumno a : listaAlumnos) {
            String nombreCompleto = (a.getNombreCompleto() != null)
                    ? a.getNombreCompleto().toLowerCase()
                    : "";

            String carnet    = a.carnet != null ? a.carnet.toLowerCase() : "";
            String facultad  = a.facultad != null ? a.facultad.toLowerCase() : "";
            String carrera   = a.carrera != null ? a.carrera.toLowerCase() : "";

            if (nombreCompleto.contains(query) ||
                    carnet.contains(query) ||
                    facultad.contains(query) ||
                    carrera.contains(query)) {

                filtrados.add(a);
            }
        }

        adapter.actualizarLista(filtrados);
    }


    private void mostrarDialogAlumno(Alumno alumnoEditar) {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_alumno, null);

        TextInputEditText etCarnet  = view.findViewById(R.id.etCarnet);
        TextInputEditText etNombres = view.findViewById(R.id.etNombres);
        TextInputEditText etApellidos = view.findViewById(R.id.etApellidos);

        androidx.appcompat.widget.AppCompatSpinner spFacultad =
                view.findViewById(R.id.spFacultadAlumno);
        androidx.appcompat.widget.AppCompatSpinner spCarrera =
                view.findViewById(R.id.spCarreraAlumno);

        ArrayAdapter<String> adFac = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, FACULTADES);
        adFac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacultad.setAdapter(adFac);

        ArrayAdapter<String> adCar = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, CARRERAS);
        adCar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCarrera.setAdapter(adCar);

        if (alumnoEditar != null) {
            etCarnet.setText(alumnoEditar.carnet);
            etNombres.setText(alumnoEditar.nombres);
            etApellidos.setText(alumnoEditar.apellidos);

            etCarnet.setEnabled(false);

            // seleccionar facultad
            for (int i = 0; i < FACULTADES.length; i++) {
                if (FACULTADES[i].equals(alumnoEditar.facultad)) {
                    spFacultad.setSelection(i);
                    break;
                }
            }

            // seleccionar carrera
            for (int i = 0; i < CARRERAS.length; i++) {
                if (CARRERAS[i].equals(alumnoEditar.carrera)) {
                    spCarrera.setSelection(i);
                    break;
                }
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        MaterialButton btnGuardar = view.findViewById(R.id.btnGuardarAlumno);

        btnGuardar.setOnClickListener(v -> {

            String carnet = getTextSafe(etCarnet);
            String nombres = getTextSafe(etNombres);
            String apellidos = getTextSafe(etApellidos);
            String facultad = (String) spFacultad.getSelectedItem();
            String carrera = (String) spCarrera.getSelectedItem();

            if (TextUtils.isEmpty(carnet) || TextUtils.isEmpty(nombres)) {
                etCarnet.setError(TextUtils.isEmpty(carnet) ? "Requerido" : null);
                etNombres.setError(TextUtils.isEmpty(nombres) ? "Requerido" : null);
                return;
            }

            Alumno alumno = new Alumno(carnet, nombres, apellidos, facultad, carrera);

            if (alumnoEditar == null) {
                long res = dbHelper.insertarAlumno(alumno);
                if (res == -1) {
                    etCarnet.setError("Carnet ya existe");
                    return;
                }
            } else {
                dbHelper.actualizarAlumno(alumno); // te lo creo si no existe
            }

            dialog.dismiss();
            cargarAlumnos();
        });

        dialog.show();
    }

    private String getTextSafe(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }


    // =====================================================
    // ADAPTER
    // =====================================================

    private class AlumnosAdapter extends RecyclerView.Adapter<AlumnosAdapter.AlumnoViewHolder> {

        private final List<Alumno> datos;

        AlumnosAdapter(List<Alumno> datos) { this.datos = datos; }

        @NonNull
        @Override
        public AlumnoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.item_alumno, parent, false);
            return new AlumnoViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull AlumnoViewHolder holder, int position) {
            holder.bind(datos.get(position));
        }

        void actualizarLista(List<Alumno> nuevaLista) {
            datos.clear();
            datos.addAll(nuevaLista);
            notifyDataSetChanged();
        }


        @Override
        public int getItemCount() { return datos.size(); }

        class AlumnoViewHolder extends RecyclerView.ViewHolder {

            TextView tvNombre, tvCarnet, tvCarrera;

            AlumnoViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre  = itemView.findViewById(R.id.tvNombreAlumno);
                tvCarnet  = itemView.findViewById(R.id.tvCarnetAlumno);
                tvCarrera = itemView.findViewById(R.id.tvFacultadCarrera); // ✔ CORRECTO
            }

            void bind(Alumno a) {
                tvNombre.setText(a.getNombreCompleto());
                tvCarnet.setText("Carnet: " + a.carnet);
                tvCarrera.setText(a.facultad + " · " + a.carrera);

                itemView.setOnClickListener(v -> mostrarDialogAlumno(a));

                itemView.setOnLongClickListener(v -> {
                    new AlertDialog.Builder(FormAdminAlumnos.this)
                            .setTitle("Eliminar alumno")
                            .setMessage("¿Eliminar a " + a.getNombreCompleto() + "?")
                            .setPositiveButton("Eliminar", (d, w) -> {
                                dbHelper.eliminarAlumno(a.carnet);
                                cargarAlumnos();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                    return true;
                });
            }
        }

    }
}
