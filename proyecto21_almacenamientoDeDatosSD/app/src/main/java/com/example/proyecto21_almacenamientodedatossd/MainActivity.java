package com.example.proyecto21_almacenamientodedatossd;

//imports
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports para realizar el ejercicio
import android.os.Environment;
import android.widget.EditText;
import android.view.View;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.widget.Toast;

//clase principal de la actividad
public class MainActivity extends AppCompatActivity {

    //declaración de variables de clase
    private EditText et1, et2;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relación entre la vista y el layout
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

    //metodo para grabar la nota en un archivo de texto
    //guardado en el almacenamiento externo
    public void grabar(View v) {
        /*Creamos dos variables de tipo String, la primera
         * para el nombre del archivo, el cual será el contenido
         * del et1, y la segunda para el contenido del et2.*/
        String nomarchivo = et1.getText().toString();
        String contenido = et2.getText().toString();
        try {
            //Instanciamos un objeto de tipo File, llamado tarjeta para obtener la ruta de la tarjeta SD
            //Enviroment es una clase que nos permite obtener la ruta de la tarjeta SD a través de un metodo
            // CAMBIAR: File tarjeta = Environment.getExternalStorageDirectory();
            //Con esto se obtiene la ruta del almacenamiento externo de la app.
            //Con esto se pueden guardar archivos sin pedir permisos, lo anterior no funcionaba
            //porque a partir de Android 10 Google limito el acceso
            //aca se devuelve un arreglo con las ubicaciones de almacenamiento
            //la cero normalmente es el almacenamiento interno y 1 para externo
            File tarjeta = getExternalFilesDirs(null)[1];
            //Acá se muestra un toast con la ruta de la tarjeta
            //accedemos a esa información con tarjeta.getAbsolutePath()
            Toast.makeText(getApplicationContext(), tarjeta.getAbsolutePath(), Toast.LENGTH_LONG).show();
            //Acá creamos un objeto de tipo file, el cual recibe como parametros la ruta y el nombre del archivo
            //el cual el usuario ingresa en el et1
            File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
            //Instanciamos un objeto de tipo OutputStreamWriter el cual sirve para escribir en un archivo
            //con el nombre especificado en el file
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file));
            //aca escribimos el contenido del et2 en el archivo
            osw.write(contenido);
            //el flush sirve para vaciar el buffer de escritura
            //osw.flush();
            //cerramos el archivo
            osw.close();
            //mostramos un toast con un mensaje de éxito
            Toast.makeText(getApplicationContext(), "Los datos fueron grabados correctamente", Toast.LENGTH_LONG).show();
            //limpiamos los campos de texto
            et1.setText("");
            et2.setText("");
        } catch (IOException ioe) {
            //si no se pudo guardar, mostramos un toast con un mensaje de error
            Toast.makeText(getApplicationContext(), "No se pudo grabar", Toast.LENGTH_SHORT).show();
        }
    }

    //METODO PARA RECUPERAR LA NOTA DE UN ARCHIVO DE TEXTO
    public void recuperar(View v) {
        /*Al igual que en el metodo anterior, creamos una variable de tipo String
        * que va a contener el nombre del archivo a recuperar*/
        String nomarchivo = et1.getText().toString();
        //Instanciamos un objeto de tipo File, llamado tarjeta para obtener la ruta de la tarjeta SD
        //CAMBIAR: File tarjeta = Environment.getExternalStorageDirectory();
        File tarjeta = getExternalFilesDirs(null)[1];
        //Acá creamos un objeto de tipo file, el cual recibe como parametros la ruta y el nombre del archivo
        File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
        try {
            //Ahora se isntancia un objeto de tipo FileInputStream que sirve para leer los datos
            //del archivo que se desea recuperar y del cual se obtuvo la ruta en el file
            FileInputStream fIn = new FileInputStream(file);
            //Instanciamos un objeto de tipo InputStreamReader que sirve para leer los datos
            //del archivo que se desea recuperar
            InputStreamReader archivo = new InputStreamReader(fIn);
            //Para este entonces, el objeto fIn tiene tanto la ruta como la lectura de los datos
            //entonces instanciamos un objeto de tipo bufferedReader el cual tiene como parámetro el nombre
            //del archivo que se desea recuperar
            BufferedReader br = new BufferedReader(archivo);
            //Leemos la primera linea del archivo
            String linea = br.readLine();
            //aca se crea la variable en la que se va a guardar el contenido
            String todo = "";
            //mediante el while y siempre y cuando el contenido no esté vacío, se va a leer la siguiente linea
            while (linea != null) {
                //aca vamos concatenando el contenido de la nota
                todo = todo + linea + "";
                //leemos la siguiente linea
                linea = br.readLine();

            }
            //cerramos el bufferedReader y el archivo
            br.close();
            archivo.close();
            //se cambia el valor del multiline por el contenido que se obtuvo mediante el while
            //para la variable todo
            et2.setText(todo);
        } catch (IOException e) {
            //si no se pudo recuperar, mostramos un toast con un mensaje de error
            Toast.makeText(getApplicationContext(), "No se pudo leer", Toast.LENGTH_SHORT).show();
        }
    }
}