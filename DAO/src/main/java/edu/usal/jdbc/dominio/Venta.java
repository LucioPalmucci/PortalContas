package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Venta")
public class Venta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_id")
    private int idVenta;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;
    @Column(name = "descripcion")
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCobro estado;
    @Column(name = "concepto_venta")
    private String conceptoVenta;
    @Column(name = "precio_unitario")
    private double precioUnitario;
    @Column(name = "cantidad")
    private int cantidad;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metodo_id")
    private MetodoOperacion metodo;
    @Column(name = "subtotal")
    private double subtotal;

    public Venta() {}

    public Venta(Usuario usuario, Date fecha, String descripcion, EstadoCobro estado, String conceptoVenta, double precioUnitario, int cantidad, MetodoOperacion metodo, double subtotal) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.conceptoVenta = conceptoVenta;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.metodo = metodo;
        this.subtotal = subtotal;
    }

    public Venta(int idVenta, Usuario usuario, Date fecha, String descripcion, EstadoCobro estado, String conceptoVenta, double precioUnitario, int cantidad, MetodoOperacion metodo, double subtotal) {
        this.idVenta = idVenta;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.conceptoVenta = conceptoVenta;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.metodo = metodo;
        this.subtotal = subtotal;
    }

    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoCobro getEstado() { return estado; }
    public void setEstado(EstadoCobro estado) { this.estado = estado; }

    public String getConceptoVenta() { return conceptoVenta; }
    public void setConceptoVenta(String conceptoVenta) { this.conceptoVenta = conceptoVenta; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public MetodoOperacion getMetodo() { return metodo; }
    public void setMetodo(MetodoOperacion metodo) { this.metodo = metodo; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", conceptoVenta='" + conceptoVenta + '\'' +
                ", subtotal=" + subtotal +
                '}';
    }
}
