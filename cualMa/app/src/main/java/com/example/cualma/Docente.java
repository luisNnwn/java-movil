package com.example.cualma;

public class Docente {

    public int id;
    public String nombres;
    public String apellidos;
    public String facultad;
    public String materias; // NUEVO

    public Docente(int id, String nombres, String apellidos, String facultad, String materias) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.facultad = facultad;
        this.materias = materias;
    }

    public Docente(String nombres, String apellidos, String facultad, String materias) {
        this(-1, nombres, apellidos, facultad, materias);
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
