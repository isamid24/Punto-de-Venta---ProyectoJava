package com.examble.puntodeventa.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class MainLayout extends AppLayout {

    public MainLayout() {
        H1 tittle = new H1("Punto de Venta - Barrio");
        tittle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);

        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Venta (Caja)", VentaView.class, VaadinIcon.CART.create()));
        nav.addItem(new SideNavItem("Catálogo Productos", ProductosView.class, VaadinIcon.PACKAGE.create()));
        nav.addItem(new SideNavItem("Clientes", ClientesView.class, VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Historial y Ventas", HistorialView.class, VaadinIcon.CLOCK.create()));
        nav.addItem(new SideNavItem("Reportes y Top 5", ReportesView.class, VaadinIcon.CHART_LINE.create()));
        nav.addItem(new SideNavItem("Cierre de caja", CierreCajaView.class, VaadinIcon.MONEY_DEPOSIT.create()));

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(new DrawerToggle(), tittle);
    }
    
}
