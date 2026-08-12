package edu.usal.servlet;

//Clase de presentacion: una barra de un grafico de barras divergente (valores positivos y negativos) renderizado en CSS puro.
public class PuntoBarra {

    private final String etiqueta;
    private final double alturaPorcentaje; // 0-100, relativo al valor absoluto maximo de la serie
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
