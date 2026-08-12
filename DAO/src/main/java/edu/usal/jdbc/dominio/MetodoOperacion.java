package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "MetodoOperacion")
public class MetodoOperacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metodo_id")
    private int idMetodo;
    @Column(name = "nombre")
    private String nombre;
    @Enumerated(EnumType.STRING)
    @Column(name = "cobro_o_pago")
    private CobroOPago cobroOPago;

    public MetodoOperacion() {}

    public MetodoOperacion(String nombre, CobroOPago cobroOPago) {
        this.nombre = nombre;
        this.cobroOPago = cobroOPago;
    }

    public MetodoOperacion(int idMetodo, String nombre, CobroOPago cobroOPago) {
        this.idMetodo = idMetodo;
        this.nombre = nombre;
        this.cobroOPago = cobroOPago;
    }

    public int getIdMetodo() { return idMetodo; }
    public void setIdMetodo(int idMetodo) { this.idMetodo = idMetodo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public CobroOPago getCobroOPago() { return cobroOPago; }
    public void setCobroOPago(CobroOPago cobroOPago) { this.cobroOPago = cobroOPago; }

    @Override
    public String toString() {
        return "MetodoOperacion{" +
                "idMetodo=" + idMetodo +
                ", nombre='" + nombre + '\'' +
                ", cobroOPago=" + cobroOPago +
                '}';
    }
}
