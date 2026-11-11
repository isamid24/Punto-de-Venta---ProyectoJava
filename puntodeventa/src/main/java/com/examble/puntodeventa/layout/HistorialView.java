package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.modelos.Venta;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

//import java.util.ArrayList;
import java.util.List;

@PageTitle("Historial | POS")
@Route(value = "historial", layout = MainLayout.class)
public class HistorialView extends VerticalLayout {

    private Grid<Venta> grid = new Grid<>(Venta.class, false);
    private List<Venta> listaVentas = com.examble.puntodeventa.archivos.ArchivoVentas.cargarVentas();

    public HistorialView() {
        setSizeFull();

        H2 titulo = new H2("Historial de Ventas Realizadas");

        grid.addColumn(Venta::getIdVenta).setHeader("ID Venta");
        grid.addColumn(Venta::getFechaHora).setHeader("Fecha y Hora");
        grid.addColumn(Venta::getClienteCedula).setHeader("Cliente");
        grid.addColumn(v -> "$" + String.format("%.2f", v.getTotal())).setHeader("Total ($)");
        grid.addColumn(v -> "$" + String.format("%.2f", v.getMontoRecibido())).setHeader("Pagado ($)");
        grid.addColumn(v -> "$" + String.format("%.2f", v.getCambio())).setHeader("Cambio ($)");

        grid.setItems(listaVentas);

        // AGREGAR LOS COMPONENTES A LA VISTA
        add(titulo, grid);
    }
}