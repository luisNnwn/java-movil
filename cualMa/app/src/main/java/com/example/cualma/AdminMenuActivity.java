package com.example.cualma;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.content.Intent;

public class AdminMenuActivity extends AppCompatActivity {

    private CardView cardViewMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_menu);

        cardViewMaterias = findViewById(R.id.cardMaterias);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void salir(View v) {
        Intent intent = new Intent(getApplicationContext(), seleccionRol.class);
        startActivity(intent);
    }

    public void abrirFormAdminMaterias(View v) {
        Intent intent = new Intent(getApplicationContext(), FormAdminMaterias.class);
        startActivity(intent);
    }

    public void abrirFormAdminAlumnos(View v){
        Intent intent = new Intent(getApplicationContext(), FormAdminAlumnos.class);
        startActivity(intent);
    }

    public void abrirFormAdminDocentes(View v){
        Intent intent = new Intent(getApplicationContext(), FormAdminDocentes.class);
        startActivity(intent);
    }
}