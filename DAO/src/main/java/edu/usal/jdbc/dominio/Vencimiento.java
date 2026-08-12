package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Vencimiento")
public class Vencimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vencimiento_id")
    private int idVencimiento;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;
    @Column(name = "ultimos_digitos_cuit")
    private String ultimosDigitosCuit;
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @Column(name = "impuesto_pagar")
    private String impuestoPagar;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoVencimiento estado;

    public Vencimiento() {}

    public Vencimiento(Date fecha, String ultimosDigitosCuit, String nombreCliente, String impuestoPagar, EstadoVencimiento estado) {
        this.fecha = fecha;
        this.ultimosDigitosCuit = ultimosDigitosCuit;
        this.nombreCliente = nombreCliente;
        this.impuestoPagar = impuestoPagar;
        this.estado = estado;
    }

    public Vencimiento(int idVencimiento, Date fecha, String ultimosDigitosCuit, String nombreCliente, String impuestoPagar, EstadoVencimiento estado) {
        this.idVencimiento = idVencimiento;
        this.fecha = fecha;
        this.ultimosDigitosCuit = ultimosDigitosCuit;
        this.nombreCliente = nombreCliente;
        this.impuestoPagar = impuestoPagar;
        this.estado = estado;
    }

    public int getIdVencimiento() { return idVencimiento; }
    public void setIdVencimiento(int idVencimiento) { this.idVencimiento = idVencimiento; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getUltimosDigitosCuit() { return ultimosDigitosCuit; }
    public void setUltimosDigitosCuit(String ultimosDigitosCuit) { this.ultimosDigitosCuit = ultimosDigitosCuit; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getImpuestoPagar() { return impuestoPagar; }
    public void setImpuestoPagar(String impuestoPagar) { this.impuestoPagar = impuestoPagar; }

    public EstadoVencimiento getEstado() { return estado; }
    public void setEstado(EstadoVencimiento estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Vencimiento{" +
                "idVencimiento=" + idVencimiento +
                ", fecha=" + fecha +
                ", impuestoPagar='" + impuestoPagar + '\'' +
                ", estado=" + estado +
                '}';
    }
}
