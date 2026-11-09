package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "clientes", layout = MainLayout.class)
public class ClientesView extends VerticalLayout {
    public ClientesView() {
        add(new H2("Gestión de Clientes"));
    }
}