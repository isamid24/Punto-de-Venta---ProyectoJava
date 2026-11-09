package com.examble.puntodeventa.modelos;

import java.util.ArrayList;
import java.util.List;


public class Venta {
    private String idVenta;
    private String fechaHora;
    private String clienteCedula;
    private double total;
    private double montoRecibido;
    private double cambio;
    //Lista vacía para almacenar todos los productos comprados en esa venta
    private List<LineaVenta> lineas = new ArrayList<>();

    public Venta() {}
    public Venta(String idVenta, String fechaHora, String clienteCedula, double total, double montoRecibido, double cambio) {
             this.idVenta = idVenta;
            this.fechaHora = fechaHora;
            this.clienteCedula = clienteCedula;
            this.total = total;
            this.montoRecibido = montoRecibido;
            this.cambio = cambio;
    }    
    
    public String getIdVenta() { return idVenta; }
    public void setIdVenta(String idVenta) {this.idVenta = idVenta; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) {this.fechaHora = fechaHora; }

    public String getClienteCedula() { return clienteCedula; }
    public void setClienteCedula(String clienteCedula) {this.clienteCedula = clienteCedula; }

    public double getTotal() { return total; }
    public void setTotal(double total) {this.total = total; }

    public double getMontoRecibido() { return montoRecibido; }
    public void setMontoRecibidok(double montoRecibido) {this.montoRecibido = montoRecibido; }

    public double getCambio() { return cambio; }
    public void setCambio(double cambio) {this.cambio = cambio; }

    public List<LineaVenta> getLineas() { return lineas; }
    public void setLineas(List<LineaVenta> lineas) {this.lineas = lineas; }


    //Convertirlo a un formato dselimitado por un "|"
    //Si el cliente no dio su cédula, guarda la palabra "GENERAL"; de lo contrario, guarda el número ingresado.
    public String aLineaTxt() {
        return idVenta + "|" + fechaHora + "|" + (clienteCedula.isEmpty() ? "GENERAL" : clienteCedula) + "|" + total + "|" + montoRecibido + "|" + cambio;
    }

}
