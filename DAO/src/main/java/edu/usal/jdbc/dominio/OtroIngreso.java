package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "OtroIngreso")
public class OtroIngreso implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otro_ingreso_id")
    private int idOtroIngreso;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private CategoriaConcepto categoria;
    @Column(name = "valor")
    private double valor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metodo_id")
    private MetodoOperacion metodo;

    public OtroIngreso() {}

    public OtroIngreso(Usuario usuario, Date fecha, String descripcion, EstadoCobro estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public OtroIngreso(int idOtroIngreso, Usuario usuario, Date fecha, String descripcion, EstadoCobro estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.idOtroIngreso = idOtroIngreso;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public int getIdOtroIngreso() { return idOtroIngreso; }
    public void setIdOtroIngreso(int idOtroIngreso) { this.idOtroIngreso = idOtroIngreso; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public EstadoCobro getEstado() { return estado; }
    public void setEstado(EstadoCobro estado) { this.estado = estado; }

    public CategoriaConcepto getCategoria() { return categoria; }
    public void setCategoria(CategoriaConcepto categoria) { this.categoria = categoria; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public MetodoOperacion getMetodo() { return metodo; }
    public void setMetodo(MetodoOperacion metodo) { this.metodo = metodo; }

    @Override
    public String toString() {
        return "OtroIngreso{" +
                "idOtroIngreso=" + idOtroIngreso +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", valor=" + valor +
                '}';
    }
}
