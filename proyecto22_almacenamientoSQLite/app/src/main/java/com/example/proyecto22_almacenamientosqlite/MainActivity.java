package com.example.proyecto22_almacenamientosqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
//imports del ejercicio
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;


//clase principal de la aplicación, esencial para que el usuario
//pueda interactuar con la aplicación gestionando una base de datos local
public class MainActivity extends AppCompatActivity {

    //variables de clase
    private EditText et1, et2, et3;

    //constructor de la clase
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //relación entre el la clase y los elementos del layout
        setContentView(R.layout.activity_main);

        //traspaso de variables mediante findviewbyid
        et1 = (EditText) findViewById(R.id.et1);
        et2 = (EditText) findViewById(R.id.et2);
        et3 = (EditText) findViewById(R.id.et3);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //metodos para los botones
    public void alta (View v) {
        /*Acá instanciamos el objeto admin de tipo SQLiteOpenHelper, le pasamos
        * el contexto de la app, el nombre de la base de datos y la versión de la misma*/
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //se crea la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        //creamos las variables de tipo string donde vamos a ir guardando
        //los datos que ingrese el usuario en los editText
        String cod = et1.getText().toString();
        String descri = et2.getText().toString();
        String pre = et3.getText().toString();
        //aca instanciamos un objeto de tipo content values, llamado registro
        //aca este objeto lo que hace es agrupar datos de diferentes tipos
        //como cadenas, enteros, booleanos y pasarlos a métodos para insertar
        //en la base de datos
        ContentValues registro = new ContentValues();
        //aca se guardan los datos en el objeto registro
        registro.put("codigo", cod);
        registro.put("description", descri);
        registro.put("precio", pre);
        //aca se inserta la información en la base de datos
        bd.insert("articulos",null, registro);
        //cerramos la base de datos
        bd.close();
        //limpiamos los editText
        et1.setText("");
        et2.setText("");
        et3.setText("");
        Toast.makeText(getApplicationContext(), "Se cargaron los datos del artículo", Toast.LENGTH_SHORT).show();
    }

    public void consultarporcodigo (View v) {
        //aca instanciamos el objeto admin de tipo SQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //se crea abre la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        //creamos una variable donde se va a guardar el codigo ingresado
        String cod = et1.getText().toString();
        //creamos un objeto de tipo cursor llamdo fila, que va a contener
        //el objeto bd que contiene la consulta de la base de datos
        Cursor fila = bd.rawQuery("select description, precio from articulos where codigo=" + cod, null);
        //si la fila contiene datos se ejecuta el if
        if (fila.moveToFirst()) {
            //se guardan los datos en los editText
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        } else {
            //si no se ejecuta el else
            Toast.makeText (this, "No existe un articulo con dicho codigo", Toast.LENGTH_SHORT).show();
            bd.close();
        }
    }

    //metodo para consultar por descripcion
    public void consultapordescripcion(View v) {
        //aca instanciamos el objeto admin de tipo SQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //se crea abre la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        //creamos una variable donde se va a guardar el codigo ingresado
        String descri = et2.getText().toString();
        //creamos un objeto de tipo cursor llamdo fila, que va a contener
        //el objeto bd que contiene la consulta de la base de datos
        Cursor fila = bd.rawQuery("select codigo, precio from articulos where description='" + descri + "'", null);
        //si la fila contiene datos se ejecuta el if
        if (fila.moveToFirst()) {
            //se guardan los datos en los editText
            et1.setText(fila.getString(0));
            et3.setText(fila.getString(1));
        } else {
            //si no se ejecuta el else
            Toast.makeText(this, "No existe un artículo con dicha descripción", Toast.LENGTH_SHORT).show();
        }
        //cerramos la base de datos
        fila.close();
        bd.close();
    }

    public void bajaporcodigo (View v) {
        //aca instanciamos el objeto admin de tipo SQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //se crea abre la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        //creamos una variable donde se va a guardar el codigo ingresado
        String cod = et1.getText().toString();
        //aca se borra el articulo con el codigo ingresado
        int cant = bd.delete("articulos", "codigo=" + cod, null);
        //cerramos la base de datos
        bd.close();
        //limpiamos los editText
        et1.setText("");
        et2.setText("");
        et3.setText("");
        //si la variable cant contiene 1 se ejecuta el if
        //estp quiere decir que se borró un articulo
        if (cant == 1) {
            Toast.makeText(this, "Se borró el artículo con dicho código", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para modificar
    public void modificacion (View v) {
        //aca instanciamos el objeto admin de tipo SQLiteOpenHelper
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        //se crea abre la base de datos
        SQLiteDatabase bd = admin.getWritableDatabase();
        //creamos una variable donde se va a guardar el codigo ingresado, la descripcion y precio
        String cod = et1.getText().toString();
        String descri = et2.getText().toString();
        String pre = et3.getText().toString();
        //aca se modifica el articulo con el codigo ingresado
        //creando un objeto llamado registro de tipo content values
        ContentValues registro = new ContentValues();
        //y con el metodo put se guardan los datos en el objeto registro
        //y se modifican los datos de acuerdo al campo que se haya modificado
        registro.put("codigo", cod);
        registro.put("description", descri);
        registro.put("precio", pre);
        //aca se modifica el articulo con el codigo ingresado
        int cant = bd.update("articulos", registro, "codigo=" + cod, null);
        //cerramos la base de datos
        bd.close();
        if (cant == 1){
            Toast.makeText(this, "Se modificaron los datos del artículo", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No existe un artículo con dicho código", Toast.LENGTH_SHORT).show();
        }
    }
}