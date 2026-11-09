package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "productos", layout = MainLayout.class)
public class ProductosView extends VerticalLayout {
    public ProductosView() {
        add(new H2("Catálogo de Productos"));
    }
}
