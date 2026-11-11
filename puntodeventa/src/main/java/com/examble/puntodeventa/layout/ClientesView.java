package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoClientes;
import com.examble.puntodeventa.modelos.Clientes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Clientes | POS")
@Route(value = "clientes", layout = MainLayout.class)
public class ClientesView extends VerticalLayout {

    private Grid<Clientes> grid = new Grid<>(Clientes.class, false);
    private TextField txtBuscar = new TextField();
    private TextField txtCedula = new TextField("Cédula / Documento");
    private TextField txtNombre = new TextField("Nombre Completo");
    private TextField txtTelefono = new TextField("Teléfono");

    private List<Clientes> listaClientes;
    private Clientes clienteSeleccionado = null;

    public ClientesView() {
        setSizeFull();
        listaClientes = ArchivoClientes.cargarClientes();

        H2 titulo = new H2("Gestión de Clientes Registrados");

        // 1. Buscador idéntico al del catálogo de productos
        txtBuscar.setPlaceholder("Buscar por nombre o cédula...");
        txtBuscar.setValueChangeMode(ValueChangeMode.LAZY);
        txtBuscar.addValueChangeListener(e -> filtrarClientes(e.getValue()));

        // Restricciones de entrada
        txtCedula.setPattern("[0-9]*");
        txtCedula.setMaxLength(10);

        txtNombre.setPattern("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*");

        txtTelefono.setPattern("[0-9]*");
        txtTelefono.setMaxLength(10);
    
        // 2. Grilla de clientes con AutoWidth
        grid.addColumn(Clientes::getCedula).setHeader("Cédula").setAutoWidth(true);
        grid.addColumn(Clientes::getNombre).setHeader("Nombre").setAutoWidth(true);
        grid.addColumn(Clientes::getTelefono).setHeader("Teléfono").setAutoWidth(true);
        grid.setItems(listaClientes);

        grid.asSingleSelect().addValueChangeListener(e -> seleccionarCliente(e.getValue()));

        // Panel Izquierdo: Buscador + Grid (Igual a ProductosView)
        VerticalLayout panelIzquierdo = new VerticalLayout(txtBuscar, grid);
        panelIzquierdo.setPadding(false);

        // 3. Formulario estructurado a 2 columnas horizontales como en Productos
        FormLayout form = new FormLayout();
        form.add(txtCedula, txtNombre, txtTelefono);
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );

        // Botones de acción
        Button btnGuardar = new Button("Registrar / Actualizar", e -> registrarCliente());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnEliminar = new Button("Eliminar", e -> confirmarBorrado());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar Formulario", e -> limpiarFormulario());

        HorizontalLayout acciones = new HorizontalLayout(btnGuardar, btnEliminar, btnLimpiar);

        VerticalLayout panelDerecho = new VerticalLayout(form, acciones);
        panelDerecho.setPadding(false);

        // Layout Principal: Dividido en 2 secciones horizontales
        HorizontalLayout contenido = new HorizontalLayout(panelIzquierdo, panelDerecho);
        contenido.setSizeFull();
        contenido.setFlexGrow(1, panelIzquierdo);
        contenido.setFlexGrow(1, panelDerecho);

        add(titulo, contenido);
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.trim().isEmpty()) {
            grid.setItems(listaClientes);
        } else {
            String f = filtro.toLowerCase().trim();
            List<Clientes> filtrados = listaClientes.stream()
                .filter(c -> c.getCedula().toLowerCase().contains(f) || c.getNombre().toLowerCase().contains(f))
                .collect(Collectors.toList());
            grid.setItems(filtrados);
        }
    }

    private void seleccionarCliente(Clientes c) {
        if (c != null) {
            clienteSeleccionado = c;
            txtCedula.setValue(c.getCedula());
            txtCedula.setReadOnly(true);
            txtNombre.setValue(c.getNombre());
            txtTelefono.setValue(c.getTelefono() != null ? c.getTelefono() : "");
        }
    }

    private void registrarCliente() {
        String cedula = txtCedula.getValue().trim();
        String nombre = txtNombre.getValue().trim();
        String telefono = txtTelefono.getValue().trim();

        if (cedula.isEmpty() || nombre.isEmpty()) {
            Notification.show("Cédula y Nombre son obligatorios", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (!cedula.matches("\\d+")) {
            Notification.show("La cédula solo puede contener números", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            Notification.show("El nombre solo puede contener letras", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (!telefono.isEmpty() && !telefono.matches("\\d+")) {
            Notification.show("El teléfono solo puede contener números", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (clienteSeleccionado == null) {
            boolean existe = listaClientes.stream().anyMatch(c -> c.getCedula().equals(cedula));
            if (existe) {
                Notification.show("La cédula " + cedula + " ya está registrada", 3000, Notification.Position.MIDDLE);
                return;
            }
            Clientes nuevo = new Clientes(cedula, nombre, telefono);
            listaClientes.add(nuevo);
            Notification.show("Cliente registrado con éxito");
        } else {
            clienteSeleccionado.setNombre(nombre);
            clienteSeleccionado.setTelefono(telefono);
            Notification.show("Cliente actualizado con éxito");
        }

        ArchivoClientes.guardarClientes(listaClientes);
        grid.setItems(listaClientes);
        limpiarFormulario();
    }

    private void confirmarBorrado() {
        if (clienteSeleccionado == null) {
            Notification.show("Seleccione un cliente de la tabla primero");
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirmar eliminación");
        dialog.add("¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombre() + "?");

        Button btnSi = new Button("Sí, eliminar", e -> {
            listaClientes.remove(clienteSeleccionado);
            ArchivoClientes.guardarClientes(listaClientes);
            grid.setItems(listaClientes);
            limpiarFormulario();
            dialog.close();
            Notification.show("Cliente eliminado");
        });
        btnSi.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnNo = new Button("Cancelar", e -> dialog.close());

        dialog.getFooter().add(btnSi, btnNo);
        dialog.open();
    }

    private void limpiarFormulario() {
        clienteSeleccionado = null;
        txtCedula.clear();
        txtCedula.setReadOnly(false);
        txtNombre.clear();
        txtTelefono.clear();
        grid.asSingleSelect().clear();
    }
}