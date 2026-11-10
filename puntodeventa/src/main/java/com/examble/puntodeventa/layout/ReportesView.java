package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Reportes | POS")
@Route(value = "reportes", layout = MainLayout.class)
public class ReportesView extends VerticalLayout {

    public ReportesView() {
        add(new H2("Reportes y Estadísticas de Ventas"));

        // Ejemplo componente tipo Card
        VerticalLayout cardDia = crearTarjeta("Ventas del Día", "$ 125,000.00", "Ventas acumuladas hoy");
        VerticalLayout cardMes = crearTarjeta("Ventas del Mes", "$ 3,450,000.00", "Acumulado mensual");
        VerticalLayout cardTop = crearTarjeta("Producto Estrella", "Leche Alquería", "Más vendido");

        HorizontalLayout resumenTarjetas = new HorizontalLayout(cardDia, cardMes, cardTop);
        resumenTarjetas.setWidthFull();
        add(resumenTarjetas);
    }

    private VerticalLayout crearTarjeta(String titulo, String valor, String descripcion) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle().set("border", "1px solid #ccc")
                       .set("border-radius", "8px")
                       .set("padding", "16px")
                       .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        card.add(new H3(titulo), new H2(valor), new Span(descripcion));
        return card;
    }
}