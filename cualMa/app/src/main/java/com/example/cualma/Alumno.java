package com.example.cualma;

public class Alumno {
    public String carnet;
    public String nombres;
    public String apellidos;
    public String facultad;
    public String carrera;

    public Alumno(String carnet, String nombres, String apellidos,
                  String facultad, String carrera) {
        this.carnet = carnet;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.facultad = facultad;
        this.carrera = carrera;
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
}
