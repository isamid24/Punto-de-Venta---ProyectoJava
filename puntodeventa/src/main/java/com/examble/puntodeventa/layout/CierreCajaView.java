package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;


@PageTitle("Cierre de caja | POS")
@Route(value = "cierre-caja", layout = MainLayout.class)

public class CierreCajaView extends VerticalLayout{

    private NumberField txtBase = new NumberField("Base Inicial ($)");
    private NumberField txtEfectivoContado = new NumberField("Efectivo contado en la caja ($)");
    
    public  CierreCajaView() {
        add(new H2("Cierre de Caja Diario (+0.5)"));
        txtBase.setValue(50000.0);

        Button btnCerrar = new Button("Procesar Arqueo y cierre", e -> {
            if (txtEfectivoContado.isEmpty()) {
                Notification.show("Ingrese la cantidad de dinero contada físiciamente");
                return;
            }
            double contado = txtEfectivoContado.getValue();
            Notification.show("Cierre guardado correctamente. Efectivo registrado: $" + contado);
        });
        btnCerrar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        add(txtBase, txtEfectivoContado, btnCerrar);
    }
}
