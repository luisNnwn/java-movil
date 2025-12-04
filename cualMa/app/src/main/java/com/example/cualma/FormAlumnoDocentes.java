package com.example.cualma;

import android.os.Bundle;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormAlumnoDocentes extends AppCompatActivity {

    private RecyclerView rvMisDocentes;
    private TextInputEditText etBuscarMisDocentes;

    private CualMaDbHelper db;
    private String facultad;
    private String carrera;

    private List<Docente> docentesFiltrados;
    private List<Docente> docentesOriginales;
    private DocenteAdapter adapter;

    private List<Materia> materiasCarrera; // materias válidas segun carrera y facultad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_alumno_docentes);

        // Recuperar datos del alumno
        facultad = getIntent().getStringExtra("FACULTAD");
        carrera  = getIntent().getStringExtra("CARRERA");

        db = new CualMaDbHelper(this);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // IDs REALES DEL LAYOUT
        rvMisDocentes = findViewById(R.id.rvMisDocentes);
        etBuscarMisDocentes = findViewById(R.id.etBuscarMisDocentes);

        rvMisDocentes.setLayoutManager(new LinearLayoutManager(this));

        // Obtener materias de la facultad/carrera
        materiasCarrera = new ArrayList<>();
        for (Materia m : db.obtenerMaterias()) {
            if (m.facultad.equalsIgnoreCase(facultad)) {
                materiasCarrera.add(m);
            }
        }

        // Filtrar docentes válidos
        docentesOriginales = filtrarDocentesPorCarrera();
        docentesFiltrados = new ArrayList<>(docentesOriginales);

        adapter = new DocenteAdapter();
        rvMisDocentes.setAdapter(adapter);

        // BUSCADOR
        etBuscarMisDocentes.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buscarDocente(s.toString());
            }
        });
    }

    // ============================================================
    //  FILTRAR DOCENTES POR CARRERA Y FACULTAD
    // ============================================================
    private List<Docente> filtrarDocentesPorCarrera() {

        List<Docente> resultado = new ArrayList<>();
        List<Docente> todos = db.obtenerTodosDocentes();

        for (Docente d : todos) {

            // solo docentes de la facultad del alumno
            if (!d.facultad.equalsIgnoreCase(facultad)) continue;

            if (d.materias == null || d.materias.trim().isEmpty()) continue;

            List<String> codigosDoc = Arrays.asList(d.materias.split(","));

            boolean coincide = false;

            for (Materia mCarrera : materiasCarrera) {
                if (codigosDoc.contains(mCarrera.codigo)) {
                    coincide = true;
                    break;
                }
            }

            if (coincide) resultado.add(d);
        }

        return resultado;
    }

    // ============================================================
    //  BUSCADOR
    // ============================================================
    private void buscarDocente(String texto) {
        docentesFiltrados.clear();

        if (texto.isEmpty()) {
            docentesFiltrados.addAll(docentesOriginales);
        } else {

            for (Docente d : docentesOriginales) {

                String materiaCoincidente = obtenerMateriaCoin(d, texto);

                if (d.nombres.toLowerCase().contains(texto.toLowerCase()) ||
                        d.apellidos.toLowerCase().contains(texto.toLowerCase()) ||
                        materiaCoincidente != null) {

                    docentesFiltrados.add(d);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private String obtenerMateriaCoin(Docente d, String texto) {

        if (d.materias == null) return null;

        List<String> codigos = Arrays.asList(d.materias.split(","));

        for (Materia m : materiasCarrera) {
            if (codigos.contains(m.codigo)) {
                if (m.nombre.toLowerCase().contains(texto.toLowerCase()))
                    return m.nombre;
            }
        }

        return null;
    }

    // ============================================================
    // ADAPTER INTERNO
    // ============================================================
    private class DocenteAdapter extends RecyclerView.Adapter<DocenteAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mi_docente, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int position) {
            Docente d = docentesFiltrados.get(position);

            h.tvNombre.setText(d.nombres + " " + d.apellidos);
            h.tvFacultad.setText(d.facultad);

            StringBuilder materiasTexto = new StringBuilder();

            List<String> codigosDoc = Arrays.asList(d.materias.split(","));

            for (Materia m : materiasCarrera) {
                if (codigosDoc.contains(m.codigo)) {
                    materiasTexto.append("• ").append(m.nombre).append("\n");
                }
            }

            h.tvMaterias.setText(materiasTexto.toString().trim());
        }

        @Override
        public int getItemCount() {
            return docentesFiltrados.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNombre, tvFacultad, tvMaterias;

            ViewHolder(View itemView) {
                super(itemView);

                tvNombre = itemView.findViewById(R.id.tvNombreDocenteAlumno);
                tvFacultad = itemView.findViewById(R.id.tvFacultadDocenteAlumno);
                tvMaterias = itemView.findViewById(R.id.tvMateriasDocenteAlumno);
            }
        }
    }
}
