package com.examble.puntodeventa.modelos;

public class LineaVenta {
    private Productos producto;
    private int cantidad;
    private double subtotal;

    public LineaVenta(Productos producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
            this.subtotal = producto.getPrecio() + cantidad;
        }
    
    public Productos getProducto() { return producto; }

    public int getCantidad() {return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = this.producto.getPrecio() + cantidad;
    }

    public double getSubtotal() { return subtotal; }

}
