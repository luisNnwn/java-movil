package com.example.cualma;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class FormAlumnoMaterias extends AppCompatActivity {

    private RecyclerView rvMisMaterias;
    private TextInputEditText etBuscar;

    private CualMaDbHelper db;
    private String carnetAlumno;
    private String facultad;
    private String carrera;

    private List<Materia> todasMaterias;      // Materias disponibles (según fac/carrera)
    private List<Materia> materiasAlumno;     // Materias ya agregadas
    private List<Materia> materiasFiltradas;  // Para buscar
    private MateriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_alumno_materias);

        // Datos del alumno
        carnetAlumno = getIntent().getStringExtra("CARNET_ALUMNO");
        facultad = getIntent().getStringExtra("FACULTAD");
        carrera = getIntent().getStringExtra("CARRERA");

        db = new CualMaDbHelper(this);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Views
        rvMisMaterias = findViewById(R.id.rvMisMaterias);
        rvMisMaterias.setLayoutManager(new LinearLayoutManager(this));

        etBuscar = findViewById(R.id.etBuscarMisMaterias);

        // Datos
        todasMaterias = db.obtenerMaterias(); // tu DB no tiene filtro por carrera, lo hago abajo
        materiasAlumno = db.obtenerMateriasAlumno(carnetAlumno);

        // Filtrar por facultad y carrera del alumno
        List<Materia> filtradasPorFacultad = new ArrayList<>();
        for (Materia m : todasMaterias) {
            if (m.facultad.equalsIgnoreCase(facultad)) {
                filtradasPorFacultad.add(m);
            }
        }
        todasMaterias = filtradasPorFacultad;

        materiasFiltradas = new ArrayList<>(todasMaterias);

        adapter = new MateriaAdapter();
        rvMisMaterias.setAdapter(adapter);

        // Buscar
        etBuscar.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void afterTextChanged(android.text.Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrar(s.toString());
            }
        });
    }

    private void filtrar(String texto) {
        materiasFiltradas.clear();

        if (texto.isEmpty()) {
            materiasFiltradas.addAll(todasMaterias);
        } else {
            for (Materia m : todasMaterias) {
                if (m.nombre.toLowerCase().contains(texto.toLowerCase()) ||
                        m.codigo.toLowerCase().contains(texto.toLowerCase())) {

                    materiasFiltradas.add(m);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private boolean estaAgregada(Materia materia) {
        for (Materia m : materiasAlumno) {
            if (m.codigo.equals(materia.codigo)) return true;
        }
        return false;
    }

    // =====================================================
    // ADAPTER INTERNO
    // =====================================================
    private class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_mi_materia, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder h, int position) {
            Materia m = materiasFiltradas.get(position);

            h.tvNombre.setText(m.nombre);
            h.tvDocente.setText("Aula: " + m.aula);

            boolean agregada = estaAgregada(m);

            // Colores según estado
            if (agregada) {
                h.card.setCardBackgroundColor(0xFFDFF5E1); // Verde claro
            } else {
                h.card.setCardBackgroundColor(0xFFFFFFFF); // Blanco
            }

            // Long click para agregar o eliminar
            h.itemView.setOnLongClickListener(v -> {

                if (agregada) {
                    // ELIMINAR
                    new AlertDialog.Builder(FormAlumnoMaterias.this)
                            .setTitle("Eliminar materia")
                            .setMessage("¿Desea eliminar esta materia?")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                db.eliminarMateriaDeAlumno(carnetAlumno, m.codigo);
                                materiasAlumno.remove(m);
                                notifyDataSetChanged();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                } else {
                    // AGREGAR
                    new AlertDialog.Builder(FormAlumnoMaterias.this)
                            .setTitle("Agregar materia")
                            .setMessage("¿Desea agregar esta materia?")
                            .setPositiveButton("Sí", (dialog, which) -> {
                                db.agregarMateriaAAlumno(carnetAlumno, m.codigo);
                                materiasAlumno.add(m);
                                notifyDataSetChanged();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                }

                return true;
            });
        }

        @Override
        public int getItemCount() {
            return materiasFiltradas.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNombre, tvDocente;
            MaterialCardView card;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre = itemView.findViewById(R.id.tvNombreMateriaAlumno);
                tvDocente = itemView.findViewById(R.id.tvDocenteMateriaAlumno);
                card = (MaterialCardView) itemView;
            }
        }
    }
}
