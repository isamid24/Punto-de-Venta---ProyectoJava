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
//import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;


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

        numCantidad.setValue(1.0);
        numCantidad.setMin(1);
        numCantidad.setStep(1);

        Button btnAgregar = new Button("Agregar al carrito", e -> agregarAlCarrito());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        VerticalLayout panelIzquierdo = new VerticalLayout(
            new H2("Catálogo de Productos"),
            gridCatálogo,
            new HorizontalLayout(numCantidad, btnAgregar)
        );
        panelIzquierdo.setSizeFull();


        //Lado derecho: Carrito y Cobro
        gridCarrito.addColumn(l -> l.getProducto().getNombre()).setHeader("Producto");
        gridCarrito.addColumn(LineaVenta::getCantidad).setHeader("Cant.");
        gridCarrito.addColumn(LineaVenta::getSubtotal).setHeader("Subtotal ($)");
        gridCarrito.setItems(carrito);

        Button btnQuitar = new Button ("Quitar Selección", e -> quitarDelCarrito());
        btnQuitar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnCobrar = new Button("Cobrar Venta", e -> procesarCobro());
        btnCobrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        VerticalLayout panelDerecho = new VerticalLayout(
            new H2("Carrito de Compras"),
            gridCarrito,
            btnQuitar,
            lblTotal,
            numMontoRecibido,
            lblCambio,
            btnCobrar
        );
        panelIzquierdo.setSizeFull();

        //Uso del componente SplitLayout
        SplitLayout splitLayout = new SplitLayout(panelIzquierdo, panelDerecho);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(55);

        add(splitLayout);

    }

    private void agregarAlCarrito() {
        Productos seleccionado = gridCatálogo.asSingleSelect().getValue();
        if (seleccionado == null) {
            Notification.show("Seleccione un producto del catálogo");
            return;
        }

        int cant = numCantidad.getValue().intValue();
        if (cant > seleccionado.getStock()) {
            Notification.show("No se puede vender más del stock disponible (" + seleccionado.getStock() + ")");
            return;
        }

        carrito.add(new LineaVenta(seleccionado, cant));
        gridCarrito.setItems(carrito);
        actualizarTotales();
    }
    
    private void quitarDelCarrito() {
        LineaVenta seleccionada = gridCarrito.asSingleSelect().getValue();
        if (seleccionada != null) {
            carrito.remove(seleccionada);
            gridCarrito.setItems(carrito);
            actualizarTotales();
        }
    }

    private double calcularTotal() {
        return carrito.stream().mapToDouble(LineaVenta::getSubtotal).sum();
    }

    private void actualizarTotales() {
        double total = calcularTotal();
        lblTotal.setText("Total: $" + String.format("%.2f", total));
        calcularCambio();
    }

    private void calcularCambio() {
        double total = calcularTotal();
        double recibido = numMontoRecibido.getValue() != null ? numMontoRecibido.getValue() : 0.0;
        double cambio = recibido - total;
        lblCambio.setText("Cambio: $" + String.format("%.2f", Math.max(0, cambio)));
    }

    private void procesarCobro() {
        if (carrito.isEmpty()) {
            Notification.show("El carrito está vacío");
            return ;
        }
        
        double total = calcularTotal();
        double recibido = numMontoRecibido.getValue() != null ? numMontoRecibido.getValue() : 0.0;

        if (recibido < total) {
            Notification.show("El monto recibido es menor al total de la venta",3000, Notification.Position.MIDDLE);
            return;
        }

        //Descuenta stock
        for (LineaVenta lv : carrito) {
            Productos p = lv.getProducto();
            p.setStock(p.getStock() - lv.getCantidad());
        }
        ArchivoProductos.guardarProductos(productos);
        gridCatálogo.setItems(productos);

        Notification.show("¡Venta procesasda con éxito! Cambio: $" + String.format("%.2f", recibido - total));

        //Limpiar para venta nueva
        carrito.clear();
        gridCarrito.setItems(carrito);
        numMontoRecibido.clear();
        actualizarTotales();
    }
}


