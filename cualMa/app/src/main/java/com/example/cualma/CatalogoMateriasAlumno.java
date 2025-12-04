package com.example.cualma;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.List;

public class CatalogoMateriasAlumno extends AppCompatActivity {

    private RecyclerView rvCatalogo;
    private TextInputEditText etBuscar;

    private CualMaDbHelper db;

    private String facultad;
    private String carrera;

    private List<Materia> materiasOriginales;
    private List<Materia> materiasFiltradas;

    private MateriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_catalogo_materias_alumno);

        // Recuperar info del alumno
        facultad = getIntent().getStringExtra("FACULTAD");
        carrera  = getIntent().getStringExtra("CARRERA");

        db = new CualMaDbHelper(this);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // UI
        rvCatalogo = findViewById(R.id.rvCatalogoMaterias);
        etBuscar = findViewById(R.id.etBuscarCatalogo);

        rvCatalogo.setLayoutManager(new LinearLayoutManager(this));

        // Cargar materias de la facultad real
        materiasOriginales = new ArrayList<>();
        for (Materia m : db.obtenerMaterias()) {
            if (m.facultad.equalsIgnoreCase(facultad)) {
                materiasOriginales.add(m);
            }
        }

        materiasFiltradas = new ArrayList<>(materiasOriginales);

        adapter = new MateriaAdapter();
        rvCatalogo.setAdapter(adapter);

        // BUSCADOR
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
        });
    }

    private void filtrar(String texto) {
        materiasFiltradas.clear();

        if (texto.isEmpty()) {
            materiasFiltradas.addAll(materiasOriginales);
        } else {
            String lower = texto.toLowerCase();

            for (Materia m : materiasOriginales) {
                if (m.nombre.toLowerCase().contains(lower) ||
                        m.codigo.toLowerCase().contains(lower) ||
                        m.aula.toLowerCase().contains(lower)) {

                    materiasFiltradas.add(m);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    // ===================================================
    // ADAPTER
    // ===================================================
    private class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_catalogo_materia, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int position) {
            Materia m = materiasFiltradas.get(position);

            h.tvNombre.setText(m.nombre);
            h.tvCodigo.setText(m.codigo);
            h.tvAula.setText("Aula: " + m.aula);
            h.tvHorario.setText(m.dias + "  " + m.horaInicio + " - " + m.horaFin);
        }

        @Override
        public int getItemCount() {
            return materiasFiltradas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNombre, tvCodigo, tvAula, tvHorario;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                tvNombre  = itemView.findViewById(R.id.tvNombreCatMat);
                tvCodigo  = itemView.findViewById(R.id.tvCodigoCatMat);
                tvAula    = itemView.findViewById(R.id.tvAulaCatMat);
                tvHorario = itemView.findViewById(R.id.tvHorarioCatMat);
            }
        }
    }
}
