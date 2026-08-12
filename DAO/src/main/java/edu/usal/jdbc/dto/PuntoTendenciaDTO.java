package edu.usal.jdbc.dto;

public class PuntoTendenciaDTO {

    private String etiquetaMes;
    private double ventas;
    private double ganancia;

    public PuntoTendenciaDTO() {}

    public PuntoTendenciaDTO(String etiquetaMes, double ventas, double ganancia) {
        this.etiquetaMes = etiquetaMes;
        this.ventas = ventas;
        this.ganancia = ganancia;
    }

    public String getEtiquetaMes() { return etiquetaMes; }
    public void setEtiquetaMes(String etiquetaMes) { this.etiquetaMes = etiquetaMes; }

    public double getVentas() { return ventas; }
    public void setVentas(double ventas) { this.ventas = ventas; }

    public double getGanancia() { return ganancia; }
    public void setGanancia(double ganancia) { this.ganancia = ganancia; }

    public double getRentabilidad() {
        return ventas > 0 ? (ganancia / ventas) * 100.0 : 0.0;
    }
}
