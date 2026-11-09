package com.examble.puntodeventa.modelos;

public class productos {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public productos() {
        public productos(String codigo, String nombre, double precio, int stock){
            this.codigo = codigo;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {this.codigo = codigo; }

    public String getNombre() { return codigo; }
    public void setNombre(String nombre) {this.nombre = nombre; }

    public String getPrecio() { return codigo; }
    public void setPrecio(String precio) {this.precio = precio; }


    //Convertirlo a un formato delimitado por un "|"
    public String aLineaTxt() {
        return codigo + "|" + nombre + "|" + precio + "|" + stock;
    }

    }

    
}
