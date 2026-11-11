package com.examble.puntodeventa.modelos;

public class CierreCaja {
    private String id;
    private String fechaHora;
    private double baseInicial;
    private double totalVentas;
    private double totalEsperado;
    private double efectivoContado;
    private double diferencia;
    private String estado; // "CUADRADO", "SOBRANTE", "FALTANTE"

    public CierreCaja() {
    }

    public CierreCaja(String id, String fechaHora, double baseInicial, double totalVentas, 
                      double totalEsperado, double efectivoContado, double diferencia, String estado) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.baseInicial = baseInicial;
        this.totalVentas = totalVentas;
        this.totalEsperado = totalEsperado;
        this.efectivoContado = efectivoContado;
        this.diferencia = diferencia;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getBaseInicial() {
        return baseInicial;
    }

    public void setBaseInicial(double baseInicial) {
        this.baseInicial = baseInicial;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTotalEsperado() {
        return totalEsperado;
    }

    public void setTotalEsperado(double totalEsperado) {
        this.totalEsperado = totalEsperado;
    }

    public double getEfectivoContado() {
        return efectivoContado;
    }

    public void setEfectivoContado(double efectivoContado) {
        this.efectivoContado = efectivoContado;
    }

    public double getDiferencia() {
        return diferencia;
    }

    public void setDiferencia(double diferencia) {
        this.diferencia = diferencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}