package com.example.Entidades;

import java.time.LocalDate;

public class DiaLaboral {
    private Long id;
    private LocalDate fecha;
    private String descripcion;
    private Calculo calculo;

    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Calculo getCalculo() {
        return calculo;
    }

    public void setCalculo(Calculo id_calculo) {
        this.calculo = id_calculo;
    }
}
