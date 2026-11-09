package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "venta", layout = MainLayout.class)
public class VentaView extends VerticalLayout {
    public VentaView() {
        add(new H2("Pantalla de Ventas"));
    }
}
