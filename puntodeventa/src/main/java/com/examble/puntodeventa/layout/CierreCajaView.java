package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoVentas;
import com.examble.puntodeventa.modelos.Venta;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Cierre de Caja | POS")
@Route(value = "cierre-caja", layout = MainLayout.class)
public class CierreCajaView extends VerticalLayout {

    private NumberField txtBaseInicial = new NumberField("Base Inicial ($)");
    private NumberField txtEfectivoContado = new NumberField("Efectivo Contado en Caja ($)");

    private Span lblVentasDia = new Span();
    private Span lblTotalEsperado = new Span();
    private Span lblResultado = new Span();

    public CierreCajaView() {
        setSizeFull();
        setSpacing(true);

        H2 titulo = new H2("Cierre de Caja Diario (Arqueo)");

        txtBaseInicial.setValue(50000.0);
        txtBaseInicial.setMin(0);

        txtEfectivoContado.setValue(0.0);
        txtEfectivoContado.setMin(0);

        // Estilos para los totales
        lblVentasDia.getStyle().set("font-size", "1.2em").set("font-weight", "bold");
        lblTotalEsperado.getStyle().set("font-size", "1.2em").set("font-weight", "bold");
        lblResultado.getStyle().set("font-size", "1.4em").set("font-weight", "bold");

        Button btnCalcular = new Button("Procesar Arqueo y Cierre", e -> procesarArqueo());
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

        add(titulo, form, resumen);
        actualizarResumen();
    }

    private double calcularTotalVentas() {
        List<Venta> ventas = ArchivoVentas.cargarVentas();
        return ventas.stream().mapToDouble(Venta::getTotal).sum();
    }

    private void actualizarResumen() {
        double totalVentas = calcularTotalVentas();
        double base = txtBaseInicial.getValue() != null ? txtBaseInicial.getValue() : 0.0;
        double esperado = base + totalVentas;

        lblVentasDia.setText("Total Ventas Registradas: $" + String.format("%.2f", totalVentas));
        lblTotalEsperado.setText("Total Esperado en Caja (Base + Ventas): $" + String.format("%.2f", esperado));
    }

    private void procesarArqueo() {
        actualizarResumen();

        double base = txtBaseInicial.getValue() != null ? txtBaseInicial.getValue() : 0.0;
        double efectivo = txtEfectivoContado.getValue() != null ? txtEfectivoContado.getValue() : 0.0;
        double totalVentas = calcularTotalVentas();
        double esperado = base + totalVentas;
        double diferencia = efectivo - esperado;

        if (diferencia == 0) {
            lblResultado.setText("ESTADO: Caja Cuadrada Perfectamente ($0.00)");
            lblResultado.getStyle().set("color", "green");
            Notification.show("¡Cierre de caja sin diferencias!", 3000, Notification.Position.MIDDLE);
        } else if (diferencia > 0) {
            lblResultado.setText("ESTADO: Sobrante de $" + String.format("%.2f", diferencia));
            lblResultado.getStyle().set("color", "blue");
            Notification.show("Sobrante registrado: $" + String.format("%.2f", diferencia), 3000, Notification.Position.MIDDLE);
        } else {
            lblResultado.setText("ESTADO: FALTANTE DE $" + String.format("%.2f", Math.abs(diferencia)));
            lblResultado.getStyle().set("color", "red");
            Notification.show("¡Atención! Hay un faltante de $" + String.format("%.2f", Math.abs(diferencia)), 4000, Notification.Position.MIDDLE);
        }
    }
}