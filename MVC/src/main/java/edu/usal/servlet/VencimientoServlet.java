package edu.usal.servlet;

import edu.usal.jdbc.dominio.EstadoVencimiento;
import edu.usal.jdbc.dominio.Vencimiento;
import edu.usal.servlet.dto.CeldaCalendario;

import edu.usal.jdbc.excepciones.ServiceException;
import edu.usal.jdbc.servicio.VencimientoServicio;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

@WebServlet("/Vencimiento")
@MultipartConfig(maxFileSize = 5L * 1024 * 1024, maxRequestSize = 5L * 1024 * 1024 + 512 * 1024)
public class VencimientoServlet extends HttpServlet {

    private static final long MAX_IMPORTACION_BYTES = 5L * 1024 * 1024;

    private final VencimientoServicio vencimientoServicio;
    private final SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
    private final ZoneId zona = ZoneId.systemDefault();

    public VencimientoServlet() {
        this.vencimientoServicio = new VencimientoServicio();
    }

    private boolean esAdmin(HttpServletRequest req) {
        return "ADMINISTRADOR".equals(req.getSession().getAttribute("rolUsuario"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }

        if ("exportarExcel".equals(req.getParameter("action"))) {
            exportarExcel(resp);
            return;
        }

        try {
            List<Vencimiento> vencimientos = vencimientoServicio.obtenerTodosLosVencimientos();
            Map<Integer, String> claseEstado = new HashMap<>();
            Map<Integer, Integer> diasRestantes = new HashMap<>();
            for (Vencimiento v : vencimientos) {
                diasRestantes.put(v.getIdVencimiento(), vencimientoServicio.calcularDiasRestantes(v));
                claseEstado.put(v.getIdVencimiento(), calcularClase(v));
            }
            req.setAttribute("vencimientos", vencimientos);
            req.setAttribute("claseEstado", claseEstado);
            req.setAttribute("diasRestantes", diasRestantes);

            int anio = parseIntODefault(req.getParameter("anio"), LocalDate.now().getYear());
            int mes = parseIntODefault(req.getParameter("mes"), LocalDate.now().getMonthValue());
            YearMonth mesActual = YearMonth.of(anio, mes);
            req.setAttribute("celdas", construirCalendario(mesActual, vencimientos, claseEstado));
            req.setAttribute("mesActual", mesActual);
            req.setAttribute("mesAnterior", mesActual.minusMonths(1));
            req.setAttribute("mesSiguiente", mesActual.plusMonths(1));
            String nombreMes = capitalizar(mesActual.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES")));
            req.setAttribute("nombreMesActual", nombreMes + " " + mesActual.getYear());

            String editarId = req.getParameter("editarId");
            if (editarId != null && !editarId.isEmpty()) {
                Vencimiento aEditar = vencimientoServicio.obtenerVencimientoPorId(Integer.parseInt(editarId));
                if (aEditar != null && aEditar.getEstado() != EstadoVencimiento.ELIMINADO) {
                    req.setAttribute("vencimientoAEditar", aEditar);
                }
            }
        } catch (ServiceException e) {
            req.setAttribute("error", "No se pudieron cargar los vencimientos: " + e.getMessage());
        }
        req.getRequestDispatcher("/Vistas/OpcionesAdministrador/Vencimientos.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdmin(req)) {
            resp.sendRedirect(req.getContextPath() + "/Vistas/Principal.jsp");
            return;
        }
        String contentType = req.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("multipart/")
                && req.getContentLengthLong() > MAX_IMPORTACION_BYTES + 512 * 1024) {
            req.setAttribute("error", "El archivo supera el maximo permitido de 5 MB.");
            doGet(req, resp);
            return;
        }
        String action = req.getParameter("action");
        if (action == null) {
            req.setAttribute("error", "No se pudo procesar la solicitud. Si estaba importando un Excel, verifique que no supere los 5 MB.");
            doGet(req, resp);
            return;
        }
        try {
            switch (action) {
                case "agregar":
                    agregarVencimiento(req);
                    break;
                case "editar":
                    editarVencimiento(req);
                    break;
                case "marcarRealizado":
                    vencimientoServicio.marcarRealizado(Integer.parseInt(req.getParameter("idVencimiento")));
                    req.setAttribute("exito", "Vencimiento marcado como realizado.");
                    break;
                case "eliminar":
                    vencimientoServicio.eliminarVencimiento(Integer.parseInt(req.getParameter("idVencimiento")));
                    req.setAttribute("exito", "Vencimiento eliminado.");
                    break;
                case "importarExcel":
                    importarExcel(req);
                    break;
                default:
                    break;
            }
        } catch (ServiceException | NumberFormatException | ServletException | ParseException e) {
            req.setAttribute("error", "No se pudo completar la operacion: " + e.getMessage());
        }
        doGet(req, resp);
    }

    private void agregarVencimiento(HttpServletRequest req) throws ParseException {
        String fecha = req.getParameter("fecha");
        String cuit = req.getParameter("ultimosDigitosCuit");
        String cliente = req.getParameter("nombreCliente");
        String impuesto = req.getParameter("impuestoPagar");

        if (fecha == null || fecha.isEmpty() || impuesto == null || impuesto.isEmpty()
                || cliente == null || cliente.isEmpty() || cuit == null || cuit.isEmpty()) {
            req.setAttribute("error", "Complete la fecha, el impuesto a pagar, el cliente y los ultimos 4 digitos del CUIT.");
            return;
        }
        Vencimiento vencimiento = new Vencimiento(formatoFecha.parse(fecha), cuit, cliente, impuesto, EstadoVencimiento.PENDIENTE);
        boolean exito = vencimientoServicio.guardarVencimiento(vencimiento);
        req.setAttribute(exito ? "exito" : "error", exito ? "Vencimiento agregado." : "No se pudo agregar el vencimiento.");
    }

    private void editarVencimiento(HttpServletRequest req) throws ParseException {
        String idVencimiento = req.getParameter("idVencimiento");
        if (idVencimiento == null || idVencimiento.isEmpty()) {
            req.setAttribute("error", "Falta el identificador del vencimiento.");
            return;
        }
        String cuit = req.getParameter("ultimosDigitosCuit");
        String cliente = req.getParameter("nombreCliente");
        String impuesto = req.getParameter("impuestoPagar");
        if (cuit == null || cuit.isEmpty() || cliente == null || cliente.isEmpty()
                || impuesto == null || impuesto.isEmpty()) {
            req.setAttribute("error", "Complete el impuesto a pagar, el cliente y los ultimos 4 digitos del CUIT.");
            return;
        }

        Vencimiento vencimiento = new Vencimiento();
        vencimiento.setIdVencimiento(Integer.parseInt(idVencimiento));
        vencimiento.setFecha(formatoFecha.parse(req.getParameter("fecha")));
        vencimiento.setUltimosDigitosCuit(cuit);
        vencimiento.setNombreCliente(cliente);
        vencimiento.setImpuestoPagar(impuesto);
        vencimiento.setEstado(EstadoVencimiento.valueOf(req.getParameter("estado")));

        boolean exito = vencimientoServicio.editarVencimiento(vencimiento);
        req.setAttribute(exito ? "exito" : "error", exito ? "Vencimiento actualizado." : "No se pudo actualizar el vencimiento.");
    }

    private void exportarExcel(HttpServletResponse resp) throws IOException {
        resp.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resp.setHeader("Content-Disposition", "attachment; filename=\"vencimientos.xlsx\"");
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Vencimientos");
            CellStyle estiloFecha = workbook.createCellStyle();
            DataFormat formato = workbook.createDataFormat();
            estiloFecha.setDataFormat(formato.getFormat("yyyy-mm-dd"));

            String[] encabezados = {"Fecha", "UltimosDigitosCuit", "NombreCliente", "ImpuestoPagar", "Estado"};
            Row filaEncabezado = hoja.createRow(0);
            for (int i = 0; i < encabezados.length; i++) {
                filaEncabezado.createCell(i).setCellValue(encabezados[i]);
            }

            int numeroFila = 1;
            for (Vencimiento v : vencimientoServicio.obtenerTodosLosVencimientos()) {
                Row fila = hoja.createRow(numeroFila++);
                Cell celdaFecha = fila.createCell(0);
                celdaFecha.setCellValue(v.getFecha());
                celdaFecha.setCellStyle(estiloFecha);
                fila.createCell(1).setCellValue(v.getUltimosDigitosCuit() == null ? "" : v.getUltimosDigitosCuit());
                fila.createCell(2).setCellValue(v.getNombreCliente() == null ? "" : v.getNombreCliente());
                fila.createCell(3).setCellValue(v.getImpuestoPagar());
                fila.createCell(4).setCellValue(v.getEstado().name());
            }
            for (int i = 0; i < encabezados.length; i++) {
                hoja.autoSizeColumn(i);
            }

            try (OutputStream salida = resp.getOutputStream()) {
                workbook.write(salida);
            }
        }
    }

    private void importarExcel(HttpServletRequest req) throws IOException, ServletException, ParseException {
        Part archivo;
        try {
            archivo = req.getPart("archivoExcel");
        } catch (IllegalStateException e) {
            req.setAttribute("error", "El archivo supera el maximo permitido de 5 MB.");
            return;
        }
        if (archivo != null && archivo.getSize() > MAX_IMPORTACION_BYTES) {
            req.setAttribute("error", "El archivo supera el maximo permitido de 5 MB.");
            return;
        }
        if (archivo == null || archivo.getSize() == 0) {
            req.setAttribute("error", "Seleccione un archivo Excel (.xlsx) para importar.");
            return;
        }
        int importados = 0;
        int omitidos = 0;
        try (InputStream entrada = archivo.getInputStream();
             Workbook workbook = WorkbookFactory.create(entrada)) {
            Sheet hoja = workbook.getSheetAt(0);
            for (int numeroFila = 1; numeroFila <= hoja.getLastRowNum(); numeroFila++) {
                Row fila = hoja.getRow(numeroFila);
                if (fila == null) continue;

                Date fecha = leerFechaCelda(fila.getCell(0));
                String cuit = leerTextoCelda(fila.getCell(1));
                String cliente = leerTextoCelda(fila.getCell(2));
                String impuesto = leerTextoCelda(fila.getCell(3));
                if (fecha == null || impuesto == null || impuesto.isEmpty() || cuit == null || cliente == null) {
                    omitidos++;
                    continue;
                }

                Vencimiento vencimiento = new Vencimiento(fecha, cuit, cliente, impuesto, EstadoVencimiento.PENDIENTE);
                if (vencimientoServicio.guardarVencimiento(vencimiento)) importados++;
            }
        }
        String mensaje = "Se importaron " + importados + " vencimientos.";
        if (omitidos > 0) {
            mensaje += " Se omitieron " + omitidos + " filas por faltar fecha, cliente, CUIT o impuesto.";
        }
        req.setAttribute("exito", mensaje);
    }

    private Date leerFechaCelda(Cell celda) throws ParseException {
        if (celda == null) return null;
        if (celda.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(celda)) {
            return celda.getDateCellValue();
        }
        String texto = leerTextoCelda(celda);
        return (texto == null || texto.isEmpty()) ? null : formatoFecha.parse(texto);
    }

    private String leerTextoCelda(Cell celda) {
        if (celda == null) return null;
        celda.setCellType(CellType.STRING);
        String texto = celda.getStringCellValue().trim();
        return texto.isEmpty() ? null : texto;
    }

    private String calcularClase(Vencimiento v) {
        if (v.getEstado() == EstadoVencimiento.REALIZADO) return "bueno";
        if (vencimientoServicio.esProximo(v)) return "advertencia";
        return "critico";
    }

    private int parseIntODefault(String valor, int porDefecto) {
        try {
            return valor == null || valor.isEmpty() ? porDefecto : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return porDefecto;
        }
    }

    private List<CeldaCalendario> construirCalendario(YearMonth mes, List<Vencimiento> vencimientos, Map<Integer, String> claseEstado) {
        Map<Integer, List<Vencimiento>> porDia = new HashMap<>();
        for (Vencimiento v : vencimientos) {
            LocalDate fechaLocal = Instant.ofEpochMilli(v.getFecha().getTime()).atZone(zona).toLocalDate();
            if (YearMonth.from(fechaLocal).equals(mes)) {
                porDia.computeIfAbsent(fechaLocal.getDayOfMonth(), k -> new ArrayList<>()).add(v);
            }
        }

        LocalDate hoy = LocalDate.now(zona);
        List<CeldaCalendario> celdas = new ArrayList<>();
        int offset = mes.atDay(1).getDayOfWeek().getValue() - 1; // Lunes = 0
        for (int i = 0; i < offset; i++) {
            celdas.add(new CeldaCalendario(0, true, List.of()));
        }
        for (int dia = 1; dia <= mes.lengthOfMonth(); dia++) {
            boolean esHoy = mes.atDay(dia).equals(hoy);
            celdas.add(new CeldaCalendario(dia, false, porDia.getOrDefault(dia, List.of()), esHoy));
        }
        while (celdas.size() % 7 != 0) {
            celdas.add(new CeldaCalendario(0, true, List.of()));
        }
        return celdas;
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }
}
