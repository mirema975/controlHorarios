package com.example.Entidades;

import java.time.LocalDateTime;

public class Turno {
    private Long id;
    private LocalDateTime ingreso;
    private LocalDateTime salida;
    private DiaLaboral diaLaboral;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getIngreso() {
        return ingreso;
    }

    public void setIngreso(LocalDateTime ingreso) {
        this.ingreso = ingreso;
    }

    public LocalDateTime getSalida() {
        return salida;
    }

    public void setSalida(LocalDateTime salida) {
        this.salida = salida;
    }

    public DiaLaboral getDiaLaboral() {
        return diaLaboral;
    }

    public void setDiaLaboral(DiaLaboral diaLaboral) {
        this.diaLaboral = diaLaboral;
    }
}
