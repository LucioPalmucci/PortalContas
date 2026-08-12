package edu.usal.servlet;

import java.util.Date;

// Fila de presentacion para las tablas de "movimientos incluidos/excluidos" del Estado de Resultados.
public class MovimientoResumen {

    private final String origen;
    private final Date fecha;
    private final String concepto;
    private final double monto;

    public MovimientoResumen(String origen, Date fecha, String concepto, double monto) {
        this.origen = origen;
        this.fecha = fecha;
        this.concepto = concepto;
        this.monto = monto;
    }

    public String getOrigen() { return origen; }
    public Date getFecha() { return fecha; }
    public String getConcepto() { return concepto; }
    public double getMonto() { return monto; }
}
