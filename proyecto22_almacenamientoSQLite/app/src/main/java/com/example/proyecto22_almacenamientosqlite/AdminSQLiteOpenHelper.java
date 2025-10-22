package com.example.proyecto22_almacenamientosqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;



/*Esta clase nos sirve como puente entre la aplicación y la bd
* Aca definimos una tabla llamada articulos que continene
* codigo del articulo, descripcion y precio*/
public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {


    //constructor de la clase con el contexto de la aplicacion, el nombre de la bd, el cursor y la version
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //metodo para crear la tabla articulos con tres columnas: codigo, descripcion y precio
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creamos la tabla articulos con tres columnas: codigo, descripcion y precio
        db.execSQL("create table articulos(codigo int primary key, description text, precio real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
