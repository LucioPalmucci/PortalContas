package edu.usal.jdbc.dto;

public class ComposicionCategoriaDTO {

    private String nombreCategoria;
    private double total;
    private double porcentaje;

    public ComposicionCategoriaDTO() {}

    public ComposicionCategoriaDTO(String nombreCategoria, double total, double porcentaje) {
        this.nombreCategoria = nombreCategoria;
        this.total = total;
        this.porcentaje = porcentaje;
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getPorcentaje() { return porcentaje; }
    public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }
}
