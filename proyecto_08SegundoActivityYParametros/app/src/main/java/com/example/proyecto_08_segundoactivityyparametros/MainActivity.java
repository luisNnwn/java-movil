package com.example.proyecto_08_segundoactivityyparametros;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//imports para el ejercicio
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.content.Intent;


//clase principal del primer activity
public class MainActivity extends AppCompatActivity {

    /*Declaración de variable de clase*/
    private EditText et1;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //enlace del layout con el activity
        setContentView(R.layout.activity_main);

        /*Traspaso de variables*/
        et1 = (EditText) findViewById(R.id.et1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*Metodo que primero instancia el intent y le pasa el contenido
    * del editText para finalmente poder iniciar la activity nueva*/
public void ver(View v){
    //instanciamos un objeto intent, le pasamos el contexto de la activity y la clase a la que queremos ir
        Intent i = new Intent(this, Actividad2.class);
        /*El metodo putExtra sirve para adjuntar datos a un intent,
        * permite pasar información como texto, numeros u objetos*/
        i.putExtra("direccion", et1.getText().toString());
        //iniciamos la activity
        startActivity(i);
}

}