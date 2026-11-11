package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoClientes;
import com.examble.puntodeventa.archivos.ArchivoProductos;
import com.examble.puntodeventa.archivos.ArchivoVentas;
import com.examble.puntodeventa.modelos.Clientes;
import com.examble.puntodeventa.modelos.LineaVenta;
import com.examble.puntodeventa.modelos.Productos;
import com.examble.puntodeventa.modelos.Venta;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@PageTitle("Venta (Caja) | POS")
@Route(value = "", layout = MainLayout.class)
public class VentaView extends VerticalLayout {

    // Componentes del Catálogo y Carrito
    private Grid<Productos> gridCatálogo = new Grid<>(Productos.class, false);
    private Grid<LineaVenta> gridCarrito = new Grid<>(LineaVenta.class, false);
    private NumberField numCantidad = new NumberField("Cantidad a llevar");

    // Componentes del Panel de Cobro
    private ComboBox<Clientes> comboCliente = new ComboBox<>("Cliente (Opcional)");
    private NumberField numMontoRecibido = new NumberField("Monto Recibido ($)");
    private Span lblTotal = new Span("Total a Pagar: $0.00");
    private Span lblCambio = new Span("Cambio: $0.00");

    // Colecciones de Datos globales
    private List<Productos> listaProductos;
    private List<Clientes> listaClientes;
    private List<LineaVenta> carrito = new ArrayList<>();

    public VentaView() {
        setSizeFull();
        setPadding(true);

        // 1. Carga de datos iniciales desde los archivos planos
        listaProductos = ArchivoProductos.cargarProductos();
        listaClientes = ArchivoClientes.cargarClientes();

        // 2. Grilla del Catálogo de Productos
        gridCatálogo.setHeight("220px");
        gridCatálogo.addColumn(Productos::getCodigo).setHeader("Código").setAutoWidth(true);
        gridCatálogo.addColumn(Productos::getNombre).setHeader("Nombre").setAutoWidth(true);
        gridCatálogo.addColumn(p -> String.format("$%.2f", p.getPrecio())).setHeader("Precio").setAutoWidth(true);
        gridCatálogo.addColumn(Productos::getStock).setHeader("Stock").setAutoWidth(true);
        gridCatálogo.setItems(listaProductos);

        // 3. Formulario para agregar al carrito
        numCantidad.setValue(1.0);
        numCantidad.setMin(1);
        numCantidad.setStep(1);

        Button btnAgregar = new Button("Agregar al carrito", e -> agregarAlCarrito());
        btnAgregar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout formAgregar = new HorizontalLayout(numCantidad, btnAgregar);
        formAgregar.setAlignItems(Alignment.END);

        // 4. Grilla del Carrito de Compras
        gridCarrito.setHeight("220px");
        gridCarrito.addColumn(l -> l.getProducto().getNombre()).setHeader("Producto").setAutoWidth(true);
        gridCarrito.addColumn(LineaVenta::getCantidad).setHeader("Cant.").setAutoWidth(true);
        gridCarrito.addColumn(l -> String.format("$%.2f", l.getSubtotal())).setHeader("Subtotal").setAutoWidth(true);
        
        gridCarrito.addComponentColumn(linea -> {
            Button btnQuitar = new Button(VaadinIcon.TRASH.create(), e -> quitarDelCarrito(linea));
            btnQuitar.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
            return btnQuitar;
        }).setHeader("Acción").setAutoWidth(true);

        gridCarrito.setItems(carrito);

        VerticalLayout panelIzquierdo = new VerticalLayout(
            new H3("Catálogo de Productos"),
            gridCatálogo,
            formAgregar,
            new H3("Carrito de Compra"),
            gridCarrito
        );
        panelIzquierdo.setSizeFull();
        panelIzquierdo.setPadding(false);

        // 5. Configurar Selección de Cliente y Panel de Cobro
        comboCliente.setItems(listaClientes);
        comboCliente.setItemLabelGenerator(c -> c.getNombre() + " (" + c.getCedula() + ")");
        comboCliente.setPlaceholder("Seleccione cliente o deje en blanco");
        comboCliente.setClearButtonVisible(true);
        comboCliente.setWidthFull();

        numMontoRecibido.setMin(0);
        numMontoRecibido.setPlaceholder("0.00");
        numMontoRecibido.setWidthFull();
        numMontoRecibido.addValueChangeListener(e -> calcularCambio());

        lblTotal.getStyle().set("font-size", "1.4em").set("font-weight", "bold");
        lblCambio.getStyle().set("font-size", "1.2em").set("color", "var(--lumo-success-text-color)");

        Button btnCobrar = new Button("Procesar Cobro y Facturar", e -> procesarCobro());
        btnCobrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        btnCobrar.setWidthFull();

        VerticalLayout panelDerecho = new VerticalLayout(
            new H3("Detalle del Cobro"),
            comboCliente,
            lblTotal,
            numMontoRecibido,
            lblCambio,
            btnCobrar
        );
        panelDerecho.setSizeFull();

        // 6. Componente SplitLayout
        SplitLayout splitLayout = new SplitLayout(panelIzquierdo, panelDerecho);
        splitLayout.setSizeFull();
        splitLayout.setSplitterPosition(65);

        add(splitLayout);
    }

    private void agregarAlCarrito() {
        Productos seleccionado = gridCatálogo.asSingleSelect().getValue();
        if (seleccionado == null) {
            Notification.show("Por favor seleccione un producto del catálogo primero", 3000, Notification.Position.MIDDLE);
            return;
        }

        int cantidadPedida = numCantidad.getValue() != null ? numCantidad.getValue().intValue() : 1;

        int yaEnCarrito = carrito.stream()
                .filter(l -> l.getProducto().getCodigo().equals(seleccionado.getCodigo()))
                .mapToInt(LineaVenta::getCantidad)
                .sum();

        if ((yaEnCarrito + cantidadPedida) > seleccionado.getStock()) {
            Notification.show("Stock insuficiente. Disponible: " + seleccionado.getStock() + " (En carrito: " + yaEnCarrito + ")", 4000, Notification.Position.MIDDLE);
            return;
        }

        boolean existe = false;
        for (LineaVenta lv : carrito) {
            if (lv.getProducto().getCodigo().equals(seleccionado.getCodigo())) {
                lv.setCantidad(lv.getCantidad() + cantidadPedida);
                existe = true;
                break;
            }
        }

        if (!existe) {
            carrito.add(new LineaVenta(seleccionado, cantidadPedida));
        }

        gridCarrito.setItems(carrito);
        actualizarTotales();
        numCantidad.setValue(1.0);
    }

    private void quitarDelCarrito(LineaVenta linea) {
        carrito.remove(linea);
        gridCarrito.setItems(carrito);
        actualizarTotales();
    }

    private double calcularTotal() {
        return carrito.stream().mapToDouble(LineaVenta::getSubtotal).sum();
    }

    private void actualizarTotales() {
        double total = calcularTotal();
        lblTotal.setText(String.format("Total a Pagar: $%.2f", total));
        calcularCambio();
    }

    private void calcularCambio() {
        double total = calcularTotal();
        double recibido = numMontoRecibido.getValue() != null ? numMontoRecibido.getValue() : 0.0;
        double cambio = recibido - total;

        if (recibido > 0 && cambio >= 0) {
            lblCambio.setText(String.format("Cambio: $%.2f", cambio));
            lblCambio.getStyle().set("color", "var(--lumo-success-text-color)");
        } else if (recibido > 0 && cambio < 0) {
            lblCambio.setText(String.format("Faltante: $%.2f", Math.abs(cambio)));
            lblCambio.getStyle().set("color", "var(--lumo-error-text-color)");
        } else {
            lblCambio.setText("Cambio: $0.00");
            lblCambio.getStyle().set("color", "var(--lumo-secondary-text-color)");
        }
    }

    private void procesarCobro() {
    if (carrito.isEmpty()) {
        Notification.show("El carrito de compras está vacío", 3000, Notification.Position.MIDDLE);
        return;
    }

    double total = calcularTotal();
    double recibido = numMontoRecibido.getValue() != null ? numMontoRecibido.getValue() : 0.0;

    if (recibido < total) {
        Notification.show("El monto recibido es menor al total a pagar", 3000, Notification.Position.MIDDLE);
        return;
    }

    double cambio = recibido - total;

    // 1. Descontar inventario de productos
    for (LineaVenta lv : carrito) {
        Productos p = lv.getProducto();
        p.setStock(p.getStock() - lv.getCantidad());
    }
    
    ArchivoProductos.guardarProductos(listaProductos);
    gridCatálogo.setItems(listaProductos);

    // 2. Captura directa de la cédula del cliente seleccionado
    String clienteCedula = "Consumidor Final";
    Clientes clienteSeleccionado = comboCliente.getValue();

    if (clienteSeleccionado != null && clienteSeleccionado.getCedula() != null && !clienteSeleccionado.getCedula().trim().isEmpty()) {
        clienteCedula = clienteSeleccionado.getCedula().trim();
    }

    // 3. Crear y agregar la venta
    String idVenta = "V" + (System.currentTimeMillis() % 10000);
    String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    Venta nuevaVenta = new Venta(idVenta, fecha, clienteCedula, total, recibido, cambio);
    ArchivoVentas.agregarVenta(nuevaVenta);

    Notification notification = Notification.show("¡Venta procesada con éxito! Cambio: $" + String.format("%.2f", cambio));
    notification.setPosition(Notification.Position.MIDDLE);
    notification.setDuration(5000);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    // 4. Reset del carrito y formulario
    carrito.clear();
    gridCarrito.setItems(carrito);
    numMontoRecibido.clear();
    comboCliente.clear();
    actualizarTotales();
    }
}