package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoProductos;
import com.examble.puntodeventa.modelos.Productos;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Productos | POS")
@Route(value = "productos", layout = MainLayout.class)
public class ProductosView extends VerticalLayout {

    // Se instancia sin autogeneración de columnas para evitar duplicados
    private Grid<Productos> grid = new Grid<>();
    private TextField txtFiltro = new TextField();
    private TextField txtCodigo = new TextField("Código");
    private TextField txtNombre = new TextField("Nombre");
    private NumberField txtPrecio = new NumberField("Precio ($)");
    private NumberField txtStock = new NumberField("Stock Inicial");

    private List<Productos> listaProductos;
    private Productos productoSeleccionado = null;

    public ProductosView() {
        setSizeFull();
        listaProductos = ArchivoProductos.cargarProductos();

        H2 titulo = new H2("Gestión del catálogo de Productos");

        // Configuración de filtro de búsqueda
        txtFiltro.setPlaceholder("Buscar por nombre o código...");
        txtFiltro.setValueChangeMode(ValueChangeMode.LAZY);
        txtFiltro.addValueChangeListener(e -> actualizarGrid());

        // Columnas manuales exactas
        grid.removeAllColumns();
        grid.addColumn(Productos::getCodigo).setHeader("Código").setKey("codigo");
        grid.addColumn(Productos::getNombre).setHeader("Nombre").setKey("nombre");
        grid.addColumn(p -> "$" + String.format("%.2f", p.getPrecio())).setHeader("Precio").setKey("precio");
        grid.addColumn(Productos::getStock).setHeader("Stock").setKey("stock");

        grid.asSingleSelect().addValueChangeListener(e -> seleccionarProducto(e.getValue()));

        FormLayout form = new FormLayout(txtCodigo, txtNombre, txtPrecio, txtStock);

        Button btnGuardar = new Button("Guardar / Actualizar", e -> guardarProducto());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnEliminar = new Button("Eliminar", e -> confirmarBorrado());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar Formulario", e -> limpiarFormulario());

        HorizontalLayout acciones = new HorizontalLayout(btnGuardar, btnEliminar, btnLimpiar);

        HorizontalLayout contenido = new HorizontalLayout(grid, new VerticalLayout(form, acciones));
        contenido.setSizeFull();
        contenido.setFlexGrow(2, grid);
        contenido.setFlexGrow(1, contenido.getComponentAt(1));

        add(titulo, txtFiltro, contenido);
        actualizarGrid();
    }

    private void actualizarGrid() {
        String filtro = txtFiltro.getValue() != null ? txtFiltro.getValue().toLowerCase().trim() : "";
        List<Productos> filtrados = listaProductos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro) || p.getCodigo().toLowerCase().contains(filtro))
                .toList();
        grid.setItems(filtrados);
    }

    private void seleccionarProducto(Productos p) {
        if (p != null) {
            productoSeleccionado = p;
            txtCodigo.setValue(p.getCodigo());
            txtCodigo.setReadOnly(true);
            txtNombre.setValue(p.getNombre());
            txtPrecio.setValue(p.getPrecio());
            txtStock.setValue((double) p.getStock());
        }
    }

    private void guardarProducto() {
        if (txtCodigo.isEmpty() || txtNombre.isEmpty() || txtPrecio.isEmpty() || txtStock.isEmpty()) {
            Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (productoSeleccionado == null) {
            // Verificar si el código ya existe
            boolean existe = listaProductos.stream().anyMatch(p -> p.getCodigo().equalsIgnoreCase(txtCodigo.getValue().trim()));
            if (existe) {
                Notification.show("Ya existe un producto con el código " + txtCodigo.getValue(), 3000, Notification.Position.MIDDLE);
                return;
            }

            Productos nuevo = new Productos(
                txtCodigo.getValue().trim(),
                txtNombre.getValue().trim(),
                txtPrecio.getValue(),
                txtStock.getValue().intValue()
            );
            listaProductos.add(nuevo);
            Notification.show("Producto " + nuevo.getNombre() + " agregado exitosamente");
        } else {
            productoSeleccionado.setNombre(txtNombre.getValue().trim());
            productoSeleccionado.setPrecio(txtPrecio.getValue());
            productoSeleccionado.setStock(txtStock.getValue().intValue());
            Notification.show("Producto actualizado exitosamente");
        }

        // Persistir en el archivo .txt
        ArchivoProductos.guardarProductos(listaProductos);

        // Refrescar lista y limpiar campos
        limpiarFormulario();
        actualizarGrid();
    }

    private void confirmarBorrado() {
        if (productoSeleccionado == null) {
            Notification.show("Seleccione un producto de la tabla primero");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar eliminación");
        dialog.add("¿Está seguro de eliminar el producto " + productoSeleccionado.getNombre() + "?");

        Button btnSi = new Button("Sí, eliminar", e -> {
            listaProductos.remove(productoSeleccionado);
            ArchivoProductos.guardarProductos(listaProductos);
            limpiarFormulario();
            actualizarGrid();
            dialog.close();
            Notification.show("Producto eliminado");
        });
        btnSi.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnNo = new Button("Cancelar", e -> dialog.close());

        dialog.getFooter().add(btnSi, btnNo);
        dialog.open();
    }

    private void limpiarFormulario() {
        productoSeleccionado = null;
        txtCodigo.clear();
        txtCodigo.setReadOnly(false);
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        grid.asSingleSelect().clear();
    }
}