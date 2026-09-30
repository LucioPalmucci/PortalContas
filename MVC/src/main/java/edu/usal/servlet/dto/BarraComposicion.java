package edu.usal.servlet.dto;

public class BarraComposicion {

    private final String nombreCategoria;
    private final double porcentaje;
    private final String valorFormateado;
    private final String colorCss;

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
