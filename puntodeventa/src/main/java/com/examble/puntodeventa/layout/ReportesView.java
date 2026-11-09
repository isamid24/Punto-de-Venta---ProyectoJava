package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "reportes", layout = MainLayout.class)
public class ReportesView extends VerticalLayout {
    public ReportesView() {
        add(new H2("Reportes y top 5"));
    }
}