package edu.usal.jdbc.servicio;

import edu.usal.jdbc.dominio.CategoriaConcepto;
import edu.usal.jdbc.dominio.Compra;
import edu.usal.jdbc.dominio.ConfiguracionEstadoResultados;
import edu.usal.jdbc.dominio.EstadoCobro;
import edu.usal.jdbc.dominio.EstadoPago;
import edu.usal.jdbc.dominio.Gasto;
import edu.usal.jdbc.dominio.OtroEgreso;
import edu.usal.jdbc.dominio.OtroIngreso;
import edu.usal.jdbc.dominio.Venta;
import edu.usal.jdbc.dto.ComparacionPeriodoDTO;
import edu.usal.jdbc.dto.ComposicionCategoriaDTO;
import edu.usal.jdbc.dto.EstadoFinancieroDTO;
import edu.usal.jdbc.dto.EstadoResultadosDTO;
import edu.usal.jdbc.dto.PuntoTendenciaDTO;
import edu.usal.jdbc.dto.TesoreriaDTO;
import edu.usal.jdbc.excepciones.ServiceException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * No tiene DAO propio: agrega datos de Venta/Compra/Gasto/OtroIngreso/OtroEgreso al vuelo.
 * Tesoreria, Estado de Resultados y Estado Financiero se calculan bajo demanda (no se persisten),
 * siguiendo el mismo criterio que Tesoreria en el diccionario de datos original.
 */
public class ReporteServicio {

    private static final double UMBRAL_ALERTA_DEFECTO = 5.0;
    private static final ZoneId ZONA = ZoneId.systemDefault();

    private final VentaServicio ventaServicio;
    private final CompraServicio compraServicio;
    private final GastoServicio gastoServicio;
    private final OtroIngresoServicio otroIngresoServicio;
    private final OtroEgresoServicio otroEgresoServicio;
    private final ConfiguracionServicio configuracionServicio;

    public ReporteServicio() {
        this.ventaServicio = new VentaServicio();
        this.compraServicio = new CompraServicio();
        this.gastoServicio = new GastoServicio();
        this.otroIngresoServicio = new OtroIngresoServicio();
        this.otroEgresoServicio = new OtroEgresoServicio();
        this.configuracionServicio = new ConfiguracionServicio();
    }

    // ---------- UC-18 Tesoreria ----------
    public TesoreriaDTO obtenerTesoreria(int idUsuario) throws ServiceException {
        List<Venta> ventasPendientes = ventaServicio.obtenerVentasPendientesPorUsuario(idUsuario);
        List<Compra> comprasPendientes = compraServicio.obtenerComprasPendientesPorUsuario(idUsuario);
        List<Gasto> gastosPendientes = gastoServicio.obtenerGastosPendientesPorUsuario(idUsuario);
        List<OtroIngreso> otrosIngresosPendientes = otroIngresoServicio.obtenerOtrosIngresosPendientesPorUsuario(idUsuario);
        List<OtroEgreso> otrosEgresosPendientes = otroEgresoServicio.obtenerOtrosEgresosPendientesPorUsuario(idUsuario);

        double cuentasPorCobrar = ventasPendientes.stream().mapToDouble(Venta::getSubtotal).sum()
                + otrosIngresosPendientes.stream().mapToDouble(OtroIngreso::getValor).sum();
        double cuentasPorPagar = comprasPendientes.stream().mapToDouble(Compra::getValorTotal).sum()
                + gastosPendientes.stream().mapToDouble(Gasto::getValor).sum()
                + otrosEgresosPendientes.stream().mapToDouble(OtroEgreso::getValor).sum();

        return new TesoreriaDTO(cuentasPorCobrar, cuentasPorPagar, cuentasPorCobrar - cuentasPorPagar,
                ventasPendientes, comprasPendientes, gastosPendientes, otrosIngresosPendientes, otrosEgresosPendientes);
    }

    // ---------- UC-09/UC-10 Estado de Resultados ----------
    public EstadoResultadosDTO generarEstadoResultados(int idUsuario, Date desde, Date hasta) throws ServiceException {
        EstadoResultadosDTO actual = calcularEstadoResultados(idUsuario, desde, hasta);

        LocalDate desdeLocal = aLocalDate(desde);
        LocalDate hastaLocal = aLocalDate(hasta);
        long dias = ChronoUnit.DAYS.between(desdeLocal, hastaLocal);
        LocalDate hastaAnteriorLocal = desdeLocal.minusDays(1);
        LocalDate desdeAnteriorLocal = hastaAnteriorLocal.minusDays(dias);

        EstadoResultadosDTO anterior = calcularEstadoResultados(idUsuario, aDate(desdeAnteriorLocal), aDate(hastaAnteriorLocal));

        double gananciaAnterior = anterior.getGanancia();
        double variacion;
        if (gananciaAnterior != 0) {
            variacion = ((actual.getGanancia() - gananciaAnterior) / Math.abs(gananciaAnterior)) * 100.0;
        } else {
            variacion = actual.getGanancia() > 0 ? 100.0 : (actual.getGanancia() < 0 ? -100.0 : 0.0);
        }
        actual.setVariacionAnterior(variacion);
        return actual;
    }

    private EstadoResultadosDTO calcularEstadoResultados(int idUsuario, Date desde, Date hasta) throws ServiceException {
        ConfiguracionEstadoResultados config = configuracionServicio.obtenerOCrearConfiguracion(idUsuario);
        Set<Integer> idsExcluidas = obtenerIdsCategoriasExcluidas(config);

        double ventasTotal = ventaServicio.obtenerVentasPorUsuarioYPeriodo(idUsuario, desde, hasta)
                .stream().mapToDouble(Venta::getSubtotal).sum();

        double cmv = ventasTotal / (1 + config.getMargenCMV() / 100.0);
        double utilidadBruta = ventasTotal - cmv;

        double gastosTotal = gastoServicio.obtenerGastosPorUsuarioYPeriodo(idUsuario, desde, hasta).stream()
                .filter(g -> !idsExcluidas.contains(g.getCategoria().getIdCategoria()))
                .mapToDouble(Gasto::getValor).sum();

        double oIngresosTotal = otroIngresoServicio.obtenerOtrosIngresosPorUsuarioYPeriodo(idUsuario, desde, hasta).stream()
                .filter(o -> !idsExcluidas.contains(o.getCategoria().getIdCategoria()))
                .mapToDouble(OtroIngreso::getValor).sum();

        double oEgresosTotal = otroEgresoServicio.obtenerOtrosEgresosPorUsuarioYPeriodo(idUsuario, desde, hasta).stream()
                .filter(o -> !idsExcluidas.contains(o.getCategoria().getIdCategoria()))
                .mapToDouble(OtroEgreso::getValor).sum();

        double ganancia = utilidadBruta - gastosTotal + oIngresosTotal - oEgresosTotal;

        EstadoResultadosDTO dto = new EstadoResultadosDTO();
        dto.setPeriodoInicio(desde);
        dto.setPeriodoFin(hasta);
        dto.setVentasTotal(ventasTotal);
        dto.setCmv(cmv);
        dto.setMargenCMV(config.getMargenCMV());
        dto.setUtilidadBruta(utilidadBruta);
        dto.setGastosTotal(gastosTotal);
        dto.setoIngresosTotal(oIngresosTotal);
        dto.setoEgresosTotal(oEgresosTotal);
        dto.setGanancia(ganancia);
        dto.setMargenUtilidad(ventasTotal > 0 ? (utilidadBruta / ventasTotal) * 100.0 : 0.0);
        dto.setRentabilidad(ventasTotal > 0 ? (ganancia / ventasTotal) * 100.0 : 0.0);
        dto.setVariacionAnterior(0.0);
        return dto;
    }

    // ---------- UC-14 Comparar periodos ----------
    public ComparacionPeriodoDTO compararPeriodos(int idUsuario, Date desde1, Date hasta1, Date desde2, Date hasta2) throws ServiceException {
        EstadoResultadosDTO p1 = calcularEstadoResultados(idUsuario, desde1, hasta1);
        EstadoResultadosDTO p2 = calcularEstadoResultados(idUsuario, desde2, hasta2);

        double variacionVentas = p1.getVentasTotal() != 0
                ? ((p2.getVentasTotal() - p1.getVentasTotal()) / Math.abs(p1.getVentasTotal())) * 100.0 : 0.0;
        double variacionGanancia = p1.getGanancia() != 0
                ? ((p2.getGanancia() - p1.getGanancia()) / Math.abs(p1.getGanancia())) * 100.0 : 0.0;

        return new ComparacionPeriodoDTO(p1, p2, variacionVentas, variacionGanancia);
    }

    // ---------- UC-15 Composicion de gastos ----------
    public List<ComposicionCategoriaDTO> obtenerComposicionGastos(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Set<Integer> idsExcluidas = obtenerIdsCategoriasExcluidas(idUsuario);
        List<Gasto> gastos = gastoServicio.obtenerGastosPorUsuarioYPeriodo(idUsuario, desde, hasta).stream()
                .filter(g -> !idsExcluidas.contains(g.getCategoria().getIdCategoria()))
                .collect(Collectors.toList());
        double total = gastos.stream().mapToDouble(Gasto::getValor).sum();

        Map<String, Double> porCategoria = new HashMap<>();
        for (Gasto g : gastos) {
            String nombre = g.getCategoria().getNombre();
            porCategoria.merge(nombre, g.getValor(), Double::sum);
        }

        List<ComposicionCategoriaDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, Double> entry : porCategoria.entrySet()) {
            double porcentaje = total > 0 ? (entry.getValue() / total) * 100.0 : 0.0;
            resultado.add(new ComposicionCategoriaDTO(entry.getKey(), entry.getValue(), porcentaje));
        }
        resultado.sort(Comparator.comparingDouble(ComposicionCategoriaDTO::getTotal).reversed());
        return resultado;
    }

    // ---------- Composicion de otros ingresos (por categoria, mismo criterio que composicion de gastos) ----------
    public List<ComposicionCategoriaDTO> obtenerComposicionOtrosIngresos(int idUsuario, Date desde, Date hasta) throws ServiceException {
        Set<Integer> idsExcluidas = obtenerIdsCategoriasExcluidas(idUsuario);
        List<OtroIngreso> otrosIngresos = otroIngresoServicio.obtenerOtrosIngresosPorUsuarioYPeriodo(idUsuario, desde, hasta).stream()
                .filter(o -> !idsExcluidas.contains(o.getCategoria().getIdCategoria()))
                .collect(Collectors.toList());
        double total = otrosIngresos.stream().mapToDouble(OtroIngreso::getValor).sum();

        Map<String, Double> porCategoria = new HashMap<>();
        for (OtroIngreso o : otrosIngresos) {
            String nombre = o.getCategoria().getNombre();
            porCategoria.merge(nombre, o.getValor(), Double::sum);
        }

        List<ComposicionCategoriaDTO> resultado = new ArrayList<>();
        for (Map.Entry<String, Double> entry : porCategoria.entrySet()) {
            double porcentaje = total > 0 ? (entry.getValue() / total) * 100.0 : 0.0;
            resultado.add(new ComposicionCategoriaDTO(entry.getKey(), entry.getValue(), porcentaje));
        }
        resultado.sort(Comparator.comparingDouble(ComposicionCategoriaDTO::getTotal).reversed());
        return resultado;
    }

    // ---------- UC-13 Tendencias mensuales ----------
    public List<PuntoTendenciaDTO> obtenerTendenciaMensual(int idUsuario, int cantidadMeses) throws ServiceException {
        List<PuntoTendenciaDTO> puntos = new ArrayList<>();
        YearMonth mesActual = YearMonth.now();

        for (int i = cantidadMeses - 1; i >= 0; i--) {
            YearMonth mes = mesActual.minusMonths(i);
            LocalDate inicioMes = mes.atDay(1);
            LocalDate finMes = mes.equals(mesActual) ? LocalDate.now() : mes.atEndOfMonth();

            EstadoResultadosDTO estadoMes = calcularEstadoResultados(idUsuario, aDate(inicioMes), aDate(finMes));
            String etiqueta = capitalizar(mes.getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES"))) + " " + mes.getYear();
            puntos.add(new PuntoTendenciaDTO(etiqueta, estadoMes.getVentasTotal(), estadoMes.getGanancia()));
        }
        return puntos;
    }

    // ---------- UC-12/UC-16 Estado financiero (panel + puntos criticos) ----------
    public EstadoFinancieroDTO obtenerEstadoFinanciero(int idUsuario) throws ServiceException {
        List<PuntoTendenciaDTO> tendencia = obtenerTendenciaMensual(idUsuario, 12);

        int mesesPerdida = 0;
        PuntoTendenciaDTO mejor = null;
        PuntoTendenciaDTO peor = null;
        List<String> alertas = new ArrayList<>();

        for (int i = 0; i < tendencia.size(); i++) {
            PuntoTendenciaDTO punto = tendencia.get(i);
            if (punto.getGanancia() < 0) {
                mesesPerdida++;
                alertas.add("Mes con perdida: " + punto.getEtiquetaMes());
            }
            if (mejor == null || punto.getGanancia() > mejor.getGanancia()) mejor = punto;
            if (peor == null || punto.getGanancia() < peor.getGanancia()) peor = punto;

            if (i > 0) {
                double rentabilidadAnterior = tendencia.get(i - 1).getRentabilidad();
                double caida = rentabilidadAnterior - punto.getRentabilidad();
                if (caida >= UMBRAL_ALERTA_DEFECTO) {
                    alertas.add("Caida de rentabilidad en " + punto.getEtiquetaMes()
                            + " (" + String.format(Locale.forLanguageTag("es"), "%.1f", caida) + " puntos respecto al mes anterior)");
                }
            }
        }

        LocalDate hoy = LocalDate.now();
        List<ComposicionCategoriaDTO> composicion = obtenerComposicionGastos(idUsuario, aDate(hoy.withDayOfMonth(1)), aDate(hoy));

        EstadoFinancieroDTO dto = new EstadoFinancieroDTO();
        dto.setTendenciaMensual(tendencia);
        dto.setComposicionGastos(composicion);
        dto.setMesesPerdida(mesesPerdida);
        dto.setMejorMes(mejor != null ? mejor.getEtiquetaMes() : "-");
        dto.setPeorMes(peor != null ? peor.getEtiquetaMes() : "-");
        dto.setUmbralAlerta(UMBRAL_ALERTA_DEFECTO);
        dto.setAlertas(alertas);
        return dto;
    }

    private Set<Integer> obtenerIdsCategoriasExcluidas(int idUsuario) throws ServiceException {
        return obtenerIdsCategoriasExcluidas(configuracionServicio.obtenerOCrearConfiguracion(idUsuario));
    }

    private Set<Integer> obtenerIdsCategoriasExcluidas(ConfiguracionEstadoResultados config) {
        return config.getCategoriasExcluidas() == null ? Set.of() :
                config.getCategoriasExcluidas().stream().map(CategoriaConcepto::getIdCategoria).collect(Collectors.toSet());
    }

    private LocalDate aLocalDate(Date fecha) {
        return new java.sql.Date(fecha.getTime()).toLocalDate();
    }

    private Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZONA).toInstant());
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}
