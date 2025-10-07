package com.example.proyecto18_almacenamientosp2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports del ejercicio
import android.widget.EditText;
import android.view.View;
import android.content.SharedPreferences;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //declaracion de variables de clase
    private EditText et1, et2;

    //constructor de la alse
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //seteo de la vista con el layout
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Metodos de la clase

    public void grabar (View v) {
        /*Declaramos dos variables de tipo string para guardar
        * el contenido de los editText*/
        String nombre = et1.getText().toString();
        String datos = et2.getText().toString();
        /*Creamos un objeto de tipo SharedPreferences para guardar los datos
        * y le asignamos el nombre del archivo xml y el modo de acceso*/
        SharedPreferences preferencias = getSharedPreferences("agenda", MODE_PRIVATE);
        /*creamos un objeto de tipo Editor para editar el archivo xml
        * preparamos el editor para guardar los datos con commit*/
        SharedPreferences.Editor editor = preferencias.edit();
        //guardamos los datos en el archivo xml mediante el editor haciendo
        //uso de la funcion putString, la cual recibe la clave nombre y los datos
        editor.putString(nombre, datos);
        //guardamos los datos en el archivo xml mediante el editor haciendo
        //uso de la funcion commit
        editor.commit();
    }

    public void recuperar (View v) {
        //declaramos una variable de tipo string para guardar el nombre
        String nombre = et1.getText().toString();
        //creamos un objeto de tipo SharedPreferences para leer los datos
        //y le asignamos el nombre del archivo xml y el modo de acceso
        SharedPreferences preferencias = getSharedPreferences("agenda", MODE_PRIVATE);
        //Creamos la variable string d para guardar los datos recuperados
        String d = preferencias.getString(nombre, "");
        //si la variable d esta vacia mostramos un mensaje de error
        if (d.length() == 0) {
            Toast.makeText(getApplicationContext(), "No existe dicho nombre en la agenda", Toast.LENGTH_SHORT).show();
        } else {
            //si la variable d no esta vacia mostramos los datos en los editText
            et2.setText(d);
        }
    }
}