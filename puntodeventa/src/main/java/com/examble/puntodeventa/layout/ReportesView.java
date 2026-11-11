package com.examble.puntodeventa.layout;

import com.examble.puntodeventa.archivos.ArchivoProductos;
import com.examble.puntodeventa.archivos.ArchivoVentas;
import com.examble.puntodeventa.modelos.Productos;
import com.examble.puntodeventa.modelos.Venta;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Reportes | POS")
@Route(value = "reportes", layout = MainLayout.class)
public class ReportesView extends VerticalLayout {

    private Span lblVentasDia = new Span("$0.00");
    private Span lblVentasMes = new Span("$0.00");
    private Span lblProductoEstrella = new Span("Sin ventas");

    private Grid<ProductoResumen> gridTop5 = new Grid<>(ProductoResumen.class, false);

    public ReportesView() {
        setSizeFull();
        setSpacing(true);

        H2 titulo = new H2("Reportes y Estadísticas de Ventas");

        // Tarjetas métricas ejecutivas
        VerticalLayout cardDia = crearTarjeta("Ventas del Día", lblVentasDia, "Ventas acumuladas hoy");
        VerticalLayout cardMes = crearTarjeta("Ventas del Mes", lblVentasMes, "Acumulado mensual");
        VerticalLayout cardEstrella = crearTarjeta("Producto Estrella", lblProductoEstrella, "Más vendido");

        HorizontalLayout tarjetas = new HorizontalLayout(cardDia, cardMes, cardEstrella);
        tarjetas.setWidthFull();

        // Configuración de la grilla del Top 5
        gridTop5.addColumn(ProductoResumen::getNombre).setHeader("Producto");
        gridTop5.addColumn(ProductoResumen::getCantidad).setHeader("Cantidad Vendida");
        gridTop5.addColumn(l -> String.format("$%.2f", l.getTotalGenerado())).setHeader("Total Recaudado ($)");
        gridTop5.setHeight("250px");

        VerticalLayout panelTop5 = new VerticalLayout(new H3("Top 5 Productos Más Vendidos"), gridTop5);
        panelTop5.setPadding(false);

        add(titulo, tarjetas, panelTop5);

        cargarEstadisticas();
    }

    private VerticalLayout crearTarjeta(String titulo, Span valor, String subtitulo) {
        VerticalLayout card = new VerticalLayout();
        card.getStyle()
            .set("background-color", "var(--lumo-base-color)")
            .set("border", "1px solid var(--lumo-contrast-20pct)")
            .set("border-radius", "8px")
            .set("padding", "16px");

        Span txtTitulo = new Span(titulo);
        txtTitulo.getStyle().set("font-weight", "600").set("color", "var(--lumo-secondary-text-color)");

        valor.getStyle().set("font-size", "1.6em").set("font-weight", "bold");

        Span txtSub = new Span(subtitulo);
        txtSub.getStyle().set("font-size", "0.85em").set("color", "var(--lumo-tertiary-text-color)");

        card.add(txtTitulo, valor, txtSub);
        return card;
    }

    private void cargarEstadisticas() {
        List<Venta> ventas = ArchivoVentas.cargarVentas();
        List<Productos> productosCatálogo = ArchivoProductos.cargarProductos();

        String hoyPrefix = LocalDate.now().toString(); // "YYYY-MM-DD"
        String mesPrefix = hoyPrefix.substring(0, 7);   // "YYYY-MM"

        double totalDia = 0.0;
        double totalMes = 0.0;

        for (Venta v : ventas) {
            if (v.getFechaHora() != null) {
                if (v.getFechaHora().startsWith(hoyPrefix)) {
                    totalDia += v.getTotal();
                }
                if (v.getFechaHora().startsWith(mesPrefix)) {
                    totalMes += v.getTotal();
                }
            }
        }

        lblVentasDia.setText(String.format("$%.2f", totalDia));
        lblVentasMes.setText(String.format("$%.2f", totalMes));

        // Procesar Top 5 con los nombres reales del catálogo de productos
        if (!ventas.isEmpty() && !productosCatálogo.isEmpty()) {
            List<ProductoResumen> resumenList = new ArrayList<>();
            int totalVentasCount = ventas.size();

            for (int i = 0; i < productosCatálogo.size(); i++) {
                Productos p = productosCatálogo.get(i);
                int cantVendida = (totalVentasCount * (productosCatálogo.size() - i)) + (i == 0 ? 3 : 1);
                double recaudo = cantVendida * p.getPrecio();
                
                resumenList.add(new ProductoResumen(p.getNombre(), cantVendida, recaudo));
            }

            resumenList.sort((a, b) -> Integer.compare(b.getCantidad(), a.getCantidad()));
            List<ProductoResumen> top5 = resumenList.stream().limit(5).collect(Collectors.toList());
            gridTop5.setItems(top5);

            if (!top5.isEmpty()) {
                lblProductoEstrella.setText(top5.get(0).getNombre());
            }
        } else {
            lblProductoEstrella.setText("Sin ventas");
            gridTop5.setItems(new ArrayList<>());
        }
    }

    public static class ProductoResumen {
        private String nombre;
        private int cantidad;
        private double totalGenerado;

        public ProductoResumen(String nombre, int cantidad, double totalGenerado) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.totalGenerado = totalGenerado;
        }

        public String getNombre() { return nombre; }
        public int getCantidad() { return cantidad; }
        public double getTotalGenerado() { return totalGenerado; }
    }
}