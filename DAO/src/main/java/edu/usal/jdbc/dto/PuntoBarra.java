package edu.usal.jdbc.dto;

public class PuntoBarra {

    private final String etiqueta;
    private final double alturaPorcentaje;
    private final boolean positivo;
    private final String valorFormateado;

    public PuntoBarra(String etiqueta, double alturaPorcentaje, boolean positivo, String valorFormateado) {
        this.etiqueta = etiqueta;
        this.alturaPorcentaje = alturaPorcentaje;
        this.positivo = positivo;
        this.valorFormateado = valorFormateado;
    }

    public String getEtiqueta() { return etiqueta; }
    public double getAlturaPorcentaje() { return alturaPorcentaje; }
    public boolean isPositivo() { return positivo; }
    public String getValorFormateado() { return valorFormateado; }
}
