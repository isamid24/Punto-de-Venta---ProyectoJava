package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "historial", layout = MainLayout.class)
public class HistorialView extends VerticalLayout {
    public HistorialView() {
        add(new H2("Historial de Ventas"));
    }
}