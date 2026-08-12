package edu.usal.jdbc.dto;

import java.util.Date;

//Objeto de transporte de solo lectura: el Estado de Resultados se calcula en el momento, no se persiste.
public class EstadoResultadosDTO {

    private Date periodoInicio;
    private Date periodoFin;
    private double ventasTotal;
    private double cmv;
    private double utilidadBruta;
    private double gastosTotal;
    private double oIngresosTotal;
    private double oEgresosTotal;
    private double ganancia;
    private double margenUtilidad;
    private double rentabilidad;
    private double variacionAnterior;

    public EstadoResultadosDTO() {}

    public Date getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(Date periodoInicio) { this.periodoInicio = periodoInicio; }

    public Date getPeriodoFin() { return periodoFin; }
    public void setPeriodoFin(Date periodoFin) { this.periodoFin = periodoFin; }

    public double getVentasTotal() { return ventasTotal; }
    public void setVentasTotal(double ventasTotal) { this.ventasTotal = ventasTotal; }

    public double getCmv() { return cmv; }
    public void setCmv(double cmv) { this.cmv = cmv; }

    public double getUtilidadBruta() { return utilidadBruta; }
    public void setUtilidadBruta(double utilidadBruta) { this.utilidadBruta = utilidadBruta; }

    public double getGastosTotal() { return gastosTotal; }
    public void setGastosTotal(double gastosTotal) { this.gastosTotal = gastosTotal; }

    public double getoIngresosTotal() { return oIngresosTotal; }
    public void setoIngresosTotal(double oIngresosTotal) { this.oIngresosTotal = oIngresosTotal; }

    public double getoEgresosTotal() { return oEgresosTotal; }
    public void setoEgresosTotal(double oEgresosTotal) { this.oEgresosTotal = oEgresosTotal; }

    public double getGanancia() { return ganancia; }
    public void setGanancia(double ganancia) { this.ganancia = ganancia; }

    public double getMargenUtilidad() { return margenUtilidad; }
    public void setMargenUtilidad(double margenUtilidad) { this.margenUtilidad = margenUtilidad; }

    public double getRentabilidad() { return rentabilidad; }
    public void setRentabilidad(double rentabilidad) { this.rentabilidad = rentabilidad; }

    public double getVariacionAnterior() { return variacionAnterior; }
    public void setVariacionAnterior(double variacionAnterior) { this.variacionAnterior = variacionAnterior; }
}
