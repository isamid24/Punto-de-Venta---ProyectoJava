package com.examble.puntodeventa.modelos;

public class Clientes {
    private String cedula;
    private String nombre;
    private String telefono;

    public Clientes() {}
        public Clientes(String cedula, String nombre, String telefono){
            this.cedula = cedula;
            this.nombre = nombre;
            this.telefono = telefono;
        }
    
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) {this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) {this.telefono = telefono; }


    //Convertirlo a un formato dselimitado por un "|"
    public String aLineaTxt() {
        return cedula + "|" + nombre + "|" + telefono;
    }

    //Convierte el texto de una línea en un objeto producto
    public static Clientes desdeLineaTxt(String linea) {
        String[] p = linea.split("\\|");
        if (p.length < 3) return null;
        return new Clientes(p[0].trim(), p[1].trim(), p[2].trim());
    }
    
}
