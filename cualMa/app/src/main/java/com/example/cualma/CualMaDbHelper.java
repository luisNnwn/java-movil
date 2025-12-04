package com.example.cualma;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class CualMaDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "cualma.db";
    private static final int DB_VERSION = 6;

    // TABLA ALUMNOS
    public static final String T_ALUMNOS = "alumnos";
    public static final String COL_ALU_CARNET = "carnet";
    public static final String COL_ALU_NOMBRES = "nombres";
    public static final String COL_ALU_APELLIDOS = "apellidos";
    public static final String COL_ALU_FACULTAD = "facultad";
    public static final String COL_ALU_CARRERA = "carrera";

    // TABLA MATERIAS
    public static final String T_MATERIAS = "materias";
    public static final String COL_MAT_CODIGO = "codigo";
    public static final String COL_MAT_NOMBRE = "nombre";
    public static final String COL_MAT_FACULTAD = "facultad";
    public static final String COL_MAT_AULA = "aula";
    public static final String COL_MAT_DIAS = "dias";
    public static final String COL_MAT_HORA_INICIO = "hora_inicio";
    public static final String COL_MAT_HORA_FIN = "hora_fin";

    // TABLA DOCENTES
    public static final String T_DOCENTES = "docentes";
    public static final String COL_DOC_ID = "id";
    public static final String COL_DOC_NOMBRES = "nombres";
    public static final String COL_DOC_APELLIDOS = "apellidos";
    public static final String COL_DOC_FACULTAD = "facultad";
    public static final String COL_DOC_MATERIAS = "materias";


    public CualMaDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // ALUMNOS
        db.execSQL("CREATE TABLE " + T_ALUMNOS + " (" +
                COL_ALU_CARNET + " TEXT PRIMARY KEY," +
                COL_ALU_NOMBRES + " TEXT NOT NULL," +
                COL_ALU_APELLIDOS + " TEXT NOT NULL," +
                COL_ALU_FACULTAD + " TEXT NOT NULL," +
                COL_ALU_CARRERA + " TEXT NOT NULL" +
                ");");

        // MATERIAS
        db.execSQL("CREATE TABLE " + T_MATERIAS + " (" +
                COL_MAT_CODIGO + " TEXT PRIMARY KEY," +
                COL_MAT_NOMBRE + " TEXT NOT NULL," +
                COL_MAT_FACULTAD + " TEXT NOT NULL," +
                COL_MAT_AULA + " TEXT NOT NULL," +
                COL_MAT_DIAS + " TEXT," +
                COL_MAT_HORA_INICIO + " TEXT," +
                COL_MAT_HORA_FIN + " TEXT" +
                ");");

        db.execSQL("CREATE TABLE " + T_DOCENTES + " (" +
                COL_DOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_DOC_NOMBRES + " TEXT NOT NULL," +
                COL_DOC_APELLIDOS + " TEXT NOT NULL," +
                COL_DOC_FACULTAD + " TEXT NOT NULL," +
                COL_DOC_MATERIAS + " TEXT" +     // NUEVA COLUMNA
                ");");

        // TABLA MATERIAS DEL ALUMNO
        db.execSQL("CREATE TABLE alumno_materias (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "carnet_alumno TEXT NOT NULL," +
                "codigo_materia TEXT NOT NULL," +
                "FOREIGN KEY(carnet_alumno) REFERENCES alumnos(carnet)," +
                "FOREIGN KEY(codigo_materia) REFERENCES materias(codigo)" +
                ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Durante desarrollo: resetear tablas
        db.execSQL("DROP TABLE IF EXISTS " + T_DOCENTES);
        db.execSQL("DROP TABLE IF EXISTS " + T_MATERIAS);
        db.execSQL("DROP TABLE IF EXISTS " + T_ALUMNOS);
        onCreate(db);
    }

    // ================= ALUMNOS =================

    public int actualizarAlumno(Alumno alumno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ALU_NOMBRES, alumno.nombres);
        values.put(COL_ALU_APELLIDOS, alumno.apellidos);
        values.put(COL_ALU_FACULTAD, alumno.facultad);
        values.put(COL_ALU_CARRERA, alumno.carrera);

        return db.update(
                T_ALUMNOS,
                values,
                COL_ALU_CARNET + " = ?",
                new String[]{ alumno.carnet }
        );
    }

    public List<Alumno> obtenerTodosAlumnos() {
        List<Alumno> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                T_ALUMNOS,
                new String[]{
                        COL_ALU_CARNET,
                        COL_ALU_NOMBRES,
                        COL_ALU_APELLIDOS,
                        COL_ALU_FACULTAD,
                        COL_ALU_CARRERA
                },
                null,
                null,
                null,
                null,
                COL_ALU_CARNET + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String carnet    = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_CARNET));
                String nombres   = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_NOMBRES));
                String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_APELLIDOS));
                String facultad  = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_FACULTAD));
                String carrera   = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_CARRERA));

                lista.add(new Alumno(carnet, nombres, apellidos, facultad, carrera));
            }
            cursor.close();
        }

        return lista;
    }

    public int eliminarAlumno(String carnet) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                T_ALUMNOS,
                COL_ALU_CARNET + " = ?",
                new String[]{ carnet }
        );
    }


    public long insertarAlumno(Alumno alumno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ALU_CARNET, alumno.getCarnet());
        values.put(COL_ALU_NOMBRES, alumno.getNombres());
        values.put(COL_ALU_APELLIDOS, alumno.getApellidos());
        values.put(COL_ALU_FACULTAD, alumno.getFacultad());
        values.put(COL_ALU_CARRERA, alumno.getCarrera());
        return db.insert(T_ALUMNOS, null, values);
    }

    public Alumno obtenerAlumnoPorCarnet(String carnetBuscado) {
        SQLiteDatabase db = getReadableDatabase();
        Alumno alumno = null;

        Cursor cursor = db.query(
                T_ALUMNOS,
                new String[]{
                        COL_ALU_CARNET,
                        COL_ALU_NOMBRES,
                        COL_ALU_APELLIDOS,
                        COL_ALU_FACULTAD,
                        COL_ALU_CARRERA
                },
                COL_ALU_CARNET + " = ?",
                new String[]{carnetBuscado},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String carnet    = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_CARNET));
                String nombres   = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_NOMBRES));
                String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_APELLIDOS));
                String facultad  = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_FACULTAD));
                String carrera   = cursor.getString(cursor.getColumnIndexOrThrow(COL_ALU_CARRERA));
                alumno = new Alumno(carnet, nombres, apellidos, facultad, carrera);
            }
            cursor.close();
        }

        return alumno;
    }

    // ================= MATERIAS =================

    public long insertarMateria(Materia materia) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MAT_CODIGO, materia.codigo);
        values.put(COL_MAT_NOMBRE, materia.nombre);
        values.put(COL_MAT_FACULTAD, materia.facultad);
        values.put(COL_MAT_AULA, materia.aula);
        values.put(COL_MAT_DIAS, materia.dias);
        values.put(COL_MAT_HORA_INICIO, materia.horaInicio);
        values.put(COL_MAT_HORA_FIN, materia.horaFin);
        return db.insert(T_MATERIAS, null, values);
    }

    public int actualizarMateria(Materia materia) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_MAT_NOMBRE, materia.nombre);
        values.put(COL_MAT_FACULTAD, materia.facultad);
        values.put(COL_MAT_AULA, materia.aula);
        values.put(COL_MAT_DIAS, materia.dias);
        values.put(COL_MAT_HORA_INICIO, materia.horaInicio);
        values.put(COL_MAT_HORA_FIN, materia.horaFin);

        return db.update(
                T_MATERIAS,
                values,
                COL_MAT_CODIGO + " = ?",
                new String[]{materia.codigo}
        );
    }

    public int eliminarMateria(String codigo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                T_MATERIAS,
                COL_MAT_CODIGO + " = ?",
                new String[]{codigo}
        );
    }

    public Materia obtenerMateriaPorCodigo(String codigoBuscado) {
        SQLiteDatabase db = getReadableDatabase();
        Materia materia = null;

        Cursor cursor = db.query(
                T_MATERIAS,
                null,
                COL_MAT_CODIGO + " = ?",
                new String[]{codigoBuscado},
                null,
                null,
                null
        );

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String codigo   = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_CODIGO));
                String nombre   = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_NOMBRE));
                String facultad = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_FACULTAD));
                String aula     = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_AULA));
                String dias     = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_DIAS));
                String horaIni  = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_HORA_INICIO));
                String horaFin  = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_HORA_FIN));
                materia = new Materia(codigo, nombre, facultad, aula, dias, horaIni, horaFin);
            }
            cursor.close();
        }

        return materia;
    }

    public List<Materia> obtenerMaterias() {
        List<Materia> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                T_MATERIAS,
                null,
                null,
                null,
                null,
                null,
                COL_MAT_NOMBRE + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String codigo   = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_CODIGO));
                String nombre   = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_NOMBRE));
                String facultad = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_FACULTAD));
                String aula     = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_AULA));
                String dias     = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_DIAS));
                String horaIni  = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_HORA_INICIO));
                String horaFin  = cursor.getString(cursor.getColumnIndexOrThrow(COL_MAT_HORA_FIN));
                lista.add(new Materia(codigo, nombre, facultad, aula, dias, horaIni, horaFin));
            }
            cursor.close();
        }

        return lista;
    }

    // ================= DOCENTES =================



    // INSERTAR
    public long insertarDocente(Docente docente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DOC_NOMBRES, docente.nombres);
        values.put(COL_DOC_APELLIDOS, docente.apellidos);
        values.put(COL_DOC_FACULTAD, docente.facultad);
        values.put(COL_DOC_MATERIAS, docente.materias); // NUEVO
        return db.insert(T_DOCENTES, null, values);
    }

    // ACTUALIZAR
    public int actualizarDocente(Docente docente) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_DOC_NOMBRES, docente.nombres);
        values.put(COL_DOC_APELLIDOS, docente.apellidos);
        values.put(COL_DOC_FACULTAD, docente.facultad);
        values.put(COL_DOC_MATERIAS, docente.materias);

        return db.update(
                T_DOCENTES,
                values,
                COL_DOC_ID + " = ?",
                new String[]{String.valueOf(docente.id)}
        );
    }

    // ELIMINAR
    public int eliminarDocente(int id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                T_DOCENTES,
                COL_DOC_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    // OBTENER TODOS
    public List<Docente> obtenerTodosDocentes() {
        List<Docente> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                T_DOCENTES,
                new String[]{
                        COL_DOC_ID,
                        COL_DOC_NOMBRES,
                        COL_DOC_APELLIDOS,
                        COL_DOC_FACULTAD,
                        COL_DOC_MATERIAS
                },
                null,
                null,
                null,
                null,
                COL_DOC_NOMBRES + " ASC"
        );

        if (c != null) {
            while (c.moveToNext()) {
                int id           = c.getInt(c.getColumnIndexOrThrow(COL_DOC_ID));
                String nombres   = c.getString(c.getColumnIndexOrThrow(COL_DOC_NOMBRES));
                String apellidos = c.getString(c.getColumnIndexOrThrow(COL_DOC_APELLIDOS));
                String facultad  = c.getString(c.getColumnIndexOrThrow(COL_DOC_FACULTAD));
                String materias  = c.getString(c.getColumnIndexOrThrow(COL_DOC_MATERIAS));

                lista.add(new Docente(id, nombres, apellidos, facultad, materias));
            }
            c.close();
        }

        return lista;
    }

    // ================= MATERIAS DE ALUMNO =================

    public long agregarMateriaAAlumno(String carnetAlumno, String codigoMateria) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("carnet_alumno", carnetAlumno);
        values.put("codigo_materia", codigoMateria);
        return db.insert("alumno_materias", null, values);
    }

    public int eliminarMateriaDeAlumno(String carnetAlumno, String codigoMateria) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(
                "alumno_materias",
                "carnet_alumno = ? AND codigo_materia = ?",
                new String[]{carnetAlumno, codigoMateria}
        );
    }

    public List<Materia> obtenerMateriasAlumno(String carnetAlumno) {
        List<Materia> lista = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT m.* FROM materias m INNER JOIN alumno_materias am ON m.codigo = am.codigo_materia WHERE am.carnet_alumno = ?",
                new String[]{carnetAlumno}
        );

        if (c != null) {
            while (c.moveToNext()) {
                String codigo   = c.getString(c.getColumnIndexOrThrow(COL_MAT_CODIGO));
                String nombre   = c.getString(c.getColumnIndexOrThrow(COL_MAT_NOMBRE));
                String facultad = c.getString(c.getColumnIndexOrThrow(COL_MAT_FACULTAD));
                String aula     = c.getString(c.getColumnIndexOrThrow(COL_MAT_AULA));
                String dias     = c.getString(c.getColumnIndexOrThrow(COL_MAT_DIAS));
                String horaIni  = c.getString(c.getColumnIndexOrThrow(COL_MAT_HORA_INICIO));
                String horaFin  = c.getString(c.getColumnIndexOrThrow(COL_MAT_HORA_FIN));

                lista.add(new Materia(codigo, nombre, facultad, aula, dias, horaIni, horaFin));
            }
            c.close();
        }

        return lista;
    }



}
