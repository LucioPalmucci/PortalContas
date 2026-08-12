package edu.usal.jdbc.dto;

import java.util.List;

public class EstadoFinancieroDTO {

    private List<PuntoTendenciaDTO> tendenciaMensual;
    private List<ComposicionCategoriaDTO> composicionGastos;
    private int mesesPerdida;
    private String mejorMes;
    private String peorMes;
    private double umbralAlerta;
    private List<String> alertas;

    public EstadoFinancieroDTO() {}

    public List<PuntoTendenciaDTO> getTendenciaMensual() { return tendenciaMensual; }
    public void setTendenciaMensual(List<PuntoTendenciaDTO> tendenciaMensual) { this.tendenciaMensual = tendenciaMensual; }

    public List<ComposicionCategoriaDTO> getComposicionGastos() { return composicionGastos; }
    public void setComposicionGastos(List<ComposicionCategoriaDTO> composicionGastos) { this.composicionGastos = composicionGastos; }

    public int getMesesPerdida() { return mesesPerdida; }
    public void setMesesPerdida(int mesesPerdida) { this.mesesPerdida = mesesPerdida; }

    public String getMejorMes() { return mejorMes; }
    public void setMejorMes(String mejorMes) { this.mejorMes = mejorMes; }

    public String getPeorMes() { return peorMes; }
    public void setPeorMes(String peorMes) { this.peorMes = peorMes; }

    public double getUmbralAlerta() { return umbralAlerta; }
    public void setUmbralAlerta(double umbralAlerta) { this.umbralAlerta = umbralAlerta; }

    public List<String> getAlertas() { return alertas; }
    public void setAlertas(List<String> alertas) { this.alertas = alertas; }
}
