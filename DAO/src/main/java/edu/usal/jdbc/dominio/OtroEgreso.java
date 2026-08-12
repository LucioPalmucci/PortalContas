package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "OtroEgreso")
public class OtroEgreso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otro_egreso_id")
    private int idOtroEgreso;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private CategoriaConcepto categoria;
    @Column(name = "valor")
    private double valor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metodo_id")
    private MetodoOperacion metodo;

    public OtroEgreso() {}

    public OtroEgreso(Usuario usuario, Date fecha, String descripcion, EstadoPago estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public OtroEgreso(int idOtroEgreso, Usuario usuario, Date fecha, String descripcion, EstadoPago estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.idOtroEgreso = idOtroEgreso;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public int getIdOtroEgreso() { return idOtroEgreso; }
    public void setIdOtroEgreso(int idOtroEgreso) { this.idOtroEgreso = idOtroEgreso; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public CategoriaConcepto getCategoria() { return categoria; }
    public void setCategoria(CategoriaConcepto categoria) { this.categoria = categoria; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public MetodoOperacion getMetodo() { return metodo; }
    public void setMetodo(MetodoOperacion metodo) { this.metodo = metodo; }

    @Override
    public String toString() {
        return "OtroEgreso{" +
                "idOtroEgreso=" + idOtroEgreso +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", valor=" + valor +
                '}';
    }
}
