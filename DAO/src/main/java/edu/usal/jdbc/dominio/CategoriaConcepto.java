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
    private CategoriaAplicaA aplicaA;
    @Column(name = "esta_activo", nullable = false, columnDefinition = "boolean not null default true")
    private boolean estaActivo = true;

    public CategoriaConcepto() {}

    public CategoriaConcepto(String nombre, CategoriaAplicaA categoriaAplicaA) {
        this.nombre = nombre;
        this.aplicaA = categoriaAplicaA;
    }

    public CategoriaConcepto(int idCategoria, String nombre, CategoriaAplicaA categoriaAplicaA) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.aplicaA = categoriaAplicaA;
    }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public CategoriaAplicaA getAplicaA() { return aplicaA; }
    public void setAplicaA(CategoriaAplicaA categoriaAplicaA) { this.aplicaA = categoriaAplicaA; }

    public boolean isEstaActivo() { return estaActivo; }
    public void setEstaActivo(boolean estaActivo) { this.estaActivo = estaActivo; }

    @Override
    public String toString() {
        return "CategoriaConcepto{" +
                "idCategoria=" + idCategoria +
                ", nombre='" + nombre + '\'' +
                ", aplicaA=" + aplicaA +
                '}';
    }
}
