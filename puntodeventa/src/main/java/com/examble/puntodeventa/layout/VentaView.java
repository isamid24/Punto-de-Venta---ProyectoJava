package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoProductos;
import com.examble.puntodeventa.modelos.LineaVenta;
import com.examble.puntodeventa.modelos.Productos;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.kotlin.it.unimi.dsi.fastutil.chars.G;


@PageTitle("Venta | POS")
@Route(value = "", layout = MainLayout.class) //Vista principal
public class VentaView extends VerticalLayout {

    private List<Productos> productos;
    private List<LineaVenta> carrito = new ArrayList<>();

    private Grid<Productos> gridCatálogo = new Grid<>(Productos.class, false);
    private Grid<LineaVenta> gridCarrito = new Grid<>(LineaVenta.class, false);

    private NumberField numCantidad = new NumberField("Cantidad a vender");
    private NumberField numMontoRecibido = new NumberField("Monto recibido ($)");
    private H3 lblTotal = new H3("Total: $0.00");
    private H3 lblCambio = new H3("Cambio: $0.00");


    public VentaView() {
        setSizeFull();
        productos = ArchivoProductos.cargarProductos();

        //Lado izquierdo: Catálogo
        gridCatálogo.addColumn(Productos::getCodigo).setHeader("Código");
        gridCatálogo.addColumn(Productos::getNombre).setHeader("Nombre");
        gridCatálogo.addColumn(Productos::getPrecio).setHeader("Precio ($)");
        gridCatálogo.addColumn(Productos::getStock).setHeader("Stock");
        gridCatálogo.setItems(productos);
    }
}
