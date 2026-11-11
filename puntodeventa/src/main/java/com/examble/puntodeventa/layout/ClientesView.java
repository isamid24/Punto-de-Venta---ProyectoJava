package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoClientes;
import com.examble.puntodeventa.modelos.Clientes;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

        grid.addColumn(Clientes::getCedula).setHeader("Cédula");
        grid.addColumn(Clientes::getNombre).setHeader("Nombre");
        grid.addColumn(Clientes::getTelefono).setHeader("Teléfono");
        grid.setItems(listaClientes);

        FormLayout form = new FormLayout(txtCedula, txtNombre, txtTelefono);
        Button btnGuardar = new Button("Registrar Cliente", e -> registrarCliente());
        btnGuardar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout contenido = new HorizontalLayout(grid, new VerticalLayout(form, btnGuardar));
        contenido.setSizeFull();
        contenido.setFlexGrow(2, grid);

        // AGREGAR LOS COMPONENTES A LA VISTA
        add(titulo, contenido);
    }

    private void registrarCliente() {
        if (txtCedula.isEmpty() || txtNombre.isEmpty()) {
            Notification.show("Cédula y Nombre son obligatorios");
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
}