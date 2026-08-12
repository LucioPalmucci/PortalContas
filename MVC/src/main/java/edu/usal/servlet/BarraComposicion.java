package edu.usal.servlet;

//Clase de presentacion: una fila del grafico de barras horizontales de composicion de gastos por categoria.
public class BarraComposicion {

    private final String nombreCategoria;
    private final double porcentaje;
    private final String valorFormateado;
    private final String colorCss; // referencia a una variable --serie-N del sistema de diseno

    public BarraComposicion(String nombreCategoria, double porcentaje, String valorFormateado, String colorCss) {
        this.nombreCategoria = nombreCategoria;
        this.porcentaje = porcentaje;
        this.valorFormateado = valorFormateado;
        this.colorCss = colorCss;
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public double getPorcentaje() { return porcentaje; }
    public String getValorFormateado() { return valorFormateado; }
    public String getColorCss() { return colorCss; }
}
