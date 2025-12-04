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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class FormAdminDocentes extends AppCompatActivity {

    private RecyclerView rvDocentes;
    private MaterialButton btnAgregarDocente;

    private CualMaDbHelper dbHelper;
    private final List<Docente> listaDocentes = new ArrayList<>();
    private DocentesAdapter adapter;

    private final String[] FACULTADES = {
            "Facultad de Arte y Diseño",
            "Facultad de Ingeniería y Sistemas",
            "Facultad de Ciencias Económicas",
            "Facultad de Ciencias Jurídicas y Sociales"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_form_admin_docentes);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bar = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bar.left, bar.top, bar.right, bar.bottom);
            return insets;
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new CualMaDbHelper(this);

        rvDocentes = findViewById(R.id.rvDocentes);
        btnAgregarDocente = findViewById(R.id.btnAgregarDocente);

        rvDocentes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DocentesAdapter(listaDocentes);
        rvDocentes.setAdapter(adapter);

        TextInputEditText etBuscar = findViewById(R.id.etBuscarDocente);

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarDocentes(s.toString());
            }
        });


        btnAgregarDocente.setOnClickListener(v -> mostrarDialogDocente(null));

        cargarDocentes();
    }

    private void cargarDocentes() {
        listaDocentes.clear();
        listaDocentes.addAll(dbHelper.obtenerTodosDocentes());
        adapter.notifyDataSetChanged();
    }

    private void filtrarDocentes(String texto) {
        String query = texto.toLowerCase().trim();

        if (query.isEmpty()) {
            adapter.actualizarLista(new ArrayList<>(listaDocentes));
            return;
        }

        List<Docente> filtrada = new ArrayList<>();

        for (Docente d : listaDocentes) {
            if (d.nombres.toLowerCase().contains(query) ||
                    d.apellidos.toLowerCase().contains(query) ||
                    d.facultad.toLowerCase().contains(query)) {
                filtrada.add(d);
            }
        }

        adapter.actualizarLista(filtrada);
    }

    private void cargarMateriasPorFacultad(String facultad, LinearLayout contenedor, List<CheckBox> checkBoxes) {
        contenedor.removeAllViews();
        checkBoxes.clear();

        List<Materia> materias = dbHelper.obtenerMaterias();

        for (Materia m : materias) {
            if (m.facultad.equals(facultad)) {
                CheckBox cb = new CheckBox(this);
                cb.setText(m.nombre + " (" + m.codigo + ")");
                cb.setTag(m.codigo);
                contenedor.addView(cb);
                checkBoxes.add(cb);
            }
        }
    }


    private void mostrarDialogDocente(Docente docenteEditar) {

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_docente, null);

        TextInputEditText etNombres = view.findViewById(R.id.etNombresDocente);
        TextInputEditText etApellidos = view.findViewById(R.id.etApellidosDocente);
        Spinner spFacultad = view.findViewById(R.id.spFacultadDocente);


        ArrayAdapter<String> adapterFac = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                FACULTADES
        );
        adapterFac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacultad.setAdapter(adapterFac);

        LinearLayout contenedorMaterias = view.findViewById(R.id.layoutMateriasDocente);

        List<CheckBox> checkBoxes = new ArrayList<>();

// Listener para cargar materias por facultad seleccionada
        spFacultad.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View v, int pos, long id) {
                String fac = FACULTADES[pos];

                cargarMateriasPorFacultad(fac, contenedorMaterias, checkBoxes);

                // Si estás editando un docente → marcar materias asignadas
                if (docenteEditar != null) {
                    String[] materiasAsignadas = docenteEditar.materias.split(",");
                    for (CheckBox cb : checkBoxes) {
                        String codigo = cb.getTag().toString();
                        for (String m : materiasAsignadas) {
                            if (m.trim().equals(codigo.trim())) {
                                cb.setChecked(true);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });



        if (docenteEditar != null) {
            etNombres.setText(docenteEditar.nombres);
            etApellidos.setText(docenteEditar.apellidos);

            for (int i = 0; i < FACULTADES.length; i++) {
                if (FACULTADES[i].equals(docenteEditar.facultad)) {
                    spFacultad.setSelection(i);
                    break;
                }
            }

            // Marcar checkboxes según materias guardadas
            String[] materiasAsignadas = docenteEditar.materias.split(",");
            for (CheckBox cb : checkBoxes) {
                String codigo = cb.getTag().toString();
                for (String m : materiasAsignadas) {
                    if (m.trim().equals(codigo.trim())) {
                        cb.setChecked(true);
                        break;
                    }
                }
            }
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        MaterialButton btnGuardar = view.findViewById(R.id.btnGuardarDocente);

        btnGuardar.setOnClickListener(v -> {

            String nombres = getSafe(etNombres);
            String apellidos = getSafe(etApellidos);
            String facultad = (String) spFacultad.getSelectedItem();

            if (TextUtils.isEmpty(nombres)) {
                etNombres.setError("Requerido");
                return;
            }

            List<String> seleccion = new ArrayList<>();
            for (CheckBox cb : checkBoxes) {
                if (cb.isChecked()) {
                    seleccion.add(cb.getTag().toString());
                }
            }

            String materiasGuardadas = TextUtils.join(",", seleccion);

            Docente d;

            if (docenteEditar == null) {
                d = new Docente(0, nombres, apellidos, facultad, materiasGuardadas);
                dbHelper.insertarDocente(d);
            } else {
                d = new Docente(docenteEditar.id, nombres, apellidos, facultad, materiasGuardadas);
                dbHelper.actualizarDocente(d);
            }

            dialog.dismiss();
            cargarDocentes();
        });

        dialog.show();
    }

    private String getSafe(TextInputEditText e) {
        return e.getText() != null ? e.getText().toString().trim() : "";
    }

    // ---------------- ADAPTER --------------------

    private class DocentesAdapter extends RecyclerView.Adapter<DocentesAdapter.DocenteVH> {

        private final List<Docente> datos;

        DocentesAdapter(List<Docente> datos) {
            this.datos = datos;
        }

        @NonNull
        @Override
        public DocenteVH onCreateViewHolder(@NonNull ViewGroup p, int v) {
            View view = getLayoutInflater().inflate(R.layout.item_docente, p, false);
            return new DocenteVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DocenteVH h, int pos) {
            h.bind(datos.get(pos));
        }

        @Override
        public int getItemCount() {
            return datos.size();
        }

        public void actualizarLista(List<Docente> nuevaLista) {
            datos.clear();
            datos.addAll(nuevaLista);
            notifyDataSetChanged();
        }

        class DocenteVH extends RecyclerView.ViewHolder {
            TextView tNombre, tFacultad, tMaterias;

            DocenteVH(@NonNull View v) {
                super(v);
                tNombre = v.findViewById(R.id.tvNombreDocente);
                tFacultad = v.findViewById(R.id.tvFacultadDocente);
                tMaterias = v.findViewById(R.id.tvMateriasDocente);
            }

            void bind(Docente d) {
                tNombre.setText(d.nombres + " " + d.apellidos);
                tFacultad.setText(d.facultad);
                tMaterias.setText("Materias: " + d.materias);

                itemView.setOnClickListener(v -> mostrarDialogDocente(d));

                itemView.setOnLongClickListener(v -> {
                    new AlertDialog.Builder(FormAdminDocentes.this)
                            .setTitle("Eliminar docente")
                            .setMessage("¿Eliminar a " + d.nombres + "?")
                            .setPositiveButton("Eliminar", (di, w) -> {
                                dbHelper.eliminarDocente(d.id);
                                cargarDocentes();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();
                    return true;
                });
            }
        }
    }
}
