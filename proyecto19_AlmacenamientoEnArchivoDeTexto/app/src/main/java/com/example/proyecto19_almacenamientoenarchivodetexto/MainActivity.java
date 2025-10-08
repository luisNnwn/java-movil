package com.example.proyecto19_almacenamientoenarchivodetexto;

import android.app.Activity;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//IMPORTS DEL EJERCICIO
import android.widget.EditText;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.widget.Toast;


//clase principal
public class MainActivity extends AppCompatActivity {

    //declaración de variables de clase
    private EditText multiLin1;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //traspaso de variables
        multiLin1 = (EditText) findViewById(R.id.multiLin1);
        //creamos un array con los nombres de los archivos
        String [] archivos = fileList();

        //ahora verificamos si existe el archivo
        if (existe(archivos, "notas.txt")) {
            try {
                //instanciamos un objeto de tipo InputStreamReader para leer el archivo
                InputStreamReader archivo = new InputStreamReader(openFileInput("notas.txt"));
                //instanciamos un objeto buffered reader
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                String todo = "";
                //mientras haya lineas en el archivo se lee
                while (linea != null) {
                    //se va concatenando la linea
                    todo = todo + linea + "\n";
                    //se lee la siguiente linea
                    linea = br.readLine();
                }
                //cerramos el buffered reader y el archivo
                br.close();
                //cerramos el archivo
                archivo.close();
                //se muestra el texto en el multiline
                multiLin1.setText(todo);
            }catch (IOException e){

            }
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //metodo para verificar si existe un archivo, tiene que recibir un array de strings y un string
    public boolean existe (String [] archivos, String archbusca) {
        //aca recorremos el arreglo de strings
        for (int f = 0; f < archivos.length; f++) {
            //se verifica con el metodo equals si el archivo existe
            if (archbusca.equals(archivos[f])) {
                //si existe retorna true
                return true;
            }
            //si no existe retorna false
        } return false;
    }

    //este metodo es para grabar los datos en el archivo
    public void grabar (View v) {
        //hacemos try catch para verificar si se pudo grabar
        try {
            //creamos un objeto archivo de tipo OutputStreamWriter con nombre notas.txt y modo privado
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("notas.txt",
                    Activity.MODE_PRIVATE));
            //ahora se escribe en el archivo, teniendo en cuenta el id del objeto, tomamos
            //el texto del multiline y lo guardamos
            archivo.write(multiLin1.getText().toString());
            //cerramos el archivo, el flush vacía el buffer
            archivo.flush();
            archivo.close();
        } catch (IOException e){
        }
        //instanciamos un objeto toast con el mensaje y su duración
        Toast t = Toast.makeText(getApplicationContext(), "Los datos fueron grabados \uD83D\uDCBE", Toast.LENGTH_SHORT);
        //mostramos el toast
        t.show();
        //cerramos la actividad
        finish();
    }
}