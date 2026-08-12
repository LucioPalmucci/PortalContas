package edu.usal.jdbc.dto;

import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.dominio.Venta;

import java.util.Date;
import java.util.List;

//Objeto de transporte de solo lectura: no es una entidad, se arma en el momento a partir de Venta/Compra/Gasto/OtroIngreso/OtroEgreso pendientes.
public class TesoreriaDTO {

    private double cuentasPorCobrar;
    private double cuentasPorPagar;
    private double saldoNeto;
    private List<Venta> ventasPendientes;
    private List<Compra> comprasPendientes;
    private List<Gasto> gastosPendientes;
    private List<OtroIngreso> otrosIngresosPendientes;
    private List<OtroEgreso> otrosEgresosPendientes;

    // Resumen del periodo seleccionado (11.1.1 / 11.3): subtotales agrupados por estado, independientes de los pendientes "a hoy" de arriba.
    private Date periodoInicio;
    private Date periodoFin;
    private double totalCobradoPeriodo;
    private double totalPendienteCobroPeriodo;
    private double totalPagadoPeriodo;
    private double totalPendientePagoPeriodo;

    public TesoreriaDTO() {}

    public TesoreriaDTO(double cuentasPorCobrar, double cuentasPorPagar, double saldoNeto, List<Venta> ventasPendientes, List<Compra> comprasPendientes, List<Gasto> gastosPendientes, List<OtroIngreso> otrosIngresosPendientes, List<OtroEgreso> otrosEgresosPendientes) {
        this.cuentasPorCobrar = cuentasPorCobrar;
        this.cuentasPorPagar = cuentasPorPagar;
        this.saldoNeto = saldoNeto;
        this.ventasPendientes = ventasPendientes;
        this.comprasPendientes = comprasPendientes;
        this.gastosPendientes = gastosPendientes;
        this.otrosIngresosPendientes = otrosIngresosPendientes;
        this.otrosEgresosPendientes = otrosEgresosPendientes;
    }

    public double getCuentasPorCobrar() { return cuentasPorCobrar; }
    public void setCuentasPorCobrar(double cuentasPorCobrar) { this.cuentasPorCobrar = cuentasPorCobrar; }

    public double getCuentasPorPagar() { return cuentasPorPagar; }
    public void setCuentasPorPagar(double cuentasPorPagar) { this.cuentasPorPagar = cuentasPorPagar; }

    public double getSaldoNeto() { return saldoNeto; }
    public void setSaldoNeto(double saldoNeto) { this.saldoNeto = saldoNeto; }

    public List<Venta> getVentasPendientes() { return ventasPendientes; }
    public void setVentasPendientes(List<Venta> ventasPendientes) { this.ventasPendientes = ventasPendientes; }

    public List<Compra> getComprasPendientes() { return comprasPendientes; }
    public void setComprasPendientes(List<Compra> comprasPendientes) { this.comprasPendientes = comprasPendientes; }

    public List<Gasto> getGastosPendientes() { return gastosPendientes; }
    public void setGastosPendientes(List<Gasto> gastosPendientes) { this.gastosPendientes = gastosPendientes; }

    public List<OtroIngreso> getOtrosIngresosPendientes() { return otrosIngresosPendientes; }
    public void setOtrosIngresosPendientes(List<OtroIngreso> otrosIngresosPendientes) { this.otrosIngresosPendientes = otrosIngresosPendientes; }

    public List<OtroEgreso> getOtrosEgresosPendientes() { return otrosEgresosPendientes; }
    public void setOtrosEgresosPendientes(List<OtroEgreso> otrosEgresosPendientes) { this.otrosEgresosPendientes = otrosEgresosPendientes; }

    public Date getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(Date periodoInicio) { this.periodoInicio = periodoInicio; }

    public Date getPeriodoFin() { return periodoFin; }
    public void setPeriodoFin(Date periodoFin) { this.periodoFin = periodoFin; }

    public double getTotalCobradoPeriodo() { return totalCobradoPeriodo; }
    public void setTotalCobradoPeriodo(double totalCobradoPeriodo) { this.totalCobradoPeriodo = totalCobradoPeriodo; }

    public double getTotalPendienteCobroPeriodo() { return totalPendienteCobroPeriodo; }
    public void setTotalPendienteCobroPeriodo(double totalPendienteCobroPeriodo) { this.totalPendienteCobroPeriodo = totalPendienteCobroPeriodo; }

    public double getTotalPagadoPeriodo() { return totalPagadoPeriodo; }
    public void setTotalPagadoPeriodo(double totalPagadoPeriodo) { this.totalPagadoPeriodo = totalPagadoPeriodo; }

    public double getTotalPendientePagoPeriodo() { return totalPendientePagoPeriodo; }
    public void setTotalPendientePagoPeriodo(double totalPendientePagoPeriodo) { this.totalPendientePagoPeriodo = totalPendientePagoPeriodo; }
}
