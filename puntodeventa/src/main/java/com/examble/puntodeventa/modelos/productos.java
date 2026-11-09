package com.examble.puntodeventa.modelos;

public class Productos {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Productos() {}
        public Productos(String codigo, String nombre, double precio, int stock){
            this.codigo = codigo;
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
        }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) {this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) {this.stock = stock; }


    //Convertirlo a un formato dselimitado por un "|"
    public String aLineaTxt() {
        return codigo + "|" + nombre + "|" + precio + "|" + stock;
    }

    //Convierte el texto de una línea en un objeto producto
    public static Productos desdeLineaTxt(String linea) {
        String[] p = linea.split("\\|");
        if (p.length < 4) return null;
        return new Productos(p[0].trim(), p[1].trim(), Double.parseDouble(p[2].trim()), Integer.parseInt(p[3].trim()));
    }

    
}
