package edu.usal.servlet;

import edu.usal.jdbc.dominio.Vencimiento;

import java.util.List;

//Clase de presentacion (no es dominio ni DTO de negocio): una celda del calendario mensual de vencimientos.
public class CeldaCalendario {

    private final int dia;
    private final boolean vacia;
    private final List<Vencimiento> vencimientos;

    public CeldaCalendario(int dia, boolean vacia, List<Vencimiento> vencimientos) {
        this.dia = dia;
        this.vacia = vacia;
        this.vencimientos = vencimientos;
    }

    public int getDia() { return dia; }
    public boolean isVacia() { return vacia; }
    public List<Vencimiento> getVencimientos() { return vencimientos; }
}
