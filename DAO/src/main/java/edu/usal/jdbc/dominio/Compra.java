package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Compra")
public class Compra implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compra_id")
    private int idCompra;
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
    private EstadoPago estado;
    @Column(name = "concepto_compra")
    private String conceptoCompra;
    @Column(name = "precio_unitario")
    private double precioUnitario;
    @Column(name = "cantidad")
    private int cantidad;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metodo_id")
    private MetodoOperacion metodo;
    @Column(name = "valor_total")
    private double valorTotal;

    public Compra() {}

    public Compra(Usuario usuario, Date fecha, String descripcion, EstadoPago estado, String conceptoCompra, double precioUnitario, int cantidad, MetodoOperacion metodo, double valorTotal) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.conceptoCompra = conceptoCompra;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.metodo = metodo;
        this.valorTotal = valorTotal;
    }

    public Compra(int idCompra, Usuario usuario, Date fecha, String descripcion, EstadoPago estado, String conceptoCompra, double precioUnitario, int cantidad, MetodoOperacion metodo, double valorTotal) {
        this.idCompra = idCompra;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.conceptoCompra = conceptoCompra;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.metodo = metodo;
        this.valorTotal = valorTotal;
    }

    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public String getConceptoCompra() { return conceptoCompra; }
    public void setConceptoCompra(String conceptoCompra) { this.conceptoCompra = conceptoCompra; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public MetodoOperacion getMetodo() { return metodo; }
    public void setMetodo(MetodoOperacion metodo) { this.metodo = metodo; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    @Override
    public String toString() {
        return "Compra{" +
                "idCompra=" + idCompra +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", conceptoCompra='" + conceptoCompra + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
