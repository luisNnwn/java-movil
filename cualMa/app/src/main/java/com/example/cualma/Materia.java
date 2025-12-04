package com.example.cualma;

public class Materia {
    public String codigo;
    public String nombre;
    public String facultad;
    public String aula;
    public String dias;
    public String horaInicio;
    public String horaFin;

    public Materia(String codigo, String nombre, String facultad,
                   String aula, String dias, String horaInicio, String horaFin) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.facultad = facultad;
        this.aula = aula;
        this.dias = dias;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }
}
