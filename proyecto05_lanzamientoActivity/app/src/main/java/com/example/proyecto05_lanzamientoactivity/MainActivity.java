package com.example.proyecto05_lanzamientoactivity;

//imports del proyecto
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports adicionales para el ejercicio
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;-

/*Clase principal de la aplicacion*/
public class MainActivity extends AppCompatActivity {

    /*Constructor de la clase*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        /*Enlace de la vista con el layout*/
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //metodo para poder abrir la activity: acerca de
    public void acercaDe(View view){
        /*Según investigación, Intent acá nos sirve como un puente para inciar
        * acciones, pasar datos y activar respuestas*/
        /*Acá creamos un objeto Intent, llamado i y le pasamos como parámetro
        * el contexto, la clase que queremos iniciar, en este caso AcercaDe.class
        * */
        Intent i = new Intent(this, AcercaDe.class);
        //Y finalmente usamos el metodo startActitivy para iniciar la actividad pasandole
        //el objeto que creamos anteriormente donde esta la clase que vamos a iniciar
        startActivity(i);
    }
}