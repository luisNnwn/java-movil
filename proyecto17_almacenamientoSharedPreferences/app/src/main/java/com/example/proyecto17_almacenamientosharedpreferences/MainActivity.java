package com.example.proyecto17_almacenamientosharedpreferences;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports del proyecto
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//clase principal del programa
public class MainActivity extends AppCompatActivity {

    //declaración de variables de clase
    private EditText et1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //traspaso de variables
        et1 = (EditText) findViewById(R.id.et1);
        //aca se crea un objeto de tipo SharedPreferences: el primer parámetro es el nombre
        //del archivo donde se van a almacenar, el segundo parámetro es el modo de acceso a esos datos
        /*
        * Amapliando sobre esto y para estudiar, los pasos son:
        * 1. Instanciar el objeto SharedPreferences
        * 2. Darle nombre y especificar el modo de acceso
        * Sobre el modo de acceso:
        * 1. MODE_PRIVATE(predeterminado): crea el archivo que solo la app puede leer y modificar
        * 2. MODE_APPEND: abre un archivo que ya existe y lo modifica
        *
        * Existe MODE_WORLD_READABLE y MODE_WORLD_WRITEABLE pero ya son obsoletos desde la api 17, según
        * https://developer.android.com/training/data-storage/shared-preferences?hl=es-419
        * */
        SharedPreferences prefe = getSharedPreferences("datos", Context.MODE_PRIVATE);
        /*Con esto seteamos el texto del EditText, el cual tiene como contenido
        * el objeto de SharedPreferences con el texto, que en este caso es el mail, el cual
        * dentro del xml tiene por nombre mail como tal, o sea que el metodo getString
        * se trae el String con ese nombre y lo pone en el EditText.
        *
        * Y se define el valor por defecto como un string vacío
        *
        * <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
        *    <map>
        *        <string name="mail">luisnblanh.z@gmail.com</string>
        *    </map>
        * */
        et1.setText(prefe.getString("mail", ""));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /*Nota: el archivo se crea la primera vez que se hace commit*/
    //Este es el metodo que se ejecuta al presionar el boton de guardar
    public void ejecutar (View view) {
       /*Acá creamos otro objeto de tipo SharedPreferences, pero en este caso
       * con el nombre "datos" y el debido contexto*/
       SharedPreferences preferencias = getSharedPreferences("datos", Context.MODE_PRIVATE);
       //Ahora creamos un objeto de tipo Editor para poder editar el archivo
        //mediante llamar a la instancia preferencias iniciamos con edit()
        //el ciclo de edicicón
       SharedPreferences.Editor editor = preferencias.edit();
       //ahora el objeto editor mediante el metodo putString() se le asigna
        //y mediante la clave "mail" se guarda el texto del EditText
       editor.putString("mail", et1.getText().toString());
       //envia los cambios al archivo, mediante el metodo commit()
       editor.commit();
       //se cierra la actividad
       finish();
    }
}