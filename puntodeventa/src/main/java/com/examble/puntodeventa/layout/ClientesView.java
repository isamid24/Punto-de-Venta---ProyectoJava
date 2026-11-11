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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Clientes | POS")
@Route(value = "clientes", layout = MainLayout.class)
public class ClientesView extends VerticalLayout {

    private Grid<Clientes> grid = new Grid<>(Clientes.class, false);
    private TextField txtCedula = new TextField("Cédula / Documento");
    private TextField txtNombre = new TextField("Nombre Completo");
    private TextField txtTelefono = new TextField("Teléfono");
    private List<Clientes> listaClientes;

    public ClientesView() {
        setSizeFull();
        listaClientes = ArchivoClientes.cargarClientes();

        H2 titulo = new H2("Gestión de Clientes Registrados");

        //Solo permite ingresar dígitos (0-9) y limita la longitud
        txtCedula.setPattern("[0-9]*");
        txtCedula.setMaxLength(10);

        txtNombre.setPattern("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*");

        txtTelefono.setPattern("[0-9]*");
        txtTelefono.setMaxLength(10);


        grid.addColumn(Clientes::getCedula).setHeader("Cédula");
        grid.addColumn(Clientes::getNombre).setHeader("Nombre");
        grid.addColumn(Clientes::getTelefono).setHeader("Teléfono");
        grid.asSingleSelect().addValueChangeListener(e -> seleccionarCliente(e.getValue()));
        grid.setItems(listaClientes);


        FormLayout form = new FormLayout(txtCedula, txtNombre, txtTelefono);

        //Botones con misma estructura de ProductosView
        Button btnGuardar = new Button("Registrar / Actualizar", e -> registrarCliente());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button btnEliminar = new Button("Eliminar", e -> confirmarBorrado());
        btnEliminar.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button btnLimpiar = new Button("Limpiar Formulario", e -> limpiarFormulario());


        HorizontalLayout acciones = new HorizontalLayout(btnGuardar, btnEliminar, btnLimpiar);
        HorizontalLayout contenido = new HorizontalLayout(grid, new VerticalLayout(form, acciones));
        contenido.setFlexGrow(2, grid);
        
        // AGREGAR LOS COMPONENTES A LA VISTA
        add(titulo, contenido);
    }

    private Clientes clienteSeleccionado = null;

    private void registrarCliente() {
        String cedula = txtCedula.getValue().trim();
    String nombre = txtNombre.getValue().trim();
    String telefono = txtTelefono.getValue().trim();

    if (cedula.isEmpty() || nombre.isEmpty()) {
        Notification.show("Cédula y Nombre son obligatorios", 3000, Notification.Position.MIDDLE);
        return;
    }

    //Validar que la Cédula solo contenga números
    if (!cedula.matches("\\d+")) {
        Notification.show("La cédula solo puede contener números", 3000, Notification.Position.MIDDLE);
        return;
    }

    if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
        Notification.show("El nombre del cliente solo puede contener letras", 3000, Notification.Position.MIDDLE);
        return;
    }

    if (!telefono.isEmpty() && !telefono.matches("\\d+")) {
        Notification.show("El teléfono solo puede contener números", 3000, Notification.Position.MIDDLE);
        return;
    }

        Clientes nuevo = new Clientes(txtCedula.getValue(), txtNombre.getValue(), txtTelefono.getValue());
        listaClientes.add(nuevo);
        ArchivoClientes.guardarClientes(listaClientes);
        grid.setItems(listaClientes);
        txtCedula.clear();
        txtNombre.clear();
        txtTelefono.clear();
        Notification.show("Cliente registrado con éxito");
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