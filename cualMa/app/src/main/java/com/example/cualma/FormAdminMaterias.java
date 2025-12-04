package com.example.cualma;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.TextView;
import android.view.ViewGroup;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FormAdminMaterias extends AppCompatActivity {

    private MaterialButton btnAgregarMateria;
    private RecyclerView rvMaterias;

    private CualMaDbHelper dbHelper;
    private final List<Materia> listaMaterias = new ArrayList<>();
    private MateriasAdapter adapter;

    // Opciones fijas del spinner
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
        setContentView(R.layout.activity_form_admin_materias);

        // Insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Toolbar con back
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        dbHelper = new CualMaDbHelper(this);

        btnAgregarMateria = findViewById(R.id.btnAgregarMateria);
        rvMaterias = findViewById(R.id.rvMaterias);

        rvMaterias.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MateriasAdapter(listaMaterias);
        rvMaterias.setAdapter(adapter);

        TextInputEditText etBuscar = findViewById(R.id.etBuscar);

        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarMaterias(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        btnAgregarMateria.setOnClickListener(v -> mostrarDialogMateria(null));

        // Carga inicial de datos
        cargarMaterias();
    }

    private void cargarMaterias() {
        listaMaterias.clear();
        listaMaterias.addAll(dbHelper.obtenerMaterias());
        adapter.notifyDataSetChanged();
    }

    private void filtrarMaterias(String texto) {
        String query = texto.toLowerCase().trim();

        // Si está vacío, recargar desde BD para asegurar datos REALES
        if (query.isEmpty()) {
            cargarMaterias();  // ← recarga todo desde SQLite
            return;
        }

        List<Materia> filtrada = new ArrayList<>();

        for (Materia m : listaMaterias) {
            String nombre   = m.nombre   != null ? m.nombre.toLowerCase() : "";
            String codigo   = m.codigo   != null ? m.codigo.toLowerCase() : "";
            String facultad = m.facultad != null ? m.facultad.toLowerCase() : "";
            String dias     = m.dias     != null ? m.dias.toLowerCase() : "";

            if (nombre.contains(query) ||
                    codigo.contains(query) ||
                    facultad.contains(query) ||
                    dias.contains(query))
            {
                filtrada.add(m);
            }
        }

        adapter.actualizarLista(filtrada);
    }


    /**
     * Muestra el diálogo para agregar/editar materia.
     * Si materiaEditar == null → modo agregar.
     * Si no → modo editar.
     */
    private void mostrarDialogMateria(Materia materiaEditar) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_materia, null);

        TextInputEditText etCodigo     = view.findViewById(R.id.etCodigo);
        TextInputEditText etNombre     = view.findViewById(R.id.etNombre);
        TextInputEditText etAula       = view.findViewById(R.id.etAula);
        TextInputEditText etHoraInicio = view.findViewById(R.id.etHoraInicio);
        TextInputEditText etHoraFin    = view.findViewById(R.id.etHoraFin);
        ChipGroup chipGroupDias        = view.findViewById(R.id.chipGroupDias);
        Chip chipLunes     = view.findViewById(R.id.chipLunes);
        Chip chipMartes    = view.findViewById(R.id.chipMartes);
        Chip chipMiercoles = view.findViewById(R.id.chipMiercoles);
        Chip chipJueves    = view.findViewById(R.id.chipJueves);
        Chip chipViernes   = view.findViewById(R.id.chipViernes);
        Chip chipSabado    = view.findViewById(R.id.chipSabado);

        androidx.appcompat.widget.AppCompatSpinner spFacultad =
                view.findViewById(R.id.spFacultad);

        // Spinner de facultades
        ArrayAdapter<String> adapterFacultad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                FACULTADES
        );
        adapterFacultad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFacultad.setAdapter(adapterFacultad);

        // Si estamos editando, rellenar campos
        if (materiaEditar != null) {
            etCodigo.setText(materiaEditar.codigo);
            etNombre.setText(materiaEditar.nombre);
            etAula.setText(materiaEditar.aula);
            etHoraInicio.setText(materiaEditar.horaInicio);
            etHoraFin.setText(materiaEditar.horaFin);

            // Código no editable para evitar líos con la PK
            etCodigo.setEnabled(false);

            // Seleccionar facultad
            for (int i = 0; i < FACULTADES.length; i++) {
                if (FACULTADES[i].equalsIgnoreCase(materiaEditar.facultad)) {
                    spFacultad.setSelection(i);
                    break;
                }
            }

            // Seleccionar días según el texto guardado
            String dias = materiaEditar.dias != null ? materiaEditar.dias : "";
            marcarChipSiContiene(dias, "Lunes", chipLunes);
            marcarChipSiContiene(dias, "Martes", chipMartes);
            marcarChipSiContiene(dias, "Miércoles", chipMiercoles);
            marcarChipSiContiene(dias, "Miercoles", chipMiercoles); // por si sin tilde
            marcarChipSiContiene(dias, "Jueves", chipJueves);
            marcarChipSiContiene(dias, "Viernes", chipViernes);
            marcarChipSiContiene(dias, "Sábado", chipSabado);
            marcarChipSiContiene(dias, "Sabado", chipSabado);
        }

        // TimePickers
        etHoraInicio.setOnClickListener(v -> mostrarTimePicker(etHoraInicio));
        etHoraFin.setOnClickListener(v -> mostrarTimePicker(etHoraFin));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        // Botón guardar
        MaterialButton btnGuardar = view.findViewById(R.id.btnGuardarMateria);
        btnGuardar.setOnClickListener(v -> {
            String codigo   = getTextSafe(etCodigo);
            String nombre   = getTextSafe(etNombre);
            String aula     = getTextSafe(etAula);
            String horaIni  = getTextSafe(etHoraInicio);
            String horaFin  = getTextSafe(etHoraFin);
            String facultad = (String) spFacultad.getSelectedItem();
            String dias     = obtenerDiasDesdeChips(chipGroupDias);

            if (TextUtils.isEmpty(codigo) || TextUtils.isEmpty(nombre)) {
                etCodigo.setError(TextUtils.isEmpty(codigo) ? "Requerido" : null);
                etNombre.setError(TextUtils.isEmpty(nombre) ? "Requerido" : null);
                return;
            }

            Materia materia = new Materia(
                    codigo,
                    nombre,
                    facultad,
                    aula,
                    dias,
                    horaIni,
                    horaFin
            );

            if (materiaEditar == null) {
                // Insertar
                long res = dbHelper.insertarMateria(materia);
                if (res == -1) {
                    etCodigo.setError("Código ya existe");
                    return;
                }
            } else {
                // Actualizar
                int updated = dbHelper.actualizarMateria(materia);
                if (updated == 0) {
                    // Algo raro pasó, pero no hacemos drama aquí
                }
            }

            dialog.dismiss();
            cargarMaterias();
        });

        dialog.show();
    }

    private void marcarChipSiContiene(String dias, String texto, Chip chip) {
        if (dias.toLowerCase().contains(texto.toLowerCase())) {
            chip.setChecked(true);
        }
    }

    private String obtenerDiasDesdeChips(ChipGroup chipGroupDias) {
        List<String> dias = new ArrayList<>();

        for (int i = 0; i < chipGroupDias.getChildCount(); i++) {
            View child = chipGroupDias.getChildAt(i);
            if (child instanceof Chip) {
                Chip c = (Chip) child;
                if (c.isChecked()) {
                    dias.add(c.getText().toString());
                }
            }
        }

        return TextUtils.join(", ", dias);
    }

    private void mostrarTimePicker(TextInputEditText targetEditText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (TimePicker view, int hourOfDay, int minuteOfHour) -> {
                    String horaFormateada = String.format("%02d:%02d", hourOfDay, minuteOfHour);
                    targetEditText.setText(horaFormateada);
                },
                hour,
                minute,
                true // formato 24h
        );

        timePickerDialog.show();
    }

    private String getTextSafe(TextInputEditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    // ================= ADAPTER INTERNO =================

    private class MateriasAdapter extends RecyclerView.Adapter<MateriasAdapter.MateriaViewHolder> {

        private final List<Materia> datos;

        MateriasAdapter(List<Materia> datos) {
            this.datos = datos;
        }

        @NonNull
        @Override
        public MateriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_materia, parent, false);
            return new MateriaViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MateriaViewHolder holder, int position) {
            holder.bind(datos.get(position));
        }

        @Override
        public int getItemCount() {
            return datos.size();
        }

        // 🔥 ESTE ES EL MÉTODO QUE FALTABA EN EL LUGAR CORRECTO
        void actualizarLista(List<Materia> nuevaLista) {
            datos.clear();
            datos.addAll(nuevaLista);
            notifyDataSetChanged();
        }

        class MateriaViewHolder extends RecyclerView.ViewHolder {

            TextView tvNombre, tvCodigo, tvHorario;

            MateriaViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNombre  = itemView.findViewById(R.id.tvNombreMateria);
                tvCodigo  = itemView.findViewById(R.id.tvCodigoMateria);
                tvHorario = itemView.findViewById(R.id.tvHorarioMateria);
            }

            void bind(Materia materia) {
                tvNombre.setText(materia.nombre);
                tvCodigo.setText("Código: " + materia.codigo);

                String textoHorario;
                if (!TextUtils.isEmpty(materia.dias) &&
                        !TextUtils.isEmpty(materia.horaInicio) &&
                        !TextUtils.isEmpty(materia.horaFin)) {
                    textoHorario = materia.dias + " · " + materia.horaInicio + " - " + materia.horaFin;
                } else {
                    textoHorario = "Sin horario definido";
                }

                tvHorario.setText(textoHorario);

                itemView.setOnClickListener(v -> mostrarDialogMateria(materia));

                itemView.setOnLongClickListener(v -> {
                    new AlertDialog.Builder(FormAdminMaterias.this)
                            .setTitle("Eliminar materia")
                            .setMessage("¿Seguro que deseas eliminar \"" + materia.nombre + "\"?")
                            .setPositiveButton("Eliminar", (dialog, which) -> {
                                dbHelper.eliminarMateria(materia.codigo);
                                cargarMaterias();
                            })
                            .setNegativeButton("Cancelar", null)
                            .show();

                    return true;
                });
            }
        }
    }

}
