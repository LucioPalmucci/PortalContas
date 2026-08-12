package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "CategoriaConcepto")
public class CategoriaConcepto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private int idCategoria;
    @Column(name = "nombre")
    private String nombre;
    @Enumerated(EnumType.STRING)
    @Column(name = "aplica_a")
    private AplicaA aplicaA;

    public CategoriaConcepto() {}

    public CategoriaConcepto(String nombre, AplicaA aplicaA) {
        this.nombre = nombre;
        this.aplicaA = aplicaA;
    }

    public CategoriaConcepto(int idCategoria, String nombre, AplicaA aplicaA) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.aplicaA = aplicaA;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public AplicaA getAplicaA() { return aplicaA; }
    public void setAplicaA(AplicaA aplicaA) { this.aplicaA = aplicaA; }

    @Override
    public String toString() {
        return "CategoriaConcepto{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", aplicaA=" + aplicaA +
                '}';
    }
}
