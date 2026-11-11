package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoCierres;
import com.examble.puntodeventa.archivos.ArchivoVentas;
import com.examble.puntodeventa.modelos.CierreCaja;
import com.examble.puntodeventa.modelos.Venta;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Route(value = "cierre-caja", layout = MainLayout.class)
@PageTitle("Cierre de Caja | POS")
public class CierreCajaView extends VerticalLayout {

    private final NumberField txtBaseInicial = new NumberField("Base Inicial ($)");
    private final NumberField txtEfectivoContado = new NumberField("Efectivo Contado en Caja ($)");

    private final Span lblVentasDia = new Span("Ventas del Día: $0.00");
    private final Span lblTotalEsperado = new Span("Total Esperado: $0.00");
    private final Span lblResultado = new Span("Estado: Pendiente de cálculo");

    private final Grid<CierreCaja> gridHistorial = new Grid<>(CierreCaja.class, false);
    private final TextField txtBuscarHistorial = new TextField("Buscar por fecha u ID");
    private List<CierreCaja> listaCierres = new ArrayList<>();

    public CierreCajaView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Arqueo y Cierre Diario de Caja"));

        // Panel superior: Nuevo Arqueo
        VerticalLayout panelNuevoArqueo = crearPanelNuevoArqueo();

        // Panel inferior: Historial de Cierres Anteriores
        VerticalLayout panelHistorial = crearPanelHistorial();

        add(panelNuevoArqueo, new Hr(), panelHistorial);

        cargarDatosIniciales();
    }

    private VerticalLayout crearPanelNuevoArqueo() {
        VerticalLayout panel = new VerticalLayout();
        panel.setPadding(false);
        panel.setSpacing(true);

        txtBaseInicial.setValue(50000.0);
        txtBaseInicial.setMin(0);

        txtEfectivoContado.setValue(0.0);
        txtEfectivoContado.setMin(0);

        // Actualizar vista previa de ventas del día
        txtBaseInicial.addValueChangeListener(e -> actualizarTotalesPreview());
        txtEfectivoContado.addValueChangeListener(e -> actualizarTotalesPreview());

        lblVentasDia.getStyle().set("font-size", "1.1em").set("font-weight", "bold");
        lblTotalEsperado.getStyle().set("font-size", "1.1em").set("font-weight", "bold");
        lblResultado.getStyle().set("font-size", "1.2em").set("font-weight", "bold");

        Button btnCalcular = new Button("Procesar Arqueo y Generar Tirilla", e -> procesarArqueo());
        btnCalcular.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        FormLayout form = new FormLayout(txtBaseInicial, txtEfectivoContado);
        form.setMaxWidth("400px");

        VerticalLayout resumen = new VerticalLayout(
                new H3("Resumen del Cuadre"),
                lblVentasDia,
                lblTotalEsperado,
                lblResultado,
                btnCalcular
        );
        resumen.setSpacing(true);

        panel.add(form, resumen);
        return panel;
    }

    private VerticalLayout crearPanelHistorial() {
        VerticalLayout panel = new VerticalLayout();
        panel.setSizeFull();
        panel.setPadding(false);
        panel.setSpacing(true);

        panel.add(new H3("Historial de Cierres de Caja Anteriores"));

        txtBuscarHistorial.setPlaceholder("Buscar fecha (YYYY-MM-DD) e ID...");
        txtBuscarHistorial.setPrefixComponent(VaadinIcon.SEARCH.create());
        txtBuscarHistorial.setValueChangeMode(ValueChangeMode.LAZY);
        txtBuscarHistorial.addValueChangeListener(e -> filtrarHistorial(e.getValue()));

        gridHistorial.addColumn(CierreCaja::getId).setHeader("ID Cierre").setAutoWidth(true);
        gridHistorial.addColumn(CierreCaja::getFechaHora).setHeader("Fecha y Hora").setAutoWidth(true);
        gridHistorial.addColumn(c -> String.format("$%.2f", c.getBaseInicial())).setHeader("Base Inicial").setAutoWidth(true);
        gridHistorial.addColumn(c -> String.format("$%.2f", c.getTotalVentas())).setHeader("Ventas").setAutoWidth(true);
        gridHistorial.addColumn(c -> String.format("$%.2f", c.getTotalEsperado())).setHeader("Esperado").setAutoWidth(true);
        gridHistorial.addColumn(c -> String.format("$%.2f", c.getEfectivoContado())).setHeader("Contado").setAutoWidth(true);
        gridHistorial.addColumn(c -> String.format("$%.2f", c.getDiferencia())).setHeader("Diferencia").setAutoWidth(true);
        gridHistorial.addColumn(CierreCaja::getEstado).setHeader("Estado").setAutoWidth(true);

        gridHistorial.addComponentColumn(cierre -> {
            Button btnVerTirilla = new Button("Ver Tirilla", VaadinIcon.PRINT.create(), e -> mostrarDialogoTirilla(cierre));
            btnVerTirilla.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            return btnVerTirilla;
        }).setHeader("Acción").setAutoWidth(true);

        panel.add(txtBuscarHistorial, gridHistorial);
        return panel;
    }

    private void cargarDatosIniciales() {
        actualizarTotalesPreview();
        recargarHistorial();
    }

    private double obtenerVentasHoy() {
        String hoy = LocalDate.now().toString();
        List<Venta> todasLasVentas = ArchivoVentas.cargarVentas();
        return todasLasVentas.stream()
                .filter(v -> v.getFechaHora() != null && v.getFechaHora().startsWith(hoy))
                .mapToDouble(Venta::getTotal)
                .sum();
    }

    private void actualizarTotalesPreview() {
        double base = txtBaseInicial.getValue() != null ? txtBaseInicial.getValue() : 0.0;
        double contado = txtEfectivoContado.getValue() != null ? txtEfectivoContado.getValue() : 0.0;
        double ventasHoy = obtenerVentasHoy();
        double esperado = base + ventasHoy;
        double diferencia = contado - esperado;

        lblVentasDia.setText(String.format("Ventas del Día: $%.2f", ventasHoy));
        lblTotalEsperado.setText(String.format("Total Esperado (Base + Ventas): $%.2f", esperado));

        if (diferencia == 0) {
            lblResultado.setText("Estado: CUADRADO ($0.00)");
            lblResultado.getStyle().set("color", "var(--lumo-success-color)");
        } else if (diferencia > 0) {
            lblResultado.setText(String.format("Estado: SOBRANTE (+$%.2f)", diferencia));
            lblResultado.getStyle().set("color", "var(--lumo-primary-color)");
        } else {
            lblResultado.setText(String.format("Estado: FALTANTE (-$%.2f)", Math.abs(diferencia)));
            lblResultado.getStyle().set("color", "var(--lumo-error-color)");
        }
    }

    private void procesarArqueo() {
        double base = txtBaseInicial.getValue() != null ? txtBaseInicial.getValue() : 0.0;
        double contado = txtEfectivoContado.getValue() != null ? txtEfectivoContado.getValue() : 0.0;
        double ventasHoy = obtenerVentasHoy();
        double esperado = base + ventasHoy;
        double diferencia = contado - esperado;

        String estado;
        if (diferencia == 0) {
            estado = "CUADRADO";
        } else if (diferencia > 0) {
            estado = "SOBRANTE";
        } else {
            estado = "FALTANTE";
        }

        String id = "CIE-" + (System.currentTimeMillis() % 100000);
        String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        CierreCaja nuevoCierre = new CierreCaja(id, fechaHora, base, ventasHoy, esperado, contado, diferencia, estado);

        // Guardar en cierres.txt
        ArchivoCierres.guardarCierre(nuevoCierre);

        Notification.show("Arqueo guardado con éxito", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        recargarHistorial();
        mostrarDialogoTirilla(nuevoCierre);
    }

    private void recargarHistorial() {
        listaCierres = ArchivoCierres.cargarCierres();
        gridHistorial.setItems(listaCierres);
    }

    private void filtrarHistorial(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            gridHistorial.setItems(listaCierres);
            return;
        }
        String f = filtro.toLowerCase().trim();
        List<CierreCaja> filtrados = listaCierres.stream()
                .filter(c -> c.getId().toLowerCase().contains(f) || c.getFechaHora().toLowerCase().contains(f))
                .collect(Collectors.toList());
        gridHistorial.setItems(filtrados);
    }

    // Modal con formato de Tirilla y botón de cierre X
    private void mostrarDialogoTirilla(CierreCaja cierre) {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setCloseOnOutsideClick(true);
        dialog.setCloseOnEsc(true);

        // Encabezado con título y botón X de cierre
        H3 tituloModal = new H3("Tirilla de Cierre de Caja");
        tituloModal.getStyle().set("margin", "0");

        Button btnCerrarX = new Button(VaadinIcon.CLOSE.create(), e -> dialog.close());
        btnCerrarX.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        btnCerrarX.getStyle().set("font-size", "1.2em").set("cursor", "pointer");

        HorizontalLayout headerLayout = new HorizontalLayout(tituloModal, btnCerrarX);
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        headerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        // Contenedor visual tipo Tirilla POS
        VerticalLayout tirilla = new VerticalLayout();
        tirilla.setWidth("320px");
        tirilla.getStyle()
                .set("background-color", "#fdfbf7")
                .set("border", "1px dashed #666")
                .set("padding", "20px")
                .set("font-family", "'Courier New', Courier, monospace")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.1)");

        Paragraph pEmpresa = new Paragraph("*** MI TIENDA POS ***");
        pEmpresa.getStyle().set("text-align", "center").set("font-weight", "bold").set("margin", "0");

        Paragraph pDetalleHeader = new Paragraph("COMPROBANTE DE ARQUEO DIARIO\nID: " + cierre.getId() + "\nFecha: " + cierre.getFechaHora());
        pDetalleHeader.getStyle().set("text-align", "center").set("font-size", "0.85em").set("margin", "5px 0");

        Hr lineaDiv1 = new Hr();
        lineaDiv1.getStyle().set("border-top", "1px dashed #000");

        VerticalLayout datosLayout = new VerticalLayout();
        datosLayout.setPadding(false);
        datosLayout.setSpacing(false);

        datosLayout.add(crearFilaTirilla("Base Inicial:", String.format("$%.2f", cierre.getBaseInicial())));
        datosLayout.add(crearFilaTirilla("Ventas del Día:", String.format("$%.2f", cierre.getTotalVentas())));
        datosLayout.add(crearFilaTirilla("Total Esperado:", String.format("$%.2f", cierre.getTotalEsperado())));
        datosLayout.add(crearFilaTirilla("Efectivo Contado:", String.format("$%.2f", cierre.getEfectivoContado())));

        Hr lineaDiv2 = new Hr();
        lineaDiv2.getStyle().set("border-top", "1px dashed #000");

        HorizontalLayout filaResultado = crearFilaTirilla("DIFERENCIA:", String.format("$%.2f", cierre.getDiferencia()));
        filaResultado.getStyle().set("font-weight", "bold");

        Paragraph pEstado = new Paragraph("ESTADO: " + cierre.getEstado());
        pEstado.getStyle().set("text-align", "center").set("font-weight", "bold").set("margin", "10px 0 0 0");

        if ("CUADRADO".equalsIgnoreCase(cierre.getEstado())) {
            pEstado.getStyle().set("color", "green");
        } else if ("SOBRANTE".equalsIgnoreCase(cierre.getEstado())) {
            pEstado.getStyle().set("color", "blue");
        } else {
            pEstado.getStyle().set("color", "red");
        }

        Paragraph pFooter = new Paragraph("----------------------------------\nFirma Cajero: _________________\n¡Arqueo finalizado con éxito!");
        pFooter.getStyle().set("text-align", "center").set("font-size", "0.8em").set("margin-top", "15px");

        tirilla.add(pEmpresa, pDetalleHeader, lineaDiv1, datosLayout, lineaDiv2, filaResultado, pEstado, pFooter);

        VerticalLayout modalContent = new VerticalLayout(headerLayout, tirilla);
        modalContent.setAlignItems(FlexComponent.Alignment.CENTER);
        modalContent.setPadding(true);

        dialog.add(modalContent);
        dialog.open();
    }

    private HorizontalLayout crearFilaTirilla(String etiqueta, String valor) {
        Span lblEtiq = new Span(etiqueta);
        Span lblVal = new Span(valor);
        HorizontalLayout row = new HorizontalLayout(lblEtiq, lblVal);
        row.setWidthFull();
        row.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        return row;
    }
}