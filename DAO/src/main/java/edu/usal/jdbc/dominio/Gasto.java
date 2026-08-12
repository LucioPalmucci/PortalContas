package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Gasto")
public class Gasto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gasto_id")
    private int idGasto;
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

    public Gasto() {}

    public Gasto(Usuario usuario, Date fecha, String descripcion, EstadoPago estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public Gasto(int idGasto, Usuario usuario, Date fecha, String descripcion, EstadoPago estado, CategoriaConcepto categoria, double valor, MetodoOperacion metodo) {
        this.idGasto = idGasto;
        this.usuario = usuario;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.valor = valor;
        this.metodo = metodo;
    }

    public int getIdGasto() { return idGasto; }
    public void setIdGasto(int idGasto) { this.idGasto = idGasto; }

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
        return "Gasto{" +
                "idGasto=" + idGasto +
                ", fecha=" + fecha +
                ", estado=" + estado +
                ", valor=" + valor +
                '}';
    }
}
