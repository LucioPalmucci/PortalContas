package edu.usal.jdbc.dto;

public class ComparacionPeriodoDTO {

    private EstadoResultadosDTO periodo1;
    private EstadoResultadosDTO periodo2;
    private double variacionVentas;
    private double variacionGanancia;

    public ComparacionPeriodoDTO() {}

    public ComparacionPeriodoDTO(EstadoResultadosDTO periodo1, EstadoResultadosDTO periodo2, double variacionVentas, double variacionGanancia) {
        this.periodo1 = periodo1;
        this.periodo2 = periodo2;
        this.variacionVentas = variacionVentas;
        this.variacionGanancia = variacionGanancia;
    }

    public EstadoResultadosDTO getPeriodo1() { return periodo1; }
    public void setPeriodo1(EstadoResultadosDTO periodo1) { this.periodo1 = periodo1; }

    public EstadoResultadosDTO getPeriodo2() { return periodo2; }
    public void setPeriodo2(EstadoResultadosDTO periodo2) { this.periodo2 = periodo2; }

    public double getVariacionVentas() { return variacionVentas; }
    public void setVariacionVentas(double variacionVentas) { this.variacionVentas = variacionVentas; }

    public double getVariacionGanancia() { return variacionGanancia; }
    public void setVariacionGanancia(double variacionGanancia) { this.variacionGanancia = variacionGanancia; }
}
