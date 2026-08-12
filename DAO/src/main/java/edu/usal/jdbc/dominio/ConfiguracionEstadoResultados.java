package edu.usal.jdbc.dominio;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ConfiguracionEstadoResultados")
public class ConfiguracionEstadoResultados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "configuracion_id")
    private int idConfiguracion;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "periodo_inicio_default")
    private Date periodoInicioDefault;
    @Temporal(TemporalType.DATE)
    @Column(name = "periodo_fin_default")
    private Date periodoFinDefault;
    @Column(name = "margen_cmv")
    private double margenCMV;

    //ManyToMany unidireccional: ConfiguracionEstadoResultados es el lado dueño, CategoriaConcepto no la referencia de vuelta
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ConfigCategoriaExcluida",
            joinColumns = @JoinColumn(name = "configuracion_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<CategoriaConcepto> categoriasExcluidas;

    public ConfiguracionEstadoResultados() {}

    public ConfiguracionEstadoResultados(Usuario usuario, Date periodoInicioDefault, Date periodoFinDefault, double margenCMV, List<CategoriaConcepto> categoriasExcluidas) {
        this.usuario = usuario;
        this.periodoInicioDefault = periodoInicioDefault;
        this.periodoFinDefault = periodoFinDefault;
        this.margenCMV = margenCMV;
        this.categoriasExcluidas = categoriasExcluidas;
    }

    public ConfiguracionEstadoResultados(int idConfiguracion, Usuario usuario, Date periodoInicioDefault, Date periodoFinDefault, double margenCMV, List<CategoriaConcepto> categoriasExcluidas) {
        this.idConfiguracion = idConfiguracion;
        this.usuario = usuario;
        this.periodoInicioDefault = periodoInicioDefault;
        this.periodoFinDefault = periodoFinDefault;
        this.margenCMV = margenCMV;
        this.categoriasExcluidas = categoriasExcluidas;
    }

    public int getIdConfiguracion() { return idConfiguracion; }
    public void setIdConfiguracion(int idConfiguracion) { this.idConfiguracion = idConfiguracion; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Date getPeriodoInicioDefault() { return periodoInicioDefault; }
    public void setPeriodoInicioDefault(Date periodoInicioDefault) { this.periodoInicioDefault = periodoInicioDefault; }

    public Date getPeriodoFinDefault() { return periodoFinDefault; }
    public void setPeriodoFinDefault(Date periodoFinDefault) { this.periodoFinDefault = periodoFinDefault; }

    public double getMargenCMV() { return margenCMV; }
    public void setMargenCMV(double margenCMV) { this.margenCMV = margenCMV; }

    public List<CategoriaConcepto> getCategoriasExcluidas() { return categoriasExcluidas; }
    public void setCategoriasExcluidas(List<CategoriaConcepto> categoriasExcluidas) { this.categoriasExcluidas = categoriasExcluidas; }

    @Override
    public String toString() {
        return "ConfiguracionEstadoResultados{" +
                "idConfiguracion=" + idConfiguracion +
                ", periodoInicioDefault=" + periodoInicioDefault +
                ", periodoFinDefault=" + periodoFinDefault +
                ", margenCMV=" + margenCMV +
                '}';
    }
}
